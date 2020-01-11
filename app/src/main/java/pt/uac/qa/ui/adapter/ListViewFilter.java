package pt.uac.qa.ui.adapter;

import android.widget.Filter;

final public class ListViewFilter<T> extends Filter {

    private final ListViewAdapter<T> adapter;

    public ListViewFilter(ListViewAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        return null;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

    }
}
