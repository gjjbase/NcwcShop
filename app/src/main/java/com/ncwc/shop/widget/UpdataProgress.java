package com.ncwc.shop.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.ncwc.shop.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdataProgress extends Dialog {
	private RoundProgressBar mProgress;
	/* 返回的安装包url */
	private String apkUrl;
	/* 下载包安装路径 */
	private static final String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ncwcAndroid/";
	private static String saveFileName;

	/* 进度条与通知ui刷新的handler和msg常量 */
	private static final int TIME_OUT = 3 * 1000;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int progress = 0;
	private Thread downLoadThread;
	private boolean interceptFlag = false;
	private Context context;

	public UpdataProgress(Context context, String apkUrl, String version) {
		super(context, R.style.MyDialogStyle);
		this.context = context;
		this.apkUrl = apkUrl;
		saveFileName = savePath + "ncwcAndroid" + version + ".apk";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress);
		mProgress = (RoundProgressBar) findViewById(R.id.mProgress);
		downloadApk();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DOWN_UPDATE:
					mProgress.setProgress(progress);
					break;
				case DOWN_OVER:
					installApk();
					break;
				default:
					Toast.makeText(context, R.string.out_time, Toast.LENGTH_SHORT).show();
					break;
			}
		}

		;
	};
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection)

						url.openConnection();
				conn.connect();
				conn.setConnectTimeout(TIME_OUT);
				conn.setReadTimeout(TIME_OUT);
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 */
	private void installApk() {
		dismiss();
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		context.startActivity(i);
	}
}
