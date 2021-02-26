package com.example.appsecurity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class AccountActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=0;
    Button back;
    TextView account_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              .requestEmail()
              .build();
        mGoogleSignInClient = GoogleSignIn.getClient(AccountActivity.this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            switch (v.getId()) {
              case R.id.sign_in_button:
                  Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                  startActivityForResult(signInIntent, RC_SIGN_IN);
                  break;
         }
         }
        });
        account_1=(TextView)findViewById(R.id.text_account);
        account_1.setText("Войдите в Google-аккаунт");
        back=(Button)findViewById(R.id.button_account);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent open3 = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(open3);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        }
    }
}
