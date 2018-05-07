package com.example.user.quanlisach;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.quanlisach.Adapter.SachAdapter;
import com.example.user.quanlisach.Adapter.SachAdapterDelete;
import com.example.user.quanlisach.Model.SachModel;
import com.example.user.quanlisach.View.SachInterface;

import java.util.ArrayList;

public class DeleteActivity extends AppCompatActivity {

    Toolbar toolbar;
    SachModel sachModel;
    FloatingActionButton btndelete;
    ListView listView;
    final ArrayList<SachModel> sachList=new ArrayList<SachModel>();
    final ArrayList<String> listDelete=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        intview();
        acToolbar();
        addListView();
    }
    public void intview(){
        btndelete=findViewById(R.id.btn_delete);
        listView=findViewById(R.id.listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listDelete.contains(sachList.get(position).getMa().toString())){
                    listDelete.remove(sachList.get(position).getMa().toString());
                    LinearLayout view1= (LinearLayout) view;
                    CheckBox check=view1.findViewById(R.id.checbox_item);
                    check.setChecked(false);
                }else {
                    listDelete.add(sachList.get(position).getMa().toString());
                    LinearLayout view1= (LinearLayout) view;
                    CheckBox check=view1.findViewById(R.id.checbox_item);
                    check.setChecked(true);

                    // Toast.makeText(DeleteActivity.this,"demo"+view.toString(),Toast.LENGTH_SHORT).show();
                    // Toast.makeText(DeleteActivity.this,"đã click "+sachList.get(position).getMa().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listDelete.size()==0){
                    Toast.makeText(DeleteActivity.this,"Không có dữ liệu xóa",Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(DeleteActivity.this,"ooo"+listDelete.toString(),Toast.LENGTH_SHORT).show();
                    SachModel sachModel=new SachModel();
                    for(int i=0;i<listDelete.size();i++){
                        sachModel.deletedata(listDelete.get(i));
                    }

                    Intent intent=new Intent(DeleteActivity.this,DeleteActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    public void acToolbar(){
        toolbar=findViewById(R.id.toolbar_nav);
        toolbar.setTitle("Demo Xóa");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(DeleteActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void addListView(){
        sachModel=new SachModel();
        final SachAdapterDelete sachAdapter=new SachAdapterDelete(this,R.layout.item_listview_delete,sachList);
        listView.setAdapter(sachAdapter);
        SachInterface sachInterface=new SachInterface() {
            @Override
            public void getSachModel(SachModel sachModel) {
                sachList.add(sachModel) ;
                sachAdapter.notifyDataSetChanged();
            }
        };

        sachModel.getdataSach(sachInterface);

    }

}