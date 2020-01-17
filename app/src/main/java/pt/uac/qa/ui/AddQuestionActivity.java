package pt.uac.qa.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;
import pt.uac.qa.R;
import pt.uac.qa.services.QuestionService;

public class EditQuestionActivity extends AppCompatActivity {
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(QuestionService.RESULT_ERROR)) {
                Exception error = (Exception) intent.getSerializableExtra(QuestionService.RESULT_ERROR);
<<<<<<< Updated upstream:app/src/main/java/pt/uac/qa/ui/AddQuestionActivity.java
                Toast.makeText(AddQuestionActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else {
                AddQuestionActivity.this.setResult(Activity.RESULT_OK);
=======
                Toast.makeText(EditQuestionActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else if (intent.hasExtra(QuestionService.RESULT_QUESTION)) {
                Question question = (Question) intent.getSerializableExtra(QuestionService.RESULT_QUESTION);
                title.setText(question.getTitle());
                body.setText(question.getBody());
                tags.setVisibility(View.GONE);
            } else {
                EditQuestionActivity.this.setResult(Activity.RESULT_OK);
>>>>>>> Stashed changes:app/src/main/java/pt/uac/qa/ui/EditQuestionActivity.java
                finish();
            }
        }
    };

    private EditText title;
    private EditText body;
    private EditText tags;
    private String questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerReceiver(receiver, new IntentFilter(QuestionService.INTENT_FILTER));

        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        tags = findViewById(R.id.tags);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String questionTitle = title.getText().toString();
                final String questionBody = body.getText().toString();

                if (questionId != null) {
                    QuestionService.updateQuestion(EditQuestionActivity.this, questionId, questionTitle, questionBody);
                } else {
                    QuestionService.addQuestion(EditQuestionActivity.this,
                            questionTitle, questionBody, Arrays.asList(tags.getText().toString().split(",")));
                }
            }
        });

        loadQuestion();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
<<<<<<< Updated upstream:app/src/main/java/pt/uac/qa/ui/AddQuestionActivity.java
=======

    private void loadQuestion() {
        Intent intent = getIntent();

        if (intent.hasExtra("question_id")) {
            questionId = intent.getStringExtra("question_id");
            QuestionService.fetchQuestion(this, questionId);
        }
    }
>>>>>>> Stashed changes:app/src/main/java/pt/uac/qa/ui/EditQuestionActivity.java
}
