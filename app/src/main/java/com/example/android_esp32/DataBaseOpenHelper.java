package com.example.android_esp32;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import androidx.core.app.ActivityCompat;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DataBaseOpenHelper extends SQLiteAssetHelper {
    private final static String DATABASE_NAME="lastresult.db";
    private static final int DATABASE_VERSION=9;
    public DataBaseOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion ) {
        switch( newVersion ) {

            case 2: /* this is your new version number */

                // ... Add new column 'my_new_col' to table 'my_table'
                db.execSQL( "alter table SECONDARY_TABLE add column name TEXT" ) ;
                break ;
            case 3: {
                db.execSQL( "alter table SECONDARY_TABLE add column name TEXT" ) ;
                break ;
            } case 9:{
                db.execSQL( "alter table SECONDARY_TABLE add column name TEXT" ) ;
//                ActivityCompat.requestPermissions(, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                String stringFilePath = Environment.getExternalStorageDirectory().getPath()+"/Download/"+"resistor"+".jpg";
                Bitmap bitmap = BitmapFactory.decodeFile(stringFilePath);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                byte[] bytesImage = byteArrayOutputStream.toByteArray();
                ContentValues cv = new ContentValues();
                cv.put("image_URL", bytesImage);
                db.update("MAIN_TABLE",cv," element_id=1",null);
                stringFilePath=Environment.getExternalStorageDirectory().getPath()+"/Download/"+"zeiner"+".jpg";
                bitmap=BitmapFactory.decodeFile(stringFilePath);
                 byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
                bytesImage = byteArrayOutputStream.toByteArray();
                cv.put("image_URL", bytesImage);
                db.update("MAIN_TABLE",cv," element_id=2",null);
                stringFilePath=Environment.getExternalStorageDirectory().getPath()+"/Download/"+"red"+".jpg";
                bitmap=BitmapFactory.decodeFile(stringFilePath);
                 byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
                bytesImage = byteArrayOutputStream.toByteArray();
                cv.put("image_URL", bytesImage);
                db.update("MAIN_TABLE",cv,"element_id=3",null);
                stringFilePath=Environment.getExternalStorageDirectory().getPath()+"/Download/"+"blue"+".jpg";
                bitmap=BitmapFactory.decodeFile(stringFilePath);
                byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
                bytesImage = byteArrayOutputStream.toByteArray();
                cv.put("image_URL", bytesImage);
                db.update("MAIN_TABLE",cv," element_id=4",null);
            }

        }
    }
}
