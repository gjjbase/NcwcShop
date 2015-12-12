package com.ncwc.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ncwc.shop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL-PC on 2015/11/26.
 */
public class ChooseKuaidiAdapter extends BaseAdapter {

	private Context context;
	private JSONArray list;

	private String kuaidi_id = "";
	private String name = "";
	private List<String> id_s = new ArrayList<String>();
	private List<String> name_s = new ArrayList<String>();

	public ChooseKuaidiAdapter(Context context, JSONArray list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.length();
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
		//解析处理数据
		try {
			JSONObject item = list.getJSONObject(position);
			kuaidi_id = item.getString("id");
			name = item.getString("name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		id_s.add(kuaidi_id);
		name_s.add(name);
		viewHolder.tv.setText(name);
		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}

	public String getKuaidi_id(int position) {
		return id_s.get(position);
	}

	public String getKuaidi_name(int position) {
		return name_s.get(position);
	}

}
