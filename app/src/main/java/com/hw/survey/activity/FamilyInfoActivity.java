package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.Family;
import com.hw.survey.util.AddressUtils;
import com.hw.survey.util.ViewUtils;
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

    @InjectView(R.id.tv_family_loc_detail)
    TextView addressDetail;

    @InjectView(R.id.editTextFamilyTotal)
    EditText total;

    @InjectView(R.id.editTextFamilyUP6)
    EditText up6Num;

    @InjectView(R.id.editTextFamilyTemp)
    EditText tempNum;

    @InjectView(R.id.editTextCar)
    EditText car;

    @InjectView(R.id.carBelong)
    BetterSpinner carBelong;

    @InjectView(R.id.editTextMobike)
    EditText moto;

    @InjectView(R.id.spinner_house_size)
    BetterSpinner spinner_house_size;

    @InjectView(R.id.spinner_total_income)
    BetterSpinner spinner_total_income;

    @InjectView(R.id.spinner_house_belong)
    BetterSpinner spinner_house_belong;

    ArrayAdapter<String> adapterHouseSize;
    ArrayAdapter<String> adapterHouseBelong;
    ArrayAdapter<String> adapterTotalIncome;

    private int pos;

    ArrayAdapter<String> adapter;

    Family family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        pos = intent.getIntExtra("pos", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_info);
        ButterKnife.inject(this);

        adapter = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.car_belong));
        carBelong.setAdapter(adapter);

        adapterHouseSize = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.house_size));
        spinner_house_size.setAdapter(adapterHouseSize);

        adapterHouseBelong = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.house_belong));
        spinner_house_belong.setAdapter(adapterHouseBelong);

        adapterTotalIncome = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.total_income));
        spinner_total_income.setAdapter(adapterTotalIncome);

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
                if(!TextUtils.isEmpty(tempNum.getText().toString())){
                    temp = Integer.valueOf(tempNum.getText().toString());
                }
                if(i > 0 && (i >= tt - temp)){
                    Toast.makeText(FamilyInfoActivity.this,
                            "家庭中应包含至少一位六周岁以上人口",Toast.LENGTH_SHORT).show();
                    up6Num.setText("0");
                }

            }
        });


        tempNum.addTextChangedListener(new TextWatcher() {
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
                int kid = 0;
                if(!TextUtils.isEmpty(up6Num.getText().toString())){
                    kid = Integer.valueOf(up6Num.getText().toString());
                }
                if(i > 0 && (i >= tt - kid)){
                    Toast.makeText(FamilyInfoActivity.this,
                            "家庭中应包含至少一位成年人",Toast.LENGTH_SHORT).show();
                    tempNum.setText("0");
                }
            }
        });

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
                int carNum = Integer.valueOf(s.toString());
                if(carNum > 0){
                    ViewUtils.enableSpinner(carBelong);
                }else {
                    ViewUtils.disableSpinner(carBelong);
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

        findViewById(R.id.btn_car_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FamilyInfoActivity.this, CarDetailActivity.class);
                intent.putExtra("carNum",Integer.valueOf(carNum.getText().toString()));
                startActivity(intent);
            }
        });

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

                if(!isEmpty(spinner_house_size.getText().toString())){
                    family.houseSize = spinner_house_size.getText().toString();
                }else {
                    Toast.makeText(FamilyInfoActivity.this,"请选择套内住房面积",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(spinner_house_belong.getText().toString())){
                    family.houseBelong = spinner_house_belong.getText().toString();
                }else {
                    Toast.makeText(FamilyInfoActivity.this,"请选择房屋产权",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(spinner_total_income.getText().toString())){
                    family.totalIncome = spinner_total_income.getText().toString();
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

                if(!isEmpty(tempNum.getText().toString())){
                    family.tempNum = Integer.valueOf(tempNum.getText().toString());
                }

                if(!isEmpty(car.getText().toString())){
                    family.carNum = Integer.valueOf(car.getText().toString());
                }

                if(family.carNum > 0){
                    if(!isEmpty(carBelong.getText().toString())){
                        family.carAddress = carBelong.getText().toString();
                    }else {
                        Toast.makeText(FamilyInfoActivity.this,"请选择车牌归属地！",Toast.LENGTH_LONG).show();
                        return false;
                    }
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

                if(!TextUtils.isEmpty(family.carAddress)){
                    carBelong.setText(family.carAddress);
                }

                if(family.totalNum > 0){
                    total.setText(String.valueOf(family.totalNum));
                }

                if(family.up6Num > 0){
                    up6Num.setText(String.valueOf(family.up6Num));
                }

                if(family.tempNum > 0){
                    tempNum.setText(String.valueOf(family.tempNum));
                }

                if(family.carNum > 0){
                    car.setText(String.valueOf(family.carNum));
                }else {
                    ViewUtils.disableSpinner(carBelong);
                }

                if(family.motoNum > 0){
                    moto.setText(String.valueOf(family.motoNum));
                }

                if(!TextUtils.isEmpty(family.houseSize)){
                    spinner_house_size.setText(family.houseSize);
                }

                if(!TextUtils.isEmpty(family.houseBelong)){
                    spinner_house_belong.setText(family.houseBelong);
                }

                if(!TextUtils.isEmpty(family.totalIncome)){
                    spinner_total_income.setText(family.totalIncome);
                }
            }
        }
    }
}
