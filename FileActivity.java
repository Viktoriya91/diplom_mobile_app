package com.example.appsecurity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
public class FileActivity extends AppCompatActivity {
    ListView l_v;
    public static ArrayList<File> fileList=new ArrayList<>();
    AdapterActivity adapter;
    public static int REQUEST_PERMISSION=1;
    boolean boolean_Permission;
    File dir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        l_v=(ListView)findViewById(R.id.pdf_list);
        dir=new File(Environment.getExternalStorageDirectory()+"/.AppSecurity");
        permission_fn();
        l_v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent pdfad = new Intent(getApplicationContext(), ViewActivity.class);
                pdfad.putExtra("position",position);
                startActivity(pdfad);
            }
        });
        l_v.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id){
                AlertDialog.Builder builder=new AlertDialog.Builder(FileActivity.this);
                builder.setMessage("Вы хотите удалить?");
                builder.setCancelable(false);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File fils=FileActivity.fileList.get(position);
                        boolean d0=fils.delete();
                        adapter.remove(FileActivity.fileList.get(position));
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }
        });
    }
    private void permission_fn(){
        if((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)){
            if((ActivityCompat.shouldShowRequestPermissionRationale(FileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))){

            } else{
                ActivityCompat.requestPermissions(FileActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION);
            }
        } else {
            boolean_Permission=true;
            getFile(dir);
            adapter=new AdapterActivity(getApplicationContext(),fileList);
            l_v.setAdapter(adapter);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                boolean_Permission=true;
                getFile(dir);
                adapter=new AdapterActivity(getApplicationContext(),fileList);
                l_v.setAdapter(adapter);
            } else{
                Toast.makeText(this,"Разрешите доступ, пожалуйста",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public ArrayList<File> getFile(File dir){
        File list_file[]=dir.listFiles();
        if(list_file!=null && list_file.length>0){
            for(int i=0; i<list_file.length;i++){
                if(list_file[i].isDirectory()){
                    getFile(list_file[i]);
                } else{
                    boolean boolpdf=false;
                    if(list_file[i].getName().endsWith(".pdf")){
                        for(int j=0; j<fileList.size();j++){
                            if(fileList.get(j).getName().equals(list_file[i].getName())){
                                boolpdf=true;
                            } else{

                            }
                        }
                        if(boolpdf){
                            boolpdf=false;
                        } else{
                            fileList.add(list_file[i]);
                        }
                    }
                }
            }
        }
        return fileList;
    }
}

