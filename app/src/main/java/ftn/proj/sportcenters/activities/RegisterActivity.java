package ftn.proj.sportcenters.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ftn.proj.sportcenters.MainActivity;
import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.adapters.MainActivityAdapter;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.DatabaseTool;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.model.SportCenter;
import ftn.proj.sportcenters.model.User;

public class RegisterActivity extends AppCompatActivity {



    private EditText mName;
    private EditText mLastname;
    private EditText mPassword;
    private EditText mRepeatPassword;
    private EditText mUsername;
    private EditText mCity;
    private EditText mEmail;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mName = findViewById(R.id.Name);
        mLastname = findViewById(R.id.Lastname);
        mPassword = findViewById(R.id.Password);
        mRepeatPassword = findViewById(R.id.RepeatPassword);
        mUsername = findViewById(R.id.Username);
        mCity = findViewById(R.id.City);
        mEmail = findViewById(R.id.Email);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        Button mRegister = findViewById(R.id.RegisterButton);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(mName.getText().toString(),mLastname.getText().toString(),
                        mPassword.getText().toString(),mRepeatPassword.getText().toString(),
                        mUsername.getText().toString(),mCity.getText().toString(), mEmail.getText().toString());

            }
        });
    }

    // za sada nista ne radi samo view prebacuje
    private void register(String name, String lastname,
                          String password, String repeat_password,
                          String username, String city, String email) {
        if(password.equals(repeat_password)) {


            String[] columns = {SportCenterSQLiteHelper.COLUMN_ID,SportCenterSQLiteHelper.COLUMN_USERNAME, SportCenterSQLiteHelper.COLUMN_PASSWORD,
                    SportCenterSQLiteHelper.COLUMN_POINTS, SportCenterSQLiteHelper.COLUMN_SPORTS,SportCenterSQLiteHelper.COLUMN_FIRSTNAME};
            Cursor cursor = getContentResolver().query(
                    DBContentProvider.CONTENT_URI_USER,columns,null,null,
                    null);

            if(cursor.getCount()==0) {
                DatabaseTool.initDB(this);
                cursor = getContentResolver().query(DBContentProvider.CONTENT_URI_USER,
                        columns,null, null,null);
            }
            boolean br= false;
            while (cursor.moveToNext()) {
                User sc = new User();
                sc.setId(cursor.getLong(0));
                sc.setUsername(cursor.getString(1));
                sc.setPassword(cursor.getString(2));
                sc.setPoints(cursor.getInt(3));
                sc.setSports(cursor.getString(4));
                sc.setFirstname(cursor.getString(5));
                if(sc.getUsername().equals(username)){
                    br = true; // br = true znaci da je ponovnjen username
                }
            }
            User newUser = new User(6, name, lastname, email,password, username, city, 0,"" );
            // newValues.put(SportCenterSQLiteHelper.COLUMN_ID, 6);

            if(!br) {
                Uri newUri;
                ContentValues newValues = new ContentValues();
                newValues.put(SportCenterSQLiteHelper.COLUMN_FIRSTNAME, name);
                newValues.put(SportCenterSQLiteHelper.COLUMN_LASTNAME, lastname);
                newValues.put(SportCenterSQLiteHelper.COLUMN_USERNAME, username);

                newValues.put(SportCenterSQLiteHelper.COLUMN_EMAIL, email);
                newValues.put(SportCenterSQLiteHelper.COLUMN_CITY, city);
                newValues.put(SportCenterSQLiteHelper.COLUMN_PASSWORD, password);
                newValues.put(SportCenterSQLiteHelper.COLUMN_POINTS, 0);
                newValues.put(SportCenterSQLiteHelper.COLUMN_SPORTS, "");

                newUri = getContentResolver().insert(
                        DBContentProvider.CONTENT_URI_USER,   // the user dictionary content URI
                        newValues                          // the values to insert
                );
                Toast.makeText(this, "Successfully registration!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "Username already exist. Set new one",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Wrong password!",Toast.LENGTH_SHORT).show();

        }



    }

}
