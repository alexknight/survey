package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.hw.survey.util.ViewUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

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
    BetterSpinner spinnerLocalPhone;
    BetterSpinner spinnerPhoneName;

    EditText planeTime;
    EditText trainTime;

    EditText tripTime;
    BetterSpinner spinnerObjectPlace;
    BetterSpinner spinnerAim;
    BetterSpinner spinnerWay;

    ArrayAdapter<String> adapterLocalPhone;
    ArrayAdapter<String> adapterPhoneName;
    ArrayAdapter<String> adapterObjectPlace;
    ArrayAdapter<String> adapterAim;
    ArrayAdapter<String> adapterWay;

    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        posFamily = intent.getIntExtra("posFamily", -1);
        posPerson = intent.getIntExtra("posPerson", -1);

        person = new Person("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info2);

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

        planeTime  = findViewById(R.id.planeTime);
        trainTime = findViewById(R.id.trainTime);

        tripTime = findViewById(R.id.tripTime);
        tripTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s.toString())){
                    int i = Integer.valueOf(s.toString());
                    if(i == 0){
                        ViewUtils.disableSpinner(spinnerAim);
                        ViewUtils.disableSpinner(spinnerObjectPlace);
                        ViewUtils.disableSpinner(spinnerWay);
                    }else {
                        ViewUtils.enableSpinner(spinnerAim);
                        ViewUtils.enableSpinner(spinnerObjectPlace);
                        ViewUtils.enableSpinner(spinnerWay);
                    }
                }
            }
        });

        //init Spinner
        adapterLocalPhone = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.yes_no));
        adapterPhoneName = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.name_type));
        adapterObjectPlace = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.object_place));
        adapterAim = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.trip_aim));
        adapterWay = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.trip_type));

        spinnerLocalPhone = findViewById(R.id.editTextChoose);
        spinnerLocalPhone.setAdapter(adapterLocalPhone);

        spinnerPhoneName = findViewById(R.id.editTextType);
        spinnerPhoneName.setAdapter(adapterPhoneName);

        spinnerObjectPlace = findViewById(R.id.objectPlace);
        spinnerObjectPlace.setAdapter(adapterObjectPlace);

        spinnerAim = findViewById(R.id.aim);
        spinnerAim.setAdapter(adapterAim);

        spinnerWay = findViewById(R.id.tripType);
        spinnerWay.setAdapter(adapterWay);
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

                planeTime.setText(String.valueOf(person.planeTime));
                trainTime.setText(String.valueOf(person.trainTime));

                tripTime.setText(String.valueOf(person.tripTime));
                if(person.tripTime > 0){
                    ViewUtils.enableSpinner(spinnerAim);
                    ViewUtils.enableSpinner(spinnerObjectPlace);
                    ViewUtils.enableSpinner(spinnerWay);
                }else {
                    ViewUtils.disableSpinner(spinnerAim);
                    ViewUtils.disableSpinner(spinnerObjectPlace);
                    ViewUtils.disableSpinner(spinnerWay);
                }



                if(!isEmpty(person.hukou)){
                    if(person.hukou.equals("重庆市主城九区户籍") || person.hukou.contains("重庆市")){
                        findViewById(R.id.localArea).setVisibility(View.GONE);
                    }else if((isEmpty(person.liveTime)) || (person.liveTime.equals("六个月以上"))){
                        findViewById(R.id.localArea).setVisibility(View.GONE);
                    }else {
                        spinnerLocalPhone.setText(person.isLocalNum);
                    }
                }

                if(!person.isStudent()){
                    findViewById(R.id.phoneArea).setVisibility(View.GONE);
                }else {
                    spinnerPhoneName.setText(person.phoneName);
                }

                if(!isEmpty(person.objectPlace)){
                    spinnerObjectPlace.setText(person.objectPlace);
                }

                if(!isEmpty(person.aim)){
                    spinnerAim.setText(person.aim);
                }

                if(!isEmpty(person.tripWay)){
                    spinnerWay.setText(person.tripWay);
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

                if(findViewById(R.id.localArea).getVisibility() == View.VISIBLE){
                    if(!isEmpty(spinnerLocalPhone.getText().toString())){
                        person.isLocalNum = spinnerLocalPhone.getText().toString();
                    }else {
                        Toast.makeText(MemberInfo2Activity.this,"请选择是否更换为本地手机号！",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else {
                    person.isLocalNum = "";
                }

                if(findViewById(R.id.phoneArea).getVisibility() == View.VISIBLE){
                    if(!isEmpty(spinnerPhoneName.getText().toString())){
                        person.phoneName = spinnerPhoneName.getText().toString();
                    }else {
                        Toast.makeText(MemberInfo2Activity.this,"请选择手机注册名！",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else {
                    person.phoneName = "";
                }

                if(!isEmpty(planeTime.getText().toString())){
                    person.planeTime = Integer.valueOf(planeTime.getText().toString());
                }else {
                    Toast.makeText(MemberInfo2Activity.this,"近一年乘坐飞机出行的次数不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(trainTime.getText().toString())){
                    person.trainTime = Integer.valueOf(trainTime.getText().toString());
                }else {
                    Toast.makeText(MemberInfo2Activity.this,"近一年乘坐火车出行的次数不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(tripTime.getText().toString())){
                    person.tripTime = Integer.valueOf(tripTime.getText().toString());
                }else {
                    Toast.makeText(MemberInfo2Activity.this,"出行次数不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }


                if(person.tripTime > 0){
                    if(!isEmpty(spinnerObjectPlace.getText().toString())){
                        person.objectPlace = spinnerObjectPlace.getText().toString();
                    }else {
                        Toast.makeText(MemberInfo2Activity.this,"请选择主要目的地！",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if(!isEmpty(spinnerAim.getText().toString())){
                        person.aim = spinnerAim.getText().toString();
                    }else {
                        Toast.makeText(MemberInfo2Activity.this,"请选择出行目的！",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if(!isEmpty(spinnerWay.getText().toString())){
                        person.tripWay = spinnerWay.getText().toString();
                    }else {
                        Toast.makeText(MemberInfo2Activity.this,"请选择出行方式！",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else {
                    person.objectPlace = "";
                    person.aim = "";
                    person.tripWay = "";
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
