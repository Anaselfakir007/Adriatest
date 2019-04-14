package com.adria.facebooktest.LoginActivity;

public class Loginpresenterimp implements Presenterlogin {
    private Loginview view;
    public Loginpresenterimp(Loginview view){this.view=view;}


    @Override
    public void login(String email, String password) {
view.login(email,password);
    }



}
