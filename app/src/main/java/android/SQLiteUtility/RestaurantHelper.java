package android.SQLiteUtility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.lunchlist.Restaurant;
import android.util.Log;

import java.util.ArrayList;

public class RestaurantHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lunchlist.db" ;
    private static final int SCHEMA_VERSION=1;
    public RestaurantHelper(Context context){
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT, address TEXT, type TEXT, notes TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(Restaurant restaurant){
        ContentValues cv = new ContentValues();
        cv.put("name", restaurant.getName());
        cv.put("address", restaurant.getAddress());
        cv.put("type", restaurant.getType());
        cv.put("notes", restaurant.getNotes());

        long a = getWritableDatabase().insert("restaurants", "name", cv);
        Log.i("success?", "status" + a);
        getWritableDatabase().close();
    }
    //get all restaurants
    public ArrayList<Restaurant> getAll(){
        ArrayList<Restaurant> listRestaurants = new ArrayList<>();
        String selectQuery = "select * from restaurants";
        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                Restaurant r = new Restaurant();
                r.setName(cursor.getString(1));
                r.setAddress(cursor.getString(2));
                r.setType(cursor.getString(3));
                r.setNotes(cursor.getString(4));
                listRestaurants.add(r);
            }while (cursor.moveToNext());
        }
        cursor.close();
        getReadableDatabase().close();
        return listRestaurants;
    }
}
