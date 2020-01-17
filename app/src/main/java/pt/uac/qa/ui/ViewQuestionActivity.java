package pt.uac.qa.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import pt.uac.qa.R;
import pt.uac.qa.model.Question;
import pt.uac.qa.services.QuestionService;

public class ViewQuestionActivity extends AppCompatActivity {
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(QuestionService.RESULT_ERROR)) {
                Exception error = (Exception) intent.getSerializableExtra(QuestionService.RESULT_ERROR);
                Toast.makeText(ViewQuestionActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else if (intent.hasExtra(QuestionService.RESULT_QUESTION)) {
                Question question = (Question) intent.getSerializableExtra(QuestionService.RESULT_QUESTION);
                //title.setText(question.getTitle());
                //body.setText(question.getBody());
                //tags.setVisibility(View.GONE);
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

        loadQuestion();
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
