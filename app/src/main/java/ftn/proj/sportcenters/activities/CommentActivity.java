package ftn.proj.sportcenters.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ftn.proj.sportcenters.MainActivity;
import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.adapters.RecyclerViewConfig;
import ftn.proj.sportcenters.database.FirebaseDatabaseHelper;
import ftn.proj.sportcenters.model.Comment;
import ftn.proj.sportcenters.model.SportCenter;

public class CommentActivity extends AppCompatActivity {

    private SportCenter sportCenter = new SportCenter();
    private RecyclerView  mRecyclerView;
    private Button mAddBtn;
    private EditText mCommentEditText;
    private String loggedFirstname;
    private long loggedId;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        loggedFirstname = sharedPreferences.getString("firstname","");
        loggedId = sharedPreferences.getLong("id",0L);

        setContentView(R.layout.activity_comment);
        sportCenter = (SportCenter) getIntent().getSerializableExtra("sportCenter");

        TextView nameView = (TextView) findViewById(R.id.Name);
        nameView.setText(sportCenter.getName());

        mAddBtn = (Button) findViewById(R.id.btnComment);
        mCommentEditText = (EditText) findViewById(R.id.commentInput);
        mRecyclerView = (RecyclerView) findViewById(R.id.commentRecyclerView);


        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                Comment comment = new Comment();
                comment.setCommentDate(String.valueOf(currentTime));
                comment.setUserFirstname(loggedFirstname);
                comment.setSportCenterId(sportCenter.getId());
                comment.setText(String.valueOf(mCommentEditText.getText()));

                new FirebaseDatabaseHelper().addComment(comment, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Comment> comments, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {
                        Toast.makeText(CommentActivity.this,"Your comment has beed sent!",Toast.LENGTH_SHORT).show();
                        mCommentEditText.setText("");

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });


        new FirebaseDatabaseHelper().readComment(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Comment> comments, List<String> keys) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);

                List<Comment> commentBySportCenter = new ArrayList<Comment>();
                for(Comment c : comments){
                    if(c.getSportCenterId() == sportCenter.getId()){
                        commentBySportCenter.add(c);
                    }

                }

                new RecyclerViewConfig().setConfig(mRecyclerView,CommentActivity.this,commentBySportCenter,keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }

}
