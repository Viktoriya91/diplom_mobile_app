package com.example.appsecurity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import java.io.File;
import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_PDF_CODE =1000 ;
    Button bt_opendevice;
    Button us_name;
    TextView tvinfo1;
    Button extract;
    Button exit;
    ImageView star_file;
    GoogleSignInClient mGoogleSignInClient1;
    TextView prov;
    GoogleSignInAccount acct;
    String personName;
    String model_tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        star_file=(ImageView)findViewById(R.id.imageView_file);
        star_file.setVisibility(View.GONE);
        model_tf= Build.MODEL;
        //prov=(TextView)findViewById(R.id.text_prov);
        tvinfo1=(TextView)findViewById(R.id.textView2);
        acct = GoogleSignIn.getLastSignedInAccount(this);
        if (tvinfo1.getText()=="" && acct==null){
            Intent open2 = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(open2);
        }
        if (acct != null) {
            personName = acct.getDisplayName();
            tvinfo1.setText(personName);
        }
        final File appS=new File (Environment.getExternalStorageDirectory()+"/.AppSecurity");
        boolean success=true;
        if(!appS.exists()){
            success=appS.mkdirs();
            appS.setExecutable(true);
            appS.setReadable(true);
            appS.setWritable(false);
        }
        if(success){
            star_file.setVisibility(View.VISIBLE);
        } else{
            star_file.setVisibility(View.GONE);
        }
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new BaseMultiplePermissionsListener(){
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report){
                        super.onPermissionsChecked(report);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        super.onPermissionRationaleShouldBeShown(permissions, token);
                    }
                }).check();
        bt_opendevice=(Button)findViewById(R.id.bt_opendevice);
        bt_opendevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent open = new Intent(MainActivity.this, FileActivity.class);
                startActivity(open);
            }
        });
        extract=(Button)findViewById(R.id.button_extract);
        extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent extract_file = new Intent(MainActivity.this, ExtractFileActivity.class);
                startActivity(extract_file);
            }
        });
        us_name=(Button)findViewById(R.id.name_user);
        us_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browsr = new Intent(MainActivity.this, AllButtonActivity.class);
                startActivity(browsr);
            }
        });
        GoogleSignInOptions gso1 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient1 = GoogleSignIn.getClient(MainActivity.this, gso1);
        exit=(Button)findViewById(R.id.button_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_exit:
                        signOut();
                        break;
                }
                Intent open4 = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(open4);
            }
        });

    }
    private void signOut() {
        mGoogleSignInClient1.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
}
