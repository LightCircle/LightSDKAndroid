package cn.alphabets.light.application;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.alphabets.light.R;

/**
 * 通用List
 * 包含一个左侧图标，一个左侧标题文本，一个右侧内容文本，一个右指示器图标
 * Created by lin on 14/12/12.
 */
public class SimpleList extends ArrayAdapter<SimpleList.Pair> {

    /** 字体 */
    private static final int DEFAULT_FONT = 12;

    /** 无头像时的行距离调整 dp单位 */
    private static final int DEFAULT_PADDING = 10;

    /** 左边宽 dp单位 */
    private static final int DEFAULT_LEFT_WIDTH = 125;

    /** 是否有右向指示器 */
    private boolean showIndicator = true;

    /** 列表行的资源ID */
    private int resource;


    /**
     * 选择行
     */
    public interface Click {
        public void done(View view, int position);
    }


    /**
     * 视图类，Programatically set view
     */
    public class ListRowView extends LinearLayout {

        /**
         * 构筑函数
         * @param context context
         * @param pair 标题
         */
        public ListRowView(Context context, Pair pair, boolean showIndicator) {

            super(context);

            // LinearLayout properties
            int padding = (pair.icon == 0) ? pixel(DEFAULT_PADDING) : 0;
            setOrientation(LinearLayout.HORIZONTAL);
            setPadding(padding, padding, 0, padding);
            setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            setGravity(Gravity.CENTER);
            setVerticalScrollBarEnabled(false);
            setHorizontalScrollBarEnabled(false);

            // Icon
            if (pair.icon > 0) {
                ImageView icon = new ImageView(context);
                LayoutParams iconParams = new LayoutParams(pixel(20), pixel(20));
                iconParams.setMargins(pixel(5), pixel(10), pixel(5), pixel(10));
                icon.setLayoutParams(iconParams);
                icon.setImageResource(pair.icon);
                icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                addView(icon);
            }

            // Left title text view
            TextView titleView = new TextView(context);
            titleView.setLayoutParams(new LayoutParams(pixel(DEFAULT_LEFT_WIDTH), LayoutParams.WRAP_CONTENT));
            titleView.setSingleLine(true);
            titleView.setText(pair.title);
            addView(titleView);

            // Right value text view
            TextView valueView = new TextView(context);
            valueView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
            valueView.setGravity(Gravity.RIGHT);
            valueView.setSingleLine(false);
            valueView.setTextColor(Color.LTGRAY);
            valueView.setTextSize(DEFAULT_FONT);
            valueView.setText(pair.value);
            addView(valueView);

            // Right image view
            if (pair.image != null) {
                ImageView image = new ImageView(context);
                LayoutParams imageParams = new LayoutParams(pixel(50), pixel(50));
                image.setLayoutParams(imageParams);
                image.setImageResource(R.drawable.ic_launcher);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setImageBitmap(pair.image);
                addView(image);
            }

            // Indicator
            ImageView indicator = new ImageView(context);
            int width = showIndicator ? pixel(15) : pixel(5);
            LayoutParams indicatorParams = new LayoutParams(width, pixel(15));
            int margins = pixel(2);
            indicatorParams.setMargins(margins, margins, margins, margins);
            indicator.setLayoutParams(indicatorParams);
            indicator.setImageResource(showIndicator && pair.indicator ? R.drawable.indicator_right : R.drawable.indicator_empty);
            indicator.setScaleType(ImageView.ScaleType.CENTER_CROP);
            addView(indicator);
        }

        private int pixel(int dip){
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
        }
    }

    /**
     * 内容类
     */
    public static class Pair {
        private String title;
        private String value;
        private Bitmap image;
        private boolean indicator;
        private int icon;

        public void setValue(String value) {
            this.value = value;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setBitmap(Bitmap image) {
            this.image = image;
        }

        public Pair(String title, String value, boolean indicator, int icon, Bitmap image) {
            this.title = title;
            this.value = value;
            this.indicator = indicator;
            this.icon = icon;
            this.image = image;
        }

        public Pair(String title, String value) {
            this(title, value, false, 0, null);
        }

        public Pair(String title, String value, boolean indicator) {
            this(title, value, indicator, 0, null);
        }

        public Pair(String title, String value, boolean indicator, int icon) {
            this(title, value, indicator, icon, null);
        }
    }

    public SimpleList(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pair pair = getItem(position);
        return new ListRowView(getContext(), pair, this.showIndicator);
    }

    /**
     * 绑定视图，设定点击事件
     * @param onClick
     */
    public void bindListView(final Click onClick) {

        ListView view = (ListView) ((Activity) getContext()).findViewById(this.resource);
        view.setAdapter(this);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onClick != null) {
                    onClick.done(view, position);
                }
            }
        });
    }

    public void bindListView() {
        bindListView(null);
    }

    public void hideIndicator() {
        this.showIndicator = false;
    }
}
