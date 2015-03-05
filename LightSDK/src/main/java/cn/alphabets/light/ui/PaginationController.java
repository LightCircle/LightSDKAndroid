package cn.alphabets.light.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

/**
 * 向上滑动ListView，加载更多内容
 * Created by lin on 14/11/20.
 */
public class PaginationController {

    private ListView listView;
    private ArrayAdapter listAdapter;
    private View footerView;
    private SwipeRefreshLayout refresh;
    private Loader loader;

    private int total = 0;                      // 总数据件数
    private int limit = 10;                     // 一次获取数据件数
    private int start = 0;                      // 获取数据的起点
    private boolean isLoading = false;          // 是否正在从后台加载数据

    /**
     * 加载数据的回调函数
     */
    public interface Loader {
        public void load();
    }

    /**
     * 重置
     */
    public void reset() {
        this.total = 0;
        this.start = 0;
        this.isLoading = false;

        this.listAdapter.clear();
    }

    /**
     * 更新数据之后，设定状态
     * @param objects 数据一览
     * @param total 所有件数
     */
    public void updateState(List objects, int total) {

        this.isLoading = false;
        this.start = this.start + objects.size();
        this.total = total;
        this.listAdapter.addAll(objects);
        this.listAdapter.notifyDataSetChanged();

        this.clearWait();
    }

    /**
     * 清除等待动画，通常在出错处理里使用
     */
    public void clearWait() {
        try {
            this.listView.removeFooterView(this.footerView);
        } catch (ClassCastException e) {
            // android4.4以前的设备，会报错误，忽略错误
        }

        if (this.refresh != null) {
            this.refresh.setRefreshing(false);
        }
    }

    /**
     * 初始化翻页
     * @param listView view
     * @param listAdapter adapter
     */
    public void initializePagination(Activity activity, ListView listView, ArrayAdapter listAdapter) {

        // 添加进度框
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);

        LinearLayout listRefresh = new LinearLayout(activity);
        listRefresh.setLayoutParams(params);
        listRefresh.setGravity(Gravity.CENTER);
        listRefresh.setOrientation(LinearLayout.VERTICAL);
        listRefresh.addView(new ProgressBar(activity, null, android.R.attr.progressBarStyle));

        initializePagination(listView, listAdapter, listRefresh);
    }
    public void initializePagination(ListView listView, ArrayAdapter listAdapter, View footerView) {
        this.footerView = footerView;
        this.listAdapter = listAdapter;
        this.listView = listView;
        this.listView.setOnScrollListener(OnScrollListener);
        this.listView.setAdapter(this.listAdapter);
    }



    /**
     * 初始化下拉刷新
     * @param refresh refresh
     */
    public void initializePullRefresh(SwipeRefreshLayout refresh) {
        this.refresh = refresh;
        this.refresh.setOnRefreshListener(OnRefreshListener);
        this.refresh.setColorSchemeColors(Color.LTGRAY, Color.GRAY, Color.DKGRAY, Color.BLACK);
    }

    /**
     * 向下滑动追加获取数据
     */
    private ListView.OnScrollListener OnScrollListener = new ListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(android.widget.AbsListView absListView, int scrollState) {
        }

        @Override
        public void onScroll(android.widget.AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if (!isLoading && totalItemCount == firstVisibleItem + visibleItemCount && totalItemCount < total) {
                isLoading = true;
                listView.addFooterView(footerView);
                loader.load();
            }
        }
    };

    /**
     * 下拉刷新，重新加载消息数据
     */
    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            reset();
            loader.load();
        }
    };

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    public Loader getLoader() {
        return loader;
    }

}
