package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.adapter.SheQuListAdapter;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.XiaoQu;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SheQuActivity extends Activity {
    @InjectView(R.id.xiaoqu_list_view)
    ListView xiaoqu_list_view;

    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.btn_confirm)
    Button btn_confim;

    private SheQuListAdapter sheQuListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_qu);
        ButterKnife.inject(this);

        sheQuListAdapter = new SheQuListAdapter(this,R.layout.item_xiaoqu, MyApplication.currentUsers.getSelectUser().xiaoQuses);
        xiaoqu_list_view.setAdapter(sheQuListAdapter);
        xiaoqu_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(SheQuActivity.this,XiaoQuActivity.class);
                intent.putExtra("pos",position);
                startActivity(intent);
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
                List<XiaoQu> xiaoQus = MyApplication.currentUsers.getSelectUser().xiaoQuses;
                boolean isFinish = false;
                for(XiaoQu xiaoQu:xiaoQus){
                    if(xiaoQu.isDone()){
                        isFinish = true;
                        break;
                    }
                }

                if (isFinish){
                    MyApplication.currentUsers.getSelectUser().isXiaoQuFinish = true;
                    DataUtil.saveUsers(SheQuActivity.this.getApplicationContext(), MyApplication.currentUsers);
                    Intent intent = new Intent(SheQuActivity.this,FamilyListActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(SheQuActivity.this,"请至少填写完整一个小区！",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sheQuListAdapter.notifyDataSetChanged();
    }
}
