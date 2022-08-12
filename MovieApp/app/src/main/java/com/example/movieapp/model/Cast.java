package com.example.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cast implements Parcelable {
    private boolean adult;
    private int gender;
    private int id;
    private String known_for_department;
    private String name;
    private String original_name;
    private double popularity;
    private String profile_path;
    private int cast_id;
    private String character;
    private String credit_id;
    private int order;



    public Cast(boolean adult, int gender, int id, String known_for_department, String name, String original_name, double popularity, String profile_path, int cast_id, String character, String credit_id, int order) {
        this.adult = adult;
        this.gender = gender;
        this.id = id;
        this.known_for_department = known_for_department;
        this.name = name;
        this.original_name = original_name;
        this.popularity = popularity;
        this.profile_path = profile_path;
        this.cast_id = cast_id;
        this.character = character;
        this.credit_id = credit_id;
        this.order = order;
    }

    protected Cast(Parcel in) {
        adult = in.readByte() != 0;
        gender = in.readInt();
        id = in.readInt();
        known_for_department = in.readString();
        name = in.readString();
        original_name = in.readString();
        popularity = in.readDouble();
        profile_path = in.readString();
        cast_id = in.readInt();
        character = in.readString();
        credit_id = in.readString();
        order = in.readInt();
    }

    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKnown_for_department() {
        return known_for_department;
    }

    public void setKnown_for_department(String known_for_department) {
        this.known_for_department = known_for_department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public int getCast_id() {
        return cast_id;
    }

    public void setCast_id(int cast_id) {
        this.cast_id = cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeInt(gender);
        dest.writeInt(id);
        dest.writeString(known_for_department);
        dest.writeString(name);
        dest.writeString(original_name);
        dest.writeDouble(popularity);
        dest.writeString(profile_path);
        dest.writeInt(cast_id);
        dest.writeString(character);
        dest.writeString(credit_id);
        dest.writeInt(order);
    }
}
