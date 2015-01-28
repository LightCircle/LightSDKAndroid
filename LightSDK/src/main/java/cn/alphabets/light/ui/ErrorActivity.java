package cn.alphabets.light.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import cn.alphabets.light.R;
import cn.alphabets.light.application.ABActivity;
import cn.alphabets.light.setting.Default;
import cn.alphabets.light.store.Eternal;

public class ErrorActivity extends ABActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Helper.setNoIconBackActionBar(this, "Error");
        setContentView(R.layout.activity_error);

        TextView error = (TextView) findViewById(R.id.text_detail);

        String lastError = Eternal.getString(Default.LastError);
        if (lastError != null) {
            error.setText(lastError);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // 返回
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
