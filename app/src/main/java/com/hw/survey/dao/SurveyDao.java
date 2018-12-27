package com.hw.survey.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hw.survey.MyDBHelper;
import com.hw.survey.family.Family;

public class SurveyDao {
    private MyDBHelper myDBHelper;

    public SurveyDao(Context context){
        myDBHelper = new MyDBHelper(context);
    }


    // 增加的方法吗，返回的的是一个long值
    public long addFamily(Family family, String user){
        // 增删改查每一个方法都要得到数据库，然后操作完成后一定要关闭
        // getWritableDatabase(); 执行后数据库文件才会生成
        // 数据库文件利用DDMS可以查看，在 data/data/包名/databases 目录下即可查看
        SQLiteDatabase sqLiteDatabase =  myDBHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put("operator_name",user);
        contentValues.put("family_name", family.name);
        contentValues.put("location", family.address);
        contentValues.put("location_detail",family.addressDetail);
        contentValues.put("total",family.totalNum);
        contentValues.put("six_num",family.up6Num);
        contentValues.put("student_num",family.tempNum);
        contentValues.put("car_num",family.carNum);
        contentValues.put("car_address",family.carAddress);
        contentValues.put("stop_place",family.tempNum);
        contentValues.put("stop_fee",family.tempNum);
        contentValues.put("batteryCar",family.tempNum);
        contentValues.put("houseSize",family.tempNum);
        contentValues.put("isDone",family.isDone);
        // 返回,显示数据添加在第几行
        // 加了现在连续添加了3行数据,突然删掉第三行,然后再添加一条数据返回的是4不是3
        // 因为自增长
        long rowid=sqLiteDatabase.insert("family",null,contentValues);

        sqLiteDatabase.close();
        return rowid;
    }
}
