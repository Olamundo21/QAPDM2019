package pt.uac.qa.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import pt.uac.qa.MainActivity;
import pt.uac.qa.R;
import pt.uac.qa.model.Question;
import pt.uac.qa.services.QuestionService;

public class MyQuestionsFragment extends BaseFragment {
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
                ((MainActivity) getActivity()).saveState("my_questions_loaded", loaded);
            }

            progressBar.setVisibility(View.GONE);
            questionList.setVisibility(View.VISIBLE);
        }
    };

    private QuestionAdapter adapter;
    private ListView questionList;
    private ProgressBar progressBar;
    private boolean loaded;

    private List<String> selectedQuestions = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_layout, container, false);
        questionList = root.findViewById(R.id.listView);
        progressBar = root.findViewById(R.id.progressBar);
        questionList.setAdapter(adapter = new QuestionAdapter(getActivity()));

        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        questionList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                questionList.setItemChecked(position, true);
                return false;
            }
        });

        questionList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Question question = (Question) adapter.getItem(position);

                if (checked) {
                    selectedQuestions.add(question.getQuestionId());
                } else {
                    selectedQuestions.remove(question.getQuestionId());
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                mode.finish();
                deleteSelectedQuestions();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {}
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(QuestionService.INTENT_FILTER));

        MainActivity activity = (MainActivity) getActivity();
        loaded = (boolean) activity.getState("my_questions_loaded", false);

        if (!loaded) {
            loadMyQuestions();
        }
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void refresh() {
        loadMyQuestions();
    }

    private void deleteSelectedQuestions() {

    }

    private void loadMyQuestions() {
        questionList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        QuestionService.fetchMyQuestions(getActivity());
    }
}