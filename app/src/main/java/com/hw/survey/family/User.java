package com.hw.survey.family;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    public String name = "";
    public int selectQu = 0;
    public int selectStreet = 0;
    public int selectShequ = 0;
    public String phone = "";
    public String qu = "";
    public String street = "";
    public String shequ = "";
    public List<Family> families;
    public List<XiaoQu> xiaoQuses;
    public boolean isXiaoQuFinish = false;

    public User(){
        xiaoQuses = new ArrayList<>();
        families = new ArrayList<>();
        xiaoQuses.add(new XiaoQu("第一个小区"));
        xiaoQuses.add(new XiaoQu("第二个小区"));
        xiaoQuses.add(new XiaoQu("第三个小区"));
        xiaoQuses.add(new XiaoQu("第四个小区"));
        xiaoQuses.add(new XiaoQu("第五个小区"));
    }

    public boolean isNull(){
        return selectQu == 0 || selectStreet == 0 || selectShequ == 0
                || TextUtils.isEmpty(phone) || TextUtils.isEmpty(qu)
                || TextUtils.isEmpty(street) || TextUtils.isEmpty(shequ)
                || TextUtils.isEmpty(name);
    }

    public boolean isSame(User user){
        return user.name.equals(name) && user.phone.equals(phone)
                && selectShequ == user.selectShequ && selectStreet == user.selectStreet
                && selectQu == user.selectQu;
    }
}
