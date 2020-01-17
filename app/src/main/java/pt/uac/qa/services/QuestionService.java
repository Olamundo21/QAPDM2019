package pt.uac.qa.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.uac.qa.client.ClientException;
import pt.uac.qa.client.QuestionClient;
import pt.uac.qa.model.Question;

public class QuestionService extends IntentService {
    private static final String TAG = "QuestionsService";

    private static final String ACTION_FETCH_QUESTIONS = "pt.uac.qa.services.action.FETCH_QUESTIONS";
    private static final String ACTION_FETCH_MY_QUESTIONS = "pt.uac.qa.services.action.FETCH_MY_QUESTIONS";
    private static final String ACTION_ADD_QUESTION = "pt.uac.qa.services.action.ADD_QUESTION";
    private static final String ACTION_UPDATE_QUESTION = "pt.uac.qa.services.action.UPDATE_QUESTION";
    private static final String ACTION_DELETE_QUESTION = "pt.uac.qa.services.action.DELETE_QUESTION";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM_TITLE = "pt.uac.qa.services.extra.PARAM_TITLE";
    private static final String EXTRA_PARAM_BODY= "pt.uac.qa.services.extra.PARAM_BODY";
    private static final String EXTRA_PARAM_TAGS= "pt.uac.qa.services.extra.PARAM_TAGS";

    public static final String INTENT_FILTER = "pt.uac.qa.services.QUESTION_SERVICE";
    public static final String RESULT_ERROR = "pt.uac.qa.services.RESULT_ERROR";
    public static final String RESULT_QUESTIONS = "pt.uac.qa.services.RESULT_QUESTIONS";


    public QuestionService() {
        super("QuestionService");
    }

    public static void fetchQuestions(Context context) {
        Intent intent = new Intent(context, QuestionService.class);
        intent.setAction(ACTION_FETCH_QUESTIONS);
        context.startService(intent);
    }

    public static void fetchMyQuestions(Context context) {
        Intent intent = new Intent(context, QuestionService.class);
        intent.setAction(ACTION_FETCH_MY_QUESTIONS);
        context.startService(intent);
    }

    public static void addQuestion(Context context, String title, String body, List<String> tags) {
        Intent intent = new Intent(context, QuestionService.class);
        intent.setAction(ACTION_ADD_QUESTION);
        intent.putExtra(EXTRA_PARAM_TITLE, title);
        intent.putExtra(EXTRA_PARAM_BODY, body);
        intent.putStringArrayListExtra(EXTRA_PARAM_TAGS, new ArrayList<>(tags));
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_FETCH_QUESTIONS.equals(action)) {
                getQuestions();
            } else if (ACTION_FETCH_MY_QUESTIONS.equals(action)) {
                getMyQuestions();
            } else if (ACTION_ADD_QUESTION.equals(action)) {
                String title = intent.getStringExtra(EXTRA_PARAM_TITLE);
                String body = intent.getStringExtra(EXTRA_PARAM_BODY);
                List<String> tags = intent.getStringArrayListExtra(EXTRA_PARAM_TAGS);

                addQuestion(title, body, tags);
            }
        }
    }

    private void getQuestions() {
        try {
            final QuestionClient client = new QuestionClient(this);
            final List<Question> questions = client.getQuestions();
            sendQuestionsBroadcast(questions);
        } catch (ClientException e) {
            Log.e(TAG, null, e);
            sendErrorBroadcast(e);
        }
    }

    private void getMyQuestions() {
        try {
            final QuestionClient client = new QuestionClient(this);
            sendQuestionsBroadcast(client.getMyQuestions());
        } catch (ClientException e) {
            Log.e(TAG, null, e);
            sendErrorBroadcast(e);
        }
    }

    private void addQuestion(String title, String body, List<String> tags) {
        try {
            final QuestionClient client = new QuestionClient(this);
            client.addQuestion(title, body, tags);
            sendBroadcast(new Intent(INTENT_FILTER));
        } catch (ClientException e) {
            Log.e(TAG, null, e);
            sendErrorBroadcast(e);
        }
    }

    private void sendErrorBroadcast(final Exception e) {
        final Intent intent = new Intent(INTENT_FILTER);
        intent.putExtra(RESULT_ERROR, e);
        sendBroadcast(intent);
    }

    public void sendQuestionsBroadcast(List<Question> questions) {
        final Intent intent = new Intent(INTENT_FILTER);
        intent.putExtra(RESULT_QUESTIONS, (ArrayList<Question>) questions);
        sendBroadcast(intent);
    }
}
