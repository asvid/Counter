package widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import asvid.beercounter.R;
import asvid.beercounter.data.CounterItem;

/**
 * Created by adam on 15.01.17.
 */
public class CounterListAdapter extends ArrayAdapter<CounterItem> {

    private final int viewResourceId;
    List<CounterItem> items;

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).getId();
    }

    public CounterListAdapter(Context context, int viewResourceId,
                              List<CounterItem> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        CounterItem item = items.get(position);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView value = (TextView) v.findViewById(R.id.value);
        name.setText(item.getName());
        value.setText(item.getValue().toString());
        return v;
    }
}
