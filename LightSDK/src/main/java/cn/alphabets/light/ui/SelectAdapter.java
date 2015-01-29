package cn.alphabets.light.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.Serializable;

import cn.alphabets.light.R;


/**
 * Select
 * Created by lin on 14/12/28.
 */
public class SelectAdapter extends ArrayAdapter<SelectAdapter.SelectItem> {

    private boolean isMultiple;
    private boolean isReadOnly;

    public static class SelectItem implements Serializable {
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

    public SelectAdapter(Context context, int resource, boolean isMultiple, boolean isReadOnly) {
        super(context, resource);
        this.isMultiple = isMultiple;
        this.isReadOnly = isReadOnly;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.activity_select_item, null);
        }

        final SelectItem item = getItem(position);

        TextView title = (TextView) convertView.findViewById(R.id.select_item_title);
        title.setText(item.title);

        final CheckBox checked = (CheckBox) convertView.findViewById(R.id.checked);
        if (!isMultiple) {
            checked.setVisibility(View.GONE);
        } else {
            checked.setVisibility(View.VISIBLE);
            checked.setChecked(item.isChecked);
            if (isReadOnly) {
                checked.setEnabled(false);
            }
            checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checked.isChecked()) {
                        checked.setChecked(true);
                        item.isChecked = true;
                    } else {
                        checked.setChecked(false);
                        item.isChecked = false;
                    }
                }
            });
        }

        return convertView;
    }
}
