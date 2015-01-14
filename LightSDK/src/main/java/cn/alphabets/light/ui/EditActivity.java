package cn.alphabets.light.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import cn.alphabets.light.R;
import cn.alphabets.light.application.ABActivity;

public class EditActivity extends ABActivity {

    public static final String VALUE = "value";
    public static final String TITLE = "title";
    public static final String DEFAULT = "defaults";
    public static final String INPUT_TYPE = "inputType";
    public static final String LINE_TYPE = "lineType";

    public static final String SINGLE_LINE = "single";
    public static final String MULTI_LINE = "multi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Helper.setShowSoftKeyboard(this);
        Helper.setNoIconBackActionBar(this, "Edit");
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(TITLE);
            setTitle(title);

            String defaults = extras.getString(DEFAULT);
            EditText edit = (EditText) findViewById(R.id.edit_text);
            edit.setText(defaults == null ? "" : defaults);

            int inputType = extras.getInt(INPUT_TYPE);
            edit.setInputType(inputType);

            String lineType = extras.getString(LINE_TYPE);
            if (SINGLE_LINE.equalsIgnoreCase(lineType)) {
                edit.setLines(1);
                if (inputType == 0) {
                    edit.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            } else {
                edit.setLines(4);
                edit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(Menu.NONE, android.R.id.edit, 1, "Save");
        item.setIcon(R.drawable.tool_diskette);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // 返回
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        // 保存
        if (id == android.R.id.edit) {

            EditText edit = (EditText) findViewById(R.id.edit_text);
            Intent data = new Intent();
            data.putExtra(VALUE, edit.getText().toString());
            setResult(RESULT_OK, data);

            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
