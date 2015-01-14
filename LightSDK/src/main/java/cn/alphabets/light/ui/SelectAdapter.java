package cn.alphabets.light.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.alphabets.light.R;


/**
 * Select
 * Created by lin on 14/12/28.
 */
public class SelectAdapter extends ArrayAdapter<SelectAdapter.SelectItem> {

    public static class SelectItem {
        String title;
        String value;
        boolean isChecked;
        public SelectItem(String title, String value) {
            this.title = title;
            this.value = value;
            this.isChecked = false;
        }
        public SelectItem(String title, String value, boolean isChecked) {
            this.title = title;
            this.value = value;
            this.isChecked = isChecked;
        }
    }

    public SelectAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.activity_select_item, null);
        }

        SelectItem item = getItem(position);

        TextView title = (TextView) convertView.findViewById(R.id.select_item_title);
        title.setText(item.title);

        return convertView;
    }
}
