package com.hw.survey.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.hw.survey.R;
import com.hw.survey.adapter.CarListAdapter;
import com.hw.survey.family.Car;

import java.util.ArrayList;
import java.util.List;

public class CarDetailActivity extends Activity {
    List<Car> cars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        int num = intent.getIntExtra("carNum", 0);

        initData(num);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        ListView carList = findViewById(R.id.family_list_view);

        CarListAdapter carListAdapter = new CarListAdapter(this,R.layout.item_car,cars);
        carList.setAdapter(carListAdapter);

        Button btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(int num){
        cars = new ArrayList<>();
        if(num > 0){
            for(int i = 1; i <= num; i++){
                Car car = new Car();
                car.name = "小汽车" + i;
                cars.add(car);
            }
        }
    }
}
