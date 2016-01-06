package com.example.debalina.personalpwm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Debalina on 1/2/2016.
 */
public class TableData implements Parcelable {

    private int _ID;
    private String _name;
    private String _subject;
    private String _userid;
    private String _password;
    private int _ID1;
    private int _parent;
    private String _effdate;
    private int _days;

    public TableData() {

    }

    public TableData(int ID, String name, String subject, String userid, String password,
                     int ID1, int parent, String effdate, int days) {

        this._ID = ID;
        this._name = name;
        this._subject = subject;
        this._userid = userid;
        this._password = password;
        this._ID = ID1;
        this._parent = parent;
        this._effdate = effdate;
        this._days = days;
    }

    public void setID(int ID) {

        this._ID = ID;
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
    public void setID1(int ID1) {

        this._ID1 = ID1;
    }
    public void setparent(int parent) {

        this._parent = parent;
    }
    public void seteffdate(String effdate) {

        this._effdate = effdate;
    }
    public void setdays(int days) {

        this._days = days;
    }


    public int getID() {

        return this._ID;
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
    public int getID1() {

        return this._ID1;
    }
    public int getparent() {

        return this._parent;
    }
    public String geteffdate() {

        return this._effdate;
    }
    public int getdays() {

        return this._days;
    }

    // Parcelling part
    public TableData(Parcel in) {
        String[] data = new String[9];

        this._ID = in.readInt();
        this._name = in.readString();
        this._subject = in.readString();
        this._userid = in.readString();
        this._password = in.readString();
        this._ID1 = in.readInt();
        this._parent = in.readInt();
        this._effdate = in.readString();
        this._days = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this._ID);
        dest.writeString(this._name);
        dest.writeString(this._subject);
        dest.writeString(this._userid);
        dest.writeString(this._password);
        dest.writeInt(this._ID1);
        dest.writeInt(this._parent);
        dest.writeString(this._effdate);
        dest.writeInt(this._days);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TableData createFromParcel(Parcel in) {
            return new TableData(in);
        }

        public TableData[] newArray(int size) {
            return new TableData[size];
        }
    };
}

