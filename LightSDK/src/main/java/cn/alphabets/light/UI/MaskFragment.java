package cn.alphabets.light.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import cn.alphabets.light.R;

/**
 * Created by 罗浩 on 14/11/20.
 */
public class MaskFragment extends DialogFragment {
    public static String TAG = "LOADING_MASK";
    private static int layout = -1;

    public static int getLayout() {
        return layout;
    }

    public static void setLayout(int layout) {
        MaskFragment.layout = layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setCancelable(false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (layout == -1) {
            return inflater.inflate(R.layout.fragment_mask, container, false);
        } else {

            return inflater.inflate(layout, container, false);
        }
    }

}
