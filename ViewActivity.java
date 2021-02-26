package com.example.appsecurity;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import java.util.Date;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
public class ViewActivity extends AppCompatActivity {
    PDFView pdfView;
    int position=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Date date=new Date();
        String data_new=date.toString();
        TextView dat_tex=(TextView)findViewById(R.id.textView1);
        dat_tex.setTextSize(18);
        dat_tex.setTextColor(Color.GRAY);
        dat_tex.setText(data_new);
        TextView user_name=(TextView)findViewById(R.id.textView3);
        user_name.setTextSize(30);
        user_name.setTextColor(Color.GRAY);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            user_name.setText(personName);
        }
        pdfView = (PDFView) findViewById(R.id.pdf_viewer);
        position=getIntent().getIntExtra("position",-1);
        pdfView.fromFile(FileActivity.fileList.get(position))
                .enableSwipe(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .enableAnnotationRendering(true)
                .load();
    }
    }



