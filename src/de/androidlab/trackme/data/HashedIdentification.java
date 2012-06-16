package de.androidlab.trackme.data;

import java.io.InputStream;
import java.util.HashMap;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import de.androidlab.trackme.R;
import de.androidlab.trackme.utils.Converter;

public class HashedIdentification {
    
    // Maps hashed phone number to contact info
    private HashMap<String, ContactInfo> map = new HashMap<String, ContactInfo>();
    private Context context;
    private final int anonymousPicture = R.drawable.ic_anonym;
    private int anonymousCounter = 0;
    
    public void init(Context context) {
        this.context = context;
        ContentResolver cr = context.getContentResolver();
       // Get all contacts
       String[] contactColumns = new String[]{Contacts._ID,
                                              Contacts.DISPLAY_NAME,
                                              Contacts.PHOTO_ID,
                                              Contacts.HAS_PHONE_NUMBER}; 
       Cursor contact = cr.query(Contacts.CONTENT_URI, contactColumns, null, null, null);
       try {
           while (contact.moveToNext()) {
               ContactInfo info = new ContactInfo();
               info.name = contact.getString(1);
               info.picture = findPhoto(contact.getLong(2));
               if (info.picture == null) {
                   info.picture = BitmapFactory.decodeResource(context.getResources(), anonymousPicture);
               }
               if (contact.getString(3).equals("1") == true) {
                   String[] numberColumns = new String[]{Phone.NUMBER};
                   String selection = Phone.CONTACT_ID + " =?"; 
                   String[] selArgs = new String[]{contact.getString(0)};
                   Cursor numbers = cr.query(Phone.CONTENT_URI, numberColumns, selection, selArgs, null);
                   while (numbers.moveToNext()) {
                       String number = Converter.normalizeNumber(numbers.getString(0)); 
                       String hashedNumber = Converter.calculateHash(number);
                    info.numbers.add(hashedNumber);
                    map.put(hashedNumber, info);
                   }
               }
           }
       } finally {
           contact.close();
       }
    }
    
    private Bitmap findPhoto(long contactID) {
        Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactID);
        InputStream input = Contacts.openContactPhotoInputStream(context.getContentResolver(), contactUri);
        if (input == null) {
            return null;
        }
        return BitmapFactory.decodeStream(input);
    }
    
    public boolean isFriend(String hashedNumber) {
        return map.containsKey(hashedNumber);
    }
    
    public ContactInfo getContactInfo(String hashedNumber) {
        return map.get(hashedNumber);
    }
    
    public ContactInfo getNewAnonymousContact() {
        ContactInfo info = new ContactInfo();
        info.picture = BitmapFactory.decodeResource(context.getResources(), anonymousPicture);
        info.name = "Anonymous" + anonymousCounter++;
        return info;
    }

}
