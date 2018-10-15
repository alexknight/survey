package com.hw.survey;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库Helper类，必须继承自 SQLiteOpenHelper
 * 当一个继承自 SQLiteOpenHelper 后需要复写两个方法，分别是 onCreate()  和 onUpgrade()
 * onCreate()： onCreate是在数据库创建的时候调用的，主要用来初始化数据表结构和插入数据初始化的记录
 * onUpgrade()：onUpGrade是在数据库版本升级的时候调用的，主要用来改变表结构
 *
 *
 *  数据库帮助类要做的事情特别简单：
 *  1、复写onCreate()  和 onUpgrade()方法
 *  2、在这两个方法里面填写相关的sql语句
 *
 *
 */
public class MyDBHelper extends SQLiteOpenHelper{

    public MyDBHelper(Context context) {
        /**
         * 参数说明：
         *
         * 第一个参数： 上下文
         * 第二个参数：数据库的名称
         * 第三个参数：null代表的是默认的游标工厂
         * 第四个参数：是数据库的版本号  数据库只能升级,不能降级,版本号只能变大不能变小
         */
        super(context, "survey.db", null, 1);
    }


    /**
     * onCreate是在数据库创建的时候调用的，主要用来初始化数据表结构和插入数据初始化的记录
     *
     * 当数据库第一次被创建的时候调用的方法,适合在这个方法里面把数据库的表结构定义出来.
     * 所以只有程序第一次运行的时候才会执行
     * 如果想再看到这个函数执行，必须写在程序然后重新安装这个app
     */

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE `operator` (" +
                "`id`    INTEGER PRIMARY KEY AUTOINCREMENT," +
                "`qu`    TEXT NOT NULL," +
                "`street`    TEXT NOT NULL," +
                "`name`    TEXT NOT NULL," +
                "`phone`    TEXT NOT NULL" +
                ");");

        db.execSQL("CREATE TABLE `operator_xiaoqu` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `operator_name` TEXT NOT NULL," +
                " `location` TEXT," +
                " `name` TEXT," +
                " `pub_shequ_area` TEXT," +
                " `pub_jiedao_area` TEXT" +
                ");");

        db.execSQL("CREATE TABLE `family` (" +
                " `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `operator_name` TEXT NOT NULL," +
                " `family_name` TEXT NOT NULL," +
                " `location` TEXT," +
                " `location_detail` TEXT," +
                " `total` INTEGER," +
                " `six_num` INTEGER," +
                " `student_num` INTEGER," +
                " `car_num` INTEGER," +
                " `car_address` TEXT," +
                " `stop_place` TEXT," +
                " `stop_fee` TEXT," +
                " `buyPlan` TEXT," +
                " `batteryCar` TEXT," +
                " `houseSize` TEXT," +
                " `isDone` INTEGER DEFAULT 0" +
                ");");

        db.execSQL("CREATE TABLE `member` (" +
                " `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `family_id` INTEGER," +
                " `name` TEXT," +
                " `location` TEXT," +
                " `location_detail` TEXT," +
                " `earth_type` TEXT," +
                " `sex` TEXT," +
                " `age` INTEGER," +
                " `hukou` TEXT," +
                " `live_time` TEXT," +
                " `come_from` TEXT," +
                " `come_from_city` TEXT," +
                " `carrer` TEXT," +
                " `study_status` TEXT," +
                " `common_way` TEXT," +
                " `stop_type` TEXT," +
                " `stop_fee` TEXT," +
                " `10086_num` INTEGER," +
                " `unicom_num` INTEGER," +
                " `dianxin_num` INTEGER," +
                " `is_local` TEXT," +
                " `fly_time` INTEGER," +
                " `train_time` INTEGER," +
                " `trip_time` INTEGER," +
                " `aim` TEXT," +
                " `aim_type` TEXT," +
                " `trip_way` TEXT," +
                " `isDone` INTEGER DEFAULT 0" +
                ");");

        db.execSQL("CREATE TABLE `trip` (" +
                " `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `member_id` INTEGER," +
                " `name` TEXT," +
                " `trip_type` TEXT," +
                " `out_time` TEXT," +
                " `out_location` TEXT," +
                " `out_address` TEXT," +
                " `out_type` TEXT," +
                " `in_time` TEXT," +
                " `in_location` TEXT," +
                " `in_address` TEXT," +
                " `in_type` TEXT," +
                " `out_fee` INTEGER," +
                " `stop_fee` INTEGER," +
                " `person_num` INTEGER," +
                " `with_family` TEXT," +
                " `isDone` INTEGER DEFAULT 0" +
                ");");
        
        db.execSQL("CREATE TABLE `trip_type` (" +
                " `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                " `trip_id` INTEGER NOT NULL," +
                " `subline_num` INTEGER," +
                " `walk_time_start` INTEGER," +
                " `wait_time` TEXT," +
                " `second_way` TEXT," +
                " `walk_time_end` INTEGER" +
                ");");
    }


    /**
     * 当数据库更新的时候调用的方法
     * 这个要显示出来得在上面的super语句里面版本号发生改变时才会 打印  （super(context, "itheima.db", null, 2); ）
     * 注意，数据库的版本号只可以变大，不能变小，假设我们当前写的版本号是3，运行，然后又改成1，运行则报错。不能变小
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}