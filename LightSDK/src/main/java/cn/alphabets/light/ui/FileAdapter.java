package cn.alphabets.light.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

        TextView fileName = (TextView) convertView.findViewById(R.id.file_name);
        fileName.setText(item.fileName);

        final ProgressBar loading = (ProgressBar) convertView.findViewById(R.id.loading);
        final ImageView downloadOrView = (ImageView) convertView.findViewById(R.id.download_view);
        if (item.file == null) {
            downloadOrView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_download));
        } else {
            downloadOrView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_view));
        }

        final View view = convertView;
        final String fileEnding = item.fileName.substring(item.fileName.lastIndexOf("."));
        int ft_res = getContext().getResources().getIdentifier("ft_" + fileEnding.replace(".", ""), "drawable", getContext().getPackageName());
        if (ft_res <= 0) {
            ft_res = R.drawable.ft_default;
        }

        ((ImageView) convertView.findViewById(R.id.file_type_icon)).setImageDrawable(getContext().getResources().getDrawable(ft_res));
        downloadOrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.file == null) {
                    loading.setVisibility(View.VISIBLE);
                    downloadOrView.setVisibility(View.GONE);
                    DownloadTask task = new DownloadTask();
                    task.listener = new LoadFileListener() {
                        @Override
                        public void onResult(File file) {
                            view.setBackgroundColor(getContext().getResources().getColor(R.color.white));
                            loading.setVisibility(View.GONE);
                            downloadOrView.setVisibility(View.VISIBLE);
                            downloadOrView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_view));
                            item.file = file;
                        }
                    };
                    if (item.fileName.lastIndexOf(".") > -1) {

                        task.execute(item.fileUrl, FileUtil.getTemporaryFile(fileEnding).getAbsolutePath());
                    } else {
                        task.execute(item.fileUrl, FileUtil.getTemporaryFile().getAbsolutePath());
                    }
                } else {
                    FileUtil.openFile(getContext(), item.file);
                }
            }
        });

        return convertView;
    }

    /**
     * 文件下载回调
     */
    public interface LoadFileListener {
        public void onResult(File file);
    }

    /**
     * 文件下载器
     */
    private static class DownloadTask extends AsyncTask<String, Void, Void> {

        public FileAdapter.LoadFileListener listener;
        private File result;

        @Override
        protected Void doInBackground(String... params) {
            try {
                this.result = new File(params[1]);
                FileUtil.downloadFile(params[0], new File(params[1]));
            } catch (NetworkException e) {
                Logger.e(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listener.onResult(this.result);
        }
    }
}
