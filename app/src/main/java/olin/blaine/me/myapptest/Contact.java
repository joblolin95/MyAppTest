package olin.blaine.me.myapptest;

import android.net.Uri;

/**
 * Created by Blaine's Laptop on 7/26/2016.
 */
public class Contact {
    private String _name, _phoneNumber, _emailAddress, _address;
    private int _id;
    private Uri _imageUri;
    public Contact(int id, String name, String phone, String email, String address, Uri image){
        _id = id;
        _name = name;
        _phoneNumber = phone;
        _emailAddress = email;
        _address = address;
        _imageUri = image;
    }

    public int getId(){ return _id; }

    public String getName(){
        return _name;
    }

    public String getPhoneNumber(){
        return _phoneNumber;
    }

    public String getEmail(){
        return _emailAddress;
    }

    public String getAddress(){
        return _address;
    }

    public Uri getImageUri(){
        return _imageUri;
    }

}
