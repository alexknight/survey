package com.hw.survey.family;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Users  implements Serializable {
    private int selectId;
    private List<User> users;

    public Users(){
        selectId = 0;
        users = new ArrayList<>();
    }

    public int getSelectId() {
        return selectId;
    }

    public void setSelectId(int selectId) {
        this.selectId = selectId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getSelectUser(){
        if(users == null || users.size() < 1 || users.size() <= selectId){
            return null;
        }
        return users.get(selectId);
    }

    public int getUserByName(String name){
        if(users.size() < 1) return -1;
        for (int i = 0; i < users.size(); i++){
            if(users.get(i).name.equals(name))
                return i;
        }
        return -1;
    }
}
