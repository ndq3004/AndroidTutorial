package android.SQLiteUtility;

import android.content.ContentValues;

public class Utility {
    public void insert(String name, String address, String type, String notes){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("address", address);
        cv.put("type", type);
        cv.put("notes", notes);

//        getWritableDatabase().insert("")
    }
}
