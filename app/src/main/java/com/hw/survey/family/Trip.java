package com.hw.survey.family;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haowang on 2018/4/9.
 */

public class Trip implements Serializable {
    public String name = "";


    public String aim = "";

    public String outDate = "";
    public String outTime = "";
    public String outAddress = "";
    public String outAddressDetail = "";
    public String outAddressType = "";

    public String arriveTime = "";
    public String arriveAddress = "";
    public String arriveAddressDetail = "";


    public int beginTime = 0;
    public int endTime = 0;

    public List<TripWay> tripWays = new ArrayList<>();


    public double fee = 0;
    public int stopFee = 0;
    public int outNum = 0;
    public String isHaveFamily = "";

    public Trip(String name, Trip lastTrip){
        this.name = name;
        if(lastTrip != null){
            this.outAddress = lastTrip.arriveAddress;
            this.outAddressDetail = lastTrip.arriveAddressDetail;
        }
    }

    public boolean isATrip(){
        if(TextUtils.isEmpty(aim)){
            return false;
        }
        if(aim.equals("上班") || aim.equals("上学") ||
                aim.equals("下班/放学回家") || aim.equals("非通勤回家") || aim.equals("回家") ){
            return false;
        }
        return true;
    }

    public boolean isTripWayContain(String tripType){
        if(tripWays == null || tripWays.size() == 0){
            return false;
        }
        for(TripWay way: tripWays){
            if(way.tripWay.equals(tripType)){
                return true;
            }
        }
        return false;
    }

    public boolean isFinish(){

        boolean isFamily = true;
        if(outNum > 0){
            isFamily = !TextUtils.isEmpty(isHaveFamily);
        }

        boolean isTripsFinish = true;
        if(tripWays.size() > 0){
            for (TripWay t: tripWays){
                if(!t.isFinish()){
                    isTripsFinish = false;
                }
            }
        }

        boolean commonFinish = !(TextUtils.isEmpty(outTime) || TextUtils.isEmpty(outAddress) ||
                TextUtils.isEmpty(outAddressDetail) ||
                TextUtils.isEmpty(arriveTime) || TextUtils.isEmpty(arriveAddress) ||
                TextUtils.isEmpty(arriveAddressDetail) ||
                TextUtils.isEmpty(aim));

        boolean outTypeFinish = (!TextUtils.isEmpty(outAddress) && !outAddress.startsWith("x=")) || !TextUtils.isEmpty(outAddressType);
        boolean inTypeFinish = (!TextUtils.isEmpty(arriveAddress) && !arriveAddress.startsWith("x="));

        return isFamily && isTripsFinish && commonFinish && outTypeFinish && inTypeFinish;
    }

    public int setTime(int hour, int min){
        return hour * 60 + min;
    }

    public void setBegin(int hour, int min){
        beginTime = setTime(hour,min);
    }

    public void setEnd(int hour, int min){
        endTime = setTime(hour,min);
    }

    public int getTirpTime(){
        return endTime - beginTime;
    }
}
