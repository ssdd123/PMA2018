package ftn.proj.sportcenters.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ftn.proj.sportcenters.MainActivity;
import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.database.DBContentProvider;
import ftn.proj.sportcenters.database.DatabaseTool;
import ftn.proj.sportcenters.database.SportCenterSQLiteHelper;
import ftn.proj.sportcenters.model.SportCenter;
import ftn.proj.sportcenters.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    DatabaseReference databaseReference;
    List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!sharedPreferences.getString("username","").equals("")) { //IF THERE IS SESSION

            Toast.makeText(this, "Logged in as: "+ sharedPreferences.getString("username",""),Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
            setContentView(R.layout.activity_login);
        mUsername = findViewById(R.id.Username);
        mPassword = findViewById(R.id.Password);
        Button mLogin = findViewById(R.id.LoginButton);
        Button mRegister = findViewById(R.id.RegisterButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        userList = new ArrayList<>();




        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(mUsername.getText().toString(),mPassword.getText().toString());
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //za sada samo prebacuje view
    private void login(String username, String password){

//SET USER SESSION
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

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
            if(sc.getUsername().equals(username) && sc.getPassword().equals(password)){
                br = true;
                editor.putLong("id", sc.getId());
                editor.putString("firstname", sc.getFirstname());
                editor.putString("username", sc.getUsername()); //SET SESSION FOR USER
                editor.putInt("points", sc.getPoints());
                editor.putString("sports", sc.getSports());
                editor.commit();
                break;
                 // zavrsava ovaj activity, na back ne ide na login opet
            }

        }

        if(br){
            Toast.makeText(this, "Successfully login! " ,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Wrong username or password!",Toast.LENGTH_SHORT).show();
        }
       // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //startActivity(intent);
       // finish(); // zavrsava ovaj activity, na back ne ide na login opet
    }

}
