package com.wenym.grooo.model.http;

/**
 * Created by runzii on 16-7-2.
 */
public class ChangePwdForm {

    private String newPassword;

    public ChangePwdForm(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
