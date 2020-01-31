package ftn.proj.sportcenters.model;

import java.util.Date;

public class Comment {


    private String userFirstname;
    private long sportCenterId;
    private String commentDate;
    private String text;

    public Comment(){

    }

    public Comment( String userFirstname, long sportCenterId, String commentDate, String text) {

        this.userFirstname = userFirstname;
        this.sportCenterId = sportCenterId;
        this.commentDate = commentDate;
        this.text = text;
    }


    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public long getSportCenterId() {
        return sportCenterId;
    }

    public void setSportCenterId(long sportCenterId) {
        this.sportCenterId = sportCenterId;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
