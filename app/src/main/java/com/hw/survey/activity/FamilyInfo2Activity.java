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
import com.hw.survey.util.ViewUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.hw.survey.MyApplication.currentUsers;

public class FamilyInfo2Activity extends Activity {

    @InjectView(R.id.btn_next)
    Button btn_next;

    @InjectView(R.id.btn_prev)
    Button btn_prev;

    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.editText6)
    BetterSpinner otherCar;

    @InjectView(R.id.editText7)
    BetterSpinner stopFee;

    @InjectView(R.id.spinnerPlan)
    BetterSpinner spinnerPlan;

    @InjectView(R.id.spinner_battery)
    BetterSpinner spinner_battery;

    @InjectView(R.id.editTextFamilyPhone)
    EditText familyPhone;


    ArrayAdapter<String> adapterStopPlace;
    ArrayAdapter<String> adapterStopFee;
    ArrayAdapter<String> adapterPlan;
    ArrayAdapter<String> adapterBattery;

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        pos = intent.getIntExtra("pos", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_info2);
        ButterKnife.inject(this);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saveCurrentFamily()){
                    return;
                }
                Intent intent = new Intent(FamilyInfo2Activity.this,PepoleLIstActivity.class);
                intent.putExtra("pos",pos);
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



        initView();
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s) || s.equals("请输入") || s.equals("x=0.0000  y=0.0000");
    }

    private boolean saveCurrentFamily(){
        if(pos > -1 &&MyApplication.currentUsers.getSelectUser().families.size() > pos){
            Family family =MyApplication.currentUsers.getSelectUser().families.get(pos);
            if(family != null){

                if(family.carNum < 1){
                    family.stopPlace = "";
                    family.stopFee = "";
                }else {
                    if(!isEmpty(otherCar.getText().toString())){
                        family.stopPlace = otherCar.getText().toString();
                    }else {
                        Toast.makeText(FamilyInfo2Activity.this,"请选择夜间停放地点",Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if(!isEmpty(stopFee.getText().toString())){
                        family.stopFee = stopFee.getText().toString();
                    }else {
                        Toast.makeText(FamilyInfo2Activity.this,"请选择夜间停放费用",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                if(!isEmpty(spinnerPlan.getText().toString())){
                    family.buyPlan = spinnerPlan.getText().toString();
                }else {
                    Toast.makeText(FamilyInfo2Activity.this,"请选择购车计划",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(spinner_battery.getText().toString())){
                    family.batteryCar = spinner_battery.getText().toString();
                }else {
                    Toast.makeText(FamilyInfo2Activity.this,"请选择是否考虑购买新能源汽车",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(familyPhone.getText().toString())){
                    family.phone = familyPhone.getText().toString();
                }

                family.initMembers();


                MyApplication.currentUsers.getSelectUser().families.set(pos,family);
                DataUtil.saveUsers(FamilyInfo2Activity.this.getApplicationContext(), currentUsers);
                return true;
            }
        }
        return false;
    }

    private void initView(){
        if(pos > -1 && MyApplication.currentUsers.getSelectUser().families.size() > pos){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(pos);
            if(family != null){

                adapterStopPlace = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.stop_place));
                otherCar.setAdapter(adapterStopPlace);
                adapterStopFee = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.stop_fee));
                stopFee.setAdapter(adapterStopFee);
                adapterPlan =new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.buy_plan));
                spinnerPlan.setAdapter(adapterPlan);
                adapterBattery = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.yes_no));
                spinner_battery.setAdapter(adapterBattery);

                if(family.carNum < 1){
                    otherCar.setDropDownHeight(0);
                    otherCar.setEnabled(false);
                    stopFee.setDropDownHeight(0);
                    stopFee.setEnabled(false);
                    ViewUtils.disableSpinner(otherCar);
                    ViewUtils.disableSpinner(stopFee);
                }else {
                    if(!TextUtils.isEmpty(family.stopPlace)){
                        otherCar.setText(family.stopPlace);
                    }
                    if(!TextUtils.isEmpty(family.stopFee)){
                        stopFee.setText(family.stopFee);
                    }
                }

                if(!TextUtils.isEmpty(family.buyPlan)){
                    spinnerPlan.setText(family.buyPlan);
                }

                if(!TextUtils.isEmpty(family.batteryCar)){
                    spinner_battery.setText(family.batteryCar);
                }

                if(!TextUtils.isEmpty(family.phone)){
                    familyPhone.setText(family.phone);
                }
            }
        }
    }
}