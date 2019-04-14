package com.adria.facebooktest.AlbumActivity;

public class Albumspresenterimp implements Presenteralbums {
    private Albumsview view;
    public Albumspresenterimp(Albumsview view){this.view=view;}


    @Override
    public void getalbumsasynch(String iduser) {
        view.getalbums(iduser);
    }

    @Override
    public void setlogin() {
view.setloginbutton();
    }

    @Override
    public void adduser(String email, String password) {
        view.addnewuser(email,password);
    }
}
