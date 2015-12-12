package com.ncwc.shop.model.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.ExpandGridAdapter;
import com.ncwc.shop.adapter.FineComAdapter;
import com.ncwc.shop.base.BaseFragment;
import com.ncwc.shop.base.ImageViewPagerAdapter;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.model.classifica.FreeComDetailActivity;
import com.ncwc.shop.util.ACache;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.DimenUtils;
import com.ncwc.shop.util.FadePageTransformer;
import com.ncwc.shop.util.ScreenUtils;
import com.ncwc.shop.widget.AllListView;
import com.ncwc.shop.widget.AutoScrollViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * 商城首页
 * Created by admin on 2015/9/2.
 */
public class HomePageFragment extends BaseFragment implements ViewPager.OnPageChangeListener, IOAuthCallBack, OnRefreshListener {
    private static final int TIME = 4000;
    private final int ROW_NUM = 4;
    @Bind(R.id.fading_bar)
    protected AutoScrollViewPager fadingBar;
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.nestedScrollView)
    protected NestedScrollView nestedScrollView;
    @Bind(R.id.toolbar_title)
    protected TextView toolbar_title;
    @Bind(R.id.lindot)
    protected LinearLayout lindot;
    @Bind(R.id.listview_type)
    protected AllListView listview_type;
    @Bind(R.id.job_name_table)
    protected TableLayout tableLayout;
    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;
    @Bind(R.id.rel_rep)
    protected RelativeLayout rel_rep;
    boolean isScirolled = false;
    int location = 0;
    private List<ImageView> mImageViews = new ArrayList<ImageView>();
    private ImageViewPagerAdapter adapter;
    private FineComAdapter fineComAdapter;
    private Integer duration = 300;
    private int page = 1;
    private List<ImageView> listimg = new ArrayList<>();
    private List<View> listview = new ArrayList<>();
    private List<ImageView> doulist = new ArrayList<>();
    private AsynHttpUtil asynHttpUtil;
    private AsyncLoadingPicture asyncLoadingPicture;
    private JSONArray allJSON;
    private ACache aCache;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_homepage;
    }

    public void initData() {
        asynHttpUtil = AsynHttpUtil.getInstance();
        asyncLoadingPicture = AsyncLoadingPicture.getInstance(getActivity());
        aCache = ACache.get(getActivity());
        tableLayout.setStretchAllColumns(true);
        toolbar_title.setText(R.string.shopcomm);
        fadingBar.addOnPageChangeListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        asynHttpUtil.setIoAuthCallBack(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_red_light, android.R.color.holo_red_light, android.R.color.holo_red_light);
        try {
            acache();
        } catch (JSONException e) {

        }

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View contentView = nestedScrollView.getChildAt(0);
                if (contentView.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
                    progressBar.setVisibility(View.VISIBLE);
                    page++;
                    asynHttpUtil.prom(getActivity(), page);
                    asynHttpUtil.menuList(getActivity());
                    asynHttpUtil.rotateImg(getActivity());
                    asynHttpUtil.setIoAuthCallBack(HomePageFragment.this);
                }
            }
        });
        asynHttpUtil.prom(getActivity(), page);
        asynHttpUtil.menuList(getActivity());
        asynHttpUtil.rotateImg(getActivity());
    }


    public void widgetClick(View v) {

    }

    private void acache() throws JSONException {
        if (aCache.getAsJSONArray(Constants.TYPE_PROM) != null)
            prom(aCache.getAsJSONArray(Constants.TYPE_PROM));
        if (aCache.getAsJSONArray(Constants.TYPE_COMMENULIST) != null)
            commenulist(aCache.getAsJSONArray(Constants.TYPE_COMMENULIST));
        if (aCache.getAsJSONArray(Constants.TYPE_CAROUAL) != null)
            caroual(aCache.getAsJSONArray(Constants.TYPE_CAROUAL));
    }

    private void setimageAtt(ImageView imageView) {
        imageView.setPadding(DimenUtils.dip2px(getActivity(), 10), DimenUtils.dip2px(getActivity(), 10), DimenUtils.dip2px(getActivity(), 10), DimenUtils.dip2px(getActivity(), 10));
        imageView.setMinimumHeight(DimenUtils.dip2px(getActivity(), 30));
        imageView.setMinimumWidth(DimenUtils.dip2px(getActivity(), 30));
        imageView.setScaleType(ImageView.ScaleType.CENTER);
    }


    /**
     * 自动切换动画
     */
    protected void initRunnable() {
        fadingBar.setInterval(TIME);
        fadingBar.startAutoScroll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fadingBar.stopAutoScroll();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < doulist.size(); i++)
            doulist.get(i).setImageResource(R.mipmap.lunbo);
        doulist.get(position).setImageResource(R.mipmap.lunbodangqian);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 1:
                isScirolled = false;
                break;
            case 2:
                isScirolled = true;
                break;
            case 0:
                if (fadingBar.getCurrentItem() == fadingBar.getAdapter().getCount() - 1 && !isScirolled) {
                    fadingBar.setCurrentItem(0);
                } else if (fadingBar.getCurrentItem() == 0 && !isScirolled) {
                    fadingBar.setCurrentItem(fadingBar.getAdapter().getCount() - 1);
                }
                break;
        }
    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        showToast(response.getString("msg"));
        if (!response.getString("status").equals("1")) return;
        JSONArray jsonArray = response.getJSONObject("datas").getJSONArray("list");
        if (type.equals(HttpService.TYPE_CAROUAL)) {
            caroual(jsonArray);
            aCache.put(Constants.TYPE_CAROUAL, jsonArray);
        } else if (type.equals(HttpService.TYPE_COMMENULIST)) {
            commenulist(jsonArray);
            aCache.put(Constants.TYPE_COMMENULIST, jsonArray);
        } else if (type.equals(HttpService.TYPE_PROM)) {
            prom(jsonArray);
            aCache.put(Constants.TYPE_PROM, jsonArray);
        }
        listview_type.setFocusable(false);
        if (page == 1)
            nestedScrollView.fullScroll(View.FOCUS_UP);

    }

    private void caroual(final JSONArray jsonArray) throws JSONException {
        lindot.removeAllViews();
        mImageViews = new ArrayList<>();
        doulist = new ArrayList<>();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ViewGroup.LayoutParams lp2 = rel_rep.getLayoutParams();
        lp2.width = ScreenUtils.getScreenWidth(getActivity());
        lp2.height = ScreenUtils.getScreenWidth(getActivity()) * 2 / 5;
        rel_rep.requestLayout();
        for (int i = 0; i < jsonArray.length(); i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            asyncLoadingPicture.LoadPicture(jsonArray.getJSONObject(i).getString("img"), imageView);
            mImageViews.add(imageView);
            imageView.setTag(jsonArray.getJSONObject(i).getString("url") + jsonArray.getJSONObject(i).getString("par"));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String tag = (String) v.getTag();
                        String msg = tag.split("=")[1];
                        if (tag.indexOf("trial") != -1) {
                            //免费试用
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), FreeComDetailActivity.class);
                            intent.putExtra("id", msg);
                            startActivity(intent);

                        } else if (tag.indexOf("product") != -1) {
                            //商品详情
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), DetailsCartActivity.class);
                            intent.putExtra("goods_id", msg);
                            intent.putExtra("goods_spec", "");
                            startActivity(intent);

                        }
                    } catch (Exception e) {

                    }
                }
            });
            ImageView imageView1 = new ImageView(getActivity());
            setimageAtt(imageView1);
            imageView1.setImageResource(R.mipmap.lunbo);
            lindot.addView(imageView1);
            doulist.add(imageView1);
        }

        fadingBar.setPageTransformer(true, new FadePageTransformer());
        adapter = new ImageViewPagerAdapter(mImageViews);
        fadingBar.setAdapter(adapter);
        doulist.get(0).setImageResource(R.mipmap.lunbodangqian);
        initRunnable();
    }

    private void commenulist(JSONArray jsonArray1) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 7; i++) jsonArray.put(jsonArray1.getJSONObject(i));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gc_id", "7");
        jsonObject.put("gc_name", "全部分类");
        jsonObject.put("gc_parent_id", "0");
        jsonObject.put("img", "http://dev.img.nichewoche.com/mobile/category/all.png");
        jsonArray.put(jsonObject);
        for (int n = 0; n < (int) Math.ceil(((double) jsonArray.length()) / ROW_NUM); n++) {
            TableRow row = new TableRow(getActivity());
            for (int m = 0; m < ROW_NUM; m++) {
                RelativeLayout rele = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.table_row_item, null);
                ImageView img_gone = (ImageView) rele.findViewById(R.id.img_gone);
                ImageView topimg = (ImageView) rele.findViewById(R.id.topimg);
                ViewGroup.LayoutParams lp = topimg.getLayoutParams();
                lp.width = ScreenUtils.getScreenWidth(getActivity()) / 4;
                lp.height = ScreenUtils.getScreenWidth(getActivity()) / 4;
                topimg.setLayoutParams(lp);
                TextView txt_title = (TextView) rele.findViewById(R.id.txt_title);
                txt_title.setText(jsonArray.getJSONObject(location).getString("gc_name"));
                asyncLoadingPicture.LoadPicture(jsonArray.getJSONObject(location).getString("img"), topimg);
                rele.setTag(location + "");
                rele.setOnClickListener(new OnCliclickItem(jsonArray, location, n, tableLayout, rele));
                listimg.add(img_gone);
                row.addView(rele);
                location++;
            }
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.expand_item, null);
            view.setTag(n);
            listview.add(view);
            view.setVisibility(View.GONE);
            tableLayout.addView(row);
            tableLayout.addView(view);
        }
    }

    private void prom(JSONArray jsonArray) throws JSONException {
        if (page == 1) allJSON = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) allJSON.put(jsonArray.get(i));
        fineComAdapter = new FineComAdapter(getActivity());
        listview_type.setAdapter(fineComAdapter);
        fineComAdapter.setData(allJSON);
    }

    @Override
    public void onRefresh() {
        page = 1;
        asynHttpUtil.prom(getActivity(), page);
        asynHttpUtil.menuList(getActivity());
        asynHttpUtil.rotateImg(getActivity());
        asynHttpUtil.setIoAuthCallBack(this);
    }


    private class OnCliclickItem implements View.OnClickListener, AdapterView.OnItemClickListener {
        JSONArray jsonArray;
        private int position;
        private int tag;
        private RelativeLayout view;
        private TableLayout tableLayout;
        private String clickPosition = "";
        private JSONArray data;
        private ImageView img_gone;

        public OnCliclickItem(JSONArray data, int position, int tag, TableLayout table, RelativeLayout view) {
            this.position = position;
            this.tag = tag;
            this.view = view;
            this.tableLayout = table;
            this.data = data;
            img_gone = (ImageView) view.findViewById(R.id.img_gone);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            try {
                bundle.putString("gc_name", jsonArray.getJSONObject(position).getString("gc_name"));
                bundle.putString("gc_id", jsonArray.getJSONObject(position).getString("gc_id"));
                readyGo(AllCommitActivity.class, bundle);
            } catch (JSONException e) {

            }
        }

        private void expand(final View v, int location) {
            GridView grd_expand = (GridView) v.findViewById(R.id.grd_expand);
            try {
                jsonArray = data.getJSONObject(location).getJSONArray("list");
                ExpandGridAdapter ea = new ExpandGridAdapter(getActivity());
                grd_expand.setAdapter(ea);
                ea.setData(jsonArray);
                grd_expand.setOnItemClickListener(this);
            } catch (JSONException e) {

            }
            v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final int targetHeight = v.getMeasuredHeight();
            for (View view : listview)
                view.setVisibility(View.GONE);
            v.setVisibility(View.VISIBLE);
            img_gone.setVisibility(View.VISIBLE);
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime,
                                                   Transformation t) {
                    v.getLayoutParams().height = (interpolatedTime == 1) ? RelativeLayout.LayoutParams.WRAP_CONTENT
                            : (int) (targetHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };
            animation.setDuration(duration);
            v.startAnimation(animation);
        }

        public void onClick(View v) {
            if (position == data.length() - 1) {
                readyGo(CategoryActivity.class);
                return;
            }
            String tempTag = (String) v.getTag();
            for (ImageView gonimg : listimg)
                gonimg.setVisibility(View.GONE);
            view = (RelativeLayout) tableLayout.findViewWithTag(tag);
            if (view.getVisibility() == View.VISIBLE) {
                collapse(view);
            } else {
                if (tempTag.equals(clickPosition)) {
                    expand(view, position);
                }
            }
            if (!tempTag.equals(clickPosition)) {
                expand(view, position);
            }
            clickPosition = tempTag;
        }

        private void collapse(final View v) {
            final int initialHeight = v.getMeasuredHeight();
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime,
                                                   Transformation t) {
                    if (interpolatedTime == 1) {
                        v.setVisibility(View.GONE);
                        img_gone.setVisibility(View.GONE);
                    } else {
                        v.getLayoutParams().height = initialHeight
                                - (int) (initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            animation.setDuration(duration);
            v.startAnimation(animation);
        }
    }
}
