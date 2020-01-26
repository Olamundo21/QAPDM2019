package pt.uac.qa.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import pt.uac.qa.client.AnswerClient;
import pt.uac.qa.client.ClientException;

public class AnswerService extends IntentService {
    private static final String ACTION_ADD_ANSWER = "pt.uac.qa.services.action.ADD_ANSWER";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM_QUESTION_ID = "pt.uac.qa.services.extra.PARAM_QUESTION_ID";
    private static final String EXTRA_PARAM_ANSWER_BODY = "pt.uac.qa.services.extra.PARAM_ANSWER_BODY";

    public static final String INTENT_FILTER = "pt.uac.qa.services.ANSWER_SERVICE";
    public static final String RESULT_ERROR = "pt.uac.qa.services.RESULT_ERROR";


    public AnswerService() {
        super("AnswerService");
    }

    public static void addAnswer(Context context, String questionId, String answerBody) {
        Intent intent = new Intent(context, AnswerService.class);
        intent.setAction(ACTION_ADD_ANSWER);
        intent.putExtra(EXTRA_PARAM_QUESTION_ID, questionId);
        intent.putExtra(EXTRA_PARAM_ANSWER_BODY, answerBody);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_ADD_ANSWER.equals(action)) {
                final String questionId = intent.getStringExtra(EXTRA_PARAM_QUESTION_ID);
                final String answerBody = intent.getStringExtra(EXTRA_PARAM_ANSWER_BODY);
                addAnswer(questionId, answerBody);
            }
        }
    }

    private void addAnswer(String questionId, String answerBody) {
        try {
            AnswerClient client = new AnswerClient(this);
            client.addAnswer(questionId, answerBody);
            sendBroadcast(new Intent(INTENT_FILTER));
        } catch (ClientException e) {
            sendErrorBroadcast(e);
        }
    }

    private void sendErrorBroadcast(final Exception e) {
        final Intent intent = new Intent(INTENT_FILTER);
        intent.putExtra(RESULT_ERROR, e);
        sendBroadcast(intent);
    }
}
