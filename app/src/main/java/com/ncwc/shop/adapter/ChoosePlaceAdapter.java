package com.ncwc.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL-PC on 2015/10/30.
 */
public class ChoosePlaceAdapter extends BaseAdapter {

	private Context context;
	private JSONArray jso;

	String area_id = "";
	String area_name = "";
	private List<String> id_s = new ArrayList<String>();
	private List<String> name_s = new ArrayList<String>();

	public ChoosePlaceAdapter(Context context, JSONArray jso) {
		this.context = context;
		this.jso = jso;
	}

	@Override
	public int getCount() {
		return jso.length();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_chooseplace, null);
			viewHolder = new ViewHolder();
			viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_04);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		try {
			JSONObject item = jso.getJSONObject(position);
			area_id = item.getString("area_id");
			area_name = item.getString("area_name");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		id_s.add(area_id);
		name_s.add(area_name);
		viewHolder.tv.setText(area_name);

		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}

	public String getAre_id(int position) {
		return id_s.get(position);
	}

	public String getAre_name(int position) {
		return name_s.get(position);
	}


}
