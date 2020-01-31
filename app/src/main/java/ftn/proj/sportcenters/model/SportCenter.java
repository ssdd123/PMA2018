package ftn.proj.sportcenters.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class SportCenter implements Serializable {

    private long id;
    private String name;
    private String address;
    private int image;
    private List<String> workingHours;
    private List<String> sports;
    private float lat;
    private float longg;
    private float rating;

//    private static Creator CREATOR;

    public SportCenter(long id, String name, String address, int image, List<String> workingHours, List<String> sports,float lat, float longg, float rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.image = image;
        this.workingHours = workingHours;
        this.sports = sports;
        this.lat = lat;
        this.longg = longg;
        this.rating = rating;

    }

    public SportCenter(){

    }

    protected SportCenter(Parcel in) {
        id = in.readInt();
        name = in.readString();
        address = in.readString();
        image = in.readInt();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public List<String> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<String> workingHours) {
        this.workingHours = workingHours;
    }

    public List<String> getSports() {
        return sports;
    }

    public void setSports(List<String> sports) {
        this.sports = sports;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLongg() {
        return longg;
    }

    public void setLongg(float longg) {
        this.longg = longg;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return name;
    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel out, int flags) {
//        out.writeString(name);
//        out.writeString(address);
//        out.writeInt(image);
//    }
}
