package com.ncwc.shop.model.common;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.bean.ImageFloder;
import com.ncwc.shop.bean.ImageItem;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.CameraManager;
import com.ncwc.shop.util.SharepreUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 图片选择的界面
 * Created by gaojiangjian on 15/10/19.
 */
public class SelectPictureActicity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final int REQUST_PREVIEW = 100;
    @Bind(R.id.toolbar_right)
    TextView toolbar_right;
    @Bind(R.id.btn_select)
    Button btn_select;
    @Bind(R.id.txt_select)
    TextView txt_select;
    @Bind(R.id.gridview)
    GridView gridview;
    @Bind(R.id.lstview)
    ListView lstview;
    private PictureAdapter adapter;
    private FolderAdapter folderAdapter;
    private ContentResolver contentResolver;
    private ImageFloder imageAll, currentImageFloder;
    private ArrayList<ImageFloder> mDirPaths = new ArrayList<ImageFloder>();
    private ArrayList<String> selectimg;//已选择的图片
    private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
    private int MAX_NUM = 4;//最大张数
    private File outFile;
    private Bundle bundle;
    private AsyncLoadingPicture asyncLoadingPicture;

    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick({R.id.btn_select, R.id.txt_select, R.id.toolbar_right})
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select:
                //弹出相册的选择
                if (lstview.getVisibility() == View.VISIBLE) {
                    hideListAnimation();
                } else {
                    lstview.setVisibility(View.VISIBLE);
                    showListAnimation();
                    folderAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.txt_select://预览
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imgpath", selectimg);
                readyGoForResult(ActivityPreview.class, REQUST_PREVIEW, bundle);
                break;
            case R.id.toolbar_right://回调页面
                Intent intent = new Intent();
                intent.putStringArrayListExtra("pathimg", selectimg);
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    public void hideListAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1, 1f);
        translateAnimation.setDuration(200);
        lstview.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lstview.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void showListAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0f, 1, 0f, 1, 1f, 1, 0f);
        translateAnimation.setDuration(200);
        lstview.startAnimation(translateAnimation);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (lstview.getVisibility() == View.VISIBLE) {
                hideListAnimation();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_selectpric;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void initData() {
        mToolbar.setTitle(R.string.photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contentResolver = getContentResolver();
        asyncLoadingPicture = AsyncLoadingPicture.getInstance(getApplicationContext());
        selectimg = new ArrayList<String>();
        imageAll = new ImageFloder();
        imageAll.setDir("/所有图片");
        currentImageFloder = imageAll;
        mDirPaths.add(imageAll);
        toolbar_right.setVisibility(View.VISIBLE);
        selectimg.addAll(getIntent().getExtras().getStringArrayList("pathlength"));
        toolbar_right.setText("完成" + selectimg.size() + "/" + MAX_NUM);
        txt_select.setText("预览(" + selectimg.size() + ")");
        try {
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!outDir.exists()) outDir.mkdirs();
            outFile = new File(outDir, SharepreUtil.getStringValue(getApplicationContext(), Constants.MEMBERID, "") + System.currentTimeMillis() + ".jpg");
        } catch (Exception e) {
            return;
        }


        getThumbnail();
        adapter = new PictureAdapter();
        folderAdapter = new FolderAdapter();
        gridview.setAdapter(adapter);
        lstview.setAdapter(folderAdapter);
        gridview.setOnItemClickListener(this);
        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentImageFloder = mDirPaths.get(position);
                hideListAnimation();
                adapter.notifyDataSetChanged();
                btn_select.setText(currentImageFloder.getName());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            goCamare();
            return;
        }
    }

    public void goCamare() {
        if (selectimg.size() + 1 > MAX_NUM) {
            showToast(getString(R.string.maxum));
            return;
        }
        CameraManager.openCamera(SelectPictureActicity.this, outFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_ACTION_CAREMA && resultCode == Activity.RESULT_OK) {
            CameraManager.openCrop(SelectPictureActicity.this, Uri.fromFile(outFile));
        } else if (requestCode == Constants.INTENT_ACTION_CROP && resultCode == Activity.RESULT_OK && null != data) {
            if (data != null) {
//                outFile.delete();
                Bitmap bitmap = data.getParcelableExtra("data");
//                ImageTools.saveBitmapFile(bitmap, outFile);
                selectimg.add(outFile.getAbsolutePath());
                Intent intent = new Intent();
                intent.putStringArrayListExtra("pathimg", selectimg);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /***
     * 得到缩略图
     */
    private void getThumbnail() {
        Cursor mCursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns.DATA}, "", null, MediaStore.MediaColumns.DATE_ADDED + " DESC");
        // Log.e("TAG", mCursor.getCount() + "");
        if (mCursor.moveToFirst()) {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                // 获取图片的路径
                String path = mCursor.getString(_date);
                // Log.e("TAG", path);
                imageAll.images.add(new ImageItem(path));
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                ImageFloder imageFloder = null;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFloder = new ImageFloder();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    mDirPaths.add(imageFloder);
                    // Log.d("zyh", dirPath + "," + path);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }
                imageFloder.images.add(new ImageItem(path));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        // for (int i = 0; i < mDirPaths.size(); i++) {
        // ImageFloder f = mDirPaths.get(i);
        // Log.d("zyh", i + "-----" + f.getName() + "---" + f.images.size());
        // }
        tmpDir = null;
    }

    public class PictureAdapter extends BaseAdapter {

        public PictureAdapter() {
            super();
        }


        @Override
        public int getCount() {
            return currentImageFloder.images.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.selectadapter_item, null);
            final ImageView img_camera = (ImageView) convertView.findViewById(R.id.img_camera);
            CheckBox check_xy = (CheckBox) convertView.findViewById(R.id.check_xy);
            if (position == 0) {
                img_camera.setImageResource(R.mipmap.pickphotos_to_camera_normal);
                check_xy.setVisibility(View.INVISIBLE);
            } else {
                position = position - 1;
                final ImageItem imageItem = currentImageFloder.images.get(position);
                asyncLoadingPicture.LoadPicture("file://" + imageItem.path, img_camera);
                img_camera.setTag(imageItem.path);
                check_xy.setTag(position);
                check_xy.setChecked(selectimg.contains(imageItem.path));
                check_xy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked && selectimg.size() + 1 > MAX_NUM) {
                            showToast("最多选择4张");
                            buttonView.setChecked(false);
                            return;
                        }
                        if (selectimg.contains(imageItem.path)) {
                            selectimg.remove(imageItem.path);
                        } else {
                            selectimg.add(imageItem.path);
                        }
                        toolbar_right.setText("完成" + selectimg.size() + "/" + MAX_NUM);
                        txt_select.setText("预览(" + selectimg.size() + ")");
                    }
                });
                img_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle = new Bundle();
                        ArrayList<String> requsetlist = new ArrayList<String>();
                        requsetlist.add(v.getTag().toString());
                        bundle.putStringArrayList("imgpath", requsetlist);
                        readyGoForResult(ActivityPreview.class, REQUST_PREVIEW, bundle);
                    }
                });
            }
            return convertView;
        }
    }

    private class FolderAdapter extends BaseAdapter {
        public FolderAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return mDirPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_dir_item, null);
            TextView id_dir_item_name = (TextView) convertView.findViewById(R.id.id_dir_item_name);
            TextView id_dir_item_count = (TextView) convertView.findViewById(R.id.id_dir_item_count);
            ImageView choose = (ImageView) convertView.findViewById(R.id.choose);
            ImageView id_dir_item_image = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
            ImageFloder imageFloder = mDirPaths.get(position);
            asyncLoadingPicture.LoadPicture("file://" + imageFloder.getFirstImagePath(), id_dir_item_image);
            id_dir_item_count.setText(imageFloder.images.size() + "张");
            id_dir_item_name.setText(imageFloder.getName());
            choose.setVisibility(currentImageFloder == imageFloder ? View.VISIBLE : View.GONE);
            return convertView;
        }
    }
}
