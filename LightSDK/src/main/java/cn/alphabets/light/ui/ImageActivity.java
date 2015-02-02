package cn.alphabets.light.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;

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

    public static final String INTENT_IMAGES    = "images";
    public static final String VALUE            = "value";
    public static final String VALUE_TITLE      = "value_title";
    public static final String TITLE            = "title";
    public static final String READONLY         = "readonly";
    public static final String SCALED_WIDTH     = "scaled_width";
    public static final String THEME            = "theme";

    public static final int PICK_PHOTO = 1;

    private int mScaledWidth;

    /** 图像一览 */
    private ImageAdapter mAdapter;
    private GridView mGridView;

    private Menu mMenu;

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

        Helper.setNoIconBackActionBar(this, "ImageList");
        setContentView(R.layout.activity_image);

        List<ImageAdapter.ImageItem> images = new ArrayList<>();
        boolean isReadOnly = false;
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

            isReadOnly = extras.getBoolean(READONLY);
        }

        mAdapter = new ImageAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
        mAdapter.addAll(images);

        // 点击预览
        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mMenu.findItem(android.R.id.empty) == null || !mMenu.findItem(android.R.id.empty).isVisible()) {
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
        if (!isReadOnly) {
            mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (mMenu.findItem(android.R.id.edit).isVisible()) {
                        for (int j = 0; j < mAdapter.getCount(); j++) {
                            View v = mGridView.getChildAt(j);
                            CheckBox cb = (CheckBox) v.findViewById(R.id.check);
                            cb.setVisibility(View.VISIBLE);
                        }
                        MenuItem addItem = mMenu.findItem(android.R.id.edit);
                        addItem.setVisible(false);

                        MenuItem removeItem = mMenu.findItem(android.R.id.empty);
                        if (removeItem == null) {
                            removeItem = mMenu.add(Menu.NONE, android.R.id.empty, 1, "Delete");
                            removeItem.setTitle(getResources().getString(R.string.remove));
                            removeItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                        } else {
                            removeItem.setVisible(true);
                        }

                        MenuItem cancelItem = mMenu.findItem(android.R.id.closeButton);
                        if (cancelItem == null) {
                            cancelItem = mMenu.add(Menu.NONE, android.R.id.closeButton, 1, "Cancel");
                            cancelItem.setTitle(getResources().getString(R.string.cancel));
                            cancelItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                        } else {
                            cancelItem.setVisible(true);
                        }
                    }
                    return true;
                }
            });
        } else {
            mGridView.setOnLongClickListener(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        boolean isReadOnly = getIntent().getExtras().getBoolean(READONLY);
        if (!isReadOnly) {
            MenuItem item = menu.add(Menu.NONE, android.R.id.edit, 1, R.string.save);
            item.setIcon(R.drawable.tool_plus_white);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        this.mMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 返回
        if (id == android.R.id.home) {

            ArrayList<String> images = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                images.add(mAdapter.getItem(i).imageUrl);
                titles.add(mAdapter.getItem(i).imageName);
            }

            Intent data = new Intent();
            data.putStringArrayListExtra(VALUE, images);
            data.putStringArrayListExtra(VALUE_TITLE, titles);
            setResult(RESULT_OK, data);

            onBackPressed();
            return true;
        }

        // 添加
        if (id == android.R.id.edit) {
            Dialog.takePhoto(this, PICK_PHOTO);
            return true;
        }

        // 删除
        if (id == android.R.id.empty) {
            List<ImageAdapter.ImageItem> removeItems = new ArrayList<ImageAdapter.ImageItem>();
            for (int j = 0, count = mAdapter.getCount(); j < count; j++) {
                View v = mGridView.getChildAt(j);
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

            MenuItem addItem = mMenu.findItem(android.R.id.edit);
            addItem.setVisible(true);
            MenuItem cancelItem = mMenu.findItem(android.R.id.closeButton);
            cancelItem.setVisible(false);
            item.setVisible(false);
        }

        // 取消删除模式
        if (id == android.R.id.closeButton) {
            for (int j = 0, count = mAdapter.getCount(); j < count; j++) {
                View v = mGridView.getChildAt(j);
                CheckBox cb = (CheckBox) v.findViewById(R.id.check);
                if (cb.isChecked()) {
                    cb.setChecked(false);
                }
                cb.setVisibility(View.GONE);
            }

            MenuItem addItem = mMenu.findItem(android.R.id.edit);
            addItem.setVisible(true);
            MenuItem removeItem = mMenu.findItem(android.R.id.empty);
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
            final String photoName = new File(photo).getName();

            boolean isFromCamera = (data == null);
            int scaledWidth = mScaledWidth > 0 ? mScaledWidth : Default.ScaledWidth;
            final String bitmap = FileUtil.scaledBitmap(photo, scaledWidth, isFromCamera);
            UPLOAD(Default.UrlSendFile, new Parameter().put(bitmap, new File(bitmap)), new Success() {
                @Override
                public void onResponse(JSONObject response) {

                    GsonParser<ModelFile> files = GsonParser.fromJson(response, ModelFile.getListTypeToken());
                    String fileId = files.getData().getItems().get(0).get_id();

                    mAdapter.add(new ImageAdapter.ImageItem(photoName, fileId));
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
