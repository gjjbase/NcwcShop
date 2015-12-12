package com.ncwc.shop.model.classifica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.impl.SwipeBackHelper;
import com.ncwc.shop.model.common.ActivityPreview;
import com.ncwc.shop.model.common.SelectPictureActicity;
import com.ncwc.shop.model.common.TrialAgrActivity;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.ImageTools;
import com.ncwc.shop.util.SharepreUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 提交试用报告页面
 * Created by admin on 2015/10/9.
 */
public class SubmitRepActivity extends BaseActivity implements IOAuthCallBack, CompoundButton.OnCheckedChangeListener {
    private static final int REQUEST_PICK = 0;
    private static final int REQUST = 100;
    @Bind(R.id.check_xy)
    CheckBox check_xy;
    @Bind(R.id.toolbar_title)
    TextView toolbar_title;
    @Bind(R.id.title)
    TextView txt_title;//头部标题
    @Bind(R.id.grd_botm)
    GridView grd_botm;
    @Bind(R.id.edit_aspect)//外观
            EditText edit_aspect;
    @Bind(R.id.edt_price)
    EditText edt_price;
    @Bind(R.id.edt_quality)
    EditText edt_quality;
    @Bind(R.id.ratingBar2)
    AppCompatRatingBar ratingBar2;
    private SubmitRepAdapter adapter;
    private AsynHttpUtil asynHttpUtil;
    private AsyncLoadingPicture asyncLoadingPicture;
    private String straspect;
    private String strprice;
    private String strquality;
    private ArrayList<String> pathlist = new ArrayList<String>();
    private Bundle bundle;
    private List<String> base64;
    private String pid;
    private float rating;//评分
    private String title;//标题

    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void initData() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pid = getIntent().getExtras().getString("pid");
        title = getIntent().getExtras().getString("title");
        txt_title.setText(title);
        toolbar_title.setText(R.string.submitrep);
        check_xy.setText(Html.fromHtml("<font size=\"3\" color=\"black\">我已阅读并同意</font><font size=\"3\" color=\"blue\">《你车我车用户协议》</font>"));
        asynHttpUtil = AsynHttpUtil.getInstance();
        base64 = new ArrayList<String>();
        asyncLoadingPicture = AsyncLoadingPicture.getInstance(getApplicationContext());
        check_xy.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.regist, R.id.txt_openimg})
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.regist:
                //提交试用报告
                if (SharepreUtil.getStringValue(getApplicationContext(), Constants.TOKEN, "").equals("")) {
                    Snackbar.make(getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_LONG).setAction(R.string.sure, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
                            sendBroadcast(intent);
                        }
                    }).show();
                    return;
                }
                if (!check_xy.isChecked()) {
                    showToast(getString(R.string.agree));
                    return;
                }
                straspect = edit_aspect.getText().toString().trim();
                strprice = edt_price.getText().toString().trim();
                strquality = edt_quality.getText().toString().trim();
                rating = ratingBar2.getRating();
                for (String file : pathlist)
                    base64.add("data:image/jpeg;base64," + ImageTools.bitmap64(file));
                asynHttpUtil.addreport(SubmitRepActivity.this, pid, straspect, strprice, strquality, rating, base64);
                asynHttpUtil.setIoAuthCallBack(this);
                break;
            case R.id.txt_openimg://打开相册
                bundle = new Bundle();
                bundle.putStringArrayList("pathlength", pathlist);
                readyGoForResult(SelectPictureActicity.class, REQUEST_PICK, bundle);
                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeEdgePercent(0.5f)
                .setSwipeSensitivity(0.5f)
                .setClosePercent(0.5f)
                .setSwipeRelateEnable(true).setSwipeSensitivity(1);
        return R.layout.activity_submitrep;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PICK && data != null) {
            pathlist = new ArrayList<String>();
            pathlist.addAll(data.getStringArrayListExtra("pathimg"));
            adapter = new SubmitRepAdapter();
            grd_botm.setAdapter(adapter);
        } else if (requestCode == REQUST && resultCode == Activity.RESULT_OK) {
            check_xy.setChecked(true);
        }
    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
        showToast(response.getString("msg"));
//        if (response.getString("status").equals("1"))
        if (type.equals(HttpService.TYPE_ADDREPORT)) {
            base64 = new ArrayList<String>();
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked)
            readyGoForResult(TrialAgrActivity.class, REQUST);
    }

    class SubmitRepAdapter extends BaseAdapter {
        public SubmitRepAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return pathlist.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.submitrepadapter_item, null);
            ImageView img_detail = (ImageView) convertView.findViewById(R.id.img_detail);
            ImageView img_title = (ImageView) convertView.findViewById(R.id.img_title);
            asyncLoadingPicture.LoadPicture("file://" + pathlist.get(position), img_title);
            img_title.setTag(pathlist.get(position));
            img_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pathlist.remove(position);
                    notifyDataSetChanged();
                }
            });
            img_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> requsetlist = new ArrayList<String>();
                    requsetlist.add(v.getTag().toString());
                    bundle.putStringArrayList("imgpath", requsetlist);
                    readyGoForResult(ActivityPreview.class, SelectPictureActicity.REQUST_PREVIEW, bundle);
                }
            });
            return convertView;
        }
    }
}
