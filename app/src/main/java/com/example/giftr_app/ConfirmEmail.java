package com.example.giftr_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
 * This class confirms the user's email address by sending the user an email verification code.
 */
public class ConfirmEmail extends AppCompatActivity implements View.OnClickListener {

    TextView email;
    EditText confirm;
    String userId;
    String confirmCode;
    String password;
    User user;
    String authenticated;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmemail);

        requestQueue = Volley.newRequestQueue(this);

        email=findViewById(R.id.username);
        confirm=findViewById(R.id.confirmation);

        findViewById(R.id.confirm).setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        userId = (String) bundle.getSerializable("userId");
        password = (String) bundle.getSerializable("password");

        email.setText(userId);

        user = new User(userId);
        user.setPassword(password);

        openingToast();

    }

    @Override
    public void onClick(View view) {

        // if fields are empty
        if(email.getText().toString().trim().equals("") || email.getText() == null || confirm.getText().toString().trim().equals("") || confirm.getText() == null) {
            Toast.makeText(this, "Email and Confirmation code cannot be empty", Toast.LENGTH_SHORT).show();
        }

        // if they filled out both
        else {
            userId = email.getText().toString();
            confirmCode = confirm.getText().toString();

            // authentication
            Amplify.Auth.confirmSignUp(
                    userId,
                    confirmCode,
                    result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                    error -> Log.e("AuthQuickstart", error.toString())
            );
            Toast.makeText(this, "Account authenticated. Please sign in.", Toast.LENGTH_SHORT).show();
            Intent success = new Intent(ConfirmEmail.this, Login.class);

            // sets user as confirmed.
            user.setAuthenticated();
            authenticated = "true";

            // puts user into db
            putUserData();


            Bundle info = new Bundle();

            //putting user object in bundle
            info.putSerializable("user", user);
            success.putExtras(info);

            startActivity(success);
        }
    }

    /**
     * Puts entered user data into the database. The users email is the key id. Email and id are used interchangeably as variable names.
     */
    public void putUserData() {
        // API stuff
        JSONObject putData = null;
        final String url = "https://pcflbmodxf.execute-api.ca-central-1.amazonaws.com/crudStage/user_info";

        try {
            putData = new JSONObject();
            putData.put("email", userId);
            putData.put("pass", password);
            putData.put("authenticated", authenticated);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // might have to change line 102 to a JSON array
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, putData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ConfirmEmail.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(EditFriendPage.this, "That did not work!", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void openingToast() {
        Toast.makeText(ConfirmEmail.this, "Account created. Check email for confirmation code.", Toast.LENGTH_LONG).show();
    }


}
