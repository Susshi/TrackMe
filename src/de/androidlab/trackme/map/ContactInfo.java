package de.androidlab.trackme.map;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import de.androidlab.trackme.R;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Pair;

public class ContactInfo {
    
    public String number;
    public Bitmap picture;
    public String name;
    private Context context;
    public boolean isFriend;
    private static int anonymousCounter = 0;
    private static int anonymousPicture = R.drawable.ic_anonym;
    
    public ContactInfo(Context context, String number) {
        // Get contact name
        this.context = context;
        this.number = number;
        Pair<String, Long> contact = findContact(number);
        if (contact != null) {
            this.name = contact.first;
            picture = findPhoto(contact.second);
            if (picture == null) {
                picture = BitmapFactory.decodeResource(context.getResources(), anonymousPicture);
            }
            isFriend = false;
        } else {
            this.name = "Anonymous" + anonymousCounter++;
            picture = BitmapFactory.decodeResource(context.getResources(), anonymousPicture);
            isFriend = true;
        }
    }
    
    private Pair<String, Long> findContact(String number) {
        Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, number);
        String[] columns = new String[]{PhoneLookup.DISPLAY_NAME,
                                        PhoneLookup._ID}; 
        Cursor cursor = context.getContentResolver().query(uri, columns, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                this.name = cursor.getString(0);
                return new Pair<String, Long>(cursor.getString(0), cursor.getLong(1));
            }
        } finally {
            cursor.close();
        }
        return null;
    }
    
    private Bitmap findPhoto(long contactID) {
        Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactID);
        InputStream input = Contacts.openContactPhotoInputStream(context.getContentResolver(), contactUri);
        if (input == null) {
            return null;
        }
        return BitmapFactory.decodeStream(input);
    }

}
