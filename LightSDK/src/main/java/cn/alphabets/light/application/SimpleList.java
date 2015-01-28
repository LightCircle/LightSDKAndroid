package cn.alphabets.light.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import cn.alphabets.light.R;
import cn.alphabets.light.network.ContextManager;
import cn.alphabets.light.ui.PreviewActivity;

/**
 * 通用List
 * 包含一个左侧图标，一个左侧标题文本，一个右侧内容文本，一个右指示器图标
 * Created by lin on 14/12/12.
 */
public class SimpleList extends ArrayAdapter<SimpleList.Pair> {

    /** 字体 */
    private static final int DEFAULT_FONT = 16;

    /** 无头像时的行距离调整 dp单位 */
    private static final int DEFAULT_PADDING = 10;

    /** 左边宽 dp单位 */
    private static final int DEFAULT_LEFT_WIDTH = 125;

    /** 是否有右向指示器 */
    private boolean showIndicator = true;

    /** 列表行的资源ID */
    private int resource;

    /** 是否只读 */
    private boolean enable = true;

    /** 父类View */
    private View containerView;

    /** 值的文字颜色 */
    private static final String DEFAULT_VALUE_COLOR = "#727272";


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
         * @param showIndicator indicator
         * @param enable enable
         */
        public ListRowView(final Context context, final Pair pair, boolean showIndicator, boolean enable) {

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
            titleView.setTextSize(DEFAULT_FONT);
            titleView.setText(pair.title);
            titleView.setTextColor(enable && pair.enable ? Color.BLACK : Color.parseColor(DEFAULT_VALUE_COLOR));
            addView(titleView);

            // Right value text view
            TextView valueView = new TextView(context);
            valueView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
            valueView.setGravity(Gravity.RIGHT);
            valueView.setSingleLine(false);
            valueView.setTextColor(Color.parseColor(DEFAULT_VALUE_COLOR));
            valueView.setTextSize(DEFAULT_FONT);
            valueView.setText(pair.value);
            addView(valueView);

            // Right image view
            if (pair.image != null) {
                ImageView image = new ImageView(context);
                int height = pair.imageHeight > 0 ? pair.imageHeight : 50;
                LayoutParams imageParams = new LayoutParams(pixel(height), pixel(height));
                image.setLayoutParams(imageParams);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setImageBitmap(pair.image);
                addView(image);

                image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> imageIdList = new ArrayList<String>(){{
                            add(pair.getImageId());
                        }};
                        Intent intent = new Intent(context, PreviewActivity.class);
                        intent.putStringArrayListExtra(PreviewActivity.IMAGES, imageIdList);
                        context.startActivity(intent);
                    }
                });
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
        private String title;           // 标题（左）
        private int icon;               // 图标（左）
        private String value;           // 值（右）
        private Bitmap image;           // 图（右）
        private String imageId;         // 图的id
        private int imageHeight;        // 图大小
        private boolean enable = true;  // 可编辑
        private boolean indicator;      // 右向剪头

        public void setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public void setValue(String value) {
            this.value = value;
        }
        public void setValue(int valueRes) {
            setValue(r(valueRes));
        }

        public String getValue() {
            return this.value;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public void setTitle(int titleRes) {
            setTitle(r(titleRes));
        }

        public void setBitmap(Bitmap image) {
            this.image = image;
        }

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public Pair(String title, String value, boolean indicator, int icon, Bitmap image) {
            this.title = title;
            this.value = value;
            this.indicator = indicator;
            this.icon = icon;
            this.image = image;
        }
        public Pair(int titleRes, String value, boolean indicator, int icon, Bitmap image) {
            this(r(titleRes), value, indicator, icon, image);
        }

        public Pair(String title, String value) {
            this(title, value, false, 0, null);
        }
        public Pair(int titleRes, String value) {
            this(r(titleRes), value, false, 0, null);
        }

        public Pair(String title, String value, boolean indicator) {
            this(title, value, indicator, 0, null);
        }
        public Pair(int titleRes, String value, boolean indicator) {
            this(r(titleRes), value, indicator, 0, null);
        }
        public Pair(int titleRes, String value, boolean indicator, int icon) {
            this(r(titleRes), value, indicator, icon, null);
        }
        public Pair(int titleRes, int valueRes, boolean indicator, int icon) {
            this(r(titleRes), r(valueRes), indicator, icon, null);
        }
        public Pair(String title, int valueRes, boolean indicator, int icon) {
            this(title, r(valueRes), indicator, icon, null);
        }

        // resource to string
        private static String r (int resource) {
            return ContextManager.getInstance().getResources().getString(resource);
        }
    }

    public SimpleList(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    public SimpleList(Context context, View containerView, int resource) {
        super(context, resource);
        this.containerView = containerView;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pair pair = getItem(position);
        return new ListRowView(getContext(), pair, this.showIndicator, this.enable);
    }

    /**
     * 绑定视图，设定点击事件
     * @param onClick click event
     */
    public void bindListView(final Click onClick) {

        ListView view = (ListView) ((Activity) getContext()).findViewById(this.resource);
        if (this.containerView != null) {
            view = (ListView) this.containerView.findViewById(this.resource);
        }

        view.setAdapter(this);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                boolean canEdit = enable && getItem(position).enable;
                if (onClick != null && canEdit) {
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

    public void setEnable(boolean isEnable) {
        this.enable = isEnable;
    }

}
