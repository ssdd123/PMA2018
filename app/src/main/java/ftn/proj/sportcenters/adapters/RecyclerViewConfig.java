package ftn.proj.sportcenters.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ftn.proj.sportcenters.R;
import ftn.proj.sportcenters.model.Comment;

public class RecyclerViewConfig {

    private Context mContext;
    private CommentAdapter mCommentAdapter;
    public void setConfig(RecyclerView recyclerView, Context context, List<Comment> comments, List<String> keys){
        mContext = context;
        mCommentAdapter = new CommentAdapter(comments,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mCommentAdapter);

    }

    class CommentItemView extends RecyclerView.ViewHolder {
        private TextView mText;
        private TextView mUser;
        private TextView mDate;

        private String key;

        public CommentItemView( View view) {
            super(view);


            mText = (TextView) view.findViewById(R.id.commentTextTextView);
            mUser = (TextView) view.findViewById(R.id.commentUser);
            mDate = (TextView) view.findViewById(R.id.commentDate);


        }


        public void bind(Comment comment, String key){
            mText.setText(comment.getText());
            mUser.setText(String.valueOf(comment.getUserFirstname()));
            mDate.setText(String.valueOf(comment.getCommentDate()));
            this.key = key;

        }


    }
    class CommentAdapter extends  RecyclerView.Adapter<CommentItemView>{

        private List<Comment> mCommentsList;
        private List<String> mKeys;

        public CommentAdapter(List<Comment> mCommentsList, List<String> mKeys) {
            this.mCommentsList = mCommentsList;
            this.mKeys = mKeys;
        }

        public CommentAdapter() {
            super();
        }

        @NonNull
        @Override
        public CommentItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.comment_item,parent, false);

            return new CommentItemView(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentItemView holder, int position) {
            holder.bind(mCommentsList.get(position), mKeys.get(position));
        }

        @Override
        public int getItemCount() {
            return mCommentsList.size();
        }
    }
}
