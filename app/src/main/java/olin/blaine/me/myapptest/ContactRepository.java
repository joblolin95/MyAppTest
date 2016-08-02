package olin.blaine.me.myapptest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blaine's Laptop on 7/27/2016.
 */
public class ContactRepository extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactManager",
    TABLE_NAME = "contacts",
    KEY_ID = "id",
    KEY_NAME = "name",
    KEY_PHONE = "phone",
    KEY_EMAIL = "email",
    KEY_ADDRESS = "address",
    KEY_IMAGEURI = "imageUri";

    public ContactRepository(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT, " + KEY_PHONE + " TEXT, " +
                    KEY_EMAIL + " TEXT, " + KEY_ADDRESS + " TEXT, " + KEY_IMAGEURI + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public void createContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, contact.getId());
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhoneNumber());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_IMAGEURI, contact.getImageUri().toString());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Contact getContact(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL, KEY_PHONE, KEY_IMAGEURI},
                KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                Uri.parse(cursor.getString(5)));

        db.close();
        cursor.close();

        return contact;
    }

    // add condition that ensures entity was deleted
    public void deleteContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public int getContactsCount(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * " + "FROM " + TABLE_NAME, null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    public int updateContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, contact.getId());
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhoneNumber());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_IMAGEURI, contact.getImageUri().toString());

        int numAffected = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
        return numAffected;
    }

    public List<Contact> GetAllContacts(){
        List<Contact> Contacts = new ArrayList<Contact>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + KEY_NAME + " ASC", null);

        if(cursor.moveToFirst()){
            do {
                Contacts.add(new Contact(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        Uri.parse(cursor.getString(5))));
            } while(cursor.moveToNext());
        }
        db.close();
        cursor.close();

        return Contacts;
    }



}
