package com.adria.facebooktest.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adria.facebooktest.AlbumActivity.AlbumsActivity;
import com.adria.facebooktest.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener,Loginview {
private Loginpresenterimp presenter;
    private EditText email;
    private EditText password;
    private ProgressBar progressBar;
    private Button signup;
    private FirebaseAuth firebaseAuth;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter=new Loginpresenterimp(this);
        signup=(Button) findViewById(R.id.connect) ;
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        email=(EditText)findViewById(R.id.login) ;
        password=(EditText)findViewById(R.id.password) ;
        signup.setOnClickListener(this);

    }





    @Override
    public void onClick(View view) {
       presenter.login( email.getText().toString(),password.getText().toString());
    }


    @Override
    public void showloading() {
        progressBar.setVisibility(View.VISIBLE);
        signup.setEnabled(false);
    }

    @Override
    public void hideloading() {
        progressBar.setVisibility(View.GONE);
        signup.setEnabled(true);
    }

    @Override
    public void login(final String email, final String password) {

       if(email.length()!=0 && password.length()!=0){
           showloading();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    hideloading();
                    LoginManager.getInstance().logOut();
                    Intent i=new Intent(getApplicationContext(),AlbumsActivity.class);
                    i.putExtra("grant",true);
                    i.putExtra("email",email);
                    i.putExtra("password",password);
                    startActivity(i);
                }else{
                  hideloading();
                    Intent i=new Intent(getApplicationContext(), AlbumsActivity.class);
                    i.putExtra("grant",false);
                    startActivity(i);   }

            }
        });}else{Toast.makeText(getApplicationContext(),"Empty values",Toast.LENGTH_LONG).show();}
    }


}
