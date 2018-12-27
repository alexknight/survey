package com.hw.survey.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.adapter.TripListAdapter;
import com.hw.survey.family.Family;
import com.hw.survey.family.Person;
import com.hw.survey.family.Trip;
import com.hw.survey.util.DialogUtils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TripListActivity extends Activity {
    @InjectView(R.id.family_list_view)
    ListView family_list_view;

    @InjectView(R.id.btn_new)
    Button btn_new;

    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.btn_confirm)
    Button btn_confim;

    @InjectView(R.id.inHomeArea)
    View inHomeArea;

    @InjectView(R.id.inHomeReason)
    BetterSpinner inHomeReason;

    @InjectView(R.id.inHomeConfirm)
    View inHomeConfirm;

    private int posFamily;
    private int posPerson;

    private TripListAdapter familyListAdapter;
    List<Trip> tripList = new ArrayList<Trip>();

    private ArrayAdapter<String> adapterInHomeReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        posFamily = intent.getIntExtra("posFamily", -1);
        posPerson = intent.getIntExtra("posPerson", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        initView();

        ButterKnife.inject(this);
        familyListAdapter = new TripListAdapter(this,R.layout.item_trip, tripList);
        family_list_view.setAdapter(familyListAdapter);
        family_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(TripListActivity.this,TripInfoActivity.class);

                intent.putExtra("posFamily",posFamily);
                intent.putExtra("posPerson",posPerson);
                intent.putExtra("posTrip",position);
                startActivity(intent);
            }
        });

        adapterInHomeReason = new ArrayAdapter<>(this,R.layout.spinner_item,getResources().getStringArray(R.array.in_home_reason));
        inHomeReason.setAdapter(adapterInHomeReason);

        inHomeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = inHomeReason.getText().toString();
                if(TextUtils.isEmpty(reason)){
                    Toast.makeText(TripListActivity.this,"请选择该成员没有出行的原因",Toast.LENGTH_LONG).show();
                    return;
                }

                inHomeArea.setVisibility(View.GONE);

                if(posFamily > -1 && MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
                    Family family = MyApplication.currentUsers.getSelectUser().families.get(posFamily);
                    if(posPerson > -1 && family != null && family.people != null && family.people.size() > posPerson){
                        Person person = family.people.get(posPerson);
                        person.isInHome = true;
                        person.isInHomeReason = reason;
                        family.people.set(posPerson,person);
                        MyApplication.currentUsers.getSelectUser().families.set(posFamily,family);

                        Intent intent = new Intent(TripListActivity.this,PepoleLIstActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tripList.size() == 0){
                    tripList.add(new Trip("出行"+(tripList.size()+1),null));
                }else {
                    Trip lastTrip = tripList.get(tripList.size() -1);
                    tripList.add(new Trip("出行"+(tripList.size()+1),lastTrip));
                }

                familyListAdapter.notifyDataSetChanged();
                saveCurrentTripList();

                if(posFamily > -1 && MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
                    Family family = MyApplication.currentUsers.getSelectUser().families.get(posFamily);
                    if(posPerson > -1 && family != null && family.people != null && family.people.size() > posPerson){
                        Person person = family.people.get(posPerson);
                        person.isInHome = false;
                        family.people.set(posPerson,person);
                        MyApplication.currentUsers.getSelectUser().families.set(posFamily,family);
                    }
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tripList.size() == 0){
                    DialogUtils.createAlertDialog(TripListActivity.this,
                            "是否确认该成员当天没有出行",
                            "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //confirm
                                    inHomeArea.setVisibility(View.VISIBLE);
                                    dialog.dismiss();

                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return;
                }
                
                saveCurrentTripList();
                Intent intent = new Intent(TripListActivity.this,PepoleLIstActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

        private void initView(){
        if(posFamily > -1 && MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1 && family != null && family.people != null && family.people.size() > posPerson){
                tripList = family.people.get(posPerson).tripList;

            }
        }
    }

    private void saveCurrentTripList(){
        if(posFamily > -1 && MyApplication.currentUsers.getSelectUser().families.size() > posFamily){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(posFamily);
            if(posPerson > -1 && family != null && family.people != null && family.people.size() > posPerson){
                Person person = family.people.get(posPerson);
                person.tripList = tripList;
                family.people.set(posPerson,person);
                MyApplication.currentUsers.getSelectUser().families.set(posFamily,family);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        familyListAdapter.notifyDataSetChanged();
    }


}
