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
            "create table " + DATABASE_TABLE + "("+ KEY_ID + " integer primary key autoincrement, " +
                    KEY_TIMESTAMP + " text not null, " +
                    KEY_CHILOMETRI + " integer not null, " +
                    KEY_PREZZO + " real not null, " +
                    KEY_LITRI + " integer not null, " +
                    KEY_IMPORTO + " real not null, " +
                    KEY_DISTRIBUTORE + " text not null, " +
                    KEY_CITTA + " text not null, " +
                    KEY_DESCRIZIONE + " text);";
    public static final String TABLE_DROP = "drop table if exists " + DATABASE_TABLE + ";";
    public static final String VIEW_DROP = "drop view if exists distributori_preferiti";
    public static final String VIEW_CREATE = "create view distributori_preferiti as " +
            "select distinct " + KEY_DISTRIBUTORE + ", " + KEY_DESCRIZIONE  +
            ", count(" + KEY_DISTRIBUTORE + ") as visite " +
            "from " + DATABASE_TABLE +
            " group by " + KEY_DISTRIBUTORE + ", " + KEY_DESCRIZIONE +
            " order by visite;";

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

    public void checkOrInitializeDB () {
        try{
            String sql = "SELECT "+ KEY_ID +" FROM " + DATABASE_TABLE + ";";
            Cursor cursor = db.rawQuery(sql, null);
            cursor.close();
        }
        catch(Exception s){
            db.execSQL(VIEW_DROP);
            db.execSQL(TABLE_DROP);
            db.execSQL(DATABASE_CREATE);
            db.execSQL(VIEW_CREATE);
        }
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

    public Cursor getRefuel(long id) {
        String[] camps = new String[]{KEY_TIMESTAMP, KEY_CHILOMETRI, KEY_PREZZO,
                KEY_LITRI, KEY_IMPORTO, KEY_DISTRIBUTORE, KEY_CITTA, KEY_DESCRIZIONE};

        Cursor mCursor = db.query(true, DATABASE_TABLE, camps, KEY_ID + "=" + id, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public int getTotalKilometers () {

        Cursor c = db.rawQuery("SELECT MAX("+ KEY_CHILOMETRI +")-MIN("+ KEY_CHILOMETRI +") " +
                "FROM " + DATABASE_TABLE, null);
        if (c != null) {
           c.moveToFirst();
        }
        return c.getInt(0);
    }

    public int getSumLiters () {
        Cursor c = db.rawQuery("SELECT SUM(" + KEY_LITRI + ") FROM " + DATABASE_TABLE, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c.getInt(0);
    }

    public int getSumPaid () {
        Cursor c = db.rawQuery("SELECT SUM(" + KEY_IMPORTO + ") FROM " + DATABASE_TABLE, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c.getInt(0);
    }

    public double getAvgPrice () {
        Cursor c = db.rawQuery("SELECT AVG(" + KEY_PREZZO + ") FROM " + DATABASE_TABLE, null);
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

        Cursor c = db.rawQuery("SELECT " + KEY_DISTRIBUTORE + ", " + KEY_DESCRIZIONE + ", MAX(visite) " +
                "FROM distributori_preferiti;", null);

        if (c != null && c.moveToFirst()) {
                if (getLastId() != 0)
                    return c.getString(0) + ", " + c.getString(1);
                else return "Nessun rifornimento";
            }

        return "Nessun rifornimento";
    }

    public int getLastId () {
        Cursor c = db.rawQuery("SELECT MAX(" + KEY_ID + ") FROM " + DATABASE_TABLE, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c.getInt(0);
    }

    public String getLastRefuel () {

        long id = getLastId();

        Cursor c = db.rawQuery("SELECT " + KEY_TIMESTAMP +
                " FROM "+ DATABASE_TABLE +
                " where " + KEY_ID + " = " + id + ";", null);

        if (c != null && c.moveToFirst()){
            return getCorrectDataFormat(c.getString(0));
        }
        else {
            return "Nessun rifornimento";
        }
    }

    public String getCorrectDataFormat (String completeData) {

        String onlyData = completeData.substring(0, 8);
        String formattedData = onlyData.substring(0, 2) + "/" + onlyData.substring(2, 4) + "/" + onlyData.substring(4, 8);
        return formattedData;
    }

    public void deleteRefuel (int id) {
        // db.execSQL("DELETE FROM " + DATABASE_TABLE + " WHERE " + KEY_ID + " = " + id + ";");
        db.delete(DATABASE_TABLE, KEY_ID + "=" + id, null);
    }

}