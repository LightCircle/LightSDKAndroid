package cn.alphabets.light.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import cn.alphabets.light.R;
import cn.alphabets.light.application.ABActivity;
import cn.alphabets.light.util.FileUtil;

/**
 * Created by sh on 15/1/28.
 */
public class FileActivity extends ABActivity {

    public static final String LIST        = "list";
    public static final String TITLE        = "title";
    public static final String READONLY     = "readonly";
    public static final String THEME        = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设定Theme，需要在setContentView之前调用
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int resourceId = extras.getInt(THEME, 0);
            if (resourceId != 0) {
                setTheme(resourceId);
            }
        }

        Helper.setNoIconBackActionBar(this, "Edit");
        setContentView(R.layout.activity_file);

        ArrayList<FileAdapter.FileItem> values = new ArrayList<FileAdapter.FileItem>();
        if (extras != null) {
            String title = extras.getString(TITLE);
            setTitle(title);

            if (extras.get(LIST) != null) {
                values.addAll((ArrayList<FileAdapter.FileItem>)extras.get(LIST));
            }
        }

        ListView fileList = (ListView) findViewById(R.id.file_list);
        final FileAdapter mAdapter = new FileAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
        mAdapter.addAll(values);
        fileList.setAdapter(mAdapter);

        fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FileAdapter.FileItem item = mAdapter.getItem(i);
                if (item.file != null) {
                    FileUtil.openFile(FileActivity.this, item.file);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        boolean isReadOnly = getIntent().getExtras().getBoolean(READONLY);
        if (!isReadOnly) {

            MenuItem item = menu.add(Menu.NONE, android.R.id.edit, 1, "Save");
            item.setIcon(R.drawable.tool_diskette_white);
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
            setResult(RESULT_OK, data);

            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
