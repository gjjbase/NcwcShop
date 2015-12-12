package com.ncwc.shop.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 判断listview是否滑动到底部
 * Created by admin on 2015/10/10.
 */
public class DownListView extends ListView implements AbsListView.OnScrollListener {
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState){
                case OnScrollListener.SCROLL_STATE_IDLE:
                    //当不滚动时，判断是否滑动到底部
                    if (view.getLastVisiblePosition()==view.getCount()-1)   {
                        Log.e("di","bu");
                    }

                    if (view.getFirstVisiblePosition()==0){
                        Log.e("ding","bu");
                    }
                    break;
            }
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public DownListView(Context context) {
        super(context);
        this.setOnScrollListener(this);
    }
    public DownListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public boolean canChildScrollUp() {

        if (android.os.Build.VERSION.SDK_INT < 14) {

            if (this instanceof AbsListView) {

                final AbsListView absListView = (AbsListView) this;

                return absListView.getChildCount() > 0 &&

                        (absListView.getFirstVisiblePosition() > 0 ||

                                absListView.getChildAt(0).getTop() < absListView.getPaddingTop());

            } else {

                return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;

            }

        } else {

            return ViewCompat.canScrollVertically(this, -1);

        }
    }
    private void judgeScrollTop(int scrollState){
        if(scrollState == OnScrollListener.SCROLL_STATE_IDLE  &&
                this.getChildCount() > 0 &&
                this.getFirstVisiblePosition() == 0 &&
                this.getChildAt(0).getTop() == this.getPaddingTop()){//滚动条停止滚动，且滚动到顶部
//            isScrollTop = true;//到顶部
        }
    }
}
