package pt.uac.qa.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pt.uac.qa.R;
import pt.uac.qa.model.Answer;
import pt.uac.qa.services.AnswerService;

public class ViewAnswerActivity extends AppCompatActivity {
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(AnswerService.RESULT_ERROR)) {
                Exception error = (Exception) intent.getSerializableExtra(AnswerService.RESULT_ERROR);
                Toast.makeText(ViewAnswerActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else if (intent.hasExtra(AnswerService.RESULT_ANSWER)) {
                Answer answer = (Answer) intent.getSerializableExtra(AnswerService.RESULT_ANSWER);

                bodyView.setText(answer.getBody());
                scoreView.setText("" + (answer.getNegativeVotes() + answer.getPositiveVotes()));

                authorView.setText(String.format(
                        "por %s %s",
                        answer.getUser().getName(),
                        DateUtils.getRelativeTimeSpanString(
                                answer.getDatePublished().getTime(),
                                System.currentTimeMillis(),
                                DateUtils.SECOND_IN_MILLIS)));

                getSupportActionBar().setTitle(answer.getBody());
            }
        }
    };

    private String answerId;
    private TextView bodyView;
    private TextView authorView;
    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);
        registerReceiver(receiver, new IntentFilter(AnswerService.INTENT_FILTER));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViews();
        loadAnswer();
    }

    private void setupViews() {

        bodyView = findViewById(R.id.answerBodyView);
        authorView = findViewById(R.id.authorAnswerView);
        scoreView = findViewById(R.id.scoreAnswerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_answer_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_upvote) {
            upVoteAnswer();
            return true;

        } else if (item.getItemId() == R.id.action_downvote) {
            downVoteAnswer();
            return true;

        } else if (item.getItemId() == R.id.action_markcorrect) {
            markCorrectAnswer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void downVoteAnswer() {
        Intent intent = getIntent();
        String answerId = intent.getStringExtra("answer_id");
        AnswerService.downVoteAnswer(this, answerId);
    }

    private void upVoteAnswer() {
        Intent intent = getIntent();
        String answerId = intent.getStringExtra("answer_id");
        AnswerService.upVoteAnswer(this, answerId);
    }

    private void markCorrectAnswer() {
        Intent intent = getIntent();
        String answerId = intent.getStringExtra("answer_id");
        AnswerService.markCorrectAnswer(this, answerId);
    }

    private void loadAnswer() {
        Intent intent = getIntent();

        if (intent.hasExtra("answer_id")) {
            answerId = intent.getStringExtra("answer_id");
            AnswerService.fetchAnswer(this, answerId);
        } else {
            Toast.makeText(this, "Need answer ID.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
