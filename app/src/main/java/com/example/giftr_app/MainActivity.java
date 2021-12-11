package com.example.giftr_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Sign up page - user registers for the app using name, email, password
 */
public class MainActivity extends AppCompatActivity{

    EditText fname,lname,email,pwd;
    Button buttonAccount;
    String userId;
    String password;
    User user;
    RequestQueue requestQueue;
    boolean authenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        email=findViewById(R.id.email);
        pwd=findViewById(R.id.pwd);
        buttonAccount=findViewById(R.id.buttonAcount);


        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if input is empty
                if(fname.getText().toString().trim().equals("") || fname.getText() == null || lname.getText().toString().trim().equals("") || lname.getText() == null ||
                        email.getText().toString().trim().equals("") || email.getText() == null || pwd.getText().toString().trim().equals("") || pwd.getText() == null)
                {
                    Toast.makeText(MainActivity.this, "Please enter complete information", Toast.LENGTH_LONG).show();
                }
                // if password isn't 8 characters
                else if (pwd.getText().toString().length() < 8) {
                    Toast.makeText(MainActivity.this, "Password must be at least 8 characters", Toast.LENGTH_LONG).show();
                }

                // if an account is successfully made
                else
                {

                    userId = email.getText().toString();
                    password = pwd.getText().toString();

                    user = new User(userId);

                    // get user data from db and if the username matches (account is already created), sends them back to login screen
                    getUser();

                    // else sends them to confirm page
                    AuthSignUpOptions options = AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), userId)
                            .build();
                    Amplify.Auth.signUp(userId, password, options,
                            result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                            error -> Log.e("AuthQuickStart", "Sign up failed", error)
                    );

                    Intent confirmation = new Intent(MainActivity.this, ConfirmEmail.class);

                    Bundle info = new Bundle();

                    //putting email in bundle
                    info.putSerializable("userId", userId);
                    info.putSerializable("password", password);
                    confirmation.putExtras(info);

                    startActivity(confirmation);


                }

            }
        });

    }

    /**
     * Gets user data from database then calls checkAuthenticated()
     */
    private void getUser() {

        // might need a try here for invalid user id
        String url = "https://pcflbmodxf.execute-api.ca-central-1.amazonaws.com/crudStage/user_info/" + userId;

        //JSONARRAYObjectRequest
        JsonObjectRequest jRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // What should we do after the response is ready

                        try {
                            JSONObject stringObj = response.getJSONObject("Item");
                            String id = stringObj.getString("email");
                            String pass = (String) stringObj.getString("pass");
                            String authenticated = stringObj.getString("authenticated");

                            // checks if user is already authenticated - if they are, sends them to login page
                            checkAuthenticated(id, authenticated);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // What should we do if an error happens
                        Toast.makeText(MainActivity.this, "Something went wrong: "+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jRequest);
    }

    /**
     * authenticates if user's account is already authenticated. If it is, sends user back to login screen
     * @param id userId
     * @param auth authenticated
     */
    public void checkAuthenticated(String id, String auth) {

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (id.equals(userId)) {

                    if (auth.equals("true")) {
                        Toast.makeText(MainActivity.this, "This account is already authenticated. Returning to login page", Toast.LENGTH_LONG).show();
                        authenticated = true;


                        Intent login = new Intent(MainActivity.this, Login.class);

                        Bundle info = new Bundle();


                        //putting email in bundle
                        info.putSerializable("userId", userId);

                        login.putExtras(info);

                        startActivity(login);

                    }

                }
            }
        });

    }
}