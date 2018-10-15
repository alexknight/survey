package com.hw.survey.adapter;

import android.content.Context;

import com.hw.survey.R;
import com.hw.survey.adapter.base.BaseViewHolder;
import com.hw.survey.adapter.base.MyBaseAdapter;
import com.hw.survey.family.Car;

import java.util.List;

/**
 * @desc 附近的地址列表适配器
 * @author Nate
 * @date 2015-12-20
 */
public class CarListAdapter extends MyBaseAdapter<Car> {

	public CarListAdapter(Context context, int resource, List<Car> list) {
		super(context, resource, list);
	}

	@Override
	public void setConvert(BaseViewHolder viewHolder, Car info) {
		viewHolder.setTextView(R.id.item_name_tv, info.name);
	}

}
