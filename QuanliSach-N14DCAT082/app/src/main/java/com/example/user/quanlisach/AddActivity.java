package com.example.user.quanlisach;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.quanlisach.Model.SachModel;
import com.example.user.quanlisach.Model.VitriModel;
import com.example.user.quanlisach.View.VitriInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnadd;
    EditText edTen,edNam,edTacgia,edSoluong;
    ImageView imgBia;
    Spinner spinnerVitri;
    VitriModel vitriModel;
    private final int REQUEST_CAMERA=1;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    final StorageReference storageRef = storage.getReference();
    boolean chonanh=false;

    private Uri imageCaptureUri;
    boolean loadanh=false;
    //
    DatabaseReference databaseReference;

    //
    private static final int PICK_FROM_FILE=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        intview();
        acToolbar();
        addListVitri();
        actionHinhanh();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddataSach();
            }
        });

    }
    public void intview(){

        spinnerVitri=findViewById(R.id.spinner_vitriup);
        edTen=findViewById(R.id.txt_nameup);
        edTacgia=findViewById(R.id.txt_tacgiaup);
        edNam=findViewById(R.id.txt_namup);
        edSoluong=findViewById(R.id.txt_slup);
        btnadd=findViewById(R.id.btn_upsach);
        imgBia=findViewById(R.id.img_biaup);


    }
    public void acToolbar(){
        toolbar=findViewById(R.id.toolbar_nav);
        toolbar.setTitle("Demo thêm");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
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
                Toast.makeText(AddActivity.this,listVitri.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddActivity.this,"nonon",Toast.LENGTH_SHORT).show();
            }
        });
        imgBia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }
        });
    }

    public void adddataSach(){
        String ten=edTen.getText().toString();
        String tacgia=edTacgia.getText().toString();
        String vitri=spinnerVitri.getSelectedItem().toString();
        Long nam=Long.parseLong(edNam.getText().toString());
        Long soluong=Long.parseLong(edSoluong.getText().toString());
        String path="";

        if(chonanh==true){
            Calendar calendar=Calendar.getInstance();
            path=ten+calendar.getTimeInMillis()+".jpg";

            StorageReference mountainsRef=storageRef.child(path);
            imgBia.setDrawingCacheEnabled(true);
            imgBia.buildDrawingCache();
            Bitmap bitmap = imgBia.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(AddActivity.this,"Lỗi up file",Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(AddActivity.this,"Thành công",Toast.LENGTH_SHORT).show();
                    Log.d("AAA",downloadUrl+"");
                }
            });
        }else {
            if (loadanh == true) {
                Calendar calendar = Calendar.getInstance();
                path = edTen.getText().toString() + calendar.getTimeInMillis() + ".jpg";

                StorageReference riversRef = storageRef.child(path);
                UploadTask uploadTask = riversRef.putFile(imageCaptureUri);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(AddActivity.this, "Lỗi up file", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(AddActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                path = "default_book_cover.jpg";
            }
        }

        SachModel sachModel=new SachModel(path,tacgia,ten,vitri,soluong,nam);
        sachModel.adddataSach();
    }
    public void actionHinhanh(){
        final String[] items=new String[] {"Từ Camera","Từ thiết bị"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Từ Camera")){
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent,REQUEST_CAMERA);
                    }
                }else{
                    Intent intent=new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Complete action using"),PICK_FROM_FILE);
                    Toast.makeText(AddActivity.this,"chọn từ thư mục",Toast.LENGTH_SHORT).show();

                }
            }
        });
        final AlertDialog dialog=builder.create();
        imgBia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!= Activity.RESULT_OK)
            return;

        Bitmap bitmap=null;
        if(requestCode==PICK_FROM_FILE&& resultCode == RESULT_OK){
            Toast.makeText(AddActivity.this,"chọn từ thư mục2",Toast.LENGTH_SHORT).show();
            imageCaptureUri=data.getData();
            imgBia.setImageURI(imageCaptureUri);
            loadanh=true;
            chonanh=false;

        }
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgBia.setImageBitmap(imageBitmap);
            chonanh=true;
            loadanh=false;
        }



    }


}
