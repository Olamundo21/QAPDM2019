package pt.uac.qa.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import pt.uac.qa.MainActivity;
import pt.uac.qa.R;
import pt.uac.qa.model.Question;
import pt.uac.qa.services.QuestionService;

public class QuestionsFragment extends BaseFragment {
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressWarnings("unchecked")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(QuestionService.RESULT_ERROR)) {
                Exception error = (Exception) intent.getSerializableExtra(QuestionService.RESULT_ERROR);
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else {
                final List<Question> questions = (List<Question>) intent.getSerializableExtra(QuestionService.RESULT_QUESTIONS);
                adapter.loadItems(questions);
                loaded = true;
                ((MainActivity) getActivity()).saveState("questions_loaded", loaded);
            }

            progressBar.setVisibility(View.GONE);
            questionList.setVisibility(View.VISIBLE);
        }
    };

    private QuestionAdapter adapter;
    private ListView questionList;
    private ProgressBar progressBar;
    private boolean loaded;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_layout, container, false);
        questionList = root.findViewById(R.id.listView);
        progressBar = root.findViewById(R.id.progressBar);

        questionList.setAdapter(adapter = new QuestionAdapter(getActivity()));
        return root;
    }

    @Override
    protected void refresh() {
        loadQuestions();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(QuestionService.INTENT_FILTER));

        MainActivity activity = (MainActivity) getActivity();
        loaded = (boolean) activity.getState("questions_loaded", false);

        if (!loaded) {
            loadQuestions();
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }

    private void loadQuestions() {
        questionList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        QuestionService.fetchQuestions(getActivity());
    }
}