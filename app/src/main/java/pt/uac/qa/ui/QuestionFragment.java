package pt.uac.qa.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.Serializable;

import pt.uac.qa.R;
import pt.uac.qa.model.Question;
import pt.uac.qa.services.QuestionService;
import pt.uac.qa.ui.adapter.ListViewAdapter;
import pt.uac.qa.ui.adapter.ViewHolder;

// TODO: fazer formulario quando se clica no buttao para criar uma nova pergunta.

public class QuestionFragment extends Fragment {

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(QuestionService.RESULT_ERROR)) {
                Exception error = (Exception) intent.getSerializableExtra(QuestionService.RESULT_ERROR);
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            } else {
                final Serializable questions = intent.getSerializableExtra(QuestionService.RESULT_QUESTIONS);
                // TODO: load list view adapter
                loaded = true;

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("loaded", loaded);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(QuestionService.INTENT_FILTER));
        loadQuestions();

        if (!loaded){
            loadQuestions();
        }
    }

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

    /**
     * Adapter
     */

    private static class QuestionAdapter extends ListViewAdapter<Question> {
        protected QuestionAdapter(Context context) {
            super(context);
        }

        @Override
        protected View inflateView() {
            return LayoutInflater.from(context).inflate(R.layout.question_item_layout, null);
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new QuestionViewHolder();
        }


        @Override
        protected boolean acceptsItem(Question item, CharSequence constraint) {
            return item.getTitle().contains(constraint);
        }
    }

    /**
     * View Holder
     */

    private static class QuestionViewHolder implements ViewHolder<Question>{

        private TextView title;
        private TextView author;

        @Override
        public void init(View view) {
            title = view.findViewById(R.id.questionTitle);
            author = view.findViewById(R.id.questionAuthor);
        }

        @Override
        public void display(Question item) {
            title.setText(item.getTitle());
            author.setText(String.format(
                    "por %s $s",
                    item.getUser().getName(),
                    DateUtils.getRelativeTimeSpanString(
                            item.getDatePublished().getTime(),
                            System.currentTimeMillis(),
                            DateUtils.SECOND_IN_MILLIS)));
        }
    }
}