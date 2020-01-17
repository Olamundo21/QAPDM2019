package pt.uac.qa.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrício Cordeiro <patricio.cordeiro@gmail.com> on 10-01-2020.
 */
public abstract class ListViewAdapter<T> extends BaseAdapter implements Filterable {
    protected List<T> sourceItems;
    protected List<T> displayItems;
    protected Context context;

    protected ListViewAdapter(final Context context) {
        this.context = context;
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
    public Object getItem(int position) {
        return displayItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T item = displayItems.get(position);
        ViewHolder<T> viewHolder;

        if (convertView == null) {
            convertView = inflateView();
            viewHolder = createViewHolder();
            viewHolder.init(convertView);
            convertView.setTag(viewHolder);
        } else {
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
    protected abstract boolean acceptsItem(T item, CharSequence constraint);
}
