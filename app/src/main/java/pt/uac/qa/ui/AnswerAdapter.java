package pt.uac.qa.ui;

import android.content.Context;
import android.view.View;

import org.w3c.dom.Text;

import pt.uac.qa.model.Answer;
import pt.uac.qa.ui.adapter.ListViewAdapter;
import pt.uac.qa.ui.adapter.ViewHolder;

final class AnswerAdapter extends ListViewAdapter<Answer> {

    protected AnswerAdapter(Context context) {
        super(context);
    }

    @Override
    protected View inflateView() {
        return null;
    }

    @Override
    protected ViewHolder<Answer> createViewHolder() {
        return new AnswerViewHolder();
    }

    @Override
    protected boolean acceptsItem(Answer item, CharSequence constraint) {
        return false;
    }

    private static final class AnswerViewHolder implements ViewHolder<Answer> {

        private TextView scoreView;
        private TextView bodyView;
        private TextView authorView;

        @Override
        public void init(View view) {
            scoreView = view.findViewById(R.id.scoreView);
            bodyView = view.findViewById(R.id.bodyView);
            authorView = view.findViewById(R.id.authorView);
        }

        @Override
        public void display(Answer item) {
            scoreView.Text(Integer.toString(item.getNegativeVotes() + item.getPositiveVotes()));
        }
    }
}
