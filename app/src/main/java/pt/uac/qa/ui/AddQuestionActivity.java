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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        registerReceiver(receiver, new IntentFilter(QuestionService.INTENT_FILTER));

        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        tags = findViewById(R.id.tags);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionService.addQuestion(AddQuestionActivity.this,
                        title.getText().toString(),
                        body.getText().toString(),
                        Arrays.asList(tags.getText().toString().split(",")));
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
