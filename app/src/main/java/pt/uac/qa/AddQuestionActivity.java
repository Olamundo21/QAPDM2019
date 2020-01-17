package pt.uac.qa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AutomaticZenRule;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import pt.uac.qa.services.AccessTokenService;
import pt.uac.qa.services.QuestionService;

public class AddQuestionActivity extends AppCompatActivity {

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(QuestionService.RESULT_ERROR)) {
                Exception error = (Exception) intent.getSerializableExtra(QuestionService.RESULT_ERROR);
                Toast.makeText(AddQuestionActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else {
                AddQuestionActivity.this.setResult(Activity.RESULT_OK);
                finish();
            }
        }
    };


    private EditText title;
    private EditText body;
    private EditText tags;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        registerReceiver(receiver, new IntentFilter(QuestionService.INTENT_FILTER));

        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        tags = findViewById(R.id.tags);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionService.addQuestion(AddQuestionActivity.this,
                        title.getText().toString(),
                        body.getText().toString(),
                        Arrays.asList(tags.getText().toString().split(","))
                        );
            }
        });
    }


/*
    private boolean validateFormQuestion() {
        boolean result = true;

        if (TextUtils.isEmpty(question.getText())) {
            question.setError("Introduza a uma questão");
            result = false;
        }

        if (TextUtils.isEmpty(description.getText())) {
            description.setError("Introduza uma descrição");
            result = false;
        }

        if (TextUtils.isEmpty(tags.getText())) {
            tags.setError("Introduza Tags");
            result = false;
        }

        return result;
    }*/

}
