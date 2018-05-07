package com.example.user.quanlisach.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.user.quanlisach.View.SachInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SachModel  implements Parcelable{
    String anh,tacgia,ten,vitri,ma;
    long soluong,namxuatban;
    private DatabaseReference dataRoot;//bien private ko up

    public DatabaseReference getDataRoot() {
        return dataRoot;
    }

    public void setDataRoot(DatabaseReference dataRoot) {
        this.dataRoot = dataRoot;
    }

    public SachModel(){
        dataRoot= FirebaseDatabase.getInstance().getReference();
    }
    protected SachModel(Parcel in){

        ma=in.readString();
        ten=in.readString();
        tacgia= in.readString();
        namxuatban=in.readLong();
        vitri=in.readString();
        soluong=in.readLong();
        anh=in.readString();
    }

    public static final Creator<SachModel> CREATOR = new Creator<SachModel>() {
        @Override
        public SachModel createFromParcel(Parcel in) {
            return new SachModel(in);
        }

        @Override
        public SachModel[] newArray(int size) {
            return new SachModel[size];
        }
    };

    public SachModel(String anh, String tacgia, String ten, String vitri, long soluong, long namxuatban) {
        this.anh = anh;
        this.tacgia = tacgia;
        this.ten = ten;
        this.vitri = vitri;
        this.soluong = soluong;
        this.namxuatban = namxuatban;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public long getNamxuatban() {
        return namxuatban;
    }

    public void setNamxuatban(long namxuatban) {
        this.namxuatban = namxuatban;
    }

    public String getTacgia() {
        return tacgia;
    }

    public void setTacgia(String tacgia) {
        this.tacgia = tacgia;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getVitri() {
        return vitri;
    }

    public void setVitri(String vitri) {
        this.vitri = vitri;
    }

    public long getSoluong() {
        return soluong;
    }

    public void setSoluong(long soluong) {
        this.soluong = soluong;
    }




    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ma);
        dest.writeString(ten);
        dest.writeString(tacgia);
        dest.writeLong(namxuatban);
        dest.writeString(vitri);
        dest.writeLong(soluong);
        dest.writeString(anh);
    }
    public void getdataSach(final SachInterface sachInterface){
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshotSach=dataSnapshot.child("sachs");

                for (DataSnapshot valueSach:dataSnapshotSach.getChildren()){
                    SachModel sachModel=new SachModel();
                    sachModel=valueSach.getValue(SachModel.class);
                    sachModel.ma=valueSach.getKey();
                    Log.d("Kiem tra",sachModel.getAnh()+"");
                    Log.d("Kiem tra",sachModel.getTen()+"");
                    Log.d("Kiem tra",sachModel.getNamxuatban()+"");
                    Log.d("Kiem tra",sachModel.getSoluong()+"");
                    Log.d("Kiem tra",sachModel.getTacgia()+"");
                    Log.d("Kiem tra",sachModel.getVitri()+"");
                    Log.d("Kiem tra",sachModel.ma.toString()+"");
                    sachInterface.getSachModel(sachModel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dataRoot.addListenerForSingleValueEvent(valueEventListener);

    }
    public void  modifydataSach(){
        dataRoot=FirebaseDatabase.getInstance().getReference().child("sachs").child(ma);
        //upanh
        dataRoot.child("ten").setValue(ten);
        dataRoot.child("vitri").setValue(vitri);
        dataRoot.child("soluong").setValue(soluong);
        dataRoot.child("tacgia").setValue(tacgia);
        dataRoot.child("ten").setValue(ten);
        dataRoot.child("anh").setValue(anh);
        dataRoot.child("namxuatban").setValue(namxuatban);
    }
    public void deletedata(String ma){
        dataRoot=FirebaseDatabase.getInstance().getReference().child("sachs").child(ma);

        //dataRoot.setValue(null);
        dataRoot.removeValue();

    }
    public void adddataSach(){
        dataRoot=FirebaseDatabase.getInstance().getReference().child("sachs").push();
        dataRoot.child("ten").setValue(ten);
        dataRoot.child("tacgia").setValue(tacgia);
        dataRoot.child("vitri").setValue(vitri);
        dataRoot.child("namxuatban").setValue(namxuatban);
        dataRoot.child("soluong").setValue(soluong);
        dataRoot.child("anh").setValue(anh);
        //up anh
    }
}
