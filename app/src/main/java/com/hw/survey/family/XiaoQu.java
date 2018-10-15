package com.hw.survey.family;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by haowang on 2018/4/9.
 */

public class XiaoQu implements Serializable {
    public int id = 0;
    public int rate = 0;
    public String title = "";
    public String address = "";
    public String name = "";
    public String age = "";
    public String start10 = "";
    public boolean isSelect01 = false;
    public boolean isSelect02 = false;
    public boolean isSelect03 = false;
    public boolean isSelect04 = false;
    public boolean isSelect05 = false;
    public boolean isSelect06 = false;
    public boolean isSelect07 = false;

    public String end10 = "";
    public boolean isSelect11 = false;
    public boolean isSelect12 = false;
    public boolean isSelect13 = false;
    public boolean isSelect14 = false;
    public boolean isSelect15 = false;
    public boolean isSelect16 = false;
    public boolean isSelect17 = false;

    public XiaoQu(String name){
        this.title = name;
    }

    public boolean isDone(){
        return !(TextUtils.isEmpty(address) || TextUtils.isEmpty(name) || TextUtils.isEmpty(age));
    }

}
