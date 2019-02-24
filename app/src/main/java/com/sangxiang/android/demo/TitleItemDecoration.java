package com.sangxiang.android.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nightonke.boommenu.Util;
import com.sangxiang.android.App;
import com.sangxiang.android.R;

import java.util.List;

/**
 * 有分类title的 ItemDecoration
     作者：张旭童
     链接：https://www.jianshu.com/p/0d49d9f51d2c
     來源：简书
     简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
 */

public abstract class TitleItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private Rect mBounds;//用于存放测量文字Rect

    private int mTitleHeight;//title的高
    private static int COLOR_TITLE_BG = Color.parseColor("#FFDFDFDF");
    private static int COLOR_TITLE_FONT = ContextCompat.getColor(App.mInstance, R.color.color_202235) ;
    private static int mTitleFontSize;//title字体大小
    private Context context;

    public TitleItemDecoration(Context context) {
        super();
        this.context=context;
        mPaint = new Paint();
        mBounds = new Rect();
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics());
        mTitleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics());
        mPaint.setTextSize(mTitleFontSize);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = params.getViewLayoutPosition();
            //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
            if (position > -1) {
                if (position == 0) {//等于0肯定要有title的
                    drawTitleArea(c, left, right, child, params, position);

                } else {//其他的通过判断
                    if (null !=getTag(position) && !getTag(position).equals(getTag(position - 1))) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        drawTitleArea(c, left, right, child, params, position);
                    } else {
                        //none
                    }
                }
            }
        }
    }

    /**
     * 绘制Title区域背景和文字的方法
     *
     * @param c
     * @param left
     * @param right
     * @param child
     * @param params
     * @param position
     */
    private void drawTitleArea(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {//最先调用，绘制在最下层
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(left, child.getTop() - params.topMargin - mTitleHeight, right, child.getTop() - params.topMargin, mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
/*
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;*/
        mPaint.getTextBounds(getTag(position), 0, getTag(position).length(), mBounds);
        c.drawText(getTag(position), child.getPaddingLeft()+ Util.dp2px(15), child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2), mPaint);
        final String tag=getTag(position);

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {//最后调用 绘制在最上层
        if(!((BaseQuickAdapter)parent.getAdapter()).getData().isEmpty()) {
            int pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
            String tag = getTag(pos);
            //View child = parent.getChildAt(pos);
            View child = parent.findViewHolderForLayoutPosition(pos).itemView;//出现一个奇怪的bug，有时候child为空，所以将 child = parent.getChildAt(i)。-》 parent.findViewHolderForLayoutPosition(pos).itemView
            mPaint.setColor(COLOR_TITLE_BG);
            c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + mTitleHeight, mPaint);
            mPaint.setColor(COLOR_TITLE_FONT);
            mPaint.getTextBounds(tag, 0, tag.length(), mBounds);
            c.drawText(tag, child.getPaddingLeft() + Util.dp2px(15),
                    parent.getPaddingTop() + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2),
                    mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
        if (position > -1&&!((BaseQuickAdapter)parent.getAdapter()).getData().isEmpty()){
            if (position == 0) {//等于0肯定要有title的
                outRect.set(0, mTitleHeight, 0, 0);
            } else {//其他的通过判断
                if (null !=getTag(position) && !getTag(position).equals(getTag(position-1))) {
                    outRect.set(0, mTitleHeight, 0, 0);//不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                } else {
                    outRect.set(0, 0, 0, 0);
                }
            }
        }

    }

    public abstract String getTag(int position);
//    public String getTag(int position){
//        if(mDatas.get(position).getWorkStatus()==3){
//            return "已超时";
//        }else if(mDatas.get(position).getWorkStatus()==1){
//            return "待确定维修日期";
//        }else {
//            return "其他";
//        }
//    }

}


