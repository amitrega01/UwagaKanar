package com.dev.insu.uwagakanar.Models;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by amitr on 30.10.2017.
 */

public class Warning {
    private String nrLini;
    private String godz;
    private String user;

    public Warning() {
        super();
    }

    public Warning(String nrLini, String godz, String user) {
        this.nrLini = nrLini;
        this.godz = godz;
        this.user = user;
    }

    public String getNrLini() {
        return nrLini;
    }

    public void setNrLini(String nrLini) {
        this.nrLini = nrLini;
    }

    public String getGodz() {
        return godz;
    }

    public void setGodz(String godz) {
        this.godz = godz;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


}
