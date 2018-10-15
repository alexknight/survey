package com.hw.survey.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.hw.survey.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    private static String DB_NAME = "address_new.db";
//    private static String DATABASE_PATH = "/data/data/com.hw.survey/databases/";
    private static String DATABASE_PATH = Environment.getExternalStoragePublicDirectory("Documents").getAbsolutePath() + "/";
    private static String outFileName = DATABASE_PATH + "/" + DB_NAME;


    private static DBUtils instance;
    public static DBUtils getInstance(Context context){
        if(instance == null)
            instance = new DBUtils(context);
        return instance;
    }

    private DBUtils (Context context){
        if(!checkDataBase()){
            try {
                copyDataBase(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断数据库是否存在
     * @return false or true
     */
    public boolean checkDataBase(){
        SQLiteDatabase db = null;
        try{
            String databaseFilename = DATABASE_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(databaseFilename, null,
                    SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            Log.i("Survey","Database not exist.");
        }
        if(db!=null){
            db.close();
        }
        return db != null;
    }
    /**
     * 复制数据库到手机指定文件夹下
     * @throws IOException
     */
    public void copyDataBase(Context context) throws IOException {
        String databaseFilenames = DATABASE_PATH + DB_NAME;
        File dir = new File(DATABASE_PATH);
        if(!dir.exists())
            dir.mkdirs();
        FileOutputStream os;
        try{
            os = new FileOutputStream(databaseFilenames);//得到数据库文件的写入流
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return;
        }
        InputStream is = context.getResources().openRawResource(R.raw.address_new);//得到数据库文件的数据流
        byte[] buffer = new byte[8192];
        int count;
        try{
            while((count = is.read(buffer))>0){
                os.write(buffer, 0, count);
                os.flush();
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        try{
            is.close();
            os.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public List<PoiInfo> searchPOIs(String name) {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(outFileName, null);
        ArrayList<PoiInfo> pois = new ArrayList<>();
        String sql = "select * from address where name like '%" + name + "%' limit 50";
        Cursor cursor = database.rawQuery(sql, null);
        int nameIndex = cursor.getColumnIndex("name");
        int addressIndex = cursor.getColumnIndex("address");
        int xIndex = cursor.getColumnIndex("loc_x");
        int yIndex = cursor.getColumnIndex("loc_y");
        while (cursor.moveToNext()){
            PoiInfo info = new PoiInfo();
            info.name = cursor.getString(nameIndex);
            info.address = cursor.getString(addressIndex);
            info.location = new LatLng(cursor.getDouble(yIndex),cursor.getDouble(xIndex));
            pois.add(info);
        }
        cursor.close();
        database.close();
        return pois;
    }

    public List<PoiInfo> searchNearByPOIs(double x, double y) {
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(outFileName, null);
        ArrayList<PoiInfo> pois = new ArrayList<>();
        String sql = "select * from address where (loc_x - "+x+")<0.001 and (loc_x - "+x+") > -0.001 and (loc_y - "+y+") < 0.001 and (loc_y - "+y+") > -0.001";
        Cursor cursor = database.rawQuery(sql, null);
        int nameIndex = cursor.getColumnIndex("name");
        int addressIndex = cursor.getColumnIndex("address");
        int xIndex = cursor.getColumnIndex("loc_x");
        int yIndex = cursor.getColumnIndex("loc_y");
        while (cursor.moveToNext()){
            PoiInfo info = new PoiInfo();
            info.name = cursor.getString(nameIndex);
            info.address = cursor.getString(addressIndex);
            info.location = new LatLng(cursor.getDouble(yIndex),cursor.getDouble(xIndex));
            pois.add(info);
        }
        cursor.close();
        database.close();
        return pois;
    }


}
