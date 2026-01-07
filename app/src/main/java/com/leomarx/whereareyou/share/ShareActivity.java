package com.leomarx.whereareyou.share;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leomarx.whereareyou.R;

import java.io.IOException;
import java.io.InputStream;

public class ShareActivity extends AppCompatActivity {

    public Uri uriContact;
    public String contactID; // contacts unique ID
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final String TAG = ShareActivity.class.getSimpleName();
    private FloatingActionButton fab;
    private TextView nameEntry, numberEntry;
    private ImageView contactPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        fab = (FloatingActionButton) findViewById(R.id.inviteByEmail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Whera Are You");
                    String sAux = "\nHey " + nameEntry.getText() + " Let me recommend you WAY APP\n\n";
                    sAux = sAux + "Soon in play store \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    e.toString();
                }

            }
        });
        fab.hide();
    }

    public void onClickSelectContact(View btnSelectContact) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            retrieveContactPhoto();

            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String email = "";
                    try {
                        Uri result = data.getData();
                        Log.d(TAG, "Got a contact result: "
                                + result.toString());

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();

                        // query for everything email
                        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[]{id},
                                null);

                        int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                        // let's just get the first email
                        if (cursor.moveToFirst()) {
                            email = cursor.getString(emailIdx);
                            Log.d(TAG, "Got email: " + email);
                        } else {
                            Log.d(TAG, "No results");
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Failed to get email data", e);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        TextView emailEntry = (TextView) findViewById(R.id.contact_mail);
                        emailEntry.setText(email);
                        if (email.length() == 0) {
                            Toast.makeText(this, "No email found for contact.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        fab.show();
                    }
                    break;
            }
        } else {
            Log.d(TAG, "Warning: activity contact not ok");
        }
    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            nameEntry = (TextView) findViewById(R.id.contact_name);
            nameEntry.setText(contactName);
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            numberEntry = (TextView) findViewById(R.id.contact_number);
            numberEntry.setText(contactNumber);
        } else {
            Toast.makeText(this, "No Phone Number found for contact.", Toast.LENGTH_SHORT).show();
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    private void retrieveContactPhoto() {

        Bitmap photo;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                contactPicture = (ImageView) findViewById(R.id.contact_img);
                contactPicture.setImageBitmap(photo);
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Contact Photo OK");
    }
}
