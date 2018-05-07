package com.example.user.quanlisach;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;


public class ModifyActivity extends AppCompatActivity {
    Toolbar toolbar;
    Spinner spinnerVitri;
    VitriModel vitriModel;
    SachModel sachModelnhan;
    EditText edTen, edTacgia, edNam, edSoluong,edTenanh;
    Button btnSave,btnUpanh;
    ImageView imgBiaMod;
    ArrayAdapter<String> arrayAdapterVitri;
    ArrayList<String> listVitri;
    private Uri imageCaptureUri;
    private final int REQUEST_CODE=166554;
    boolean chonanh=false;
    boolean loadanh=false;
    //
    DatabaseReference databaseReference;

    //
    private static final int PICK_FROM_FILE=2;

    private final int REQUEST_CAMERA=1;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    final StorageReference storageRef = storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
       // StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
       // StrictMode.setVmPolicy(newbuilder.build());
        intview();

        acToolbar();
        addListVitri();
        loadData();
        actionHinhanh();
    }

    public void intview() {
        sachModelnhan = getIntent().getParcelableExtra("sachModel");
        edTen = findViewById(R.id.txt_namemod);
        edTacgia = findViewById(R.id.txt_tacgiamod);
        edSoluong = findViewById(R.id.txt_slmod);
        edNam = findViewById(R.id.txt_nammod);
        btnSave = findViewById(R.id.btn_mod);
        imgBiaMod = findViewById(R.id.img_biamod);
    }

    public void acToolbar() {
        toolbar = findViewById(R.id.toolbar_nav);
        toolbar.setTitle("Demo Sửa");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModifyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addListVitri() {
        listVitri = new ArrayList<>();
        vitriModel = new VitriModel();
        spinnerVitri = findViewById(R.id.spinner_vitrimod);

        arrayAdapterVitri = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listVitri);
        arrayAdapterVitri.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerVitri.setAdapter(arrayAdapterVitri);
        VitriInterface vitriInterface = new VitriInterface() {
            @Override
            public void getVitriModel(VitriModel vitriModel) {
                listVitri.add(vitriModel.getTenvitri());
                Toast.makeText(ModifyActivity.this, "sizelist" + listVitri.size(), Toast.LENGTH_SHORT).show();
                arrayAdapterVitri.notifyDataSetChanged();
                spinnerVitri.setSelection(arrayAdapterVitri.getPosition(sachModelnhan.getVitri()));
            }
        };
        vitriModel.getdataVitri(vitriInterface);
        spinnerVitri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ModifyActivity.this, listVitri.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(ModifyActivity.this, "nonon", Toast.LENGTH_SHORT).show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifydata();
            }
        });

    }

    public void loadData() {
        edTen.setText(sachModelnhan.getTen());
        edTacgia.setText(sachModelnhan.getTacgia());
        edSoluong.setText(String.valueOf(sachModelnhan.getSoluong()));
        edNam.setText(String.valueOf(sachModelnhan.getNamxuatban()));

        StorageReference storagehinhanh = FirebaseStorage.getInstance().getReference().child(sachModelnhan.getAnh().toString());
        long ONE_MEGABYTE = 1024 * 1024;
        storagehinhanh.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgBiaMod.setImageBitmap(bitmap);
            }
        });
    }

    public void modifydata() {
        if(chonanh==true){
            String path="";

            Calendar calendar=Calendar.getInstance();
            path=edTen.getText().toString()+calendar.getTimeInMillis()+".jpg";

            StorageReference mountainsRef=storageRef.child(path);

            imgBiaMod.setDrawingCacheEnabled(true);
            imgBiaMod.buildDrawingCache();
            Bitmap bitmap = imgBiaMod.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(ModifyActivity.this,"Lỗi up file",Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(ModifyActivity.this,"Sửa thành công",Toast.LENGTH_SHORT).show();
                    Log.d("AAA",downloadUrl+"");
                }
            });
            sachModelnhan.setAnh(path);
        }else{
            if(loadanh==true){
                String path="";

                Calendar calendar=Calendar.getInstance();
                path=edTen.getText().toString()+calendar.getTimeInMillis()+".jpg";

                StorageReference riversRef=storageRef.child(path);
                UploadTask uploadTask = riversRef.putFile(imageCaptureUri);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ModifyActivity.this,"Lỗi up file",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(ModifyActivity.this,"Up thanh công",Toast.LENGTH_SHORT).show();
                    }
                });
                sachModelnhan.setAnh(path);
            }else{
                sachModelnhan.setAnh(sachModelnhan.getAnh().toString());
            }
        }



        sachModelnhan.setTen(edTen.getText().toString());
        sachModelnhan.setTacgia(edTacgia.getText().toString());
        sachModelnhan.setSoluong(Long.parseLong(edSoluong.getText().toString()));
        sachModelnhan.setNamxuatban(Long.parseLong(edNam.getText().toString()));
        sachModelnhan.setVitri(spinnerVitri.getSelectedItem().toString());


        //hooac tao sachmodel mới có mã là mã sachModelnhan sau đó gọi pt modifydataSach()

        sachModelnhan.modifydataSach();




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
                    Toast.makeText(ModifyActivity.this,"chọn từ thư mục",Toast.LENGTH_SHORT).show();

                }
            }
        });
        final AlertDialog dialog=builder.create();
        imgBiaMod.setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(ModifyActivity.this,"chọn từ thư mục2",Toast.LENGTH_SHORT).show();
            imageCaptureUri=data.getData();
            imgBiaMod.setImageURI(imageCaptureUri);
            loadanh=true;
            chonanh=false;

        }
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgBiaMod.setImageBitmap(imageBitmap);
            chonanh=true;
            loadanh=false;
        }



    }



//private void verifyPermissons() {
//    Log.d("Modifyactivity", "verifyPermissions:");
//    String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
//    if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permission[0]) == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(this.getApplicationContext(), permission[1]) == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(this.getApplicationContext(), permission[2]) == PackageManager.PERMISSION_GRANTED) {
//        intview();
//
//    } else {
//        ActivityCompat.requestPermissions(ModifyActivity.this, permission, REQUEST_CODE);
//    }
//}
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        verifyPermissons();
//    }
}
