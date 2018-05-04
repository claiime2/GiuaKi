package com.example.ms_ngoc.n14dcat006;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import com.example.ms_ngoc.n14dcat006.MainActivity;

public class myAdapter extends ArrayAdapter {
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(context);
        convertView=inflater.inflate(resource,parent,false);
        TextView tvHoTen;
        TextView tvDiaChi;
        ImageView ivTinhTrang;
        tvHoTen=(TextView) convertView.findViewById(R.id.tvHoTen);
        tvDiaChi=(TextView) convertView.findViewById(R.id.tvDiaChi);
        ivTinhTrang=(ImageView) convertView.findViewById(R.id.ivTinhTrang);
        Person p=object.get(position);
        tvHoTen.setText(p.getTen());
        tvDiaChi.setText(p.getDiaChi());
        if(p.getTinhTrang()==true){
            ivTinhTrang.setImageResource(R.drawable.ic_accessibility_24dp);
        }
        else{
            ivTinhTrang.setImageResource(R.drawable.ic_accessible_24dp);

        }
        return convertView;
        //return super.getView(position, convertView, parent);
    }

    private Context context;
    private int resource;
    private ArrayList<Person> object;
    public myAdapter(Context context, int resource, ArrayList<Person> object){
        super(context,resource,object);
        this.context=context;
        this.resource=resource;
        this.object=object;
    }

}
