package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.hw.survey.family.Person;
import com.hw.survey.util.AddressUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.hw.survey.MyApplication.currentUsers;

public class MemberInfoActivity extends Activity implements PopupMenu.OnMenuItemClickListener{

    TextView loc;
    TextView locDetail;

    EditText age;
    TextView sex;

    Button btn_next;

    Button btn_prev;

    BetterSpinner hukou;

    @InjectView(R.id.hukouDetail)
    BetterSpinner hukouDetail;

    @InjectView(R.id.hukouDetailLinearLayout)
    LinearLayout hukouDetailLinearLayout;

    BetterSpinner spinnerLiveTime;

    @InjectView(R.id.eduLevel)
    BetterSpinner eduLevel;

    BetterSpinner spinnerCarrer;

    @InjectView(R.id.carrerLevel)
    BetterSpinner carrerLevel;

    @InjectView(R.id.carrerLevelLinearLayout)
    LinearLayout carrerLevelLinearLayout;

    @InjectView(R.id.jobTypeLinearLayout)
    LinearLayout jobTypeLinearLayout;

    @InjectView(R.id.jobType)
    BetterSpinner jobType;

    @InjectView(R.id.yearIncome)
    BetterSpinner yearIncome;

    @InjectView(R.id.hasFreeParking)
    BetterSpinner hasFreeParking;

    @InjectView(R.id.yearIncomeLinearLayout)
    LinearLayout yearIncomeLinearLayout;

    BetterSpinner spinnerStudy;

    BetterSpinner spinnerStopType;

    BetterSpinner spinnerStopFee;

    ImageView btn_back;

    private int posFamily;
    private int posPerson;

    private Person person;

    private View inHomeArea;
    private BetterSpinner inHomeReason;
    private TextView inHomeOk;

    ArrayAdapter<String> adapterHukou;
    ArrayAdapter<String> adapterHukouDetail;
    ArrayAdapter<String> adapterLiveTime;
    ArrayAdapter<String> adapterEduLevel;
    ArrayAdapter<String> adapterComeFromCity;
    ArrayAdapter<String> adapterCarrer;
    ArrayAdapter<String> adapterCarrerLevel;
    ArrayAdapter<String> adapterJobType;
    ArrayAdapter<String> adapterStudy;
    ArrayAdapter<String> adapterTripWay;
    ArrayAdapter<String> adapterStopType;
    ArrayAdapter<String> adapterStopFee;
    ArrayAdapter<String> adapterYearIncome;
    ArrayAdapter<String> adapterHasFreeParking;

    ArrayAdapter<String> adapterInHome;

    private int hukou_pos = -1;

    private String familyLoc = "";

    private String workInHomeReason = "";

    private View stopTypeArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        posFamily = intent.getIntExtra("posFamily", -1);
        posPerson = intent.getIntExtra("posPerson", -1);

        person = new Person("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);
        ButterKnife.inject(this);


        adapterHukou = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.hukou));
        adapterHukouDetail = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.hukouDetail));
        adapterLiveTime = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.live_time));
        adapterEduLevel = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.eduLevel));
        adapterComeFromCity = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.out_code));
        adapterCarrer = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.carree));
        adapterCarrerLevel= new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.carrerLevel));
        adapterJobType = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.jobType));
        adapterYearIncome = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.yearIncome));
        adapterHasFreeParking = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.hasFreeParking));
        adapterStudy = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.study_type));
        adapterTripWay = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.traffic_way));
        adapterStopType = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.stop_place_complan));
        adapterStopFee = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.stop_fee));

        adapterInHome = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.work_in_home));

        loc = findViewById(R.id.tv_family_loc);
        locDetail = findViewById(R.id.tv_family_loc_detail);

        inHomeArea = findViewById(R.id.inHomeArea);
        inHomeArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        inHomeReason = findViewById(R.id.inHomeReason);
        inHomeReason.setAdapter(adapterInHome);
        inHomeOk = findViewById(R.id.inHomeConfirm);
        inHomeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = inHomeReason.getText().toString();
                if(TextUtils.isEmpty(reason)){
                    Toast.makeText(MemberInfoActivity.this,"请选择该成员工作单位地址与家庭地址一致的原因",Toast.LENGTH_LONG).show();
                    return;
                }

                inHomeArea.setVisibility(View.GONE);

                person.workInHomeReason = reason;
                workInHomeReason = reason;
            }
        });


        stopTypeArea = findViewById(R.id.stopTypeArea);

        age = findViewById(R.id.editTextFamilyUP6);
        age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(TextUtils.isEmpty(age.getText().toString()))
                        return;
                    int i = Integer.valueOf(age.getText().toString());
                    if(i<=6){
                        Toast.makeText(MemberInfoActivity.this,
                                "调查成员应大于六周岁",Toast.LENGTH_SHORT).show();
                        age.setText("");
                    }
                }
            }
        });

        sex = findViewById(R.id.tv_sex);


        spinnerLiveTime = findViewById(R.id.liveTime);
        spinnerLiveTime.setAdapter(adapterLiveTime);

        eduLevel.setAdapter(adapterEduLevel);

        spinnerCarrer = findViewById(R.id.editTextCarrer);
        spinnerCarrer.setAdapter(adapterCarrer);
        spinnerCarrer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(position < 3){
//                    findViewById(R.id.studyArea).setVisibility(View.VISIBLE);
//                }else {
//                    findViewById(R.id.studyArea).setVisibility(View.GONE);
//                }
                if (position == 3 || position == 4 || position == 7 || position == 8){
                    carrerLevelLinearLayout.setVisibility(View.VISIBLE);
                }else{
                    carrerLevelLinearLayout.setVisibility(View.GONE);
                    if (position == 0 || position == 1 || position == 2 || position == 9 || position == 10){
                        jobTypeLinearLayout.setVisibility(View.GONE);
                    }else {
                        jobTypeLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
                if (position == 0 || position == 1 || position == 2){
                    yearIncomeLinearLayout.setVisibility(View.GONE);
                }else {
                    yearIncomeLinearLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        carrerLevel.setAdapter(adapterCarrerLevel);
        jobType.setAdapter(adapterJobType);
        yearIncome.setAdapter(adapterYearIncome);
        hasFreeParking.setAdapter(adapterHasFreeParking);

        spinnerStudy = findViewById(R.id.editTextStudyType);
        spinnerStudy.setAdapter(adapterStudy);

        hukou = findViewById(R.id.editTextHukou);
        hukou.setAdapter(adapterHukou);
        hukou.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hukou_pos = position;
                if(position != 0){
                    hukouDetailLinearLayout.setVisibility(View.GONE);
                    findViewById(R.id.liveTimeArea).setVisibility(View.VISIBLE);

                }else {
                    //杭州户口
                    hukouDetailLinearLayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.liveTimeArea).setVisibility(View.GONE);
                }
                spinnerLiveTime.setText("");
            }
        });

        hukouDetail.setAdapter(adapterHukouDetail);
        hukouDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    //非本户户籍
                    findViewById(R.id.liveTimeArea).setVisibility(View.VISIBLE);
                }else {
                    findViewById(R.id.liveTimeArea).setVisibility(View.GONE);
                }
            }
        });


        spinnerStopType = findViewById(R.id.editTextStopType);
        spinnerStopType.setAdapter(adapterStopType);
        spinnerStopFee = findViewById(R.id.editTextStopFee);
        spinnerStopFee.setAdapter(adapterStopFee);

        btn_next = (Button)findViewById(R.id.btn_next);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_prev = (Button)findViewById(R.id.btn_prev);

        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MemberInfoActivity.this, v);//第二个参数是绑定的那个view
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                //填充菜单
                inflater.inflate(R.menu.sex, popup.getMenu());
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        sex.setText(item.getTitle());
                        return false;
                    }
                });
                //显示(这一行代码不要忘记了)
                popup.show();
            }
        });

        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] lo = AddressUtils.getAddressFromString(loc.getText().toString());
                Intent intent = new Intent(MemberInfoActivity.this, LocationActivity.class);
                intent.putExtra("x",lo[0]);
                intent.putExtra("y",lo[1]);
                startActivityForResult(intent, 1);
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saveCurrentMember()){
                    return;
                }
                Intent intent = new Intent(MemberInfoActivity.this,MemberInfo2Activity.class);
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
            String slocPoint = "x="+data.getStringExtra("Ing") + "  y="+data.getStringExtra("Iat");
            String slocAddress = data.getStringExtra("DetailedAddress");

            if(slocPoint.equals(familyLoc)){
                inHomeArea.setVisibility(View.VISIBLE);
            }

            loc.setText("x="+data.getStringExtra("Ing") + "  y="+data.getStringExtra("Iat"));
            locDetail.setText(data.getStringExtra("DetailedAddress"));
        }
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s) || s.equals("请输入") || s.equals("请选择") ||s.equals("x=0.0000  y=0.0000");
    }

    private void initView(){
        if(posFamily > -1 &&MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family =MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1 && family != null && family.people != null && family.people.size() > posPerson){
                person = family.people.get(posPerson);
                familyLoc = family.address;

                if(!isEmpty(person.address)){
                    loc.setText(person.address);
                }

                if(!isEmpty(person.addressDetail)){
                    locDetail.setText(person.addressDetail);
                }

                if(person.age > 6){
                    age.setText(String.valueOf(person.age));
                    age.setSelection(String.valueOf(person.age).length());
                }

                if(!isEmpty(person.sex)){
                    sex.setText(person.sex);
                }

                if(!isEmpty(person.hukou)){
                    hukou.setText(person.hukou);
                    if(person.hukou.equals("重庆市主城九区户籍")){
                        findViewById(R.id.liveTimeArea).setVisibility(View.GONE);
                        spinnerLiveTime.setText("");
                    }else {
                        findViewById(R.id.liveTimeArea).setVisibility(View.VISIBLE);
                    }
                }

                if(!isEmpty(person.hukouDetail)){
                    hukouDetail.setText(person.hukouDetail);
                }

                if(!isEmpty(person.liveTime)){
                    spinnerLiveTime.setText(person.liveTime);
                }

                if(!isEmpty(person.eduLevel)){
                    eduLevel.setText(person.eduLevel);
                }

                if(!isEmpty(person.carrer)){
                    spinnerCarrer.setText(person.carrer);
                    if(person.carrer.equals("高中生") || person.carrer.equals("小学生")||person.carrer.equals("初中生")){
                        findViewById(R.id.studyArea).setVisibility(View.VISIBLE);
                    }
                }

                if(!isEmpty(person.carrerLevel)){
                    carrerLevel.setText(person.carrerLevel);
                }

                if(!isEmpty(person.jobType)){
                    jobType.setText(person.jobType);
                }

                if(!isEmpty(person.yearIncome)){
                    yearIncome.setText(person.yearIncome);
                }

                if(!isEmpty(person.hasFreeParking)){
                    hasFreeParking.setText(person.hasFreeParking);
                }


                if(!isEmpty(person.studyType)){
                    spinnerStudy.setText(person.studyType);
                }

                if(!isEmpty(person.commonTripWay)){
                    if(person.commonTripWay.equals("自驾小汽车")){
                        stopTypeArea.setVisibility(View.VISIBLE);
                        findViewById(R.id.stopFeeArea).setVisibility(View.VISIBLE);
                    }
                }

                if(!isEmpty(person.stopType)){
                    spinnerStopType.setText(person.stopType);
                }

                if(!isEmpty(person.stopFee)){
                    spinnerStopFee.setText(person.stopFee);
                }

            }
        }
    }

    private boolean saveCurrentMember(){
        if(posFamily > -1 &&MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family =MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1 && family != null && family.people != null &&
                    family.people.size() > posPerson && person != null){

                if(!isEmpty(loc.getText().toString())){
                    person.address = loc.getText().toString();
                }else {
                    Toast.makeText(MemberInfoActivity.this,"地址不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(locDetail.getText().toString())){
                    person.addressDetail = locDetail.getText().toString();
                }else {
                    Toast.makeText(MemberInfoActivity.this,"地址不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(age.getText().toString())){
                    person.age = Integer.valueOf(age.getText().toString());
                }else {
                    Toast.makeText(MemberInfoActivity.this,"年龄不能为空！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(sex.getText().toString())){
                    person.sex = sex.getText().toString();
                }else {
                    Toast.makeText(MemberInfoActivity.this,"请选择性别！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(hukou.getText().toString())){
                    person.hukou = hukou.getText().toString();
                }else {
                    Toast.makeText(MemberInfoActivity.this,"请选择杭州市户籍详细情况！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(hukouDetail.getText().toString())){
                    person.hukouDetail = hukouDetail.getText().toString();
                }else {
                    Toast.makeText(MemberInfoActivity.this,"请选择户籍情况！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(findViewById(R.id.liveTimeArea).getVisibility() == View.VISIBLE){
                    if(!isEmpty(spinnerLiveTime.getText().toString())){
                        person.liveTime = spinnerLiveTime.getText().toString();
                    }else {
                        Toast.makeText(MemberInfoActivity.this,"请选择本地居住时间！",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

                if(!isEmpty(eduLevel.getText().toString())){
                    person.eduLevel = eduLevel.getText().toString();
                }

                if(!isEmpty(spinnerCarrer.getText().toString())){
                    person.carrer = spinnerCarrer.getText().toString();
                }else {
                    Toast.makeText(MemberInfoActivity.this,"请选择职业！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(!isEmpty(carrerLevel.getText().toString())){
                    person.carrerLevel = carrerLevel.getText().toString();
                }else {
                    Toast.makeText(MemberInfoActivity.this,"请选择职位！",Toast.LENGTH_LONG).show();
                    return false;
                }

                if(jobTypeLinearLayout.getVisibility() == View.VISIBLE){
                    if(!isEmpty(jobType.getText().toString())){
                        person.jobType = jobType.getText().toString();
                    }else {
                        Toast.makeText(MemberInfoActivity.this,"请选择工作行业！",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }


                if(!isEmpty(yearIncome.getText().toString())){
                    person.yearIncome = yearIncome.getText().toString();
                }else {
                    Toast.makeText(MemberInfoActivity.this,"请选择年收入！",Toast.LENGTH_LONG).show();
                    return false;
                }


                if(!isEmpty(hasFreeParking.getText().toString())){
                    person.hasFreeParking = hasFreeParking.getText().toString();
                }else {
                    Toast.makeText(MemberInfoActivity.this,"请选择单位是否有免费停车位！",Toast.LENGTH_LONG).show();
                    return false;
                }


                if(findViewById(R.id.studyArea).getVisibility() == View.VISIBLE){
                    if(!isEmpty(spinnerStudy.getText().toString())){
                        person.studyType = spinnerStudy.getText().toString();
                    }else {
                        Toast.makeText(MemberInfoActivity.this,"请选择就学情况！",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else {
                    person.studyType = "";
                }

                if(findViewById(R.id.stopTypeArea).getVisibility() == View.VISIBLE){
                    if(!isEmpty(spinnerStopType.getText().toString())){
                        person.stopType = spinnerStopType.getText().toString();
                    }else {
                        Toast.makeText(MemberInfoActivity.this,"请选择单位使用停车场类别！",Toast.LENGTH_LONG).show();
                        return false;
                }
                }else {
                    person.stopType = "";
                }

                if(findViewById(R.id.stopFeeArea).getVisibility() == View.VISIBLE){
                    if(!isEmpty(spinnerStopFee.getText().toString())){
                        person.stopFee = spinnerStopFee.getText().toString();
                    }else {
                        Toast.makeText(MemberInfoActivity.this,"请选择单位月均停车费！",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }else {
                    person.stopFee = "";
                }

                family.people.set(posPerson,person);

                MyApplication.currentUsers.getSelectUser().families.set(posFamily,family);
                DataUtil.saveUsers(MemberInfoActivity.this.getApplicationContext(), currentUsers);
                return true;
            }
        }
        return false;
    }
}
