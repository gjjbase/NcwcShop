package com.ncwc.shop.interactor;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class ViewHolderInject {
	/**
	 * 装载数据 <功能详细描述>
	 *
	 * @param object
	 * @see [类、类#方法、类#成员]
	 */
	public abstract void loadData(JSONObject object, int position) throws JSONException;
}
