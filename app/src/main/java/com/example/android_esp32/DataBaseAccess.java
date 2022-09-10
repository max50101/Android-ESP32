package com.example.android_esp32;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

public class DataBaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DataBaseAccess instance;
    Cursor c=null;
    private DataBaseAccess(Context context){
        this.openHelper=new DataBaseOpenHelper(context);
    }
    public static DataBaseAccess getInstance(Context context){
        if(instance==null){
            instance=new DataBaseAccess(context);
        }
        return instance;
    }
    public void open(){
        this.db=openHelper.getWritableDatabase();

    }
    public void close(){
        if(db!=null){
            db.close();
        }
    }
    public ArrayList<DbClassOne> getDBFirst(){
        c=db.rawQuery("select * from MAIN_TABLE",new String[]{});
        ArrayList list=new ArrayList<DbClassOne>();
        while(c.moveToNext()){
            DbClassOne dbClassOne=new DbClassOne(c.getInt(0),c.getString(1),c.getBlob(2));
            list.add(dbClassOne);
        }
        return list;
    }
    public DbClassOne getDBFirstTable(Integer id){
        c=db.rawQuery("select * from MAIN_TABLE where element_id="+id,new String[]{});
        //ArrayList list=new ArrayList<DbClassOne>();
        while(c.moveToNext()){
            DbClassOne dbClassOne=new DbClassOne(c.getInt(0),c.getString(1),c.getBlob(2));
            return dbClassOne;
            //list.add(dbClassOne);
        }
        return null;
    }
    public DBTableTwo getDBSecondTable(Integer id){
        c=db.rawQuery("select * from SECONDARY_TABLE where id="+id,new String[]{});
        while (c.moveToNext()){
            DBTableTwo dbTableTwo=new DBTableTwo(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),c.getString(4));
            return dbTableTwo;
        }
        return null;
    }
    public void insertMainMeasurments(Integer id){
        List<kotlin.Pair<Double, Double>> list= SupporterKt.testList2();
        String sql="INSERT INTO measurements_Main(volt_value,ampere_value,element_id) VALUES(?,?,?)";
        SQLiteStatement sqLiteStatement=db.compileStatement(sql);
        for(int i=0;i<list.size();i++){
            sqLiteStatement.clearBindings();
            sqLiteStatement.bindDouble(1,list.get(i).component1());
            sqLiteStatement.bindDouble(2,list.get(i).component2());
            sqLiteStatement.bindLong(3,id);
            sqLiteStatement.executeInsert();
        }
    }
    public List<Pair<Double,Double>> getMeasurmentsFirst(Integer eleme_id){
        c=db.rawQuery("select * from measurements_Main where element_id="+eleme_id,new String[]{});
        List list=new ArrayList<Pair<Double,Double>>();
        while(c.moveToNext()){
            int id=c.getInt(0);
            Double volt=c.getDouble(1);
            Double ampere=c.getDouble(2);
            list.add(new Pair(volt,ampere));
        }
        return list;
    }
    public List<Pair<Double,Double>> getMeasurmentsSecond(Integer id){
        c=db.rawQuery("select * from measurements_SECOND where measurments_id="+id,new String[]{});
        List list=new ArrayList<Pair<Double,Double>>();
        while(c.moveToNext()){
            int idc=c.getInt(0);
            Double volt=c.getDouble(1);
            Double ampere=c.getDouble(2);
            list.add(new Pair(volt,ampere));
        }
        return list;
    }
    public String getName(Integer eleme_id){
        c=db.rawQuery("select element_name from MAIN_TABLE where element_id="+eleme_id,new String[]{});
        while(c.moveToNext()){
            return c.getString(0);
        }
        return null;
    }
    public List<DBTableTwo> getSecondDB()
    {
        List arr=new ArrayList<DBTableTwo>();
        c=db.rawQuery("select * from SECONDARY_TABLE",new String[]{});
        while(c.moveToNext()){
            DBTableTwo dbTableTwo=new DBTableTwo(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),c.getString(4));
            arr.add(dbTableTwo);
        }
        return arr;
    }
    public void insertIntoMainTable(Activity a, String name){
        ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
//        String sql1="DELETE FROM MAIN_TABLE";
//        SQLiteStatement sqLiteStatement1=db.compileStatement(sql1);
//        sqLiteStatement1.executeUpdateDelete();
//        sql1="DELETE FROM measurements_MAIN ";
//        sqLiteStatement1=db.compileStatement(sql1);
//        sqLiteStatement1.executeUpdateDelete();

        String stringFilePath = Environment.getExternalStorageDirectory().getPath()+"/Download/"+"giperbola"+".jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(stringFilePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] bytesImage = byteArrayOutputStream.toByteArray();
        String sql="INSERT INTO MAIN_TABLE(element_name,image_URL) VALUES(?,?)";
        SQLiteStatement sqLiteStatement=db.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,name);
        sqLiteStatement.bindBlob(2,bytesImage);
//       sqLiteStatement.executeInsert();
//        int id=0;
//        c=db.rawQuery("select last_insert_rowid()",new String[]{});
//        while(c.moveToNext()){
//            id=c.getInt(0);
//        }
//        insertMainMeasurments(id);
    }
    public void insertIntoSecondTable(JSONfile file,String ip,String element_name){
        Integer id=0;
        c=db.rawQuery("select element_id from MAIN_TABLE where element_name='"+element_name+"'",new String[]{});
        while(c.moveToNext()){
            id=c.getInt(0);
        }
        String sql="INSERT INTO SECONDARY_TABLE(date,ip,element_id,name) VALUES(?,?,?,?)";
        SQLiteStatement sqLiteStatement=db.compileStatement(sql);
        sqLiteStatement.clearBindings();
        sqLiteStatement.bindString(1,file.getC());
        sqLiteStatement.bindString(2,ip);
        sqLiteStatement.bindLong(3,id);
        sqLiteStatement.bindString(4,file.getName());
        sqLiteStatement.executeInsert();
        c=db.rawQuery("select last_insert_rowid()",new String[]{});
        while(c.moveToNext()){
            id=c.getInt(0);
        }
        insertSecondaryMeasures(file.getList(),id);
    }
    public void insertSecondaryMeasures(List<Pair<Double,Double>> list,Integer id){
        String sql="INSERT INTO measurements_SECOND(volt_value,ampere_value,measurments_id) VALUES(?,?,?)";
        SQLiteStatement sqLiteStatement=db.compileStatement(sql);
        for(int i=0;i<list.size();i++){
            sqLiteStatement.clearBindings();
            sqLiteStatement.bindDouble(1,list.get(i).component1());
            sqLiteStatement.bindDouble(2,list.get(i).component2());
            sqLiteStatement.bindLong(3,id);
            sqLiteStatement.executeInsert();
        }
    }

}
