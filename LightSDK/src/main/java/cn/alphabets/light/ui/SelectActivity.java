package cn.alphabets.light.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
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


    public static final String VALUE            = "value";
    public static final String VALUE_TEXT       = "value_text";

    public static final String TITLE            = "title";
    public static final String LIST             = "list";
    public static final String DEFAULT          = "default";
    public static final String MULTIPLE         = "multiple";   //
    public static final String MODEL            = "model";         // 传递数据的方式
    public static final String THEME            = "theme";
    public static final String READONLY         = "readonly";

    private boolean isMultiple = true;
    private boolean isReadOnly = true;

    private SelectAdapter mAdapter;

    private static Parameter iParameter;
    public static void setParams(Parameter parameter) {
        iParameter = parameter;
    }

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

        Helper.setNoIconBackActionBar(this, "Select");
        setContentView(R.layout.activity_select);

        // 设定标题
        if (extras != null) {
            String title = extras.getString(TITLE);
            if (title != null) {
                setTitle(title);
            }

            // 单选，多选模式
            isMultiple = extras.getBoolean(MULTIPLE);
            // 只读，可编辑模式
            isReadOnly = extras.getBoolean(READONLY);
        }

        // 初始化Adapter
        mAdapter = new SelectAdapter(this, R.layout.activity_select, isMultiple, isReadOnly);
        ListView listView = (ListView) findViewById(R.id.select_list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectAdapter.SelectItem item = mAdapter.getItem(position);
                if (isMultiple) {
                    ((CheckBox) view.findViewById(R.id.checked)).performClick();
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
            String model = extras.getString(MODEL);
            if (model != null && model.equals("data")) {
                setData((ArrayList<SelectAdapter.SelectItem>)extras.get(LIST));
            } else {
                fetchListData();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (isMultiple) {
            MenuItem item = menu.add(Menu.NONE, android.R.id.edit, 1, "Save");
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

        // 保存(只需要保存多选的)
        if (id == android.R.id.edit) {
            Intent data = new Intent();
            ArrayList<String> values = new ArrayList<String>();
            ArrayList<String> titles = new ArrayList<String>();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                SelectAdapter.SelectItem select = mAdapter.getItem(i);
                if (select.isChecked) {
                    values.add(select.value);
                    titles.add(select.title);
                }
            }
            data.putStringArrayListExtra(VALUE, values);
            data.putStringArrayListExtra(VALUE_TEXT, titles);
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
    private void setData(ArrayList<SelectAdapter.SelectItem> lists) {
        if (lists != null) {
            mAdapter.addAll(lists);
            mAdapter.notifyDataSetChanged();
        }
    }
}
