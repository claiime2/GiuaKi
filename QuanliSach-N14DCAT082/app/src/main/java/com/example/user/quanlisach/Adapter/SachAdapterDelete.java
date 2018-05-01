package com.example.user.quanlisach.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.quanlisach.Model.SachModel;
import com.example.user.quanlisach.ModifyActivity;
import com.example.user.quanlisach.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SachAdapterDelete extends ArrayAdapter<SachModel> {
    private Context context;
    private int resource;
    private List<SachModel> arrSach;

    public SachAdapterDelete(Context context, int resource, ArrayList<SachModel> arrSach) {
        super(context, resource, arrSach);
        this.context = context;
        this.resource = resource;
        this.arrSach = arrSach;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final SachAdapterDelete.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_delete, parent, false);
            viewHolder = new SachAdapterDelete.ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.txt_name);
            viewHolder.tvTacgia = (TextView) convertView.findViewById(R.id.txt_tacgia);
            viewHolder.tvNamxb = (TextView) convertView.findViewById(R.id.txt_namxb);
            viewHolder.tvSoluong = (TextView) convertView.findViewById(R.id.txt_slcon);
            viewHolder.tvVitri=convertView.findViewById(R.id.txt_vitri);
            viewHolder.imgBia= convertView.findViewById(R.id.img_bia);
            viewHolder.check=convertView.findViewById( R.id.checkbox);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SachAdapterDelete.ViewHolder) convertView.getTag();
        }
        final SachModel sach = arrSach.get(position);
        viewHolder.tvTacgia.setText(String.valueOf(sach.getTacgia()));
        viewHolder.tvName.setText(sach.getTen());
        viewHolder.tvSoluong.setText("còn " +String.valueOf(sach.getSoluong()));
        viewHolder.tvNamxb.setText(String.valueOf(sach.getNamxuatban()));
        viewHolder.tvVitri.setText(sach.getVitri().toString());

        StorageReference storagehinhanh= FirebaseStorage.getInstance().getReference().child(sach.getAnh().toString());
        long ONE_MEGABYTE=1024*1024;
        storagehinhanh.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length) ;
                viewHolder.imgBia.setImageBitmap(bitmap);
            }
        });


        return convertView;
    }

    public class ViewHolder {
        TextView tvName, tvTacgia, tvSoluong,tvNamxb,tvVitri;
        ImageView imgBia;
        CheckBox check;

    }
}
