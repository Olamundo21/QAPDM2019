package pt.uac.qa.ui.adapter;

import android.view.View;

public interface ViewHolder<T> {
    void init(View view);
    void display(T item);
}
