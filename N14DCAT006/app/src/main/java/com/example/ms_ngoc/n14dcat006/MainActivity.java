package com.example.ms_ngoc.n14dcat006;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etHoTen;
    private EditText etDiaChi;
    private ListView lvDanhSach;
    private RadioButton rbBinhThuong;
    private RadioButton rbKhuyetTat;
    private Button btThem;
    private Button btSua;
    public SQLiteHelper db;
    myAdapter adapter;
    ArrayList<Person> data;
    int idSua=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        db=new SQLiteHelper(this);
        data=new ArrayList<>();
        db.loadData(data);
        adapter=new myAdapter(this,R.layout.itemlayout1,data);
        lvDanhSach.setAdapter(adapter);
        btSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etHoTen.getText().toString().equals("") || etDiaChi.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Các trường không được rỗng");
                    builder.setPositiveButton("OK",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }
                Person person=new Person();
                person.setTen(etHoTen.getText().toString());
                person.setDiaChi((etDiaChi.getText().toString()));
                person.setId(idSua);
                if(rbBinhThuong.isChecked()){
                    person.setTinhTrang(true);
                }
                else {
                    person.setTinhTrang(false);
                }
                db.update(person,idSua);
                ArrayList<Person> tam=new ArrayList<>();

                tam=db.getAllData();
                data.clear();
                data.addAll(tam);
                adapter.notifyDataSetChanged();
                etHoTen.setText("");
                etDiaChi.setText("");
            }
        });
        btThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etHoTen.getText().toString().equals("") || etDiaChi.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Thông báo");
                    builder.setMessage("Các trường không được rỗng");
                    builder.setPositiveButton("OK",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return;
                }
                Person person=new Person();
                person.setTen(etHoTen.getText().toString());
                person.setDiaChi((etDiaChi.getText().toString()));
                if(rbBinhThuong.isChecked()){
                    person.setTinhTrang(true);
                }
                else {
                    person.setTinhTrang(false);
                }
                db.insert(person);
                ArrayList<Person> tam=new ArrayList<>();

                tam=db.getAllData();
                data.clear();
                data.addAll(tam);
                //data.add(person);
                adapter.notifyDataSetChanged();
                etHoTen.setText("");
                etDiaChi.setText("");
            }
        });
    }

    private void setControl() {
        etHoTen=(EditText) findViewById(R.id.etHoTen);
        etDiaChi=(EditText) findViewById(R.id.etDiaChi);
        lvDanhSach=(ListView) findViewById(R.id.lvDanhSach);
        rbBinhThuong=(RadioButton) findViewById(R.id.rbBinhThuong);
        rbKhuyetTat=(RadioButton) findViewById(R.id.rbKhuyetTat);
        btThem=(Button) findViewById(R.id.btThem);
        btSua=(Button) findViewById(R.id.btSua);
    }
    public void SuaItem(View v){
        Person person=new Person();
        int pos=lvDanhSach.getPositionForView(v);
        person=(Person) lvDanhSach.getItemAtPosition(pos);
        etHoTen.setText(person.getTen());
        etDiaChi.setText(person.getDiaChi());
        if(person.getTinhTrang()==true){
            rbBinhThuong.isChecked();
        }
        else{
            rbKhuyetTat.isChecked();
        }
        idSua=person.getId();
    }
    public void XoaItem(View v){
        Person person=new Person();
        int pos=lvDanhSach.getPositionForView(v);
        person=(Person) lvDanhSach.getItemAtPosition(pos);
        db.delete(person.getId());
        ArrayList<Person> tam=new ArrayList<>();

        tam=db.getAllData();
        data.clear();
        data.addAll(tam);
        //data.add(person);
        adapter.notifyDataSetChanged();
    }
}
