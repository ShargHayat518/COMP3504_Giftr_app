package com.example.giftr_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class changes the user's password
 *
 */
public class ChangePass extends AppCompatActivity implements View.OnClickListener {

    RequestQueue requestQueue;
    EditText oldPass;
    EditText newPass;
    String stringOld;
    String stringNew;
    String userId;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);

        requestQueue = Volley.newRequestQueue(this);

        findViewById(R.id.save).setOnClickListener(this);

        oldPass = findViewById(R.id.old_pwd);
        newPass = findViewById(R.id.new_pwd);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        userId = (String) bundle.getSerializable("userId");
    }



    @Override
    public void onClick(View view) {

        // if either password is empty
        if(oldPass.getText().toString().trim().equals("") || oldPass.getText() == null || newPass.getText().toString().trim().equals("") || newPass.getText() == null) {
            Toast.makeText(this, "Passwords cannot be blank", Toast.LENGTH_SHORT).show();
        }

        // if new password is less than 8 characters
        else if (newPass.getText().toString().length() < 8) {
            Toast.makeText(ChangePass.this, "Password must be at least 8 characters", Toast.LENGTH_LONG).show();
        }

        // if fields are not blank or less than 8 chars
        else {
            stringOld = oldPass.getText().toString();
            stringNew = newPass.getText().toString();
            getUser();

        }


    }


    /**
     * Gets the user data from the database then calls authenticateUser()
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


                            authenticateUser(id, pass);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // What should we do if an error happens
                        Toast.makeText(ChangePass.this, "Something went wrong: "+ error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jRequest);
    }

    /**
     * Authenticates password - checks entered old password against the one stored in the db - if correct, calls updateNewPass()
     * @param id user's id from db
     * @param pass user's pass from db
     */
    public void authenticateUser(String id, String pass) {

        if (id.equals(userId)) {

            if (!pass.equals(stringOld)) {
                Toast.makeText(this, "Incorrect old password", Toast.LENGTH_SHORT).show();
            }
            // entered old pass matches the one in the db
            else {
                updateNewPass();
            }
        } else {
            Toast.makeText(this, "An error has occured.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * puts the new password into the database - overwriting the old password - then calls goToLogin()
     */
    public void updateNewPass() {
        // API stuff
        JSONObject putData = null;
        final String url = "https://pcflbmodxf.execute-api.ca-central-1.amazonaws.com/crudStage/user_info";

        try {
            putData = new JSONObject();
            putData.put("email", userId);
            putData.put("pass", stringNew);
            putData.put("authenticated", "true");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // might have to change line 102 to a JSON array
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, putData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ChangePass.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(EditFriendPage.this, "That did not work!", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
        user = new User(userId, stringNew);
        user.setAuthenticated();
        goToLogin();
    }

    /**
     * Returns user to login screen to login with new password
     */
    public void goToLogin() {
        Toast.makeText(this, "Password successfully changed", Toast.LENGTH_SHORT).show();
        Intent success = new Intent(ChangePass.this, Login.class);

        Bundle info = new Bundle();

        //putting user object in bundle
        info.putSerializable("user", user);
        success.putExtras(info);

        startActivity(success);
    }
}
