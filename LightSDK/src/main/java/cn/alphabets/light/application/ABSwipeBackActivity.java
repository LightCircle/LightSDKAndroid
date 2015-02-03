package cn.alphabets.light.application;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import cn.alphabets.light.R;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Created by 罗浩 on 15/2/2.
 */
public class ABSwipeBackActivity extends ABActivity implements SwipeBackActivityBase {

    private ABSwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new ABSwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    private class ABSwipeBackActivityHelper {

        private Activity mActivity;

        private SwipeBackLayout mSwipeBackLayout;

        public ABSwipeBackActivityHelper(Activity activity) {
            mActivity = activity;
        }

        public void onActivityCreate() {
            mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
            mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity).inflate(
                    R.layout.swipeback_layout, null);
            mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
                @Override
                public void onScrollStateChange(int state, float scrollPercent) {
                }

                @Override
                public void onEdgeTouch(int edgeFlag) {
                    Utils.convertActivityToTranslucent(mActivity);
                }

                @Override
                public void onScrollOverThreshold() {

                }
            });
        }

        public void onPostCreate() {
            mSwipeBackLayout.attachToActivity(mActivity);
        }

        public View findViewById(int id) {
            if (mSwipeBackLayout != null) {
                return mSwipeBackLayout.findViewById(id);
            }
            return null;
        }

        public SwipeBackLayout getSwipeBackLayout() {
            return mSwipeBackLayout;
        }
    }
}
