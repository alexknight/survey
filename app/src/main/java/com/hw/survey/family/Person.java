package com.hw.survey.family;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haowang on 2018/4/9.
 */

public class Person implements Serializable {
    public String name = "";
    public List<Trip> tripList;

    public String address = "";
    public String addressDetail = "";

    public int age;
    public String sex = "";

    public String hukou = "";
    public String hukouDetail = "";
    public String liveTime = "";
    public String eduLevel = "";
    public String carrer = "";
    public String carrerLevel="";
    public String jobType="";
    public String yearIncome="";
    public String hasFreeParking="";
    public String studyType = "";
    public String commonTripWay = "";
    public String stopType = "";
    public String stopFee = "";
    public int yidongNum = 0;
    public int liantongNum = 0;
    public int dianxinNum = 0;
    public String isLocalNum = "";
    public String phoneName = "";
    public int leaveHzTimes = 0;
    public String bearMaxTime = "";
    public String workInHomeReason = "";

    public boolean isInHome =false;

    public String isInHomeReason = "";

    public Person(String name){
        this.name = name;
        tripList = new ArrayList<>();
    }

    public int getWorkPlaceType(){
        if(TextUtils.isEmpty(address) || TextUtils.isEmpty(addressDetail)){
            return 0;
        }else if(isInHome){
            return 2;
        }else {
            return 1;
        }
    }

    public boolean isFinish(){
        boolean common = !(TextUtils.isEmpty(address) || TextUtils.isEmpty(addressDetail)
                || age < 7 || TextUtils.isEmpty(sex) || TextUtils.isEmpty(hukou)
                || TextUtils.isEmpty(carrer) || TextUtils.isEmpty(commonTripWay));
        boolean hukouFinsh = true;

        boolean studyFinish = true;
        if(isStudent()){
            studyFinish = !(TextUtils.isEmpty(studyType)||TextUtils.isEmpty(phoneName));

        }
        boolean stopFinish = true;
        if(commonTripWay.equals("自驾小汽车")){
            stopFinish = !(TextUtils.isEmpty(stopFee) || TextUtils.isEmpty(stopType));
        }

        return common && hukouFinsh && studyFinish && stopFinish;
    }

    public boolean isAllDone(){
        boolean isTravelFinish = true;
        if(isInHome){
            isTravelFinish = !TextUtils.isEmpty(isInHomeReason);
        }else {
            if(tripList.size() < 1){
                isTravelFinish = false;
            }else {
                for (Trip t: tripList){
                    if(!t.isFinish()){
                        isTravelFinish = false;
                    }
                }
            }
        }
        return isTravelFinish && isFinish();
    }

    public boolean isStudent() {
        return carrer.equals("小学生") || carrer.equals("初中生") || carrer.equals("高中生");
    }
}
