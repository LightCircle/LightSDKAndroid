package cn.alphabets.light.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;

import cn.alphabets.light.R;
import cn.alphabets.light.application.ABActivity;
import cn.alphabets.light.application.ABSwipeBackActivity;

public class EditActivity extends ABSwipeBackActivity {

    public static final String VALUE        = "value";
    public static final String TITLE        = "title";
    public static final String DEFAULT      = "defaults";
    public static final String INPUT_TYPE   = "inputType";
    public static final String LINE_TYPE    = "lineType";
    public static final String READONLY     = "readonly";

    public static final String SINGLE_LINE  = "single";
    public static final String MULTI_LINE   = "multi";

    public static final String THEME        = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // 设定Theme，需要在setContentView之前调用
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int resourceId = extras.getInt(THEME, 0);
            if (resourceId != 0) {
                setTheme(resourceId);
            }
        }
        super.onCreate(savedInstanceState);
        Helper.setShowSoftKeyboard(this);
        Helper.setNoIconBackActionBar(this, "Edit");
        setContentView(R.layout.activity_edit);

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

            boolean isReadOnly = extras.getBoolean(READONLY);
            edit.setEnabled(!isReadOnly);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        boolean isReadOnly = getIntent().getExtras().getBoolean(READONLY);
        if (!isReadOnly) {

            MenuItem item = menu.add(Menu.NONE, android.R.id.edit, 1, R.string.save);
            item.setIcon(R.drawable.bar_btn_save);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

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
