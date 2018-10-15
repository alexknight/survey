package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.family.XiaoQu;
import com.hw.survey.util.AddressUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

public class XiaoQuActivity extends Activity {
    private int pos;

    private XiaoQu xiaoQu;

    private TextView loc;
    private TextView name;
    private BetterSpinner age;
    private View liveRateArea;
    private EditText liveRate;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiao_qu);
        Intent intent = this.getIntent();
        pos = intent.getIntExtra("pos", -1);
        if(pos == -1 ){
            finish();
        }
        xiaoQu = MyApplication.currentUsers.getSelectUser().xiaoQuses.get(pos);

        TextView title = findViewById(R.id.title);
        title.setText(xiaoQu.title);

        liveRateArea = findViewById(R.id.liveRateArea);
        liveRate = findViewById(R.id.liveRate);

        loc = findViewById(R.id.tv_family_loc);
        if(!TextUtils.isEmpty(xiaoQu.address)){
            loc.setText(xiaoQu.address);
        }
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] lo = AddressUtils.getAddressFromString(loc.getText().toString());
                Intent intent = new Intent(XiaoQuActivity.this, LocationActivity.class);
                intent.putExtra("x",lo[0]);
                intent.putExtra("y",lo[1]);
                startActivityForResult(intent, 1);
            }
        });

        findViewById(R.id.iv_loc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] lo = AddressUtils.getAddressFromString(loc.getText().toString());
                Intent intent = new Intent(XiaoQuActivity.this, LocationActivity.class);
                intent.putExtra("x",lo[0]);
                intent.putExtra("y",lo[1]);
                startActivityForResult(intent, 1);
            }
        });

        name = findViewById(R.id.tv_family_loc_detail);
        if(!TextUtils.isEmpty(xiaoQu.name)){
            name.setText(xiaoQu.name);
        }

        adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,getResources().getStringArray(R.array.house_age));
        age = findViewById(R.id.finishTime);
        age.setAdapter(adapter);
        if(!TextUtils.isEmpty(xiaoQu.age)){
            age.setText(xiaoQu.age);
            if(xiaoQu.age.equals("2010年至2014年") || xiaoQu.age.equals("2015年") ||
                    xiaoQu.age.equals("2016年") ||  xiaoQu.age.equals("2017年及以后")){
                liveRateArea.setVisibility(View.VISIBLE);
            }
        }

        age.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                xiaoQu.age = adapter.getItem(position);
                if(position > 3){
                    liveRateArea.setVisibility(View.VISIBLE);
                }else {
                    liveRateArea.setVisibility(View.GONE);
                    liveRate.setText("");
                }

            }
        });

        liveRate.setText(String.valueOf(xiaoQu.rate));
        liveRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString()))
                    return;
                int i = Integer.valueOf(s.toString());
                if(i > 100){
                    liveRate.setText("100");
                }else if(i < 0){
                    liveRate.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final CheckBox c01 = findViewById(R.id.ck_01);
        final CheckBox c02 = findViewById(R.id.ck_02);
        final CheckBox c03 = findViewById(R.id.ck_03);
        final CheckBox c04 = findViewById(R.id.ck_04);
        final CheckBox c05 = findViewById(R.id.ck_05);
        final CheckBox c06 = findViewById(R.id.ck_06);
        final CheckBox c07 = findViewById(R.id.ck_07);

        final CheckBox c11 = findViewById(R.id.ck_11);
        final CheckBox c12 = findViewById(R.id.ck_12);
        final CheckBox c13 = findViewById(R.id.ck_13);
        final CheckBox c14 = findViewById(R.id.ck_14);
        final CheckBox c15 = findViewById(R.id.ck_15);
        final CheckBox c16 = findViewById(R.id.ck_16);
        final CheckBox c17 = findViewById(R.id.ck_17);

        c01.setChecked(xiaoQu.isSelect01);
        c02.setChecked(xiaoQu.isSelect02);
        c03.setChecked(xiaoQu.isSelect03);
        c04.setChecked(xiaoQu.isSelect04);
        c05.setChecked(xiaoQu.isSelect05);
        c06.setChecked(xiaoQu.isSelect06);
        c07.setChecked(xiaoQu.isSelect07);

        c11.setChecked(xiaoQu.isSelect11);
        c12.setChecked(xiaoQu.isSelect12);
        c13.setChecked(xiaoQu.isSelect13);
        c14.setChecked(xiaoQu.isSelect14);
        c15.setChecked(xiaoQu.isSelect15);
        c16.setChecked(xiaoQu.isSelect16);
        c17.setChecked(xiaoQu.isSelect17);

        Button button = findViewById(R.id.btn_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(loc.getText()) || loc.getText().equals("x=0.0000  y=0.0000")){
                    Toast.makeText(XiaoQuActivity.this,"请选择小区地址",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(name.getText())){
                    Toast.makeText(XiaoQuActivity.this,"请选择小区地址",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(age.getText())){
                    Toast.makeText(XiaoQuActivity.this,"请选择小区年代",Toast.LENGTH_LONG).show();
                    return;
                }

                xiaoQu.address = loc.getText().toString();
                xiaoQu.name = name.getText().toString();
                if(liveRateArea.getVisibility() == View.VISIBLE){
                    if(TextUtils.isEmpty(liveRate.getText())){
                        Toast.makeText(XiaoQuActivity.this,"请输入入住率",Toast.LENGTH_LONG).show();
                        return;
                    }else {
                        xiaoQu.rate = Integer.valueOf(liveRate.getText().toString());
                    }
                }

                xiaoQu.start10 = "";
                if(c01.isChecked()){
                    xiaoQu.start10 = c01.getText().toString();
                    xiaoQu.isSelect01 = true;
                }else {
                    xiaoQu.isSelect01 = false;
                }
                if(c02.isChecked()){
                    xiaoQu.start10 = xiaoQu.start10 + "," + c02.getText().toString();
                    xiaoQu.isSelect02 = true;
                }else {
                    xiaoQu.isSelect02 = false;
                }
                if(c03.isChecked()){
                    xiaoQu.start10 = xiaoQu.start10 + "," + c03.getText().toString();
                    xiaoQu.isSelect03 = true;
                }else {
                    xiaoQu.isSelect03 = false;
                }
                if(c04.isChecked()){
                    xiaoQu.start10 = xiaoQu.start10 + "," + c04.getText().toString();
                    xiaoQu.isSelect04 = true;
                }else {
                    xiaoQu.isSelect04 = false;
                }
                if(c05.isChecked()){
                    xiaoQu.start10 = xiaoQu.start10 + "," + c05.getText().toString();
                    xiaoQu.isSelect05 = true;
                }else {
                    xiaoQu.isSelect05 = false;
                }
                if(c06.isChecked()){
                    xiaoQu.start10 = xiaoQu.start10 + "," + c06.getText().toString();
                    xiaoQu.isSelect06 = true;
                }else {
                    xiaoQu.isSelect06 = false;
                }
                if(c07.isChecked()){
                    xiaoQu.start10 = xiaoQu.start10 + "," + c07.getText().toString();
                    xiaoQu.isSelect07 = true;
                }else {
                    xiaoQu.isSelect07 = false;
                }

                xiaoQu.end10 = "";
                if(c11.isChecked()){
                    xiaoQu.end10 = xiaoQu.end10 + "," + c11.getText().toString();
                    xiaoQu.isSelect11 = true;
                }else {
                    xiaoQu.isSelect11 = false;
                }
                if(c12.isChecked()){
                    xiaoQu.end10 = xiaoQu.end10 + "," + c12.getText().toString();
                    xiaoQu.isSelect12 = true;
                }else {
                    xiaoQu.isSelect12 = false;
                }
                if(c13.isChecked()){
                    xiaoQu.end10 = xiaoQu.end10 + "," + c13.getText().toString();
                    xiaoQu.isSelect13 = true;
                }else {
                    xiaoQu.isSelect13 = false;
                }
                if(c14.isChecked()){
                    xiaoQu.end10 = xiaoQu.end10 + "," + c14.getText().toString();
                    xiaoQu.isSelect14 = true;
                }else {
                    xiaoQu.isSelect14 = false;
                }
                if(c15.isChecked()){
                    xiaoQu.end10 = xiaoQu.end10 + "," + c15.getText().toString();
                    xiaoQu.isSelect15 = true;
                }else {
                    xiaoQu.isSelect15 = false;
                }
                if(c16.isChecked()){
                    xiaoQu.end10 = xiaoQu.end10 + "," + c16.getText().toString();
                    xiaoQu.isSelect16 = true;
                }else {
                    xiaoQu.isSelect16 = false;
                }
                if(c17.isChecked()){
                    xiaoQu.end10 = xiaoQu.end10 + "," + c17.getText().toString();
                    xiaoQu.isSelect17 = true;
                }else {
                    xiaoQu.isSelect17 = false;
                }

                if(xiaoQu.start10.startsWith(",")){
                    xiaoQu.start10 = xiaoQu.start10.substring(1);
                }
                if(xiaoQu.end10.startsWith(",")){
                    xiaoQu.end10 = xiaoQu.end10.substring(1);
                }
                MyApplication.currentUsers.getSelectUser().xiaoQuses.set(pos,xiaoQu);
                finish();
            }
        });

    }

    private String getCheckBoxText(CheckBox checkBox){
        if(checkBox.isChecked()){
            return checkBox.getText().toString();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1
                && data != null && data.getExtras() != null) {

            loc.setText("x="+data.getStringExtra("Ing") + "  y="+data.getStringExtra("Iat"));
            name.setText(data.getStringExtra("Address"));
        }
    }
}
