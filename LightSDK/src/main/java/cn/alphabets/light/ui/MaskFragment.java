package cn.alphabets.light.ui;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Mask
 * Created by luo on 14/12/2.
 */
public class MaskFragment extends DialogFragment {

    public static final String TAG = "LOADING_MASK";

    private boolean isAdded = false;
    private boolean isProgress = false;
    private TextView mProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 设定背景为透明
        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // 设定状态
        this.setCancelable(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 添加进度框
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);

        ProgressBar cycle = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        layout.addView(cycle);

        // 显示进度
        if (isProgress) {
            mProgress = new TextView(getActivity());
            mProgress.setGravity(Gravity.CENTER_HORIZONTAL);
            mProgress.setTextColor(Color.LTGRAY);
            layout.addView(mProgress);
        }

        return layout;
    }

    public void updateProgress(final int progress) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println(progress);
                mProgress.setText(progress + "%");
            }
        });
    }

    public void show(FragmentManager manager) {
        this.show(manager, false);
    }

    public void show(FragmentManager manager, boolean progress) {
        if (isAdded) {
            return;
        }

        isProgress = progress;
        isAdded = true;
        this.show(manager, TAG);
    }

    public void hide() {
        isAdded = false;

        try {
            this.dismiss();
        } catch (IllegalStateException e) {
            // 忽略该异常，发生的条件尚不明
        }
    }
}
