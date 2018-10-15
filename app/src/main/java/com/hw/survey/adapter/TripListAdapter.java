package com.hw.survey.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hw.survey.R;
import com.hw.survey.adapter.base.BaseViewHolder;
import com.hw.survey.adapter.base.MyBaseAdapter;
import com.hw.survey.family.Trip;

import java.util.List;

/**
 * @desc 附近的地址列表适配器
 * @author Nate
 * @date 2015-12-20
 */
public class TripListAdapter extends MyBaseAdapter<Trip> {

	public TripListAdapter(Context context, int resource, List<Trip> list) {
		super(context, resource, list);
	}

	@Override
	public void setConvert(BaseViewHolder viewHolder, Trip info) {
		viewHolder.setTextView(R.id.item_name_tv, info.name);
		final int p = viewHolder.getPosition();
		viewHolder.getView(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				list.remove(p);
				TripListAdapter.this.notifyDataSetChanged();
			}
		});

		if(info.isFinish()){
			((TextView)viewHolder.getView(R.id.item_name_tv)).setTextColor(Color.parseColor("#666666"));
		}
	}

}
