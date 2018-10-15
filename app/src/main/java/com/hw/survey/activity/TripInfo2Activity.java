package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.Family;
import com.hw.survey.family.Person;
import com.hw.survey.family.Trip;
import com.hw.survey.util.ViewUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.List;

import static com.hw.survey.MyApplication.currentUsers;

public class TripInfo2Activity extends Activity {

    private int posFamily;
    private int posPerson;
    private int posTrip;

    EditText outFee;
    EditText stopFee;
    EditText outPersonNum;
    BetterSpinner spinner;
    View stopFeeArea;
    View peopleNumArea;
    View withFamilyArea;
    Trip trip;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        final Intent intent = this.getIntent();
        posFamily = intent.getIntExtra("posFamily", -1);
        posPerson = intent.getIntExtra("posPerson", -1);
        posTrip = intent.getIntExtra("posTrip", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info2);


        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saveCurrentTrip()){
                    return;
                }
                Intent intent1 = new Intent(TripInfo2Activity.this,TripListActivity.class);
                intent1.putExtra("posFamily",posFamily);
                intent1.putExtra("posPerson",posPerson);
                startActivity(intent1);
            }
        });

        spinner = findViewById(R.id.haveFamily);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.yes_no));
        spinner.setAdapter(adapter);

        outFee = findViewById(R.id.editTextOutFee);
        stopFee = findViewById(R.id.editTextFee);
        outPersonNum = findViewById(R.id.editTextNum);

        outFee.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String lastInputContent = dest.toString();
                if (lastInputContent.contains(".")) {
                    int index = lastInputContent.indexOf(".");
                    if(dend - index >= 2){
                        return "";
                    }
                }
                return null;
            }
        }});

        outPersonNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String text = outPersonNum.getText().toString();
                    if(!TextUtils.isEmpty(text)){
                        int i = Integer.valueOf(text);
                        if(i > 0){
                            withFamilyArea.setVisibility(View.VISIBLE);
                        }else {
                            withFamilyArea.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        outPersonNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s.toString())){
                    int i = Integer.valueOf(s.toString());
                    if(i > 0){
                        ViewUtils.enableSpinner(spinner);
                    }else {
                        ViewUtils.disableSpinner(spinner);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        stopFeeArea = findViewById(R.id.stopFeeArea);
        peopleNumArea = findViewById(R.id.peopleArea);
        withFamilyArea = findViewById(R.id.withFamilyArea);

        initData();

    }

    private void initData(){
        if(posFamily > -1 && MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1 && family != null && family.people != null && family.people.size() > posPerson){
                List<Trip> trips = family.people.get(posPerson).tripList;
                if(posTrip > -1 && trips != null && trips.size() > posTrip){
                    trip = trips.get(posTrip);
                    outFee.setText(String.valueOf(trip.fee));

                    if(trip.isTripWayContain("自驾小汽车")
                            || trip.isTripWayContain("出租车")
                            || trip.isTripWayContain("摩托车")){
                        stopFee.setText(String.valueOf(trip.stopFee));
                        outPersonNum.setText(String.valueOf(trip.outNum));
                        if(trip.outNum == 0){
                            ViewUtils.disableSpinner(spinner);
                        }else {
                            if(!isEmpty(trip.isHaveFamily)){
                                spinner.setText(trip.isHaveFamily);
                            }
                        }
                    }else {
                        stopFee.setEnabled(false);
                        stopFee.setBackgroundColor(0xFFDDDDDD);
                        outPersonNum.setEnabled(false);
                        outPersonNum.setBackgroundColor(0xFFDDDDDD);
                        ViewUtils.disableSpinner(spinner);
                    }
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


                    if(!isEmpty(outFee.getText().toString())){
                        trip.fee = Double.valueOf(outFee.getText().toString());
                    }else {
                        Toast.makeText(TripInfo2Activity.this, "请输入出行费用",Toast.LENGTH_LONG).show();
                        return false;
                    }

                    if(stopFeeArea.getVisibility() == View.VISIBLE){
                        if(!isEmpty(stopFee.getText().toString())){
                            trip.stopFee = Integer.valueOf(stopFee.getText().toString());
                        }else {
                            Toast.makeText(TripInfo2Activity.this, "请输入停车费用",Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }else {
                        trip.stopFee = 0;
                    }

                    if(peopleNumArea.getVisibility() == View.VISIBLE){
                        if(!isEmpty(outPersonNum.getText().toString())){
                            trip.outNum = Integer.valueOf(outPersonNum.getText().toString());
                        }else {
                            Toast.makeText(TripInfo2Activity.this, "请输入同行人数",Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }else {
                        trip.outNum = 0;
                    }

                    if(withFamilyArea.getVisibility() == View.VISIBLE){
                        if(!isEmpty(spinner.getText().toString())){
                            trip.isHaveFamily = spinner.getText().toString();
                        }else if(trip.outNum > 0){
                            Toast.makeText(TripInfo2Activity.this, "请选择是否含家人同行",Toast.LENGTH_LONG).show();
                            return false;
                        }else {
                            trip.isHaveFamily = "";
                        }
                    }else {
                        trip.isHaveFamily = "";
                    }

                    MyApplication.currentUsers.getSelectUser().families.get(posFamily).people.get(posPerson).tripList.set(posTrip,trip);
                    DataUtil.saveUsers(TripInfo2Activity.this.getApplicationContext(), currentUsers);
                    return true;

                }

            }
        }
        return false;
    }
}
