package com.ncwc.shop.adapter;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapterInject;
import com.ncwc.shop.interactor.ViewHolderInject;
import com.ncwc.shop.util.AsyncLoadingPicture;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by admin on 2015/10/7.
 */
public class FreeWillAdapter extends BaseAdapterInject {
    public FreeWillAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.freerunadpter_item;
    }

    @Override
    public ViewHolderInject getNewHolder(int position) {
        return new ViewHolder();
    }


    class ViewHolder extends ViewHolderInject implements Runnable {

        public boolean run = false;
        @Bind(R.id.img_detail)
        ImageView img_detail;
        @Bind(R.id.txt_per)
        TextView txt_per;//期数
        @Bind(R.id.txt_title)
        TextView txt_title;
        @Bind(R.id.txt_penum)
        TextView txt_penum;
        @Bind(R.id.txt_dnum) //产品数量
                TextView txt_dnum;
        @Bind(R.id.txt_day)
        TextView txt_day;
        @Bind(R.id.txt_hour)
        TextView txt_hour;
        @Bind(R.id.txt_minute)
        TextView txt_minute;
        @Bind(R.id.txt_free)//免费试用
                Button txt_free;
        private int minute;//分
        private int hour;//小时
        private int day;//天
        private int colleg;
        private Handler handler = new Handler();

        public void loadData(final JSONObject object, int position) throws JSONException {
            txt_title.setText(object.getString("title"));
            txt_penum.setText(object.getString("try_people"));
            txt_dnum.setText(object.getString("number"));
            txt_per.setText(object.getString("period_no"));
            colleg = Integer.parseInt(object.getString("surplus_time")) / 60;
            day = colleg / (60 * 24);
            hour = (colleg % (60 * 24)) / 60;
            minute = (colleg % (60 * 24)) % 60;
            txt_day.setText(day < 10 ? "0" + day : String.valueOf(day));
            txt_hour.setText(hour < 10 ? "0" + hour : String.valueOf(hour));
            txt_minute.setText(minute < 10 ? "0" + minute : String.valueOf(minute));
            beginRun();
            txt_free.setText(R.string.staytuned);
            txt_free.setTextColor(mContext.getResources().getColor(R.color.white));
            txt_free.setBackgroundResource(R.mipmap.dianji);
            txt_free.setEnabled(false);
            AsyncLoadingPicture.getInstance(mContext).LoadPicture(object.getString("logo"), img_detail);
        }

        public boolean isRun() {
            return run;
        }

        public void beginRun() {
            this.run = true;
            run();
        }

        public void stopRun() {
            this.run = false;
        }

        @Override
        public void run() {
            if (run) {
                ComputeTime();
                txt_day.setText(day < 10 ? "0" + day : String.valueOf(day));
                txt_hour.setText(hour < 10 ? "0" + hour : String.valueOf(hour));
                txt_minute.setText(minute < 10 ? "0" + minute : String.valueOf(minute));
                handler.postDelayed(this, 1000 * 60);
                if (colleg == 0) stopRun();
            } else {
                handler.removeCallbacks(this);
            }
        }

        /**
         * 倒计时计算
         */
        private void ComputeTime() {
            minute--;
            if (minute < 0) {
                minute = 59;
                hour--;
                if (hour < 0) {
                    hour = 23;  // 倒计时结束，一天有24个小时
                    day--;
                }
            }
        }
    }
}
