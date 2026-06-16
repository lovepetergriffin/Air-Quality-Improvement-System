package com.cqupt.nep.dto;

public class LoginDto {
    private String account;
    private String adminCode;
    private String gmCode;
    private String telId;
    private String password;

    public String getAccount() {
        if (notBlank(account)) {
            return account;
        }
        if (notBlank(adminCode)) {
            return adminCode;
        }
        if (notBlank(gmCode)) {
            return gmCode;
        }
        return telId;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public String getGmCode() {
        return gmCode;
    }

    public void setGmCode(String gmCode) {
        this.gmCode = gmCode;
    }

    public String getTelId() {
        return telId;
    }

    public void setTelId(String telId) {
        this.telId = telId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private boolean notBlank(String value) {
        return value != null && value.trim().length() > 0;
    }
}
