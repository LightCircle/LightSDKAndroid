package cn.alphabets.light.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.alphabets.light.R;
import cn.alphabets.light.application.ABActivity;
import cn.alphabets.light.model.GsonParser;
import cn.alphabets.light.model.ModelCategory;
import cn.alphabets.light.network.Parameter;
import cn.alphabets.light.setting.Default;

/**
 * 选择
 */
public class SelectActivity extends ABActivity {


    public static final String VALUE = "value";
    public static final String VALUE_TEXT = "value_text";

    public static final String TITLE = "title";
    public static final String LIST = "list";
    public static final String DEFAULT = "default";
    public static final String MULTIPLE = "multiple";   //
    public static final String MODEL = "model";         // 传递数据的方式

    private boolean isMultiple = true;

    private SelectAdapter mAdapter;

    private static Parameter iParameter;
    public static void setParams(Parameter parameter) {
        iParameter = parameter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.setNoIconBackActionBar(this, "Select");
        setContentView(R.layout.activity_select);

        // 设定标题
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(TITLE);
            if (title != null) {
                setTitle(title);
            }
        }

        // 初始化Adapter
        mAdapter = new SelectAdapter(this, R.layout.activity_select);
        ListView listView = (ListView) findViewById(R.id.select_list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SelectAdapter.SelectItem item = mAdapter.getItem(position);
                if (isMultiple) {
                    // TODO: 多行模式
                } else {
                    Intent data = new Intent();
                    data.putExtra(VALUE, item.value);
                    data.putExtra(VALUE_TEXT, item.title);
                    setResult(RESULT_OK, data);
                    onBackPressed();
                }
            }
        });

        // 一览数据
        if (extras != null) {

            // 单选，多选模式
            isMultiple = extras.getBoolean(MULTIPLE);

            String model = extras.getString(MODEL);
            if (model != null && model.equals("data")) {
                setData(extras.getStringArray(LIST));
            } else {
                fetchListData();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (isMultiple) {
            MenuItem item = menu.add(Menu.NONE, android.R.id.edit, 1, "Save");
            item.setIcon(R.drawable.tool_diskette);
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

        // TODO: 保存
        if (id == android.R.id.edit) {

            EditText edit = (EditText) findViewById(R.id.edit_text);
            Intent data = new Intent();
//            data.putExtra(VALUE, edit.getText().toString());
            setResult(RESULT_OK, data);

            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 获取分类信息显示
     */
    private void fetchListData() {
        GET(Default.UrlCategoryList, iParameter, new Success() {
            @Override
            public void onResponse(JSONObject response) {

                GsonParser<ModelCategory> category = GsonParser.fromJson(response, ModelCategory.getListTypeToken());
                List<ModelCategory> list = category.getData().getItems();

                List<SelectAdapter.SelectItem> items = new ArrayList<>();
                for (ModelCategory item : list) {
                    items.add(new SelectAdapter.SelectItem(item.getName(), item.getCategoryId()));
                }

                mAdapter.addAll(items);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 显示给定的数据显示
     * @param lists
     */
    private void setData(String[] lists) {
        if (lists != null) {
            // TODO:
        }
    }
}
