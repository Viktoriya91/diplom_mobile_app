package com.example.appsecurity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioGroup;


public class AllButtonActivity extends AppCompatActivity {
    ImageButton button_google;
    ImageButton button_mail;
    ImageButton button_yandex;
    String Number;
    RadioGroup group1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allbutton);
        group1=(RadioGroup)findViewById(R.id.radioGroup);
        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton:
                        Number="one";
                        break;
                    case R.id.radioButton2:
                        Number="twot";
                        AlertDialog.Builder builder2=new AlertDialog.Builder(AllButtonActivity.this);
                        builder2.setMessage("Если ваш документ содержит электронную подпись, то при добавлении информации о пользователь в свойства файла электронная подпись будет нарушена!");
                        builder2.setCancelable(false);
                        builder2.setPositiveButton("Согласен", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder2.show();
                        break;
                    case R.id.radioButton3:
                        Number="three";
                        AlertDialog.Builder builder3=new AlertDialog.Builder(AllButtonActivity.this);
                        builder3.setMessage("При встраивание цифрового водяного знака оформление текста будет потеряно, убедитесь что файл содержит только текст!");
                        builder3.setCancelable(false);
                        builder3.setPositiveButton("Согласен", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder3.show();
                        break;
                    default:
                        break;
                }
            }
        });
        button_google=(ImageButton)findViewById(R.id.Button_Google);
        button_google.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent int1=new Intent(AllButtonActivity.this, BrowserActivity.class);
            int1.putExtra("URRI",Uri.parse("https://mail.google.com"));
            int1.putExtra("NUMBER",Number);
            startActivity(int1);
           }
        });
        button_mail=(ImageButton)findViewById(R.id.Button_Mail);
        button_mail.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
           Intent int2=new Intent(AllButtonActivity.this, BrowserActivity.class);
           int2.putExtra("URRI",Uri.parse("https://e.mail.ru"));
           int2.putExtra("NUMBER",Number);
           startActivity(int2);
          }
        });
        button_yandex=(ImageButton)findViewById(R.id.Button_Yandex);
        button_yandex.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             Intent int3=new Intent(AllButtonActivity.this, BrowserActivity.class);
             int3.putExtra("URRI",Uri.parse("https://mail.yandex.ru"));
             int3.putExtra("NUMBER",Number);
             startActivity(int3);
         }
        });

   
    }
}
