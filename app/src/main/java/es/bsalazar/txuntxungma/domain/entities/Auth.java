package es.bsalazar.txuntxungma.domain.entities;

import com.google.gson.Gson;

public class Auth {

    public static final String COMPONENT_ROLE = "0";
    public static final String CEO_ROLE = "1";

    private long date;
    private String roleID;

    public Auth(long date, String roleID) {
        this.date = date;
        this.roleID = roleID;
    }

    public Auth(String Json) {
        Auth auth = new Gson().fromJson(Json, this.getClass());
        this.date = auth.getDate();
        this.roleID = auth.getRoleID();
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    @Override
    public String toString() {
        return  new Gson().toJson(this);
    }
}
