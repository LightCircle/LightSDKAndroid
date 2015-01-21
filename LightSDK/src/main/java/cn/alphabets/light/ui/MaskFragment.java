package cn.alphabets.light.ui;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * Mask
 * Created by luo on 14/12/2.
 */
public class MaskFragment extends DialogFragment {

    public static final String TAG = "LOADING_MASK";

    private boolean isAdded = false;

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
        ProgressBar progress = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);

        layout.setLayoutParams(params);
        layout.addView(progress);

        return layout;
    }

    public void show(FragmentManager manager) {
        if (isAdded) {
            return;
        }

        isAdded = true;
        this.show(manager, TAG);
    }

    public void hide() {
        isAdded = false;
        this.dismiss();
    }
}
