package com.hw.survey.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.Family;
import com.hw.survey.family.Person;
import com.hw.survey.family.Trip;
import com.hw.survey.family.TripWay;
import com.hw.survey.util.AddressUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.hw.survey.MyApplication.currentUsers;

public class TripInfoActivity extends Activity {

    @InjectView(R.id.btn_back)
    View btn_back;

    @InjectView(R.id.tv_out_date)
    TextView tv_out_date;

    @InjectView(R.id.tv_out_time)
    TextView tv_out_time;

    @InjectView(R.id.tv_family_loc)
    TextView tv_out_address;

    @InjectView(R.id.tv_family_loc_detail)
    TextView tv_out_address_detail;

    @InjectView(R.id.tv_in_time)
    TextView tv_in_time;

    @InjectView(R.id.tv_in_loc)
    TextView tv_in_address;

    @InjectView(R.id.tv_in_loc_detail)
    TextView tv_in_address_detail;

    @InjectView(R.id.editTextAim)
    BetterSpinner spinnerAim;

    private ArrayAdapter<String> adapterAim;

    private int posFamily;
    private int posPerson;
    private int posTrip;
    private Trip trip;

    private int timeFrom = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        posFamily = intent.getIntExtra("posFamily", -1);
        posPerson = intent.getIntExtra("posPerson", -1);
        posTrip = intent.getIntExtra("posTrip", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);
        ButterKnife.inject(this);
        
        adapterAim = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.aim));
        spinnerAim.setAdapter(adapterAim);

        Calendar ca = Calendar.getInstance();
        final int mYear = ca.get(Calendar.YEAR);
        final int mMonth = ca.get(Calendar.MONTH);
        final int mDay = ca.get(Calendar.DAY_OF_MONTH);
        final int mHour = ca.get(Calendar.HOUR_OF_DAY);
        final int mMin = ca.get(Calendar.MINUTE);

        findViewById(R.id.btn_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saveCurrentTrip()){
                    return;
                }

                Intent intent = new Intent(TripInfoActivity.this,TripWayActivity.class);

                intent.putExtra("posFamily",posFamily);
                intent.putExtra("posPerson",posPerson);
                intent.putExtra("posTrip",posTrip);
                intent.putExtra("posTripWay",0);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_out_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TripInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String days = "";
                        if (mMonth + 1 < 10) {
                            if (mDay < 10) {
                                days = new StringBuffer().append(mYear).append("年").append("0").
                                        append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                            } else {
                                days = new StringBuffer().append(mYear).append("年").append("0").
                                        append(mMonth + 1).append("月").append(mDay).append("日").toString();
                            }

                        } else {
                            if (mDay < 10) {
                                days = new StringBuffer().append(mYear).append("年").
                                        append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                            } else {
                                days = new StringBuffer().append(mYear).append("年").
                                        append(mMonth + 1).append("月").append(mDay).append("日").toString();
                            }

                        }
                        tv_out_date.setText(days);
                    }
                },mYear,mMonth,mDay).show();
            }
        });

        tv_in_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(TripInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String myMin = String.valueOf(minute);
                        String myHour = String.valueOf(hourOfDay);
                        if(minute < 10 ){
                            myMin = "0" + myMin;
                        }
                        if(hourOfDay < 10){
                            myHour = "0" + myHour;
                        }

                        if(trip.setTime(hourOfDay,minute) <= trip.beginTime){
                            Toast.makeText(TripInfoActivity.this,"出行的到达时间应晚于出行的出发时间",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(trip.setTime(hourOfDay,minute) <= timeFrom){
                            Toast.makeText(TripInfoActivity.this,"本次出行的到达时间应晚于上一次出行的到达时间",Toast.LENGTH_LONG).show();
                            return;
                        }

                        tv_in_time.setText(myHour+" : "+myMin);
                        trip.setEnd(hourOfDay,minute);
                    }
                },mHour,mMin,true){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //焦点释放处理
                        if (dialog instanceof TimePickerDialog) {
                            ((TimePickerDialog)dialog).getWindow().getDecorView().clearFocus();
                        }
                        super.onClick(dialog, which);
                    }
                }.show();
            }
        });

        tv_out_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(TripInfoActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String myMin = String.valueOf(minute);
                        String myHour = String.valueOf(hourOfDay);
                        if(minute < 10 ){
                            myMin = "0" + myMin;
                        }
                        if(hourOfDay < 10){
                            myHour = "0" + myHour;
                        }

                        if(trip.setTime(hourOfDay,minute) <= timeFrom){
                            Toast.makeText(TripInfoActivity.this,"本次出行的到达时间应晚于上一次出行的到达时间",Toast.LENGTH_LONG).show();
                            return;
                        }
                        tv_out_time.setText(myHour+" : "+myMin);
                        trip.setBegin(hourOfDay,minute);
                    }
                },mHour,mMin,true){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //焦点释放处理
                        if (dialog instanceof TimePickerDialog) {
                            ((TimePickerDialog)dialog).getWindow().getDecorView().clearFocus();
                        }
                        super.onClick(dialog, which);
                    }
                }.show();
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_out_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] loc = AddressUtils.getAddressFromString(tv_out_address.getText().toString());

                Intent intent = new Intent(TripInfoActivity.this, LocationActivity.class);
                intent.putExtra("posFamily",posFamily);
                intent.putExtra("posPerson",posPerson);
                intent.putExtra("posTrip",posTrip);
                intent.putExtra("x",loc[0]);
                intent.putExtra("y",loc[1]);
                startActivityForResult(intent, 1);
            }
        });

        tv_in_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] loc = AddressUtils.getAddressFromString(tv_in_address.getText().toString());

                Intent intent = new Intent(TripInfoActivity.this, LocationActivity.class);
                intent.putExtra("posFamily",posFamily);
                intent.putExtra("posPerson",posPerson);
                intent.putExtra("posTrip",posTrip);
                intent.putExtra("x",loc[0]);
                intent.putExtra("y",loc[1]);
                startActivityForResult(intent, 2);
            }
        });

        initView();

    }

    private void initView(){
        if(posFamily > -1 && MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1 && family != null && family.people != null && family.people.size() > posPerson){
                List<Trip> trips = family.people.get(posPerson).tripList;
                if(posTrip > -1 && trips != null && trips.size() > posTrip){
                    trip = trips.get(posTrip);

                    if(posTrip > 0){
                        Trip lastTrip = trips.get(posTrip -1);
                        timeFrom = lastTrip.endTime;

                        if(!isEmpty(lastTrip.arriveAddress)){
                            tv_out_address.setText(lastTrip.arriveAddress);
                        }

                        if(!isEmpty(lastTrip.arriveAddressDetail)){
                            tv_out_address_detail.setText(lastTrip.arriveAddressDetail);
                        }

                    }else {
                        if(!isEmpty(trip.outAddress)){
                            tv_out_address.setText(trip.outAddress);
                        }

                        if(!isEmpty(trip.outAddressDetail)){
                            tv_out_address_detail.setText(trip.outAddressDetail);
                        }

                    }

                    if(!isEmpty(trip.aim)){
                        spinnerAim.setText(trip.aim);
                    }

                    if(!isEmpty(trip.outTime)){
                        tv_out_time.setText(trip.outTime);
                    }



                    if(!isEmpty(trip.arriveTime)){
                        tv_in_time.setText(trip.arriveTime);
                    }

                    if(!isEmpty(trip.arriveAddress)){
                        tv_in_address.setText(trip.arriveAddress);
                    }

                    if(!isEmpty(trip.arriveAddressDetail)){
                        tv_in_address_detail.setText(trip.arriveAddressDetail);
                    }


                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1
                && data != null && data.getExtras() != null) {
            int type = data.getIntExtra("type",-1);
            if(type == 1){
                String address = data.getStringExtra("address");
                String addressDetail = data.getStringExtra("addressDetail");
                String addressType = data.getStringExtra("addressType");
                if((!isEmpty(address)) && address.equals(tv_in_address.getText().toString())){
                    Toast.makeText(TripInfoActivity.this,"此次出行的出发地与到达地一致，请重新填写",Toast.LENGTH_LONG).show();
                }else {
                    tv_out_address.setText(address);
                    tv_out_address_detail.setText(addressDetail);
                }
            }else {
                String address = "x="+data.getStringExtra("Ing") + "  y="+data.getStringExtra("Iat");
                if((!isEmpty(address)) && address.equals(tv_in_address.getText().toString())){
                    Toast.makeText(TripInfoActivity.this,"此次出行的出发地与到达地一致，请重新填写",Toast.LENGTH_LONG).show();
                }else {
                    tv_out_address.setText(address);
                    tv_out_address_detail.setText(data.getStringExtra("DetailedAddress"));
                }
            }
        }else if (resultCode == RESULT_OK && requestCode == 2
                && data != null && data.getExtras() != null) {
            int type = data.getIntExtra("type",-1);
            if(type == 1){
                String address = data.getStringExtra("address");
                if((!isEmpty(address)) && address.equals(tv_out_address.getText().toString())){
                    Toast.makeText(TripInfoActivity.this,"此次出行的出发地与到达地一致，请重新填写",Toast.LENGTH_LONG).show();
                }else {
                    tv_in_address.setText(data.getStringExtra("address"));
                    tv_in_address_detail.setText(data.getStringExtra("addressDetail"));
                }
            }else {
                String address = "x="+data.getStringExtra("Ing") + "  y="+data.getStringExtra("Iat");
                if((!isEmpty(address)) && address.equals(tv_out_address.getText().toString())){
                    Toast.makeText(TripInfoActivity.this,"此次出行的出发地与到达地一致，请重新填写",Toast.LENGTH_LONG).show();
                }else {
                    tv_in_address.setText(address);
                    tv_in_address_detail.setText(data.getStringExtra("DetailedAddress"));
                }
            }
        }
    }

    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s) || s.equals("请输入") || s.equals("x=0.0000  y=0.0000");
    }

    private boolean saveCurrentTrip(){
        if(posFamily > -1 && MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1 && family != null && family.people != null && family.people.size() > posPerson){
                List<Person> people = family.people;
                Person person = people.get(posPerson);
                List<Trip> trips = person.tripList;
                if(posTrip > -1 && trips != null && trips.size() > posTrip){
                    if(!isEmpty(spinnerAim.getText().toString())){
                        trip.aim = spinnerAim.getText().toString();
                    }else {
                        Toast.makeText(TripInfoActivity.this, "请选择出行目的",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if(!isEmpty(tv_out_time.getText().toString())){
                        trip.outTime = tv_out_time.getText().toString();
                    }else {
                        Toast.makeText(TripInfoActivity.this, "请选择出行时间",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if(!isEmpty(tv_out_address.getText().toString())){
                        trip.outAddress = tv_out_address.getText().toString();
                    }else {
                        Toast.makeText(TripInfoActivity.this, "请选择出行地址",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if(!isEmpty(tv_out_address_detail.getText().toString())){
                        trip.outAddressDetail = tv_out_address_detail.getText().toString();
                    }else {
                        Toast.makeText(TripInfoActivity.this, "请选择出行地址",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if(!isEmpty(tv_in_time.getText().toString())){
                        trip.arriveTime = tv_in_time.getText().toString();
                    }else {
                        Toast.makeText(TripInfoActivity.this, "请选择到达时间",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if(!isEmpty(tv_in_address.getText().toString())){
                        trip.arriveAddress = tv_in_address.getText().toString();
                    }else {
                        Toast.makeText(TripInfoActivity.this, "请选择到达地址",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if(!isEmpty(tv_in_address_detail.getText().toString())){
                        trip.arriveAddressDetail = tv_in_address_detail.getText().toString();
                    }else {
                        Toast.makeText(TripInfoActivity.this, "请选择到达地址",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if(trip.tripWays.size() == 0){
                        trip.tripWays.add(new TripWay("第1种交通方式",trip.getTirpTime()));
                    }else {
                        trip.tripWays.get(0).totalTime = trip.getTirpTime();
                    }

                    MyApplication.currentUsers.getSelectUser().families.get(posFamily).people.get(posPerson).tripList.set(posTrip,trip);
                    DataUtil.saveUsers(TripInfoActivity.this.getApplicationContext(), currentUsers);
                    return true;

                }

            }
        }
        return false;
    }
}
