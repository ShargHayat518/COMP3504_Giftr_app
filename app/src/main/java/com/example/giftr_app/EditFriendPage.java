package com.example.giftr_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Random;
import yuku.ambilwarna.AmbilWarnaDialog;


/**
 * This class is used to add or edit the user's friends' data: Name, colour, gender, dob, interests, and desired gift price range.
 */
public class EditFriendPage extends AppCompatActivity implements View.OnClickListener {

    RequestQueue requestQueue;

    Friend friend;
    Boolean newFriend;
    String userId;

    // id of friend
    int id;
    String friendId;

    // colours
    int mDefaultColour;
    Button mButton;
    TextView image;

    // name
    EditText editName;
    String friendName;

    // dates
    DatePickerDialog datePickerDialog;
    Button dateButton;
    int bYear;
    int bMonth;
    int bDay;

    // gender
    RadioButton maleButton;
    RadioButton femaleButton;
    RadioButton otherButton;
    RadioGroup radioGroup;
    String gender;

    // interests
    EditText interests;
    String line = "";
    ArrayList<String> interestAList = new ArrayList<>();
    String[] interestArray;

    // for price slider
    RangeSlider slider;
    float minPrice;
    float maxPrice;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfriendpage);
        requestQueue = Volley.newRequestQueue(this);

        findViewById(R.id.saveButton).setOnClickListener(this);

        maleButton = findViewById(R.id.maleGender);
        femaleButton = findViewById(R.id.femaleGender);
        otherButton = findViewById(R.id.otherGender);

        editName = findViewById(R.id.name);

        interests = findViewById(R.id.editInterests);

        newFriend = false;


        //receiving friend object if there is one
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // formatting slider to be in CAD
        slider = findViewById(R.id.priceSlider);
        initCadSlider();

        // initializing price slider listener
        initPriceSlider();

        // initializing colour picker listener
        image = findViewById(R.id.friendPic);
        mButton = findViewById(R.id.changePhoto);
        initMButton();

        // initializing radio group listener
        radioGroup = findViewById(R.id.genderGroup);
        initGenderGroup();

        // initializing date picker listener
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);

        //sets default colour to purple
        mDefaultColour = ContextCompat.getColor(this, R.color.purple_200);

        // if a friend is successfully passed (this becomes a populated edit friend page)

        friend = (Friend) bundle.getSerializable("friend");
        userId = (String) bundle.getSerializable("userId");


        // this doesn't work because variables can't be assigned within try catch. Creates a duplicate friend after editing friend.
        try {
            friendId = friend.getId();
        } catch (NullPointerException e) {
            newFriend = true;
        }


        // if the friend has an id - it is not a new friend
        if (!newFriend) {

            //getting friend name
            friendName = friend.getName();

            //setting displayed name to friend's name
            editName.setText(friendName);

            // setting dob to the friend's dob
            dateButton.setText(getFriendDob());

            //getting friend gender and setting displayed gender
            if (friend.getGender() != null) {
                displayGender(friend.getGender());
                gender = friend.getGender();
            }

            // get and set displayed slider price values - there can't be null prices set as a default is set when a friend is instantiated
            slider.setValues(friend.getMinPrice(), friend.getMaxPrice());

            // gets friend's colour
            mDefaultColour = friend.getColour();

            // gets friend's interests
            if (!friend.getInterests().isEmpty()) {
                interests.setText(friend.getStringInterests());
            }


        }

        // if it is a new friend
        else {
            friend = new Friend();
        }


        // displays friend's colour or default colour
        image.setBackgroundColor(mDefaultColour);

    }

    /**
     * Opens colour popup window, allows user to change the friend's preferred colour.
     */
    public void openColourPicker() {
        AmbilWarnaDialog colourPicker = new AmbilWarnaDialog(this, mDefaultColour, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // auto goes back
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int colour) {
                mDefaultColour = colour;
                image.setBackgroundColor(mDefaultColour);

            }
        });
        colourPicker.show();
    }

    /**
     * Opens date picker window
     * @param v view
     */
    public void openDatePicker(View v) {
        datePickerDialog.show();
    }

    /**
     * Initializes listener to date picker window, reads input and converts to desired format
     */
    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
                bYear = year;
                bMonth = month;
                bDay = day;
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    /**
     * Gets the friend's dob for the initial date text display
     * @return dob
     */
    public String getFriendDob() {
        bYear = friend.getbYear();
        bMonth = friend.getbMonth();
        bDay = friend.getbDay();
        return makeDateString(bDay, bMonth, bYear);

    }

    /**
     * Creates a string of the date for the date text display
     * @param day birth day
     * @param month birth month
     * @param year birth year
     * @return String of the dob
     */
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    /**
     * Changes the birth month from int to string for the display
     * @param month birth month
     * @return String month
     */
    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        //default should never happen
        return "JAN";

    }

    /**
     * Allows the price slider to be displayed in CAD
     */
    public void initCadSlider() {
        slider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                currencyFormat.setCurrency(Currency.getInstance("CAD"));
                return currencyFormat.format(value);
            }
        });
    }

    /**
     * Adds an on change listener to the price slider
     */
    public void initPriceSlider() {
        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> array;
                array = slider.getValues();
                minPrice = array.get(0);
                maxPrice = array.get(1);
            }
        });
    }

    /**
     * initializes the colour picker button
     */
    public void initMButton() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColourPicker();
            }
        });
    }

    /**
     * initializes the onclick listener equivalent for the radio group buttons.
     */
    public void initGenderGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId) {
                    //male
                    case R.id.maleGender:
                        gender = "male";
                        break;
                    //female
                    case R.id.femaleGender:
                        gender = "female";
                        break;
                    //other
                    case R.id.otherGender:
                        gender = "other";
                        break;
                }
            }
        });
    }

    /**
     * Reads and displays gender from the friend object.
     * @param gender Friend's gender
     */
    public void displayGender(String gender) {
        if (gender.equalsIgnoreCase("male")) {
            maleButton.setChecked(true);
        }
        else if (gender.equalsIgnoreCase("female")) {
            femaleButton.setChecked(true);
        }
        // doesn't check this if gender isn't chosen.
        else {
            otherButton.setChecked(true);
        }
    }


    /**
     * Creates an arraylist of interests from the editText field
     */
    public void createInterestArrayList() {

        line = interests.getText().toString();
        interestArray = line.split(",");

        interestAList.addAll(Arrays.asList(interestArray));

    }


    /**
     * updates the database with the friend's information
     */
    public void updateFriend() {
        // API stuff
        JSONObject putData = null;
        final String url = "https://pcflbmodxf.execute-api.ca-central-1.amazonaws.com/crudStage/friend_item";
        Random rand = new Random(System.currentTimeMillis());
        id = Math.abs(rand.nextInt());
        friendId = String.valueOf(id);

        try {
            putData = new JSONObject();
            putData.put("friend_id", friendId);
            putData.put("user_id", userId);
            //putData.put("user_id","jordan.email.com");
            putData.put("name", this.friendName);
            putData.put("birth_year", this.bYear);
            putData.put("birth_month", this.bMonth);
            putData.put("birth_day", this.bDay);
            // test what happens when we don't enter a gender
            putData.put("gender", gender);
            putData.put("color_id", mDefaultColour);
            putData.put("min_price", minPrice);
            putData.put("max_price", maxPrice);
            putData.put("interests", line);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // might have to change line 102 to a JSON array
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, putData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(EditFriendPage.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(EditFriendPage.this, "That did not work!", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.saveButton) {

            friendName = editName.getText().toString();

            // if friend name is null - page fails to save
            if (friendName.equals("")) {
                Toast.makeText(this, "Please enter your friend's name", Toast.LENGTH_SHORT).show();
            }

            else if (gender == null) {
                Toast.makeText(this, "Please enter a gender", Toast.LENGTH_SHORT).show();
            }

            // if friend dob is null - page fails to save
            else if (dateButton.getText().equals("Enter Birth Date")) {
                Toast.makeText(this, "Please enter your friend's date of birth", Toast.LENGTH_LONG).show();
            }

            // if max price is 0 (user wants to search only for free items) - page fails to save
            else if (minPrice == 0 && maxPrice == 0) {
                Toast.makeText(this, "Max price cannot be $0", Toast.LENGTH_LONG).show();
            }

            // if the required fields are filled out correctly
            else {

                //sets friend object's name
                friend.setName(friendName);

                // sets user ID
                friend.setUserId(userId);

                //sets friend's birth date
                friend.setbYear(bYear);
                friend.setbMonth(bMonth);
                friend.setbDay(bDay);

                //sets friend's colour
                friend.setColour(mDefaultColour);

                //sets friend's price range
                friend.setPriceRange(minPrice, maxPrice);

                //sets friend's gender
                friend.setGender(gender);


                // sets interests from the editText to the friend arraylist and string of the arraylist
                createInterestArrayList();
                friend.setInterests(interestAList);
                friend.setStringInterests(line);

                // updates friend to database using api
                updateFriend();


                Bundle info = new Bundle();

                //putting edited friend in bundle
                info.putSerializable("friend", friend);
                info.putSerializable("userId", userId);


                Intent save = new Intent(this, FriendPage.class);
                save.putExtras(info);
                startActivity(save);

                Toast.makeText(this, "Friend data saved", Toast.LENGTH_SHORT).show();
            }


        }

    }
}
