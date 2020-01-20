package pt.uac.qa.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pt.uac.qa.R;
import pt.uac.qa.model.Answer;
import pt.uac.qa.services.AnswersService;

public class EditAnswerActivity extends AppCompatActivity {
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(AnswersService.RESULT_ERROR)) {
                Exception error = (Exception) intent.getSerializableExtra(AnswersService.RESULT_ERROR);
                Toast.makeText(EditAnswerActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else if (intent.hasExtra(AnswersService.RESULT_ANSWER)) {
                Answer answer = (Answer) intent.getSerializableExtra(AnswersService.RESULT_ANSWER);
                body.setText(answer.getBody());
            } else {
                EditAnswerActivity.this.setResult(Activity.RESULT_OK);
                finish();
            }
        }
    };

    private EditText body;
    private String answerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        registerReceiver(receiver, new IntentFilter(AnswersService.INTENT_FILTER));

        body = findViewById(R.id.body);

        findViewById(R.id.button_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String answerBody = body.getText().toString();

                if (answerId != null) {
                    AnswersService.updateAnswer(EditAnswerActivity.this, answerId, answerBody);
                } else {
                    AnswersService.addAnswer(EditAnswerActivity.this,answerBody);
                }
            }
        });

        loadAnswer();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void loadAnswer() {
        Intent intent = getIntent();

        if (intent.hasExtra("answer_id")) {
            answerId = intent.getStringExtra("answer_id");
            AnswersService.fetchAnswer(this, answerId);
        }
    }
}
