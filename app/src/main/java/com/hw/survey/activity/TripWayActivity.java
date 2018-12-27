package com.hw.survey.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

    @InjectView(R.id.way)
    BetterSpinner spinnerWay;


    @InjectView(R.id.waitTime)
    EditText waitTime;
    boolean isWaitTimeOK = true;


    @InjectView(R.id.waitTimeArea)
    View waitTimeArea;


    @InjectView(R.id.stopTypeArea)
    View stopTypeArea;

    @InjectView(R.id.stopFeeArea)
    View stopFeeArea;

    @InjectView(R.id.stopType)
    BetterSpinner stopType;

    @InjectView(R.id.editTextOutFee)
    EditText editTextOutFee;

    ArrayAdapter<String> adapter;
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
        if(tripWay.hasNext.equals("是") && posTripWay < 4){
            int size = MyApplication.currentUsers.getSelectUser().families.get(posFamily).people.get(posPerson).tripList.get(posTrip).tripWays.size();
            int nextPos = posTripWay + 1;
            if(size <= nextPos){
                MyApplication.currentUsers.getSelectUser().families.get(posFamily).people.
                        get(posPerson).tripList.get(posTrip).tripWays.add(
                        new TripWay("第"+(nextPos + 1)+"种交通方式",tripWay.totalTime - getCurrentTotalTime()));
            }

            Intent intent = new Intent(TripWayActivity.this,TripWayActivity.class);

            intent.putExtra("posFamily",posFamily);
            intent.putExtra("posPerson",posPerson);
            intent.putExtra("posTrip",posTrip);
            intent.putExtra("posTripWay",nextPos);
            startActivity(intent);
        }else {
            Intent intent = new Intent(TripWayActivity.this,TripInfo2Activity.class);

            intent.putExtra("posFamily",posFamily);
            intent.putExtra("posPerson",posPerson);
            intent.putExtra("posTrip",posTrip);
            startActivity(intent);
        }
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
                spinnerWay.requestFocus();
                handler.postDelayed(delayRun, 100);
            }
        });

        adapter = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.traffic_way));
        spinnerWay.setAdapter(adapter);
        spinnerWay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    waitTimeArea.setVisibility(View.GONE);
                    stopTypeArea.setVisibility(View.GONE);
                    stopFeeArea.setVisibility(View.GONE);
                }else if(position == 3){
                    waitTimeArea.setVisibility(View.VISIBLE);
                    stopTypeArea.setVisibility(View.GONE);
                    stopFeeArea.setVisibility(View.GONE);
                }else if(position == 1 || position == 10 || position == 11 || position == 12 || position == 13 || position == 14 || position == 15){
                    waitTimeArea.setVisibility(View.GONE);
                }else {
                    waitTimeArea.setVisibility(View.VISIBLE);
                }
                //添加停车选项
                if(position == 1 && isATrip){
                    stopTypeArea.setVisibility(View.VISIBLE);
                    stopFeeArea.setVisibility(View.VISIBLE);
                }else {
                    stopTypeArea.setVisibility(View.GONE);
                    stopFeeArea.setVisibility(View.GONE);
                }

                }
        });


        adapterStopType = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.stop_place_complan));
        stopType.setAdapter(adapterStopType);


        if(posTripWay > 0){
            findViewById(R.id.waitTimeArea).setVisibility(View.GONE);
        }

        waitTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String s = waitTime.getText().toString();
                    if(!TextUtils.isEmpty(s)){
                        if(tripWay.totalTime < getCurrentTotalTime()){
                            Toast.makeText(TripWayActivity.this,"累计时间已大于本次出行花费的总时间，请重新填写",Toast.LENGTH_LONG).show();
                            waitTime.setText("");
                        }else {
                            try{
                                int st = Integer.valueOf(s);
                                if(st > 10){
                                    isWaitTimeOK = false;
                                    DialogUtils.createAlertDialog(TripWayActivity.this,
                                            "步行时间或候车时间超过10分钟，确认是否存在输入错误",
                                            "是","否", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //confirm
                                                    waitTime.setText("");
                                                    dialog.dismiss();
                                                    isWaitTimeOK = true;
                                                }
                                            }, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    isWaitTimeOK = true;
                                                }
                                            }).show();
                                }else {
                                    isWaitTimeOK = true;
                                }
                            }catch (Exception e){

                            }
                        }
                    }
                }
            }
        });

    }

    private int getCurrentTotalTime(){
        int i = 0;
        int j = 0;
        int k = 0;
        String l = waitTime.getText().toString();

        if(!TextUtils.isEmpty(l)){
            j = Integer.valueOf(l);
        }

        return i + j + k;
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
                    if(!isEmpty(tripWay.name)){
                        title.setText(tripWay.name);
                    }

                    if(!isEmpty(tripWay.tripWay)){
                        spinnerWay.setText(tripWay.tripWay);
                        if(tripWay.tripWay.equals("步行")){
                            waitTimeArea.setVisibility(View.GONE);

                        }else if(tripWay.tripWay.equals("轨道")){
                            waitTimeArea.setVisibility(View.VISIBLE);
                        }else {
                            waitTimeArea.setVisibility(View.VISIBLE);
                        }

                        //添加停车选项
                        if(tripWay.tripWay.equals("自驾小汽车") && isATrip){
                            stopTypeArea.setVisibility(View.VISIBLE);
                            stopFeeArea.setVisibility(View.VISIBLE);
                            if(!isEmpty(tripWay.stopType)){
                                stopType.setText(tripWay.stopType);
                            }

                            editTextOutFee.setText(String.valueOf(tripWay.stopFee));
                        }else {
                            stopTypeArea.setVisibility(View.GONE);
                            stopFeeArea.setVisibility(View.GONE);
                        }
                    }



                    waitTime.setText(String.valueOf(tripWay.waitTime));

                    if(!isEmpty(tripWay.hasNext)){
                        if(tripWay.hasNext.equals("否")){
                        }else {
                        }
                    }

                }

            }
        }
    }

    private boolean saveData(){
        if(!isEmpty(spinnerWay.getText().toString())){
            tripWay.tripWay = spinnerWay.getText().toString();
        }else {
            Toast.makeText(TripWayActivity.this,"请选择交通方式",Toast.LENGTH_LONG).show();
            return false;
        }


        if(stopTypeArea.getVisibility() == View.VISIBLE){
            if(!isEmpty(stopType.getText().toString())){
                tripWay.stopType = stopType.getText().toString();
            }else {
                Toast.makeText(TripWayActivity.this,"请选择停车场类型！！",Toast.LENGTH_LONG).show();
                return false;
            }
        }else {
            tripWay.stopType = "";
        }

        if(stopFeeArea.getVisibility() == View.VISIBLE){
            if(!isEmpty(editTextOutFee.getText().toString())){
                tripWay.stopFee = Integer.valueOf(editTextOutFee.getText().toString());
            }else {
                Toast.makeText(TripWayActivity.this,"请输入停车费用！",Toast.LENGTH_LONG).show();
                return false;
            }
        }else {
            tripWay.stopFee = 0;
        }


        if(waitTimeArea.getVisibility() == View.VISIBLE){
            if(!isEmpty(waitTime.getText().toString())){
                tripWay.waitTime = Integer.valueOf(waitTime.getText().toString());
            }else {
                Toast.makeText(TripWayActivity.this,"请输入候车时间！",Toast.LENGTH_LONG).show();
                return false;
            }
        }else {
            tripWay.waitTime = 0;
        }


        if(posFamily < 0 || posPerson < 0 || posTrip < 0 || posTripWay < 0){
            return false;
        }
        MyApplication.currentUsers.getSelectUser().families.get(posFamily).people.get(posPerson).tripList.get(posTrip).tripWays.set(posTripWay,tripWay);
        DataUtil.saveUsers(TripWayActivity.this.getApplicationContext(), currentUsers);
        return true;
    }
}
