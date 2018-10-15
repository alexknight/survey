package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.hw.survey.MyApplication;
import com.hw.survey.R;
import com.hw.survey.adapter.PersonListAdapter;
import com.hw.survey.family.Family;
import com.hw.survey.family.Person;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PepoleLIstActivity extends Activity {

    @InjectView(R.id.family_list_view)
    ListView family_list_view;

    @InjectView(R.id.btn_new)
    Button btn_new;

    @InjectView(R.id.btn_back)
    ImageView btn_back;

    @InjectView(R.id.btn_confirm)
    Button btn_confim;

    private PersonListAdapter familyListAdapter;
    List<Person> personList = new ArrayList<Person>();

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        pos = intent.getIntExtra("pos", -1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pepole_list);
        ButterKnife.inject(this);

        initView();

        familyListAdapter = new PersonListAdapter(this,R.layout.item_person, personList);
        family_list_view.setAdapter(familyListAdapter);
        family_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(PepoleLIstActivity.this,MemberInfoActivity.class);
                intent.putExtra("posFamily",pos);
                intent.putExtra("posPerson",position);
                startActivityForResult(intent,1);
            }
        });


        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personList.add(new Person("成员"+(personList.size()+1)));
                saveCurrentFamily();
                familyListAdapter.notifyDataSetChanged();

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
                saveCurrentFamily();
                Intent intent = new Intent(PepoleLIstActivity.this,FamilyListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView(){
        if(pos > -1 && MyApplication.currentUsers.getSelectUser().families.size() > pos){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(pos);
            if(family != null && family.people != null && family.people.size() > 0){
                personList = family.people;
            }
        }
    }

    private void saveCurrentFamily(){
        if(pos > -1 && MyApplication.currentUsers.getSelectUser().families.size() > pos){
            Family family = MyApplication.currentUsers.getSelectUser().families.get(pos);
            if(family != null && personList != null && personList.size() > 0){
                family.people = personList;

                MyApplication.currentUsers.getSelectUser().families.set(pos,family);
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
