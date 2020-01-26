package pt.uac.qa.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.uac.qa.client.AnswerClient;
import pt.uac.qa.client.ClientException;
import pt.uac.qa.model.Answer;

import static android.content.ContentValues.TAG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AnswersService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FETCH_ANSWER = "pt.uac.qa.services.action.FETCH_ANSWER";
    private static final String ACTION_FETCH_MY_ANSWERS = "pt.uac.qa.services.action.FETCH_MY_ANSWERS";
    private static final String ACTION_ADD_ANSWER = "pt.uac.qa.services.action.ADD_ANSWER";
    private static final String ACTION_DELETE_ANSWERS = "pt.uac.qa.services.action.DELETE_ANSWERS";
    private static final String ACTION_UPDATE_ANSWER = "pt.uac.qa.services.action.UPDATE_ANSWER";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM_BODY = "pt.uac.qa.services.extra.PARAM_BODY";
    private static final String EXTRA_PARAM_QUESTION_ID = "pt.uac.qa.services.extra.PARAM_QUESTION_ID";
    private static final String EXTRA_PARAM_ANSWER_ID = "pt.uac.qa.services.PARAM_ANSWER_ID";
    private static final String EXTRA_PARAM_ANSWERS_IDS = "pt.uac.qa.services.extra.PARAM_ANSWERS_IDS";

    public static final String INTENT_FILTER = "pt.uac.qa.services.ANSWER_SERVICE";
    public static final String RESULT_ERROR = "pt.uac.qa.services.RESULT_ERROR";
    public static final String RESULT_ANSWERS = "pt.uac.qa.services.RESULT_ANSWERS";
    public static final String RESULT_ANSWER = "pt.uac.qa.services.RESULT_ANSWER";


    public AnswersService() {
        super("AnswersService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    //  VAI BUSCAR UMA RESPOSTA QUANDO FOR PARA ABRIR PARA VER O TEXTO.
    public static void fetchAnswer(Context context, String answerId) {
        Intent intent = new Intent(context, AnswersService.class);
        intent.setAction(ACTION_FETCH_ANSWER);
        intent.putExtra(EXTRA_PARAM_ANSWER_ID, answerId);
        context.startService(intent);
    }

    // VAI BUSCAR AS RESPOSTAS
    public static void fetchMyAnswers(Context context) {
        Intent intent = new Intent(context, AnswersService.class);
        intent.setAction(ACTION_FETCH_MY_ANSWERS);
        context.startService(intent);
    }

    // VAI ADICIONAR UMA RESPOSTA
    public static void addAnswer(Context context, String questionId, String body) {
        Intent intent = new Intent(context, AnswersService.class);
        intent.setAction(ACTION_ADD_ANSWER);
        intent.putExtra(EXTRA_PARAM_BODY, body);
        intent.putExtra(EXTRA_PARAM_QUESTION_ID, questionId);
        context.startService(intent);
    }

    // VAI ELIMINAR RESPOSTAS
    public static void deleteAnswers(Context context, String answerId){
        Intent intent = new Intent(context, AnswersService.class);
        intent.setAction(ACTION_DELETE_ANSWERS);
        context.startService(intent);
    }

    public static void updateAnswer(Context context, String answerId, String body){
        Intent intent = new Intent(context, AnswersService.class);
        intent.setAction(ACTION_UPDATE_ANSWER);
        intent.putExtra(EXTRA_PARAM_ANSWER_ID, answerId);
        intent.putExtra(EXTRA_PARAM_BODY, body);
        context.startService(intent);
    }

    // HANDLER *****
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_FETCH_MY_ANSWERS.equals(action)) {
                getMyAnswers();

            }else if ((ACTION_FETCH_ANSWER.equals(action))) {
                String answerId = intent.getStringExtra(EXTRA_PARAM_ANSWER_ID);
                getAnswer(answerId);

            } else if ((ACTION_UPDATE_ANSWER.equals(action))) {
                String answerId = intent.getStringExtra(EXTRA_PARAM_ANSWER_ID);
                String body = intent.getStringExtra(EXTRA_PARAM_BODY);
                updateAnswer(answerId, body);

            } else if (ACTION_ADD_ANSWER.equals(action)) {
                String questionId = intent.getStringExtra(EXTRA_PARAM_QUESTION_ID);
                String body = intent.getStringExtra(EXTRA_PARAM_BODY);
                addAnswer(questionId,body);

            } else if (ACTION_DELETE_ANSWERS.equals((action))){
                String[] answersIds = intent.getStringArrayExtra(EXTRA_PARAM_ANSWERS_IDS);
                deleteAnswers(answersIds);
            }
        }
    }

    // RECEBER AS MINHAS RESPOSTAS
    private void getMyAnswers() {
        try {
            final AnswerClient client = new AnswerClient(this);
            sendAnswersBroadcast(client.getMyAnswers());
        } catch (ClientException e) {
            Log.e(TAG, null, e);
            sendErrorBroadcast(e);
        }
    }

    //  RECEBER RESPOSTA
    private void getAnswer(String answerId) {
            try {
                AnswerClient client= new AnswerClient(this);
                Answer answer = client.getAnswer(answerId);
                Intent intent = new Intent(INTENT_FILTER);
                intent.putExtra(RESULT_ANSWER, answer);
                sendBroadcast(intent);
            } catch (ClientException e) {
                Log.e(TAG, null, e);
                sendErrorBroadcast(e);
            }
    }

    // ADICIONAR RESPOSTA
    private void addAnswer(String questionId,String body) {
        try {
            final AnswerClient client = new AnswerClient(this);
            client.addAnswer(questionId, body);
            sendBroadcast(new Intent(INTENT_FILTER));
        } catch (ClientException e) {
            Log.e(TAG, null, e);
            sendErrorBroadcast(e);
        }
    }

    // ELIMINAR RESPOSTAS
    private void deleteAnswers(String[] answersIds) {
            try {
                AnswerClient client = new AnswerClient(this);
                for (String answerId : answersIds) {
                    client.deleteAnswer(answerId);
                }
                sendBroadcast(new Intent((INTENT_FILTER)));
            } catch (ClientException e) {
                Log.e(TAG, null, e);
                sendErrorBroadcast(e);
            }
        }

    // ACTUALIZAR RESPOSTA
    private void updateAnswer(String answerId, String body) {
        try {
            AnswerClient client = new AnswerClient(this);
            client.updateAnswer(answerId, body);
            sendBroadcast(new Intent(INTENT_FILTER));
        } catch (ClientException e) {
            Log.e(TAG, null, e);
            sendErrorBroadcast(e);
        }
    }

    // ERROR BROADCASTS **
    private void sendErrorBroadcast(final Exception e) {
        final Intent intent = new Intent(INTENT_FILTER);
        intent.putExtra(RESULT_ERROR, e);
        sendBroadcast(intent);
    }

    public void sendAnswersBroadcast(List<Answer> answers) {
        final Intent intent = new Intent(INTENT_FILTER);
        intent.putExtra(RESULT_ANSWERS, (ArrayList<Answer>) answers);
        sendBroadcast(intent);
    }
}
