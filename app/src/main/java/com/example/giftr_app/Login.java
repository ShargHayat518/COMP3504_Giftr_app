package com.example.giftr_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
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
 * Login page for the app - default starting page
 */
public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    EditText password;
    Button login;
    Button signUp;
    String userId;
    String truePass;
    String enteredPass;
    User user;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);


        findViewById(R.id.btnlogin).setOnClickListener(this);
        findViewById(R.id.createnewac).setOnClickListener(this);

        email=findViewById(R.id.etemail);
        password=findViewById(R.id.mypass);
        login=findViewById(R.id.btnlogin);
        signUp=findViewById(R.id.createnewac);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        //try to get user data from bundle to populate email slot to save user time
        try {
            user = (User) bundle.getSerializable("user");
            userId = user.getUserId();
            truePass = user.getPassword();
            email.setText(userId);

        }catch (Exception e) {
            e.printStackTrace();
        }

        // initialize amplify
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");

        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

    }


    @Override
    public void onClick(View view) {

        // if they hit the login button
        if(view.getId() == R.id.btnlogin) {
            // if they didn't fill out the fields
            if(email.getText().toString().trim().equals("") || email.getText() == null || password.getText().toString().trim().equals("") || password.getText() == null) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
            // if fields are correct
            else {
                userId = email.getText().toString();
                enteredPass = (String) password.getText().toString();


                // lets them sign in
                if (truePass != null) {
                    if (enteredPass.equals(truePass)) {
                        Amplify.Auth.signIn(
                                userId,
                                enteredPass,
                                result -> Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete"),
                                error -> Log.e("AuthQuickstart", error.toString())
                        );

                        // takes user to closest bday page
                        Intent mainPage = new Intent(this, ClosestBday.class);

                        Bundle info = new Bundle();

                        //putting email in bundle
                        info.putSerializable("userId", userId);
                        mainPage.putExtras(info);

                        startActivity(mainPage);
                    }
                    else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                }

                // if they try to login without coming from sign up page (on startup)
                else {

                    // get user from database - if entered username and pass = db user and pass, login success.
                      getUser();

                }

            }
        }

        // if the user needs to create a new account
        else if (view.getId() == R.id.createnewac) {
            Intent mainPage = new Intent(this, MainActivity.class);
            startActivity(mainPage);

        }
    }

    /**
     * gets user info from database then calls authenticateUser()
     * email and userId variables are used interchangeably and are the key for the database
     */
    private void getUser() {

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


                            authenticateUser(id, pass);

                            String authenticated = stringObj.getString("authenticated");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // What should we do if an error happens
                        Toast.makeText(Login.this, "Something went wrong: "+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jRequest);
    }

    /**
     * Checks if entered username and password are equal to the ones stored in the db
     * @param id email/userId
     * @param pass password
     */
    public void authenticateUser(String id, String pass) {
        if (id.equals(userId)) {

            if (!pass.equals(enteredPass)) {
                Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            } else {

                Amplify.Auth.signIn(
                        userId,
                        enteredPass,
                        result -> Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );

                // login success-  goes to closestbday page
                Intent mainPage = new Intent(this, ClosestBday.class);

                Bundle info = new Bundle();

                //putting email in bundle
                info.putSerializable("userId", userId);

                mainPage.putExtras(info);

                startActivity(mainPage);


            }
        }
        // username doesn't match
        else{
                Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show();
            }

    }

}

