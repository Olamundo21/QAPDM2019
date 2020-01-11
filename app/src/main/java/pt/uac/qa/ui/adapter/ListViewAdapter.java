package pt.uac.qa.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

public abstract class ListViewAdapter<T> extends BaseAdapter implements Filterable {
    protected List<T> sourceItems;
    protected List<T> displayItems;
    protected Context context;

    protected ListViewAdapter(final Context context) {
        sourceItems = new ArrayList<>();
        displayItems = sourceItems;
    }

    public void loadItems(List<T> items) {
        sourceItems = items;
        displayItems = sourceItems;
    }

    @Override
    public int getCount() {
        return displayItems.size();
    }

    @Override
    public Object getItem(int i) {
        return displayItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        T item = displayItems.get(i);
        ViewHolder<T> viewHolder;

        if (convertView == null){
            convertView = inflateView();
            viewHolder = createViewHolder();
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder<T>) convertView.getTag();
        }
        viewHolder.display(item);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    protected abstract View inflateView();
    protected abstract ViewHolder<T> createViewHolder();
    protected abstract boolean acceptsItem(T item, CharSequence constrain);
}
