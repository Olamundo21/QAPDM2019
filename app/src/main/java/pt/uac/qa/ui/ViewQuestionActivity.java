package pt.uac.qa.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pt.uac.qa.R;
import pt.uac.qa.StringUtils;
import pt.uac.qa.model.Answer;
import pt.uac.qa.model.Question;
import pt.uac.qa.services.QuestionService;
import pt.uac.qa.ui.adapter.ListViewAdapter;
import pt.uac.qa.ui.adapter.ViewHolder;

public class ViewQuestionActivity extends AppCompatActivity {

    private TextView title;
    private TextView body;
    private TextView tags;
    private TextView author;
    private FloatingActionButton fab;
    private LinearLayout container;
    private AnswerAdapter adapter;
    //private String questionId;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(QuestionService.RESULT_ERROR)) {
                Exception error = (Exception) intent.getSerializableExtra(QuestionService.RESULT_ERROR);
                Toast.makeText(ViewQuestionActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else if (intent.hasExtra(QuestionService.RESULT_QUESTION)) {
                Question question = (Question) intent.getSerializableExtra(QuestionService.RESULT_QUESTION);
                title.setText(question.getTitle());
                body.setText(question.getBody());

                if (question.getTags() != null && !question.getTags().isEmpty()) {
                    tags.setVisibility(Integer.parseInt(StringUtils.join(",", question.getTags())));
                }else {
                    tags.setVisibility(View.GONE);
                }
                author.setText(String.format(
                        "por %s %s",question.getUser().getName(),
                        DateUtils.getRelativeTimeSpanString(
                        question.getDatePublished().getTime(),
                        System.currentTimeMillis(),
                        DateUtils.SECOND_IN_MILLIS)));

                if (question.getAnswers() != null && !question.getAnswers().isEmpty()) {
                    adapter.loadItems(question.getAnswers());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(receiver, new IntentFilter(QuestionService.INTENT_FILTER));
        setContentView(R.layout.activity_view_question);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: setup the views
        setupViews();
        setupActionButton();
        loadQuestion();
    }

    private void setupViews(){
        ListView answerListView = findViewById(R.id.answerListView);
        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        tags = findViewById(R.id.tags);
        author = findViewById(R.id.author);
        container = findViewById(R.id.questionContainer);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (container != collapsed) {
                    title.setCompoundDrawables(
                        ContextCompat.getDrawable(
                    ViewQuestionActivity.this, R.drawable.ic_keyboard_arrow_down_grey_700_18dp),
                        null, null , null);
                        container.setVisibility(View.GONE);
                } else {
                    title.setCompoundDrawables(
                            ContextCompat.getDrawable(
                                    ViewQuestionActivity.this, R.drawable.ic_keyboard_arrow_up_grey_700_18dp),
                            null, null , null);
                    container.setVisibility(View.VISIBLE);
                }
            }
        });

        answerListView.setAdapter(adapter = new AnswerAdapter(this));
    }

    // FAB BUTTON QUE ABRE EditAnswerActivity PARA ADICIONAR RESPOSTAS
    private void setupActionButton() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewQuestionActivity.this, EditAnswerActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void loadQuestion() {
        Intent intent = getIntent();

        if (intent.hasExtra("question_id")) {
            String questionId = intent.getStringExtra("question_id");
            QuestionService.fetchQuestion(this, questionId);
        } else {
            Toast.makeText(this, "Need question ID.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
