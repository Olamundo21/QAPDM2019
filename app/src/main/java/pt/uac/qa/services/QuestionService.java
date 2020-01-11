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
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    private static final String TAG = "Question Service";
    private static final String ACTION_FETCH_QUESTIONS = "pt.uac.qa.services.action.FETCH_QUESTIONS";
    private static final String ACTION_FETCH_MY_QUESTIONS = "pt.uac.qa.services.action.FETCH_MY_QUESTIONS";
    private static final String ACTION_ADD_QUESTION = "pt.uac.qa.services.action.ADD_QUESTION";
    private static final String ACTION_UPDATE_QUESTION = "pt.uac.qa.services.action.UPDATE_QUESTION";
    private static final String ACTION_DELETE_QUESTION = "pt.uac.qa.services.action.DELETE_QUESTION";


    // TODO: Rename parameters
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


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH_QUESTIONS.equals(action)) {
                getQuestions();
            } else if (ACTION_FETCH_MY_QUESTIONS.equals(action)) {
                getMyQuestions();
            }
        }
    }


    private void getQuestions() {
        try{
            final QuestionClient client = new QuestionClient(this);
            final List<Question> questions = client.getQuestions();
            sendQuestionsBroadcast(questions);
        }catch (ClientException e) {
            Log.e(TAG, null, e);
            sendErrorBroadcast(e);
        }
    }

    private void getMyQuestions() {
        try{
            final QuestionClient client = new QuestionClient(this);
            sendQuestionsBroadcast(client.getMyQuestions());
        }catch (ClientException e) {
            Log.e(TAG, null, e);
            sendErrorBroadcast(e);
        }
    }

    private void sendErrorBroadcast(ClientException e) {
        final  Intent intent = new Intent(INTENT_FILTER);
        intent.putExtra(RESULT_ERROR, e);
        sendBroadcast(intent);
    }

    private void sendQuestionsBroadcast(List<Question> questions) {
        final Intent intent = new Intent(INTENT_FILTER);
        intent.putExtra(RESULT_QUESTIONS, (ArrayList<Question>)questions);
        sendBroadcast(intent);
    }

}
