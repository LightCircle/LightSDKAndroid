package cn.alphabets.light.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import cn.alphabets.light.R;
import cn.alphabets.light.application.ABSwipeBackActivity;
import cn.alphabets.light.util.DateTimeUtil;

public class DateTimeActivity extends ABSwipeBackActivity {

    public static final String VALUE          = "value";
    public static final String TITLE          = "title";
    public static final String DEFAULT        = "defaults";
    public static final String READONLY       = "readonly";
    public static final String THEME          = "theme";
    public static final String DATE_ONLY      = "date_only";
    public static final String FORMAT         = "format";


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
        setContentView(R.layout.activity_datetime);

        if (extras != null) {
            String title = extras.getString(TITLE);
            setTitle(title);

            boolean isDateOnly = extras.getBoolean(DATE_ONLY);
            String defaults = extras.getString(DEFAULT);
            String format = extras.getString(FORMAT);

            Calendar calendar = Calendar.getInstance();

            DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
            if (defaults != null && !"".equals(defaults)) {
                if (format != null && !"".equals(format)) {
                    calendar.setTime(DateTimeUtil.parse(defaults, format));
                } else {
                    calendar.setTime(DateTimeUtil.parse(defaults, isDateOnly));
                }
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), null);
            }
            // 判断是否需要显示小时分钟
            if (!isDateOnly) {
                TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
                timePicker.setVisibility(View.VISIBLE);
                if (defaults != null && !"".equals(defaults)) {
                    timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                    timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                }
            }
        }
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

            Calendar calendar = Calendar.getInstance();
            DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
            boolean isDateOnly = getIntent().getExtras().getBoolean(DATE_ONLY);
            if (!isDateOnly) {
                TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(), timePicker.getCurrentMinute());
            } else {
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            }

            String dateStr = "";
            String format = getIntent().getExtras().getString(FORMAT);
            if (format != null && !"".equals(format)) {
                dateStr = DateTimeUtil.format(calendar.getTime(), format);
            } else {
                dateStr = DateTimeUtil.format(calendar.getTime(), isDateOnly);
            }
            Intent data = new Intent();
            data.putExtra(VALUE, dateStr);
            setResult(RESULT_OK, data);

            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
