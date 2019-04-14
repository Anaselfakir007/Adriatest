package com.adria.facebooktest.AlbumActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adria.facebooktest.Adapter.GridAdapter;
import com.adria.facebooktest.Models.Album;
import com.adria.facebooktest.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AlbumsActivity extends AppCompatActivity implements Albumsview  {

private RelativeLayout fbview;
private Albumspresenterimp presenter;
private LoginButton buttonlogin;
private RecyclerView recyclerView;
private ArrayList<Album> alFBAlbum = new ArrayList<>();
private CallbackManager callbackManager;
private FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

setContentView(R.layout.activity_albums);
fbview=(RelativeLayout)findViewById(R.id.fcb_view);
buttonlogin=(LoginButton)fbview.findViewById(R.id.btn_fb);
recyclerView=(RecyclerView)findViewById(R.id.recycleralbums);
firebaseAuth = FirebaseAuth.getInstance();
recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
presenter=new Albumspresenterimp(this);
presenter.setlogin();
if(getIntent().getBooleanExtra("grant",false)){ showloginface();
}else{ presenter.getalbumsasynch(AccessToken.getCurrentAccessToken().getUserId());} }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private JSONArray sortJSONArrayAlphabetically(JSONArray jArray) throws JSONException
    {
        if (jArray != null) {

            ArrayList<String> arrayForSorting = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                arrayForSorting.add(jArray.get(i).toString());
            }
            Collections.sort(arrayForSorting);


            JSONArray resultArray = new JSONArray();
            for (int i = 0; i < arrayForSorting.size(); i++) {
                resultArray.put(new JSONObject(arrayForSorting.get(i)));
            }
            return resultArray;
        }
        return null;
    }

    @Override
    public void showloginface() {
        fbview.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideloginface() {
        fbview.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void getalbums(String iduser) {
        GraphRequest graphforalbums=  new GraphRequest(
                AccessToken.getCurrentAccessToken(),  //your fb AccessToken
                "/" + iduser + "/albums",//user id of login user
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            if (response.getError() == null) {
                                JSONObject joMain = response.getJSONObject(); //convert GraphResponse response to JSONObject
                                if (joMain.has("data")) {
                                    JSONArray jaData = sortJSONArrayAlphabetically(joMain.optJSONArray("data")); //find JSONArray from JSONObject
                                    if(jaData.length()==0){Toast.makeText(getApplicationContext(),"Empty albums",Toast.LENGTH_LONG).show();}else{
                                        for (int i = 0; i < jaData.length(); i++) {//find no. of album using jaData.length()
                                            JSONObject joAlbum = jaData.getJSONObject(i); //convert perticular album into JSONObject
                                            Album album=new Album(joAlbum.getString("name"));
                                            alFBAlbum.add(album);
                                        }
                                        recyclerView.setAdapter(new GridAdapter(alFBAlbum));
                                    }}
                            } else {
                                Log.d("Test", response.getError().toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "user_photos");
        graphforalbums.setParameters(parameters);
        graphforalbums.executeAsync();

    }
    @Override
    public void addnewuser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_LONG).show();


                        } else {

                            Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
    @Override
    public void setloginbutton() {
        buttonlogin.setReadPermissions(Arrays.asList("user_photos"));
        callbackManager = CallbackManager.Factory.create();
        buttonlogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            hideloginface();
            presenter.getalbumsasynch(AccessToken.getCurrentAccessToken().getUserId());
            presenter.adduser(getIntent().getStringExtra("email"),getIntent().getStringExtra("password"));}

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(),"Error occured",Toast.LENGTH_LONG).show();
            }
        });
    }
}
