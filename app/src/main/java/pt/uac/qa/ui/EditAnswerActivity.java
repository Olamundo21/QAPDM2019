package pt.uac.qa.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import pt.uac.qa.R;
import pt.uac.qa.services.AnswerService;

public class EditAnswerActivity extends AppCompatActivity {
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(AnswerService.RESULT_ERROR)) {
                Exception error = (Exception) intent.getSerializableExtra(AnswerService.RESULT_ERROR);
                Toast.makeText(EditAnswerActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else {
                EditAnswerActivity.this.setResult(Activity.RESULT_OK);
                finish();
            }
        }
    };

    private EditText answerBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_answer);
        answerBody = findViewById(R.id.answerBody);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.answer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_accept) {
            submitAnswer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void submitAnswer() {
        if (TextUtils.isEmpty(answerBody.getText())) {
            answerBody.setError("Por favor escreva a sua resposta");
            return;
        }

        Intent intent = getIntent();
        String questionId = intent.getStringExtra("question_id");

        AnswerService.addAnswer(this, questionId, answerBody.getText().toString());
    }
}
