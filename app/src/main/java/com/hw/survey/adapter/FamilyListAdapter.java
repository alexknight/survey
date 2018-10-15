package com.hw.survey.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hw.survey.R;
import com.hw.survey.adapter.base.BaseViewHolder;
import com.hw.survey.adapter.base.MyBaseAdapter;
import com.hw.survey.family.Family;
import com.hw.survey.family.Person;

import java.util.List;

/**
 * @desc 附近的地址列表适配器
 * @author Nate
 * @date 2015-12-20
 */
public class FamilyListAdapter extends MyBaseAdapter<Family> {

	public FamilyListAdapter(Context context, int resource, List<Family> list) {
		super(context, resource, list);
	}

	@Override
	public void setConvert(BaseViewHolder viewHolder, Family info) {
		viewHolder.setTextView(R.id.item_name_tv, info.name);
		viewHolder.setTextView(R.id.item_detail_tv, "成员数："+info.people.size());
		int ourNum = 0;
		if(info.people != null){
			for (Person p :
					info.people) {
				ourNum = ourNum + p.tripList.size();
			}
		}
		viewHolder.setTextView(R.id.item_detail_out,"出行："+ourNum);

		final int p = viewHolder.getPosition();
		viewHolder.getView(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				list.remove(p);
				FamilyListAdapter.this.notifyDataSetChanged();
			}
		});

		if(info.isAllDone()){
			((TextView)viewHolder.getView(R.id.item_name_tv)).setTextColor(Color.parseColor("#666666"));
		}else {
			((TextView)viewHolder.getView(R.id.item_name_tv)).setTextColor(Color.parseColor("#ff3b3b"));
		}
	}

}
