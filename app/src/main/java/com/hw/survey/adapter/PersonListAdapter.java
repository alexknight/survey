package com.hw.survey.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hw.survey.R;
import com.hw.survey.adapter.base.BaseViewHolder;
import com.hw.survey.adapter.base.MyBaseAdapter;
import com.hw.survey.family.Person;

import java.util.List;

/**
 * @desc 附近的地址列表适配器
 * @author Nate
 * @date 2015-12-20
 */
public class PersonListAdapter extends MyBaseAdapter<Person> {

	public PersonListAdapter(Context context, int resource, List<Person> list) {
		super(context, resource, list);
	}

	@Override
	public void setConvert(BaseViewHolder viewHolder, Person info) {
		viewHolder.setTextView(R.id.item_name_tv, info.name);
		viewHolder.setTextView(R.id.item_detail_tv, "出行："+info.tripList.size());
		final int p = viewHolder.getPosition();
		viewHolder.getView(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				list.remove(p);
				PersonListAdapter.this.notifyDataSetChanged();
			}
		});

		if(info.isAllDone()){
			((TextView)viewHolder.getView(R.id.item_name_tv)).setTextColor(Color.parseColor("#666666"));
		}
	}

}
