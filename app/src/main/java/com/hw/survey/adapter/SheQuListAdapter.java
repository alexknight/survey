package com.hw.survey.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.hw.survey.R;
import com.hw.survey.adapter.base.BaseViewHolder;
import com.hw.survey.adapter.base.MyBaseAdapter;
import com.hw.survey.family.XiaoQu;

import java.util.List;

/**
 * @desc 附近的地址列表适配器
 * @author Nate
 * @date 2015-12-20
 */
public class SheQuListAdapter extends MyBaseAdapter<XiaoQu> {

	public SheQuListAdapter(Context context, int resource, List<XiaoQu> list) {
		super(context, resource, list);
	}

	@Override
	public void setConvert(BaseViewHolder viewHolder, XiaoQu info) {
		viewHolder.setTextView(R.id.item_name_tv, info.title);

		if(info.isDone()){
			((TextView)viewHolder.getView(R.id.item_name_tv)).setTextColor(Color.parseColor("#666666"));
		}
	}

}
