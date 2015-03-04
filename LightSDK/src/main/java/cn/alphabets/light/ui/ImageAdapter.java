package cn.alphabets.light.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import cn.alphabets.light.R;
import cn.alphabets.light.network.VolleyManager;

/**
 * Image
 * Created by lin on 14/12/28.
 */
public class ImageAdapter extends ArrayAdapter<ImageAdapter.ImageItem> {

    private int itemSize;

    public static class ImageItem {
        Bitmap image;
        String imageName;
        String imageUrl;

        public ImageItem(Bitmap image) {
            this.image = image;
        }

        public ImageItem(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public ImageItem(String imageName, String imageUrl) {
            this.imageName = imageName;
            this.imageUrl = imageUrl;
        }

        public boolean isUrl() {
            return image == null;
        }
    }

    public ImageAdapter(Context context, int resource) {
        super(context, resource);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        itemSize = size.x / 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.activity_image_item, null);
        }

        ImageItem item = getItem(position);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        if (item.isUrl()) {
            VolleyManager.loadImage(item.imageUrl, imageView);
        } else {
            imageView.setImageBitmap(item.image);
        }

//        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x / 3;
//        int height = width;
//        convertView.setLayoutParams(new ListView.LayoutParams(itemSize, itemSize));

        return convertView;
    }
}
