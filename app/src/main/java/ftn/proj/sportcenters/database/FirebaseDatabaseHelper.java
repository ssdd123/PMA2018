package ftn.proj.sportcenters.database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ftn.proj.sportcenters.model.Comment;

public class FirebaseDatabaseHelper {


    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private List<Comment> comments = new ArrayList<>();

    public interface  DataStatus{
        void DataIsLoaded(List<Comment> comments, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper(){

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("comment");

    }
    public  void readComment( final DataStatus dataStatus){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren()){

                    keys.add(keyNode.getKey());
                    Comment comment = keyNode.getValue(Comment.class);
                    comments.add(comment);
                }

                dataStatus.DataIsLoaded(comments,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addComment(Comment comment, final DataStatus dataStatus){
      String key =   mReference.push().getKey();
      mReference.child(key).setValue(comment)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      dataStatus.DataIsInserted();
                  }
              });

    }
}
