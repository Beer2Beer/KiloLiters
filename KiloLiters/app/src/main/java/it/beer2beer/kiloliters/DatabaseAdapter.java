package it.beer2beer.kiloliters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;


/**
 * Created by Francesco on 21/03/2014.
 */

public class DatabaseAdapter {

    public static final String KEY_ID = "_id";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_CHILOMETRI = "chilometri";
    public static final String KEY_PREZZO = "prezzo";
    public static final String KEY_LITRI = "litri";
    public static final String KEY_IMPORTO = "importo";
    public static final String KEY_DISTRIBUTORE = "distributore";
    public static final String KEY_CITTA = "citta";
    public static final String KEY_DESCRIZIONE = "descrizione";

    public static final String TAG = "DatabaseAdapter";
    public static final String DATABASE_NAME = "rifornimenti.db";
    public static final String DATABASE_TABLE = "rifornimenti";
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table rifornimenti (_id integer primary key autoincrement, timestamp text not null, " +
                    "chilometri integer not null, prezzo real not null, litri integer not null, " +
                    "importo real not null, distributore text not null, " +
                    "citta text not null, descrizione text);";
    public static final String TABLE_DROP = "drop table rifornimenti if exists;";
    public static final String VIEW_DROP = "drop view distributori_preferiti";
    public static final String VIEW_CREATE = "create view distributori_preferiti as " +
            "select distinct distributore, descrizione, count(distributore) as visite " +
            "from rifornimenti " +
            "group by distributore, descrizione " +
            "order by visite;";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DatabaseAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper (Context context) {
            super (context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(VIEW_CREATE);
        }

        @Override
        public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL(TABLE_DROP);
            onCreate(db);
        }

    }

    public DatabaseAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public long insertRefuel(String timestamp, int chilometri,
                             double prezzo, double litri, double importo,
                             String distributore, String citta, String descrizione){

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TIMESTAMP, timestamp);
        initialValues.put(KEY_CHILOMETRI, chilometri);
        initialValues.put(KEY_PREZZO, prezzo);
        initialValues.put(KEY_LITRI, litri);
        initialValues.put(KEY_IMPORTO, importo);
        initialValues.put(KEY_DISTRIBUTORE, distributore);
        initialValues.put(KEY_CITTA, citta);
        initialValues.put(KEY_DESCRIZIONE, descrizione);

        return db.insert(DATABASE_TABLE, null, initialValues);

    }

    public boolean deleteRefuel(long id) {
        return db.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    public Cursor getAllRefuels() {
        String[] camps = new String[]{KEY_TIMESTAMP, KEY_CHILOMETRI, KEY_PREZZO,
        KEY_LITRI, KEY_IMPORTO, KEY_DISTRIBUTORE, KEY_CITTA, KEY_DESCRIZIONE};

        return db.query(DATABASE_TABLE, camps, null, null, null, null, null);
    }

    public Cursor getRefuel(long id) {
        String[] camps = new String[]{KEY_TIMESTAMP, KEY_CHILOMETRI, KEY_PREZZO,
                KEY_LITRI, KEY_IMPORTO, KEY_DISTRIBUTORE, KEY_CITTA, KEY_DESCRIZIONE};

        Cursor mCursor = db.query(true, DATABASE_TABLE, camps, KEY_ID + "=" + id, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }


    // TODO reset types
    public boolean updateRefuel(long id, String timestamp, int chilometri,
                                float prezzo, float litri, float importo,
                                String distributore, String citta, String descrizione) {
        ContentValues values = new ContentValues();
        values.put(KEY_TIMESTAMP, timestamp);
        values.put(KEY_CHILOMETRI, chilometri);
        values.put(KEY_PREZZO, prezzo);
        values.put(KEY_LITRI, litri);
        values.put(KEY_IMPORTO, importo);
        values.put(KEY_DISTRIBUTORE, distributore);
        values.put(KEY_CITTA, citta);
        values.put(KEY_DESCRIZIONE, descrizione);


        return db.update(DATABASE_TABLE, values, KEY_ID + "=" + id, null) > 0;
    }

    public int getTotalKilometers () {
        Cursor c = db.rawQuery("SELECT MAX(chilometri)-MIN(chilometri) FROM rifornimenti", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c.getInt(0);
    }

    public int getSumLiters () {
        Cursor c = db.rawQuery("SELECT SUM(litri) FROM rifornimenti", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c.getInt(0);
    }

    public int getSumPaid () {
        Cursor c = db.rawQuery("SELECT SUM(importo) FROM rifornimenti", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c.getInt(0);
    }

    public double getAvgPrice () {
        Cursor c = db.rawQuery("SELECT AVG(prezzo) FROM rifornimenti", null);
        if (c != null) {
            c.moveToFirst();
        }
        int i = (int)(c.getDouble(0)*1000);
        return i/1000d;
    }

    public double getKiloliters (int k, int l) {

        if (l == 0) return 0;

        int kl = (int) (k/l*100);
        return kl/100;
    }

    public String getMostUsedStation () {
        db.execSQL(VIEW_DROP);
        db.execSQL(VIEW_CREATE);
        Cursor c = db.rawQuery("SELECT distributore, descrizione, MAX(visite) FROM distributori_preferiti;", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c.getString(0);

    }

    public int getLastId () {
        Cursor c = db.rawQuery("SELECT MAX(_id) FROM rifornimenti", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c.getInt(0);
    }

    public String getLastRefuel () {
        long id = getLastId();
        Cursor c = getRefuel(id);
        if (c != null) {
            c.moveToFirst();
        }
        if (c.getString(0) == null)
            return "Nessun rifornimento";
        return getCorrectDataFormat(c.getString(0));
    }

    public String getCorrectDataFormat (String completeData) {
        /*Parameters: beginIndex -- the begin index, inclusive.
        endIndex -- the end index, exclusive.*/
        String onlyData = completeData.substring(0, 8);
        String formattedData = onlyData.substring(0, 2) + "/" + onlyData.substring(2, 4) + "/" + onlyData.substring(4, 8);
        return formattedData;
    }

}