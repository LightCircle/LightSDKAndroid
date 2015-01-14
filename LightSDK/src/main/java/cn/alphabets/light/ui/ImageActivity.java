package cn.alphabets.light.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.alphabets.light.R;
import cn.alphabets.light.application.ABActivity;
import cn.alphabets.light.model.GsonParser;
import cn.alphabets.light.model.ModelFile;
import cn.alphabets.light.network.Parameter;
import cn.alphabets.light.setting.Default;
import cn.alphabets.light.util.FileUtil;

public class ImageActivity extends ABActivity {

    public static final String INTENT_IMAGES = "images";
    public static final String VALUE = "value";
    public static final String TITLE = "title";
    public static final String SCALED_WIDTH = "scaled_width";

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    private int mScaledWidth;

    /** 图像一览 */
    private ImageAdapter mAdapter;
    private GridView gridView;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Helper.setNoIconBackActionBar(this, "ImageList");
        setContentView(R.layout.activity_image);

        Bundle extras = getIntent().getExtras();
        List<ImageAdapter.ImageItem> images = new ArrayList<>();
        if (extras != null) {
            // 设置自定义标题
            String title = extras.getString(TITLE);
            if (title != null) {
                setTitle(title);
            }

            // 获取传入的图片一览
            String[] files = extras.getStringArray(INTENT_IMAGES);
            if (files != null) {
                for (String fid : files) {
                    images.add(new ImageAdapter.ImageItem(fid));
                }
            }

            // 获取图片缩放宽度大小
            mScaledWidth = extras.getInt(SCALED_WIDTH);
        }

        mAdapter = new ImageAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
        mAdapter.addAll(images);

        // 点击预览
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (menu.findItem(android.R.id.edit).isVisible()) {
                    ArrayList<String> photoUriList = new ArrayList<String>();
                    for (int i = 0; i < mAdapter.getCount(); i++) {
                        ImageAdapter.ImageItem item = mAdapter.getItem(i);
                        photoUriList.add(item.imageUrl);
                    }

                    Intent intent = new Intent(ImageActivity.this, PreviewActivity.class);
                    intent.putExtra(PreviewActivity.INDEX, position);
                    intent.putStringArrayListExtra(PreviewActivity.IMAGES, photoUriList);
                    startActivity(intent);
                } else {
                    CheckBox cb = (CheckBox)view.findViewById(R.id.check);
                    if (cb.isChecked()) {
                        cb.setChecked(false);
                    } else {
                        cb.setChecked(true);
                    }
                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (menu.findItem(android.R.id.edit).isVisible()) {
                    for (int j = 0; j < mAdapter.getCount(); j++) {
                        View v = gridView.getChildAt(j);
                        CheckBox cb = (CheckBox) v.findViewById(R.id.check);
                        cb.setVisibility(View.VISIBLE);
                    }
                    MenuItem addItem = menu.findItem(android.R.id.edit);
                    addItem.setVisible(false);

                    MenuItem removeItem = menu.findItem(android.R.id.empty);
                    if (removeItem == null) {
                        removeItem = menu.add(Menu.NONE, android.R.id.empty, 1, "Delete");
                        removeItem.setTitle(getResources().getString(R.string.remove));
                        removeItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    } else {
                        removeItem.setVisible(true);
                    }

                    MenuItem cancelItem = menu.findItem(android.R.id.closeButton);
                    if (cancelItem == null) {
                        cancelItem = menu.add(Menu.NONE, android.R.id.closeButton, 1, "Cancel");
                        cancelItem.setTitle(getResources().getString(R.string.cancel));
                        cancelItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    } else {
                        cancelItem.setVisible(true);
                    }
                }
                return false;
            }
        });

        // TODO: 一次选择多张图片
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(Menu.NONE, android.R.id.edit, 1, "Add");
        item.setIcon(R.drawable.tool_plus);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 返回
        if (id == android.R.id.home) {

            ArrayList<String> images = new ArrayList<>();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                images.add(mAdapter.getItem(i).imageUrl);
            }

            if (images.size() > 0) {
                Intent data = new Intent();
                data.putStringArrayListExtra(VALUE, images);
                setResult(RESULT_OK, data);
            }

            onBackPressed();
            return true;
        }

        // 添加
        if (id == android.R.id.edit) {
            Dialog.takePhoto(this);
            return true;
        }

        // 删除
        if (id == android.R.id.empty) {
            List<ImageAdapter.ImageItem> removeItems = new ArrayList<ImageAdapter.ImageItem>();
            for (int j = 0, count = mAdapter.getCount(); j < count; j++) {
                View v = gridView.getChildAt(j);
                CheckBox checkBox = (CheckBox) v.findViewById(R.id.check);
                if (checkBox.isChecked()) {
                    removeItems.add(mAdapter.getItem(j));
                }
                checkBox.setVisibility(View.GONE);
            }
            for (ImageAdapter.ImageItem imageObj : removeItems) {
                mAdapter.remove(imageObj);
            }
            mAdapter.notifyDataSetChanged();

            MenuItem addItem = menu.findItem(android.R.id.edit);
            addItem.setVisible(true);
            MenuItem cancelItem = menu.findItem(android.R.id.closeButton);
            cancelItem.setVisible(false);
            item.setVisible(false);
        }

        // 取消删除模式
        if (id == android.R.id.closeButton) {
            for (int j = 0, count = mAdapter.getCount(); j < count; j++) {
                View v = gridView.getChildAt(j);
                CheckBox cb = (CheckBox) v.findViewById(R.id.check);
                if (cb.isChecked()) {
                    cb.setChecked(false);
                }
                cb.setVisibility(View.GONE);
            }

            MenuItem addItem = menu.findItem(android.R.id.edit);
            addItem.setVisible(true);
            MenuItem removeItem = menu.findItem(android.R.id.empty);
            removeItem.setVisible(false);
            item.setVisible(false);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            String photo = Dialog.parsePhoto(requestCode, resultCode, data);

            int scaledWidth = mScaledWidth > 0 ? mScaledWidth : Default.ScaledWidth;
            String bitmap = FileUtil.scaledBitmap(photo, scaledWidth);
            UPLOAD(Default.UrlSendFile, new Parameter().put(bitmap, new File(bitmap)), new Success() {
                @Override
                public void onResponse(JSONObject response) {

                    GsonParser<ModelFile> files = GsonParser.fromJson(response, ModelFile.getListTypeToken());
                    String fileId = files.getData().getItems().get(0).get_id();

                    mAdapter.add(new ImageAdapter.ImageItem(fileId));
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
