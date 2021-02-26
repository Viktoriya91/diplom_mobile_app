package com.example.appsecurity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.io.File;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.tom_roush.pdfbox.cos.COSDocument;
import com.tom_roush.pdfbox.io.RandomAccessFile;
import com.tom_roush.pdfbox.pdfparser.PDFParser;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDDocumentInformation;
import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission;
import com.tom_roush.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;


import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


public class BrowserActivity extends AppCompatActivity{
    private WebView webbrow;
    String download_file="";
    private DownloadListener downloadListener = null;
    private long Downloading;
    String personName_f;
    String model_tf;
    String Numer;
    int col;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        GoogleSignInAccount acct5 = GoogleSignIn.getLastSignedInAccount(this);
        if (acct5 != null) {
            personName_f = acct5.getDisplayName();
        }
        model_tf= Build.MODEL;
        Bundle ur=getIntent().getExtras();
        String ur_f=ur.get("URRI").toString();
        Numer=ur.get("NUMBER").toString();
        if(Numer.length()==3){
            col=1;
        }
        if(Numer.length()==4){
            col=2;
        }
        if(Numer.length()==5){
            col=3;
        }
        registerReceiver(onComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        webbrow=findViewById(R.id.webbrowser);
       webbrow.getSettings().setJavaScriptEnabled(true);
        webbrow.setWebViewClient(new MWebVC());
        webbrow.loadUrl(ur_f);
        webbrow.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
                Date date1=new Date();
                String data_new1=date1.toString();
                download_file=data_new1;
                request.setVisibleInDownloadsUi(true);
                request.allowScanningByMediaScanner();
                request.setDestinationInExternalPublicDir("/.AppSecurity",data_new1+".pdf");
                DownloadManager Manager=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                Downloading=Manager.enqueue(request);
                Toast.makeText(BrowserActivity.this,"Ваш файл скачивается...",Toast.LENGTH_SHORT).show();

            }
        });

    }
    @Override
    public void onBackPressed(){
        if(webbrow.canGoBack()){
            webbrow.goBack();
        } else {
            super.onBackPressed();
        }
    }
    private class MWebVC extends WebViewClient{
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
    BroadcastReceiver onComplete=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long refer=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            Toast toast=Toast.makeText(BrowserActivity.this,"Загрузка завершена",Toast.LENGTH_LONG);
            toast.show();
            NotificationCompat.Builder builder=new NotificationCompat.Builder(BrowserActivity.this, "Download_file")
                    .setSmallIcon(R.drawable.icon_1691371_1280)
                    .setContentTitle("Уведомление")
                    .setContentText("Загрузка завершена");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Уведомление";
                String description = "Загрузка завершена";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("Download_file", name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManager=NotificationManagerCompat.from(BrowserActivity.this);
            notificationManager.notify(1,builder.build());
            if (col==1) {
                   File file_new = new File(Environment.getExternalStorageDirectory() + "/.AppSecurity" + download_file + ".pdf");
                   file_new.setExecutable(true);
                   file_new.setReadable(true);
                   file_new.setWritable(false);
            }
            if(col==2) {
                PDFBoxResourceLoader.init(getApplicationContext());
                PDDocument pdDocument1;
                try {
                    File file_1 = new File(Environment.getExternalStorageDirectory() + "/.AppSecurity/" + download_file + ".pdf");
                    pdDocument1 = PDDocument.load(file_1);
                    PDDocumentInformation pdDocumentInformation = pdDocument1.getDocumentInformation();
                    pdDocumentInformation.setAuthor(personName_f);
                    pdDocumentInformation.setKeywords("Дата скачивания файла на устроуство " + model_tf + ": " + download_file + " " + personName_f);
                    AccessPermission accessPermission = new AccessPermission();
                    accessPermission.setCanFillInForm(false);
                    accessPermission.setCanModify(false);
                    StandardProtectionPolicy standardProtectionPolicy = new StandardProtectionPolicy("", "", accessPermission);
                    standardProtectionPolicy.setPermissions(accessPermission);
                    pdDocument1.protect(standardProtectionPolicy);
                    pdDocument1.save(Environment.getExternalStorageDirectory() + "/.AppSecurity/" + download_file + ".pdf");
                    Toast toast9=Toast.makeText(BrowserActivity.this,"Информация добавлена в свойства файла",Toast.LENGTH_LONG);
                    toast9.show();
                    pdDocument1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(col==3){
                PDFBoxResourceLoader.init(getApplicationContext());
                File f_1=new File(Environment.getExternalStorageDirectory() + "/.AppSecurity/" +download_file + ".pdf");
                String text_watermark_ru=personName_f;
                String [] Original_ru={"2d","3b","410","412","415","41a","41c","41d","41e","420","421","422","425","435","43e","441","445"};
                String [] Duplicate_ru={"2010","37e","41","42","45","4b","4d","48","4f","50","43","54","58","65","6f","63","78"};
                String [] Spaces={"20","2000","2004","2005","2008","2009","202f","205f"};
                String [] bit_spaces={"000","001","010","011","100","101","110","111"};
                String [] Confusables_ru={"2d","3b","410","412","415","41a","41c","41d","41e","420","421","422","425","435","43e","441","445","20","2000","2004","2005","2008","2009","202f","205f"};
                int size_Con_ru=Confusables_ru.length;
                String Watermark_ru="";
                String hashtext_1="";
                String text_final="";
                try{
                    MessageDigest messageDigest=MessageDigest.getInstance("SHA-224");
                    byte[] messageDigest_1=messageDigest.digest(text_watermark_ru.getBytes());
                    BigInteger one_en=new BigInteger(1,messageDigest_1);
                    hashtext_1=one_en.toString(2);
                } catch(NoSuchAlgorithmException e){
                    throw new RuntimeException(e);
                }
                 Watermark_ru=hashtext_1;
                int len_enc_ru=Watermark_ru.length();
                char[] watermark_hash_ru=new char[len_enc_ru];
                for (int i = 0; i < Watermark_ru.length(); i++) {
                    watermark_hash_ru[i] = Watermark_ru.charAt(i);
                }
                try{
                    PDDocument document=PDDocument.load(f_1);
                    PdfDocument myPdfDocument = new PdfDocument();
                    document.getClass();
                    int coll_str=document.getNumberOfPages();
                    RandomAccessFile randomAccessFile=new RandomAccessFile(f_1,"r");
                    PDFParser pdfParser=new PDFParser(randomAccessFile);
                    pdfParser.parse();
                    COSDocument cosDocument=pdfParser.getDocument();
                    PDFTextStripper pdfTextStripper=new PDFTextStripper();
                    PDDocument doccc=new PDDocument(cosDocument);
                    int p1_ru = 0;
                    for(int i9=0;i9<coll_str;i9++) {
                        pdfTextStripper.setStartPage(i9);
                        pdfTextStripper.setEndPage(i9 + 1);
                        String Text_pdf = pdfTextStripper.getText(doccc);
                        int len_text_ru = Text_pdf.length();
                        String text_ru = Text_pdf;
                        if(p1_ru==len_enc_ru){
                            p1_ru=0;
                        }
                        Character[] bits_ru = new Character[3];
                        String text_ru_new = "";
                        for (int i = 0; i < len_text_ru; i++) {
                            if(p1_ru==len_enc_ru){
                                p1_ru=0;
                            }
                            char aa = text_ru.charAt(i);
                            String symb_en = Integer.toHexString(aa);
                            int con_otvet = 0;
                            int index_c = 0;
                            for (int j = 0; j < 17; j++) {
                                int pr_1 = 0;
                                int l_1 = 0;
                                String pr = Confusables_ru[j];
                                if (symb_en.length() == pr.length()) {
                                    l_1 = symb_en.length();
                                    for (int j6 = 0; j6 < symb_en.length(); j6++) {
                                        if (symb_en.charAt(j6) == pr.charAt(j6)) {
                                            pr_1++;
                                        }
                                    }
                                    if (pr_1 == l_1) {
                                        con_otvet++;
                                        index_c = j;
                                    }
                                }
                            }
                            if (con_otvet == 1) {
                                if (watermark_hash_ru[p1_ru] == '1') {
                                    int numer_s_d = index_c;
                                    String unicode_symnol_new_d = Duplicate_ru[numer_s_d];
                                    char new_s_d = (char) Integer.parseInt(unicode_symnol_new_d, 16);
                                    text_ru_new += new_s_d;
                                } else {
                                    text_ru_new += aa;
                                }
                                p1_ru++;
                            } else {
                                text_ru_new += aa;
                            }
                        }
                        text_final=text_ru_new;
                        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(400,500,i9).create();
                        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
                        TextPaint myPaint = new TextPaint();
                        myPaint.setTextSize(7);
                        myPaint.setWordSpacing(0F);
                        myPaint.setTextAlign(Paint.Align.LEFT);
                        myPaint.setColor(Color.BLACK);
                        float fl=1F;
                        myPaint.setTextScaleX(fl);
                        myPaint.setTypeface(Typeface.create("Times_New_Roman",Typeface.NORMAL));
                       String myString = text_final;
                        int x = 30, y=35;
                        for (String line:myString.split("\n")){
                            myPage.getCanvas().drawText(line, x, y, myPaint);
                            y+=myPaint.descent()-myPaint.ascent();
                        }
                        myPdfDocument.finishPage(myPage);
                    }
                     String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/.AppSecurity/" + download_file+"_DWM" + ".pdf";
                    File myFile = new File(myFilePath);
                    try {
                        myPdfDocument.writeTo(new FileOutputStream(myFile));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                     Toast toast3=Toast.makeText(BrowserActivity.this,"Встраивание цифрового водяного знака завершено",Toast.LENGTH_LONG);
                     toast3.show();
                    myPdfDocument.close();
                    document.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }

}
