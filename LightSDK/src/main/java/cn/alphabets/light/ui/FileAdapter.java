package cn.alphabets.light.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.Serializable;

import cn.alphabets.light.R;
import cn.alphabets.light.exception.NetworkException;
import cn.alphabets.light.log.Logger;
import cn.alphabets.light.util.FileUtil;

/**
 * Created by sh on 15/1/28.
 */
public class FileAdapter extends ArrayAdapter<FileAdapter.FileItem> {

    public static class FileItem implements Serializable {
        File file;
        String fileUrl;
        String fileName;

        public FileItem(String fileName, String fileUrl) {
            this.fileName = fileName;
            this.fileUrl = fileUrl;
        }

        public FileItem(String fileName, String fileUrl, File file) {
            this(fileName, fileUrl);
            this.file = file;
        }
    }

    public FileAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.activity_file_item, null);
        }

        final FileItem item = getItem(position);

        //显示文件名
        {
            TextView fileName = (TextView) convertView.findViewById(R.id.file_name);
            fileName.setText(item.fileName);
        }

        final ProgressBar loading = (ProgressBar) convertView.findViewById(R.id.loading);

        //下载完成显示放大镜,未下载显示下载icon
        {
            final ImageView downloadOrView = (ImageView) convertView.findViewById(R.id.download_view);
            if (item.file == null) {
                downloadOrView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_download));
            } else {
                downloadOrView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_view));
            }
        }


        //根据文件后缀显示相应的icon
        {
            final String fileEnding = FilenameUtils.getExtension(item.fileName);
            int ft_res = getContext().getResources().getIdentifier("ft_" + fileEnding, "drawable", getContext().getPackageName());
            if (ft_res <= 0) {
                ft_res = R.drawable.ft_default;
            }
            ((ImageView) convertView.findViewById(R.id.file_type_icon)).setImageDrawable(getContext().getResources().getDrawable(ft_res));
        }

        return convertView;
    }

    /**
     * 文件下载回调
     */
    public interface LoadFileListener {
        public void onResult(File file);
    }


}
