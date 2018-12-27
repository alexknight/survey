package com.hw.survey.family;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haowang on 2018/4/9.
 */

public class Family implements Serializable {
    public int id = 0;
    public List<Person> people;

    public String name = "";
    public String address = "";
    public String addressDetail = "";

    public int totalNum = 0;
    public int up6Num = 0;
    public int tempNum = 0;

    public int carNum = 0;
    public String carAddress = "";
    public int motoNum = 0;

    public String stopPlace = "";
    public String editParkingOwner = "";
    public String stopFee = "";
    public String editDriverDist="";

    public String batteryCar = "";
    public String houseSize = "";
    public String houseBelong = "";
    public String phone = "";
    public int isDone = 0;

    public String buildFinishTime = "";

    public Family(){
        people = new ArrayList<>();
    }

    public Family(String name){
        this.name = name;
        people = new ArrayList<>();
    }

    public boolean isFinish(){
        boolean isBaseOK =  !(TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(addressDetail) ||
                totalNum == 0  ||
                TextUtils.isEmpty(buildFinishTime) ||
                TextUtils.isEmpty(batteryCar) ||
                TextUtils.isEmpty(houseSize)) || TextUtils.isEmpty(houseBelong);
        if(carNum == 0){
            return isBaseOK;
        }else {
            return isBaseOK && (!TextUtils.isEmpty(stopPlace)) && (!TextUtils.isEmpty(editParkingOwner)) &&(!TextUtils.isEmpty(stopFee));
        }
    }

    public boolean isAllDone(){
        if(people == null || people.size() < 1){
            return false;
        }

        for(Person p: people){
            if(!p.isAllDone()){
                return false;
            }
        }

        return isFinish();
    }

    public void initMembers(){
        int size = people.size();
        int num = totalNum - up6Num;

        if(totalNum - up6Num > size){
            for (int i = size + 1; i <=totalNum - up6Num; i++){
                people.add(new Person("成员" + i));
            }
        }else if(num < size && num > 0){
            try {
                for (int j = num; j < size; j++){
                    people.remove(num);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

}
