package com.hw.survey.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.Family;
import com.hw.survey.util.AddressUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.hw.survey.MyApplication.currentUsers;

public class FamilyInfoActivity extends Activity implements PopupMenu.OnMenuItemClickListener {


    @InjectView(R.id.btn_next)
    Button btn_next;

    @InjectView(R.id.btn_prev)
    Button btn_prev;

    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.tv_family_loc)
    TextView address;

    @InjectView(R.id.buildFinishTime)
    BetterSpinner buildFinishTime;

    @InjectView(R.id.tv_family_loc_detail)
    TextView addressDetail;

    @InjectView(R.id.editTextFamilyTotal)
    EditText total;

    @InjectView(R.id.editTextFamilyUP6)
    EditText up6Num;

    @InjectView(R.id.editBike)
    EditText editBike;

    @InjectView(R.id.editAutoBike)
    EditText editAutoBike;

    @InjectView(R.id.editTextMobike)
    EditText editTextMobike;

    @InjectView(R.id.editTextCar)
    EditText editTextCar;

    @InjectView(R.id.hangzhouCarNumLinearLayout)
    LinearLayout hangzhouCarNumLinearLayout;

    @InjectView(R.id.newEnergyCarNumLinearLayout)
    LinearLayout newEnergyCarNumLinearLayout;

    @InjectView(R.id.editTextCompanyCar)
    EditText editTextCompanyCar;

    @InjectView(R.id.editOtherCar)
    EditText editOtherCar;

    @InjectView(R.id.hangzhouCarNum)
    EditText hangzhouCarNum;

    @InjectView(R.id.newEnegyCarNum)
    EditText newEnegyCarNum;

    @InjectView(R.id.limitCarNum)
    EditText limitCarNum;


    @InjectView(R.id.editTextMobike)
    EditText moto;

    @InjectView(R.id.spinner_house_size)
    BetterSpinner spinner_house_size;


    ArrayAdapter<String> adapterHouseSize;
    ArrayAdapter<String> adapterFinishTime;

    private int pos;

    Family family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        pos = intent.getIntExtra("pos", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_info);
        ButterKnife.inject(this);

        adapterHouseSize = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.house_size));
        spinner_house_size.setAdapter(adapterHouseSize);

        adapterFinishTime = new ArrayAdapter<String>(this,R.layout.spinner_item,getResources().getStringArray(R.array.house_age));
        buildFinishTime = findViewById(R.id.buildFinishTime);
        buildFinishTime.setAdapter(adapterFinishTime);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saveCurrentFamily()){
                    return;
                }
                DataUtil.saveUsers(FamilyInfoActivity.this.getApplicationContext(), currentUsers);
                Intent intent = new Intent(FamilyInfoActivity.this,FamilyInfo2Activity.class);
                intent.putExtra("pos",pos);
                startActivity(intent);
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] loc = AddressUtils.getAddressFromString(address.getText().toString());
                Intent intent = new Intent(FamilyInfoActivity.this, LocationActivity.class);
                intent.putExtra("x",loc[0]);
                intent.putExtra("y",loc[1]);
                startActivityForResult(intent, 1);
            }
        });

        total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        up6Num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(TextUtils.isEmpty(s.toString()))
                    return;
                int i = Integer.valueOf(s.toString());
                int tt = 0;
                if(!TextUtils.isEmpty(total.getText().toString())){
                    tt = Integer.valueOf(total.getText().toString());
                }
                int temp = 0;

                if(i > 0 && (i >= tt - temp)){
                    Toast.makeText(FamilyInfoActivity.this,
                            "家庭中应包含至少一位六周岁以上人口",Toast.LENGTH_SHORT).show();
                    up6Num.setText("0");
                }

            }
        });

        EditText[] allCars = {editBike, editAutoBike, editTextMobike, editTextCar, editTextCompanyCar, editOtherCar};

        EditText[] partCars = {hangzhouCarNum,newEnegyCarNum,limitCarNum};


        for (final EditText car: allCars){
            car.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(s.toString()))
                        return;
                    int i = Integer.valueOf(s.toString());

                    if(i > 10 ){
                        Toast.makeText(FamilyInfoActivity.this,
                                "请确认该车辆数量是否超过10辆",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        for (final EditText partCar: partCars){
            if (Integer.valueOf(partCar.getText().toString())>Integer.valueOf(editTextCar.getText().toString())){
                Toast.makeText(FamilyInfoActivity.this,
                        "数量超过本家庭私人小客车数量，请核实",Toast.LENGTH_SHORT).show();
            }
        }

        editTextCar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(TextUtils.isEmpty(s.toString()))
                    return;
                int num = Integer.valueOf(s.toString());
                if (num > 0){
                    hangzhouCarNumLinearLayout.setVisibility(View.VISIBLE);
                    newEnergyCarNumLinearLayout.setVisibility(View.VISIBLE);
                }else {
                    hangzhouCarNumLinearLayout.setVisibility(View.INVISIBLE);
                    newEnergyCarNumLinearLayout.setVisibility(View.INVISIBLE);
                }


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

        final EditText carNum = findViewById(R.id.editTextCar);

        initView();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1
                && data != null && data.getExtras() != null) {

            address.setText("x="+data.getStringExtra("Ing") + "  y="+data.getStringExtra("Iat"));
            addressDetail.setText(data.getStringExtra("DetailedAddress"));
        }
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s) || s.equals("请输入") || s.equals("x=0.0000  y=0.0000");
    }

    private boolean saveCurrentFamily(){
        if(pos > -1 && MyApplication.currentUsers.getSelectUser().families.size() > pos){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(pos);
            if(family != null){

                if(!isEmpty(address.getText().toString())){
                    family.address = address.getText().toString();
                }else {
                    Toast.makeText(FamilyInfoActivity.this,"家庭地址不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(addressDetail.getText().toString())){
                    family.addressDetail = addressDetail.getText().toString();
                }else {
                    Toast.makeText(FamilyInfoActivity.this,"家庭地址不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(buildFinishTime.getText().toString())){
                    family.buildFinishTime = buildFinishTime.getText().toString();
                }

                if(!isEmpty(spinner_house_size.getText().toString())){
                    family.houseSize = spinner_house_size.getText().toString();
                }else {
                    Toast.makeText(FamilyInfoActivity.this,"请选择套内住房面积",Toast.LENGTH_LONG).show();
                    return false;
                }


                if(!isEmpty(total.getText().toString())){
                    family.totalNum = Integer.valueOf(total.getText().toString());
                    if(family.totalNum == 0){
                        Toast.makeText(FamilyInfoActivity.this,"家庭总人数不能为0！",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else {
                    Toast.makeText(FamilyInfoActivity.this,"家庭总人数不能为0！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(up6Num.getText().toString())){
                    family.up6Num = Integer.valueOf(up6Num.getText().toString());
                }


                if(!isEmpty(editTextCar.getText().toString())){
                    family.carNum = Integer.valueOf(editTextCar.getText().toString());
                }


                if(!isEmpty(moto.getText().toString())){
                    family.motoNum = Integer.valueOf(moto.getText().toString());
                }

                family.initMembers();

                MyApplication.currentUsers.getSelectUser().families.set(pos,family);
                return true;
            }
        }
        return false;
    }

    private void initView(){
        if(pos > -1 && MyApplication.currentUsers.getSelectUser().families.size() > pos){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(pos);
            if(family != null){

                if(!TextUtils.isEmpty(family.address)){
                    address.setText(family.address);
                }

                if(!TextUtils.isEmpty(family.addressDetail)){
                    addressDetail.setText(family.addressDetail);
                }


                if(family.totalNum > 0){
                    total.setText(String.valueOf(family.totalNum));
                }

                if(family.up6Num > 0){
                    up6Num.setText(String.valueOf(family.up6Num));
                }

                if(family.carNum > 0){
                    editTextCar.setText(String.valueOf(family.carNum));
                }

                if(family.motoNum > 0){
                    moto.setText(String.valueOf(family.motoNum));
                }

                if(!TextUtils.isEmpty(family.houseSize)){
                    spinner_house_size.setText(family.houseSize);
                }

            }
        }
    }
}
