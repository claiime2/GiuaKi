package com.example.user.quanlisach.Model;

import android.util.Log;

import com.example.user.quanlisach.View.SachInterface;
import com.example.user.quanlisach.View.VitriInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VitriModel {
    String mavitri,tenvitri;
    DatabaseReference dataRoot;

    public VitriModel(){
        dataRoot= FirebaseDatabase.getInstance().getReference();
    }

    public String getMavitri() {
        return mavitri;
    }

    public void setMavitri(String mavitri) {
        this.mavitri = mavitri;
    }

    public String getTenvitri() {
        return tenvitri;
    }

    public void setTenvitri(String tenvitri) {
        this.tenvitri = tenvitri;
    }
    public void getdataVitri(final VitriInterface vitriInterface){
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshotVitri=dataSnapshot.child("vitris");
                for (DataSnapshot valueVitri:dataSnapshotVitri.getChildren()){
                    VitriModel vitriModel=valueVitri.getValue(VitriModel.class);
                    Log.d("Kiem tra",vitriModel.getTenvitri());
                    vitriInterface.getVitriModel(vitriModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dataRoot.addListenerForSingleValueEvent(valueEventListener);

    }
}
