package com.sangxiang.android.utils.recycleView;

import android.content.Context;
import android.widget.AbsListView;
import com.squareup.picasso.Picasso;

/**
 * From picasso/picasso-sample/src/main/java/com/example/picasso/SampleScrollListener.java
 * https://github.com/square/picasso
 *  使用例子
 *  mThreadList.setOnScrollListener(new LoadPauseScrollListener(getContext()));
 */
public class LoadPauseScrollListener implements AbsListView.OnScrollListener {
    private final Context context;

    public LoadPauseScrollListener(Context context) {
        this.context = context;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        final Picasso picasso =Picasso.with();
        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            picasso.resumeTag(context.getApplicationContext());
        } else {
            picasso.pauseTag(context.getApplicationContext());
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // Do nothing.
    }
}
