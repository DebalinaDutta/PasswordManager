package com.example.debalina.personalpwm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Debalina on 8/30/2015.
 */
public class AccountProfile implements Parcelable {

    private String _member;
    private String _userID;
    private String _password;
    private String _viewPassword;
    private String _email;


    //blank constructor
    public AccountProfile() {

    }

    //constructor
    public AccountProfile(String member, String userID, String password, String _viewPassword, String email) {

        this._member = member;
        this._userID = userID;
        this._password = password;
        this._viewPassword = _viewPassword;
        this._email = email;
    }

    //getters
    public void setmember(String member) {

        this._member = member;
    }
    public void setuserID(String userID) {

        this._userID = userID;
    }
    public void setpassword(String password) {

        this._password = password;
    }
    public void setviewPassword (String viewPassword) {

        this._viewPassword = viewPassword;
    }
    public void setemail (String email) {

        this._email = email;
    }

    //getters
    public String getmember() {

        return this._member;
    }

    public String getuserID() {

        return this._userID;
    }
    public String getpassword() {

        return this._password;
    }
    public String getviewPassword() {

        return this._viewPassword;
    }
    public String getemail() {

        return this._email;
    }

    // Parcelling part
    public AccountProfile(Parcel in) {
        String[] data = new String[5];

        this._member = in.readString();
        this._userID = in.readString();
        this._password = in.readString();
        this._viewPassword = in.readString();
        this._email = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

//        dest.writeInt(this._id);
        dest.writeString(this._member);
        dest.writeString(this._userID);
        dest.writeString(this._password);
        dest.writeString(this._viewPassword);
        dest.writeString(this._email);

    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AccountProfile createFromParcel(Parcel in) {
            return new AccountProfile(in);
        }

        public AccountProfile[] newArray(int size) {
            return new AccountProfile[size];
        }
    };
}
