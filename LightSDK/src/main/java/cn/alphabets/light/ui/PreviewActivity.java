package cn.alphabets.light.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.alphabets.light.R;
import cn.alphabets.light.application.ABActivity;
import cn.alphabets.light.application.ABSwipeBackActivity;
import cn.alphabets.light.network.VolleyManager;
import uk.co.senab.photoview.PhotoView;

public class PreviewActivity extends ABSwipeBackActivity implements ViewPager.OnPageChangeListener {

    public static final String IMAGES = "images";
    public static final String INDEX = "index";

    List<String> mPhotoUriList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_preview);

        Bundle extras = getIntent().getExtras();
        int index = extras.getInt(INDEX);
        mPhotoUriList = extras.getStringArrayList(IMAGES);
        if (mPhotoUriList == null) {
            mPhotoUriList = new ArrayList<String>();
        }

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        pager.setAdapter(adapter);
        pager.setCurrentItem(index);
        pager.setOnPageChangeListener(this);

        pager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onPageScrolled(int i, float v, int i2) {
    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }


    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 使用PhotoView控件来预览图片
            PhotoView photoView = new PhotoView(container.getContext());
            VolleyManager.loadImage(mPhotoUriList.get(position), photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public int getCount() {
            return mPhotoUriList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }
}
