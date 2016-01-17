package com.example.debalina.personalpwm;

import java.io.File;

/**
 * Created by Debalina on 1/14/2016.
 */
public class sqlite2ExcelResponse {

    Boolean success;
    String mesg;
    String filename;
    File directory;

    public sqlite2ExcelResponse(Boolean success, String mesg, String filename, File directory) {

        this.success=success;
        this.mesg=mesg;
        this.filename=filename;
        this.directory=directory;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public String getmesg() {
        return this.mesg;
    }

    public String getfilename() {
        return this.filename;
    }

    public File getdirectory() {
        return this.directory;
    }
}
