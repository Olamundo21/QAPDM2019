package pt.uac.qa.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 27-12-2019.
 */
public class Answer implements Serializable {
    private String answerId;
    private String body;
    private String questionId;
    private User user;
    private Date datePublished;
    private boolean correctAnswer;
    private int positiveVotes;
    private int negativeVotes;

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getPositiveVotes() {
        return positiveVotes;
    }

    public void setPositiveVotes(int positiveVotes) {
        this.positiveVotes = positiveVotes;
    }

    public int getNegativeVotes() {
        return negativeVotes;
    }

    public void setNegativeVotes(int negativeVotes) {
        this.negativeVotes = negativeVotes;
    }

    public static Answer fromJson(final JSONObject json) throws JSONException {
        final Answer answer = new Answer();
        final JsonHelper helper = new JsonHelper(json);

        answer.setAnswerId(helper.getString("answerId"));
        answer.setBody(helper.getString("body"));
        answer.setDatePublished(helper.getDate("datePublished"));
        answer.setCorrectAnswer(helper.getBoolean("correctAnswer"));
        answer.setQuestionId(json.getJSONObject("question").getString("questionId"));
        answer.setUser(helper.getUser("user"));
        answer.setNegativeVotes(helper.getInt("negativeVotes"));

        return answer;
    }
}
