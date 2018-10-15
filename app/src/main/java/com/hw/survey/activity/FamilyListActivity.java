package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.adapter.FamilyListAdapter;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.Family;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FamilyListActivity extends Activity {

    @InjectView(R.id.family_list_view)
    ListView family_list_view;

    @InjectView(R.id.tv_total_family)
    TextView beizhu;

    @InjectView(R.id.btn_new)
    Button btn_new;

    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.btn_confirm)
    Button btn_confim;


    private FamilyListAdapter familyListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_list);
        ButterKnife.inject(this);

        familyListAdapter = new FamilyListAdapter(this,R.layout.item_family,MyApplication.currentUsers.getSelectUser().families);
        family_list_view.setAdapter(familyListAdapter);
        family_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(FamilyListActivity.this,FamilyInfoActivity.class);
                intent.putExtra("pos",position);
                startActivity(intent);
            }
        });


        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Family> families = MyApplication.currentUsers.getSelectUser().families;
                if(families.size() > 0){
                    for(Family f: families){
                        if(!f.isAllDone()){
                            Toast.makeText(FamilyListActivity.this,"您有未完成调查的家庭，请完成后再开始新一户家庭的调查",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }
                Family family = new Family("家庭"+(MyApplication.currentUsers.getSelectUser().families.size()+1));
                MyApplication.currentUsers.getSelectUser().families.add(family);
                familyListAdapter.notifyDataSetChanged();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtil.saveUsers(FamilyListActivity.this.getApplicationContext(), MyApplication.currentUsers,true);
                finish();
            }
        });

        beizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FamilyListActivity.this,SheQuActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        familyListAdapter.notifyDataSetChanged();
    }
}
