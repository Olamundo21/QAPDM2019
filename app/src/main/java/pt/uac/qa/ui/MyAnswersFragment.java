package pt.uac.qa.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import pt.uac.qa.R;

public class MyAnswersFragment extends BaseFragment {
    //private MyAnswersAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_layout, container, false);
        return root;
    }

    @Override
    protected void refresh() {
        Toast.makeText(getActivity(), "Refresh my answers", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void search(CharSequence constraint) {
        //adapter.getFilter().filter(constraint);
    }
}