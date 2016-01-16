package com.example.debalina.personalpwm;

import java.io.File;

/**
 * Created by Debalina on 1/15/2016.
 */
public class Excel2SqliteResponse {

    Boolean success;
    String mesg;

    public Excel2SqliteResponse(Boolean success, String mesg) {

        this.success=success;
        this.mesg=mesg;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public String getmesg() { return this.mesg;}
}
