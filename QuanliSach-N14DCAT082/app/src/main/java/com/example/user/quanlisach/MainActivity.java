package com.example.user.quanlisach;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.quanlisach.Adapter.SachAdapter;
import com.example.user.quanlisach.Model.SachModel;
import com.example.user.quanlisach.Model.VitriModel;
import com.example.user.quanlisach.View.SachInterface;
import com.example.user.quanlisach.View.VitriInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btnhand,btnadd,btndelete;
    boolean show=false;
    Toolbar toolbar;
    Spinner spinnerVitri,spinnerSort;
    ListView listView;
    final ArrayList<SachModel> sachList=new ArrayList<SachModel>();

    SachModel sachModel;
    VitriModel vitriModel;
    SachAdapter sachAdapter;
    DatabaseReference mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData= FirebaseDatabase.getInstance().getReference().child("sachs");
        intView();
        acToolbar();
        addListSort();
        addListVitri();
        addListView();



    }
    public void acToolbar(){
        toolbar=(Toolbar)findViewById(R.id.toolbar_nav);
        toolbar.setTitle("Home currency");
        toolbar.setNavigationIcon(R.drawable.ic_home_black_24dp);
    }
    public void intView(){
        btnhand=findViewById(R.id.bottomhand);
        btnadd=findViewById(R.id.btn_add);
        btndelete=findViewById(R.id.btn_delete);
        spinnerVitri=findViewById(R.id.spinner_vitri);
        spinnerSort=findViewById(R.id.spinner_sort);

        listView=findViewById(R.id.listview);
        btnhand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show==false){
                    hienbtn();
                    show=true;
                }else{
                    anbtn();
                    show=false;
                }
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,DeleteActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,sachList.get(position).getMa()+" ",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,ModifyActivity.class);
                intent.putExtra("sachModel",sachList.get(position));
                startActivity(intent);
            }
        });


    }
    public  void hienbtn(){
        btndelete.show();
        btnadd.show();
    }
    public  void anbtn(){
        btndelete.hide();
        btnadd.hide();
    }
    public void addListVitri(){
        final ArrayList<String> listVitri=new ArrayList<>();
        vitriModel=new VitriModel();

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listVitri);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVitri.setAdapter(arrayAdapter);
        VitriInterface vitriInterface=new VitriInterface() {
            @Override
            public void getVitriModel(VitriModel vitriModel) {
               listVitri.add(vitriModel.getTenvitri());
               arrayAdapter.notifyDataSetChanged();
            }
        };
        vitriModel.getdataVitri(vitriInterface);
        spinnerVitri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,listVitri.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this,"nonon",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addListSort(){
        final ArrayList<String> listSort=new ArrayList<>();
        listSort.add("Năm xuất bản tăng");
        listSort.add("Năm xuất bản giảm");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listSort);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSort.setAdapter(arrayAdapter);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,listSort.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this,"nonon",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addListView(){
        sachModel=new SachModel();
        sachAdapter=new SachAdapter(this,R.layout.item_listview,sachList);
        listView.setAdapter(sachAdapter);
        final SachInterface sachInterface=new SachInterface() {
            @Override
            public void getSachModel(SachModel sachModel) {
               sachList.add(sachModel) ;
              sachAdapter.notifyDataSetChanged();
            }
        };
        sachModel.getDataRoot().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sachList.clear();
                sachModel.getdataSach(sachInterface);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
