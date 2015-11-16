package org.blowstuff.manualfb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();
    }

    private void publishOnMyFeed(){
        Profile pt = Profile.getCurrentProfile();
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(pt.getLinkUri())
                .setContentDescription("Olá, consegui entrar no meu próprio facebook")
                .build();

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.meudrawable);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();

        SharePhotoContent photoContent = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();


        ShareDialog.show(this, photoContent);
    }

    private void getMyInformation(){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        JSONArray jsonArray = response.getJSONArray();
                        try {
                            String name = object.getString("name");
                            String id = object.getString("id");
                            String link = object.getString("link");
                            TextView tv = (TextView)findViewById(R.id.info_user);
                            tv.setText("Nome: " + name + "\nID: " + id + "\nLink: " + link);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void LoginClick(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, publish_actions"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //publishOnMyFeed();
                accessToken = AccessToken.getCurrentAccessToken();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void getMyInformationByProfile(){
        Profile profile = Profile.getCurrentProfile();
        TextView tv = (TextView)findViewById(R.id.info_user);
        tv.setText("Nome: " + profile.getName() + "\nID: " + profile.getId() + "\nLink: " + profile.getLinkUri().toString());

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(profile.getLinkUri())
                .setContentDescription("Olá, criei uma aplicação brutal mas não percebi nada do que me explicaram!!!")
                .build();

        //MessageDialog.show(this, content);
        ShareApi.share(content, null);
    }

    public void InfoUser(View view) {
        if(accessToken == null) {
            TextView tv = (TextView)findViewById(R.id.info_user);
            tv.setText("[ERRO] - Access Token Inválido");
            return;
        }

        //getMyInformation();
        getMyInformationByProfile();
    }
}

