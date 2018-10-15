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

    public String earthType = "";

    public int age;
    public String sex = "";

    public String hukou = "";
    public String liveTime = "";
    public String comeFrom = "";
    public String cityCode = "";
    public String carrer = "";
    public String studyType = "";
    public String commonTripWay = "";
    public String stopType = "";
    public String stopFee = "";
    public int yidongNum = 0;
    public int liantongNum = 0;
    public int dianxinNum = 0;
    public String isLocalNum = "";
    public String phoneName = "";
    public int planeTime = 0;
    public int trainTime = 0;
    public int tripTime = 0;
    public String objectPlace = "";
    public String aim = "";
    public String tripWay = "";
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
                || TextUtils.isEmpty(earthType)
                || age < 7 || TextUtils.isEmpty(sex) || TextUtils.isEmpty(hukou)
                || TextUtils.isEmpty(carrer) || TextUtils.isEmpty(commonTripWay));
        boolean hukouFinsh = true;
        if(!hukou.equals("重庆市主城九区户籍")){
            if(TextUtils.isEmpty(liveTime)){
                hukouFinsh = false;
            }else {
                if(!liveTime.equals("六个月以上")){
                    if(hukou.equals("重庆市其它区县户籍")){
                        hukouFinsh = !TextUtils.isEmpty(comeFrom);
                    }else {
                        hukouFinsh = (!TextUtils.isEmpty(cityCode)) && (!TextUtils.isEmpty(isLocalNum));
                    }
//
//                    if(hukouFinsh){
//                        hukouFinsh = !TextUtils.isEmpty(isLocalNum);
//                    }
                }
            }
        }
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
