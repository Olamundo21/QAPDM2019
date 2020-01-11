package pt.uac.qa.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 27-12-2019.
 */
public class Question implements Serializable {
    private String questionId;
    private String title;
    //private String body;
    private Date datePublished;
    private List<String> tags;
    private int answersGiven;
    private User user;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getAnswersGiven() {
        return answersGiven;
    }

    public void setAnswersGiven(int answersGiven) {
        this.answersGiven = answersGiven;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Question fromJson(@NonNull final JSONObject json) throws JSONException {
        final Question question = new Question();
        final JsonHelper helper = new JsonHelper(json);

        question.setQuestionId(helper.getString("questionId"));
        question.setTitle(helper.getString("title"));
        question.setAnswersGiven(helper.getInt("answersGiven"));
        question.setUser(helper.getUser("user"));
        question.setTags(helper.getStringList("tags"));
        question.setDatePublished(helper.getDate("datePublished"));

        return question;
    }
}