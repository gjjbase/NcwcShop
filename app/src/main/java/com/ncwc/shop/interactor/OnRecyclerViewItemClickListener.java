package com.ncwc.shop.interactor;

import android.view.View;

/**
 * 模拟RecyclerView中item点击事件
 * Created by admin on 2015/9/13.
 */
public  interface OnRecyclerViewItemClickListener {
    void itemClick(View view, String data);
}
