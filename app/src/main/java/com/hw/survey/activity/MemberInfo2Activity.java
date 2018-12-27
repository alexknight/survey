package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.Family;
import com.hw.survey.family.Person;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.hw.survey.MyApplication.currentUsers;

public class MemberInfo2Activity extends Activity {

    Button btn_next;

    Button btn_prev;

    ImageView btn_back;

    private int posFamily;
    private int posPerson;

    EditText yidong;
    EditText liantong;
    EditText dianxin;

    @InjectView(R.id.leaveHzTimes)
    BetterSpinner leaveHzTimes;

//    @InjectView(R.id.mainTrafficWay)
//    BetterSpinner mainTrafficWay;

    BetterSpinner bearMaxTime;

    ArrayAdapter<String> adapterBearMaxTime;

    ArrayAdapter<String> adapterLeaveHzTimes;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        posFamily = intent.getIntExtra("posFamily", -1);
        posPerson = intent.getIntExtra("posPerson", -1);

        person = new Person("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info2);
        ButterKnife.inject(this);

        initView();

        initData();
    }

    private void initView(){
        //init Button
        btn_next = findViewById(R.id.btn_next);
        btn_back = findViewById(R.id.btn_back);
        btn_prev = findViewById(R.id.btn_prev);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saveCurrentMember()){
                    return;
                }
                Intent intent = new Intent(MemberInfo2Activity.this,TripListActivity.class);
                intent.putExtra("posFamily",posFamily);
                intent.putExtra("posPerson",posPerson);
                startActivity(intent);
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //init Input
        yidong = findViewById(R.id.editTextPhoneNum);
        liantong = findViewById(R.id.editTextLianTongNum);
        dianxin = findViewById(R.id.editTextDianxinNum);



        //init Spinner
        adapterLeaveHzTimes = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.leaveHzTimes));
        adapterBearMaxTime = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.bearMaxTime));


        leaveHzTimes.setAdapter(adapterLeaveHzTimes);


        bearMaxTime = findViewById(R.id.bearMaxTime);
        bearMaxTime.setAdapter(adapterBearMaxTime);
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s) || s.equals("请输入") || s.equals("x=0.0000  y=0.0000");
    }

    private void initData(){
        if(posFamily > -1 && MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family =MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1 && family != null && family.people != null && family.people.size() > posPerson){
                person = family.people.get(posPerson);

                yidong.setText(String.valueOf(person.yidongNum));
                liantong.setText(String.valueOf(person.liantongNum));
                dianxin.setText(String.valueOf(person.dianxinNum));

                leaveHzTimes.setText(String.valueOf(person.leaveHzTimes));



                if(!isEmpty(person.hukou)){
                    if(person.hukou.equals("重庆市主城九区户籍") || person.hukou.contains("重庆市")){
                        findViewById(R.id.localArea).setVisibility(View.GONE);
                    }else if((isEmpty(person.liveTime)) || (person.liveTime.equals("六个月以上"))){
                        findViewById(R.id.localArea).setVisibility(View.GONE);
                    }
                }

                if(!person.isStudent()){
                    findViewById(R.id.phoneArea).setVisibility(View.GONE);
                }


                if(!isEmpty(person.bearMaxTime)){
                    bearMaxTime.setText(person.bearMaxTime);
                }
            }
        }
    }

    private boolean saveCurrentMember(){
        if(posFamily > -1 &&MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family =MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1 && family != null && family.people != null &&
                    family.people.size() > posPerson && person != null){

                if(!isEmpty(yidong.getText().toString())){
                    person.yidongNum = Integer.valueOf(yidong.getText().toString());
                }else {
                    Toast.makeText(MemberInfo2Activity.this,"移动常用号数量不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(liantong.getText().toString())){
                    person.liantongNum = Integer.valueOf(liantong.getText().toString());
                }else {
                    Toast.makeText(MemberInfo2Activity.this,"联通常用号数量不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(dianxin.getText().toString())){
                    person.dianxinNum = Integer.valueOf(dianxin.getText().toString());
                }else {
                    Toast.makeText(MemberInfo2Activity.this,"电信常用号数量不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }


                if(!isEmpty(leaveHzTimes.getText().toString())){
                    person.leaveHzTimes = Integer.valueOf(leaveHzTimes.getText().toString());
                }else {
                    Toast.makeText(MemberInfo2Activity.this,"近一年乘坐飞机出行的次数不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }


                family.people.set(posPerson,person);

                MyApplication.currentUsers.getSelectUser().families.set(posFamily,family);
                DataUtil.saveUsers(MemberInfo2Activity.this.getApplicationContext(), currentUsers);
                return true;
            }
        }
        return false;
    }
}
