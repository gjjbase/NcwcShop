package com.ncwc.shop.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ncwc.shop.interactor.VisibleListener;
import com.ncwc.shop.util.GlobalUtils;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements View.OnClickListener{

	protected Context context;

	private VisibleListener visibleListener;
	/** 子类实现初始化数据操作(子类自己调用) */
	public abstract void initData();
	/**控件的点击事件*/
	public abstract void widgetClick(View v);
	/**实例化布局*/
	protected  abstract int getContentViewLayoutID();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (getContentViewLayoutID() != 0) {
			return  inflater.inflate(getContentViewLayoutID(), container,false);
		} else {
			return super.onCreateView(inflater, container, savedInstanceState);
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initPrepare();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.bind(this, view);
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	private synchronized void initPrepare() {
			initData();
	}
	private void setVisibleListener(VisibleListener visibleListener){
		this.visibleListener=visibleListener;
	}
	/**
	 * this method like the fragment's lifecycle method onPause()
	 */
	protected  void onUserInvisible(){
		if(visibleListener!=null)
			visibleListener.onUserInvisible();
	};
	/**
	 * when fragment is visible for the first time, here we can do some initialized work or refresh data only once
	 */
	protected  void onFirstUserVisible(){
		if (visibleListener!=null)
			visibleListener.onFirstUserVisible();
	};

	/**
	 * this method like the fragment's lifecycle method onResume()
	 */
	protected  void onUserVisible(){
		if (visibleListener!=null)
			visibleListener.onUserVisible();
	};

	/**
	 * when fragment is invisible for the first time
	 */

	private void onFirstUserInvisible() {
		// here we do not recommend do something
	}

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}
	/**
	 * startActivity
	 *
	 * @param clazz
	 */
	protected void readyGo(Class<?> clazz) {
		Intent intent = new Intent(getActivity(), clazz);
		startActivity(intent);
	}

	/**
	 * startActivity with bundle
	 *
	 * @param clazz
	 * @param bundle
	 */
	protected void readyGo(Class<?> clazz, Bundle bundle) {
		Intent intent = new Intent(getActivity(), clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/**
	 * startActivityForResult
	 *
	 * @param clazz
	 * @param requestCode
	 */
	protected void readyGoForResult(Class<?> clazz, int requestCode) {
		Intent intent = new Intent(getActivity(), clazz);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * startActivityForResult with bundle
	 *
	 * @param clazz
	 * @param requestCode
	 * @param bundle
	 */
	protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
		Intent intent = new Intent(context, clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
	}

	/**
	 * show toast
	 *
	 * @param msg
	 */
	protected void showToast(String msg) {
		if (null != msg && !GlobalUtils.isEmpty(msg)) {
			Snackbar.make(((Activity) context).getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
		}
	}

}
