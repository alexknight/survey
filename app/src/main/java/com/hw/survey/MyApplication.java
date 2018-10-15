package com.hw.survey;

import android.app.Application;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hw.survey.dao.DataUtil;
import com.hw.survey.family.Users;
import com.hw.survey.map.MyLocationListener;
import com.hw.survey.util.DBUtils;

/**
 * Created by haowang on 2018/4/10.
 */

public class MyApplication extends Application {
    public static Users currentUsers = new Users();

    public static LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    @Override
    public void onCreate() {
        super.onCreate();

        Users users = DataUtil.getUsers(this);
        if(users != null){
            currentUsers = users;
        }

        DBUtils.getInstance(this);

        initLocation();
    }

    private void initLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);

        option.setCoorType("bd09ll");

        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(true);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(true);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);

        option.setEnableSimulateGps(false);

        mLocationClient.setLocOption(option);

//        mLocationClient.start();
    }
}
