package com.hw.survey.family;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by haowang on 2018/4/9.
 */

public class TripWay implements Serializable {
    public String name = "";


    public String tripWay = "";
    public String subWay = "";
    public int startWalkTime = 0;
    public int waitTime = 0;
    public String hasNext = "";
    public int endWalkTime = 0;

    public String stopType = "";
    public int stopFee = 0;

    public String tripPeopleNum = "";
    public String tripCost = "";
    public String busChangeTimes = "";
    public String lastCost = "";
    public String firstArriveStationTime = "";

    public String startStation = "";
    public String changeSubwaystation = "";
    public String leaveSubwayStation = "";


    public int totalTime = 0;


    public TripWay(String name, int totalTime){
        this.name = name;
        this.totalTime = totalTime;
    }

    public boolean isVaildTime(){
        return startWalkTime + waitTime + endWalkTime < totalTime;
    }

    public boolean isFinish(){
        if(!(TextUtils.isEmpty(tripWay))){
            if(tripWay.equals("步行")){
                return true;
            }else if(tripWay.equals("轨道")){
                return !(TextUtils.isEmpty(subWay) || TextUtils.isEmpty(hasNext));
            }else {
                return !( TextUtils.isEmpty(hasNext));
            }
        }
        return false;
    }
}
