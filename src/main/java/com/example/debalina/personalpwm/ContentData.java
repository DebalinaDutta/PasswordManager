package com.example.debalina.personalpwm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Debalina on 8/30/2015.
 */
public class ContentData implements Parcelable {

    private String _name;
    private String _subject;
    private String _userid;
    private String _password;

    public ContentData() {

    }

    public ContentData(String name, String subject, String userid, String password) {

        this._name = name;
        this._subject = subject;
        this._userid = userid;
        this._password = password;
    }

    public void setname(String name) {

        this._name = name;
    }
    public void setsubject(String subject) {

        this._subject = subject;
    }
    public void setuserid(String userid) {

        this._userid = userid;
    }
    public void setpassword(String password) {

        this._password = password;
    }
    public String getname() {

        return this._name;
    }

    public String getsubject() {

        return this._subject;
    }
    public String getuserid() {

        return this._userid;
    }
    public String getpassword() {

        return this._password;
    }
    // Parcelling part
    public ContentData(Parcel in) {
        String[] data = new String[4];

        //       this._id = in.readInt();
        this._name = in.readString();
        this._subject = in.readString();
        this._userid = in.readString();
        this._password = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

//        dest.writeInt(this._id);
        dest.writeString(this._name);
        dest.writeString(this._subject);
        dest.writeString(this._userid);
        dest.writeString(this._password);

    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ContentData createFromParcel(Parcel in) {
            return new ContentData(in);
        }

        public ContentData[] newArray(int size) {
            return new ContentData[size];
        }
    };
}
