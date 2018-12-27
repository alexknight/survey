package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.Family;
import com.hw.survey.family.Person;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.ArrayList;

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


    @InjectView(R.id.ck_01)
    CheckBox ck_01;

    @InjectView(R.id.ck_02)
    CheckBox ck_02;

    @InjectView(R.id.ck_03)
    CheckBox ck_03;

    @InjectView(R.id.ck_04)
    CheckBox ck_04;

    @InjectView(R.id.ck_05)
    CheckBox ck_05;

    @InjectView(R.id.ck_06)
    CheckBox ck_06;

    @InjectView(R.id.ck_07)
    CheckBox ck_07;

    @InjectView(R.id.ck_08)
    CheckBox ck_08;

    @InjectView(R.id.ck_09)
    CheckBox ck_09;



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

        ck_01.setChecked(person.isSelect01);
        ck_02.setChecked(person.isSelect02);
        ck_03.setChecked(person.isSelect03);
        ck_04.setChecked(person.isSelect04);
        ck_05.setChecked(person.isSelect05);
        ck_06.setChecked(person.isSelect06);
        ck_07.setChecked(person.isSelect07);
        ck_08.setChecked(person.isSelect08);
        ck_09.setChecked(person.isSelect09);
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


                if(!isEmpty(person.bearMaxTime)){
                    bearMaxTime.setText(person.bearMaxTime);
                }
            }
        }
    }

    private boolean saveCurrentMember(){
        if(posFamily > -1 &&MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family =MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1  && family.people != null &&
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
                    person.leaveHzTimes = leaveHzTimes.getText().toString();
                }else {
                    Toast.makeText(MemberInfo2Activity.this,"近一年乘坐飞机出行的次数不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }

                ArrayList<String> s = new ArrayList<>();

                if(ck_01.isChecked()){
                    person.isSelect01 = true;
                    s.add("高铁或城际铁路");
                }
                if (ck_03.isChecked()){
                    person.isSelect03 = true;
                    s.add("普通铁路");
                }
                if (ck_04.isChecked()){
                    person.isSelect04 = true;
                    s.add("长途客车");
                }
                if (ck_05.isChecked()){
                    person.isSelect05 = true;
                    s.add("旅游大巴");
                }
                if (ck_06.isChecked()){
                    person.isSelect06 = true;
                    s.add("出租车");
                }
                if (ck_07.isChecked()){
                    person.isSelect07 = true;
                    s.add("网约车");
                }
                if (ck_08.isChecked()){
                    person.isSelect08 = true;
                    s.add("私人小汽车");
                }
                if (ck_09.isChecked()){
                    person.isSelect09 = true;
                    s.add("其他");
                }

                if (s.size()>2){
                    Toast.makeText(MemberInfo2Activity.this,"最近一个月离开的杭州的主要交通工具不超过两项",Toast.LENGTH_LONG).show();
                    return false;
                }else{
                    person.choosen = s;
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
