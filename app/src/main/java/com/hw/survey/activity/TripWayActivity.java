package com.hw.survey.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.Family;
import com.hw.survey.family.Trip;
import com.hw.survey.family.TripWay;
import com.hw.survey.util.DialogUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.hw.survey.MyApplication.currentUsers;

public class TripWayActivity extends Activity {
    @InjectView(R.id.btn_next)
    Button btn_next;

    @InjectView(R.id.btn_prev)
    Button btn_prev;

    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.title)
    TextView title;

    @InjectView(R.id.first_way)
    BetterSpinner spinnerWay1;

    @InjectView(R.id.second_way)
    BetterSpinner spinnerWay2;

    @InjectView(R.id.third_way)
    BetterSpinner spinnerWay3;

    @InjectView(R.id.fourth_way)
    BetterSpinner spinnerWay4;

    @InjectView(R.id.fifth_way)
    BetterSpinner spinnerWay5;

    @InjectView(R.id.firstWayLinearLayout)
    View firstWayLinearLayout;

    @InjectView(R.id.secondWayLinearLayout)
    View secondWayLinearLayout;

    @InjectView(R.id.thirdWayLinearLayout)
    View thirdWayLinearLayout;

    @InjectView(R.id.fourthWayLinearLayout)
    View fourthWayLinearLayout;

    @InjectView(R.id.fifthWayLinearLayout)
    View fifthWayLinearLayout;


    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    ArrayAdapter<String> adapterStopType;

    private boolean isATrip = false;
    private int totalTime = 0;

    private int posFamily;
    private int posPerson;
    private int posTrip;
    private int posTripWay;

    private TripWay tripWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_way);
        ButterKnife.inject(this);

        Intent intent = this.getIntent();
        posFamily = intent.getIntExtra("posFamily", -1);
        posPerson = intent.getIntExtra("posPerson", -1);
        posTrip = intent.getIntExtra("posTrip", -1);
        posTripWay = intent.getIntExtra("posTripWay", -1);
        initView();
        initData();
    }

    private Handler handler = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            processNext();
        }
    };

    private void processNext(){

        if(!saveData()){
            return;
        }

        Intent intent = new Intent(TripWayActivity.this,TripWay2Activity.class);

        intent.putExtra("posFamily",posFamily);
        intent.putExtra("posPerson",posPerson);
        intent.putExtra("posTrip",posTrip);
        intent.putExtra("posTrip",posTripWay);
        startActivity(intent);

//        if(tripWay.hasNext.equals("是") && posTripWay < 4){
//            int size = MyApplication.currentUsers.getSelectUser().families.get(posFamily).people.get(posPerson).tripList.get(posTrip).tripWays.size();
//            int nextPos = posTripWay + 1;
//            if(size <= nextPos){
//                MyApplication.currentUsers.getSelectUser().families.get(posFamily).people.
//                        get(posPerson).tripList.get(posTrip).tripWays.add(
//                        new TripWay("第"+(nextPos + 1)+"种交通方式",tripWay.totalTime - getCurrentTotalTime()));
//            }
//
//            Intent intent = new Intent(TripWayActivity.this,TripWayActivity.class);
//
//            intent.putExtra("posFamily",posFamily);
//            intent.putExtra("posPerson",posPerson);
//            intent.putExtra("posTrip",posTrip);
//            intent.putExtra("posTripWay",nextPos);
//            startActivity(intent);
//        }else {
//            Intent intent = new Intent(TripWayActivity.this,TripInfo2Activity.class);
//
//            intent.putExtra("posFamily",posFamily);
//            intent.putExtra("posPerson",posPerson);
//            intent.putExtra("posTrip",posTrip);
//            startActivity(intent);
//        }
    }

    private void initView(){
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
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerWay1.requestFocus();
                handler.postDelayed(delayRun, 100);
            }
        });

        adapter1 = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.traffic_way));
        spinnerWay1.setAdapter(adapter1);
        adapter2 = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.traffic_way2));
        spinnerWay2.setAdapter(adapter2);
        spinnerWay3.setAdapter(adapter2);
        spinnerWay4.setAdapter(adapter2);
        spinnerWay5.setAdapter(adapter2);

        spinnerWay1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    secondWayLinearLayout.setVisibility(View.GONE);
                    thirdWayLinearLayout.setVisibility(View.GONE);
                    fourthWayLinearLayout.setVisibility(View.GONE);
                    fifthWayLinearLayout.setVisibility(View.GONE);
                }else {
                    secondWayLinearLayout.setVisibility(View.VISIBLE);
                    thirdWayLinearLayout.setVisibility(View.VISIBLE);
                    fourthWayLinearLayout.setVisibility(View.VISIBLE);
                    fifthWayLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    private boolean isEmpty(String s){
        return TextUtils.isEmpty(s) || s.equals("请输入");
    }

    private void initData(){
        if(posFamily > -1 && MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1 && family != null && family.people != null && family.people.size() > posPerson){
                List<Trip> trips = family.people.get(posPerson).tripList;
                if(posTrip > -1 && trips != null && trips.size() > posTrip){
                    isATrip = trips.get(posTrip).isATrip();
                    totalTime = trips.get(posTrip).getTirpTime();
                   tripWay = trips.get(posTrip).tripWays.get(posTripWay);
//                    if(!isEmpty(tripWay.name)){
//                        title.setText(tripWay.name);
//                    }

                }

            }
        }
    }

    private boolean saveData(){
        if(!isEmpty(spinnerWay1.getText().toString())){
            tripWay.tripWay = spinnerWay1.getText().toString();
        }else {
            Toast.makeText(TripWayActivity.this,"请选择交通方式",Toast.LENGTH_LONG).show();
            return false;
        }


        if(posFamily < 0 || posPerson < 0 || posTrip < 0 || posTripWay < 0){
            return false;
        }
        MyApplication.currentUsers.getSelectUser().families.get(posFamily).people.get(posPerson).tripList.get(posTrip).tripWays.set(posTripWay,tripWay);
        DataUtil.saveUsers(TripWayActivity.this.getApplicationContext(), currentUsers);
        return true;
    }
}
