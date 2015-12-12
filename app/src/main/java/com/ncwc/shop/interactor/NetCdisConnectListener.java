package com.ncwc.shop.interactor;

import com.ncwc.shop.util.NetUtils;

/**
 * Created by admin on 2015/9/15.
 */
public interface NetCdisConnectListener {
    void onNetworkConnected(NetUtils.NetType type);
    void onNetworkDisConnected();
}
