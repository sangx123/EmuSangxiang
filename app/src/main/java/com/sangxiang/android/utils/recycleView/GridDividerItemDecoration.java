package com.sangxiang.android.utils.recycleView;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by sangxiang on 26/4/17.
 */

public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpanCount;
    private int mHorizonSpan;
    private int mVerticalSpan;
    private int mOrientation;
    private int mHeaderCount=0;
    public GridDividerItemDecoration(int count) {
        mSpanCount = count;
        mOrientation = GridLayoutManager.VERTICAL;
        this.mHeaderCount=0;

    }
    public GridDividerItemDecoration(int count, int mHeaderCount) {
        mSpanCount = count;
        mOrientation = GridLayoutManager.VERTICAL;
        this.mHeaderCount=mHeaderCount;

    }

    public GridDividerItemDecoration(int count, int orientation, int mHeaderCount) {
        mSpanCount = count;
        setOrientation(orientation);
    }

    public GridDividerItemDecoration(int count, int mHeaderCount, int horizonSpan, int verticalSpan) {
        this(count,mHeaderCount);
        setDivideParams(horizonSpan, verticalSpan);
    }
    public GridDividerItemDecoration(int count, int mHeaderCount, int orientation, int horizonSpan, int verticalSpan) {
        this(count,mHeaderCount);
        setDivideParams(horizonSpan, verticalSpan);
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        //获取dapter中的位置
        final int position = parent.getChildAdapterPosition(view)-mHeaderCount;
        //获取总共的数量
        final int totalCount = parent.getAdapter().getItemCount()-mHeaderCount;
        //如果是第一列的话left为0，否则为定义的大小
        int left = (position % mSpanCount == 0) ? 0 : mHorizonSpan;
        int right=(position%mSpanCount==mSpanCount-1)?0:mHorizonSpan;
        //如果不是最后一列的话bottom为mVerticalSpan
        int bottom = ((position + 1) % mSpanCount == 0) ? 0 : mVerticalSpan;
        if(position<0){
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (isVertical(parent)) {
            //如果存在header的话就不设置间距
            if (!isLastRaw(parent, position, mSpanCount, totalCount)) {
                outRect.set(left/2, 0, right/2, mVerticalSpan);
            } else {
                outRect.set(left/2, 0, right/2, 0);
            }

        } else {
            if (!isLastColumn(parent, position, mSpanCount, totalCount)) {
                outRect.set(0, 0, mHorizonSpan, bottom);
            } else {
                outRect.set(0, 0, mHorizonSpan, bottom);
            }
        }
    }

    private boolean isVertical(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager)
                    .getOrientation();
            return orientation == StaggeredGridLayoutManager.VERTICAL;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            return orientation == StaggeredGridLayoutManager.VERTICAL;
        }
        return false;
    }

    public void setDivideParams(int horizon, int vertical) {
        mHorizonSpan = horizon;
        mVerticalSpan = vertical;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        if (isVertical(parent)) {
            if ((pos - pos % spanCount) + spanCount >= childCount)
                return true;
        } else {
            if ((pos + 1) % spanCount == 0) {
                return true;
            }
        }
        return false;
    }


    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount,
                                 int childCount) {
        if (isVertical(parent)) {
            if ((pos + 1) % spanCount == 0) {
                return true;
            }
        } else {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)
                return true;
        }
        return false;
    }
}
