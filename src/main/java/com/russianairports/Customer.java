package com.russianairports;

public class Customer
{
    private String login;
    private String email;
    private int passportData;
    private String fullname;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPassportData() {
        return passportData;
    }

    public void setPassportData(int passportData) {
        this.passportData = passportData;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Customer(String login,String email,int passportData,String fullname)
    {
        this.login = login;
        this.email = email;
        this.passportData = passportData;
        this.fullname = fullname;
    }
}
