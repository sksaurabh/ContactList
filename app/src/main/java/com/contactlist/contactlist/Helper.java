package com.contactlist.contactlist;

/**
 * Created by saurabh on 5/20/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.contactlist.contactlist.bean.ContactDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;


import java.util.ArrayList;
import java.util.List;


public class Helper extends SQLiteOpenHelper {


    private static String DB_PATH = "/data/data/com.contactlist.contactlist/databases/";
    private static String DB_NAME = "Contact.sqlite";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private String TAG = "Helper";
    Cursor cursorGetData;


    public Helper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }


    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
       /* File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();*/
        try {
            String myPath = DB_PATH + DB_NAME;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
                checkDB = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error is" + e.toString());
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }


    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    public Helper openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        return null;
    }

    /**
     * Closing database after operation done
     */
    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    /**
     * getting information based on SQL Query
     *
     * @param sql
     * @return Output of Query
     */
    private Cursor getData(String sql) {
        try {
            openDataBase();
            cursorGetData = getReadableDatabase().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursorGetData;
    }


    /**
     * Inserting information based on table name and values
     *
     * @param tableName
     * @param values
     * @return
     */
    private long insertData(String tableName, ContentValues values) {
        try {
            openDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myDataBase.insert(tableName, null, values);
    }


    /**
     * Updating information based on table name and Condition
     *
     * @param tableName
     * @param values
     * @return
     */
    private int updateData(String tableName, ContentValues values, String condition) {
        try {
            openDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myDataBase.update(tableName, values, condition, null);
    }


    public void exportDatabase() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.deepshooter.grozip//databases//grozip.sqlite";
                String backupDBPath = "grozip.sqlite";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.e("db", "copied");
                } else {
                    Log.e("db", "dbnotexist");
                }
            } else {
                Log.e("db", "notcopied");
            }
        } catch (Exception e) {
            Log.e("db", "error");
        }
    }


    // Insert Into contactlist Table
    public long insertIntoContactTable(ContactDetails contactDetailses) {

        long mTotalInsertedValues = 0;

        try {

            ContactDetails pd = contactDetailses;
            ContentValues cv = new ContentValues();
            cv.put("FirstName", pd.getmFName());
            cv.put("LastName", pd.getmLName());
            cv.put("PhoneNumber", pd.getpNo());
            cv.put("NickName", pd.getnName());

            long rowId = 0;

            rowId = insertData("contactlist", cv);
            exportDatabase();


            Log.e("contactlist", "insertion contactlist working");

            if (rowId != 0) {
                mTotalInsertedValues++;
            }

            Log.e("contactlist", ""
                    + mTotalInsertedValues);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "Insertion contactlist failed");
        }
        exportDatabase();

        return mTotalInsertedValues;


    }


    //Get From contactlist Table
    public ArrayList<ContactDetails> getContactTable() {

        ArrayList<ContactDetails> arrayList = new ArrayList<>();

        String sql = "select * from contactlist";

        Cursor cursor = getData(sql);
        Log.e("chk", "chk" + cursor.getCount());
        if (cursor != null || cursor.getCount() > 0) {
            cursor.moveToFirst();
            Log.e("check", "check");
            for (int size = 0; size < cursor.getCount(); size++) {


                ContactDetails contactDetails = new ContactDetails();
                contactDetails.setId(cursor.getString(0));
                contactDetails.setmFName(cursor.getString(1));
                contactDetails.setmLName(cursor.getString(2));
                contactDetails.setpNo(cursor.getString(3));
                contactDetails.setnName(cursor.getString(4));
                arrayList.add(contactDetails);
                cursor.moveToNext();
            }
            cursor.close();
            cursorGetData.close();
            myDataBase.close();

        }

        return arrayList;
    }


    public void deleteItem(String s) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM contactlist WHERE PhoneNumber='" + s + "'");
        db.close();
    }

    public boolean isthereExists(String s) {

        boolean isexist = false;
        String sql = "select * from contactlist WHERE PhoneNumber='" + s + "'";

        Cursor cursor = getData(sql);
        Log.e("chk", "chk" + cursor.getCount());
        if (cursor != null || cursor.getCount() > 0) {
            isexist = true;

        }
        return isexist;
    }

    public ContactDetails getContact(String phone) {

        ContactDetails contactDetails = new ContactDetails();
        String sql = "select * from contactlist WHERE PhoneNumber='" + phone + "'";

        Cursor cursor = getData(sql);
        Log.e("chk", "chk" + cursor.getCount());
        if (cursor != null || cursor.getCount() > 0) {
            cursor.moveToFirst();
            Log.e("check", "check");
            contactDetails.setmFName(cursor.getString(1));
            contactDetails.setmLName(cursor.getString(2));
            contactDetails.setpNo(cursor.getString(3));
            contactDetails.setnName(cursor.getString(4));
            cursor.moveToNext();
        }
        cursor.close();
        cursorGetData.close();
        myDataBase.close();
        return contactDetails;

    }

    public void updateIntoContactTable(ContactDetails contactDetails, String id) {
        ContentValues cv = new ContentValues();
        cv.put("FirstName", contactDetails.getmFName());
        cv.put("LastName", contactDetails.getmLName());
        cv.put("PhoneNumber", contactDetails.getpNo());
        cv.put("NickName", contactDetails.getnName());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("contactlist", cv, "ID=" + id, null);
    }
}
