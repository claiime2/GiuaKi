package com.example.ms_ngoc.n14dcat006;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "db_person";
    private static final int DB_VERSION = 1;
    private static final String TB_NAME = "tb_person";
    private static final String TB_COL_ID = "id";
    private static final String TB_COL_NAME = "ten";
    private static final String TB_COL_DIACHI = "diachi";
    private static final String TB_COL_TINHTRANG= "tinhtrang";
    SQLiteDatabase db;
    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TB_NAME + "( " +
                TB_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TB_COL_NAME + " TEXT, " +
                TB_COL_DIACHI + " TEXT, " +
                TB_COL_TINHTRANG + " BOOL )"
        );
    }

    public void insert(Person data){
        db = this.getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put(TB_COL_NAME,data.getTen());
        value.put(TB_COL_DIACHI,data.getDiaChi());
        value.put(TB_COL_TINHTRANG,data.getTinhTrang());
        db.insert(TB_NAME,null,value);
    }

    public void update( Person data, int id){
        db = this.getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put(TB_COL_ID,id);
        value.put(TB_COL_NAME,data.getTen());
        value.put(TB_COL_DIACHI,data.getDiaChi());
        value.put(TB_COL_TINHTRANG,data.getTinhTrang());
        db.update(TB_NAME,value, TB_COL_ID+"="+id,null);
    }
    public void deleteAll(){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TB_NAME,null,null);
    }
    public void loadData(ArrayList<Person> arrPersion){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TB_NAME,null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(TB_COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(TB_COL_NAME));
                String diachi = cursor.getString(cursor.getColumnIndex(TB_COL_DIACHI));
                boolean tinhtrang = cursor.getString(cursor.getColumnIndex(TB_COL_TINHTRANG)).equals("1");
                arrPersion.add(new Person(id,name,diachi,tinhtrang));
                Log.d("DB",id+"");
            }while (cursor.moveToNext());
        }
        db.close();
    }
    public ArrayList<Person> getAllData(){
        ArrayList<Person> data=new ArrayList<>();
        Person person;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TB_NAME,null);
        if(cursor.moveToFirst()){
            do{
                person=new Person();
                person.setId(cursor.getInt(cursor.getColumnIndex(TB_COL_ID)));
                person.setTen(cursor.getString(cursor.getColumnIndex(TB_COL_NAME)));
                person.setDiaChi(cursor.getString(cursor.getColumnIndex(TB_COL_DIACHI)));
                person.setTinhTrang(cursor.getString(cursor.getColumnIndex(TB_COL_TINHTRANG)).equals("1"));
                data.add(person);
            }while (cursor.moveToNext());
        }
        db.close();
        return data;
    }
    public Person getData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Person person=new Person();
        Cursor cursor=db.query(TB_NAME,new String[]{TB_COL_ID,TB_COL_NAME,TB_COL_DIACHI,TB_COL_TINHTRANG},TB_COL_ID+"="+id,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                person.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(TB_COL_ID))));
                person.setTen(cursor.getString(cursor.getColumnIndex(TB_COL_NAME)));
                person.setDiaChi(cursor.getString(cursor.getColumnIndex(TB_COL_DIACHI)));
                person.setTinhTrang(cursor.getString(cursor.getColumnIndex(TB_COL_TINHTRANG)).equals("1"));
            }while (cursor.moveToNext());
        }
        db.close();
        return person;
    }
    public void saveData(ArrayList<Person> personArrayList){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + TB_NAME);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME = '" + TB_NAME + "'");
        for (Person nv : personArrayList){
            ContentValues values = new ContentValues();
            values.put(TB_COL_NAME,nv.getTen());
            values.put(TB_COL_DIACHI, nv.getDiaChi());
            values.put(TB_COL_TINHTRANG, nv.getTinhTrang());
            db.insert(TB_NAME,null,values);

        }
        db.close();
    }
    public void delete(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TB_NAME,TB_COL_ID + "="+id,null);
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);

        // Create tables again
        onCreate(db);
    }
}
