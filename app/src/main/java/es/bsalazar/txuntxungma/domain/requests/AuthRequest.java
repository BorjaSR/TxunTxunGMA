package es.bsalazar.txuntxungma.domain.requests;

public class AuthRequest {

    private String roleID;
    private String encryptPass;

    public AuthRequest(String roleID, String encryptPass) {
        this.roleID = roleID;
        this.encryptPass = encryptPass;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getEncryptPass() {
        return encryptPass;
    }

    public void setEncryptPass(String encryptPass) {
        this.encryptPass = encryptPass;
    }
}
