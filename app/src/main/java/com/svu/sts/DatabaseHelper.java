package com.svu.sts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sts.db";
    private static final int DATABASE_VERSION = 1;

    // SQL commands to create tables
    private static final String CREATE_TABLE_REGIONS = "CREATE TABLE Regions (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL" +
            ");";

    private static final String CREATE_TABLE_SALESPERSONS = "CREATE TABLE Salespersons (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "phone_number TEXT ," +
            "image BLOB," +
            "main_region_id INTEGER," +
            "FOREIGN KEY (main_region_id) REFERENCES Regions(id)" +
            ");";

    private static final String CREATE_TABLE_SALES = "CREATE TABLE Sales (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "salesperson_id INTEGER," +
            "date DATE," +
            "FOREIGN KEY (salesperson_id) REFERENCES Salespersons(id)" +
            ");";

    private static final String CREATE_TABLE_SALE_DETAILS = "CREATE TABLE Sale_Details (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "sale_id INTEGER," +
            "region_id INTEGER," +
            "amount REAL," +
            "FOREIGN KEY (sale_id) REFERENCES Sales(id)," +
            "FOREIGN KEY (region_id) REFERENCES Regions(id)" +
            ");";

    private static final String CREATE_TABLE_COMMISSIONS = "CREATE TABLE Commissions (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "salesperson_id INTEGER," +
            "month INTEGER," +
            "year INTEGER," +
            "amount REAL," +
            "FOREIGN KEY (salesperson_id) REFERENCES Salespersons(id)" +
            ");";

    private static final String INSERT_INIT_DATA = "INSERT INTO Regions (name)" +
            "VALUES " +
            "('Lebanon')," +
            "('Coastal')," +
            "('Northern')," +
            "('Southern')," +
            "('Eastern');";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_REGIONS);
        db.execSQL(CREATE_TABLE_SALESPERSONS);
        db.execSQL(CREATE_TABLE_SALES);
        db.execSQL(CREATE_TABLE_SALE_DETAILS);
        db.execSQL(CREATE_TABLE_COMMISSIONS);
        db.execSQL(INSERT_INIT_DATA);
    }
    public void init(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();

    }
    public ArrayList<String> getRegions() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorRegions
                = db.rawQuery("SELECT * FROM Regions" , null);

        // on below line we are creating a new array list.
        ArrayList<String> regionsNamesArrayList
                = new ArrayList<>();
        // moving our cursor to first position.
        if (cursorRegions.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                regionsNamesArrayList.add(
                        cursorRegions.getString(1));

            } while (cursorRegions.moveToNext());
            // moving our cursor to next.
        }
        cursorRegions.close();

        return regionsNamesArrayList;
    }
    public ArrayList<SalesPersonModel> getSalesPersons() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorSalesPersons
                = db.rawQuery("SELECT Salespersons.*," +
                "Regions.name " +
                "FROM Salespersons " +
                "LEFT JOIN Regions " +
                "ON Salespersons.main_region_id = Regions.id" , null);

        // on below line we are creating a new array list.
        ArrayList<SalesPersonModel> SalesPersonsArrayList
                = new ArrayList<>();
        // moving our cursor to first position.
        if (cursorSalesPersons.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                SalesPersonsArrayList.add(new SalesPersonModel(
                        cursorSalesPersons.getInt(0),
                        cursorSalesPersons.getString(1),
                        cursorSalesPersons.getString(2),
                        cursorSalesPersons.getBlob(3),
                        cursorSalesPersons.getString(5)));

            } while (cursorSalesPersons.moveToNext());
            // moving our cursor to next.
        }
        cursorSalesPersons.close();

        return SalesPersonsArrayList;
    }

    public void addSalesman(String name, int phoneNumber,int region,byte[] image) throws SQLException {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put("name", name);
        values.put("phone_number",phoneNumber);
        values.put("image", image);
        values.put("main_region_id ", region);

        // after adding all values we are passing
        // content values to our table.
        db.insert("Salespersons", null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Commissions");
        db.execSQL("DROP TABLE IF EXISTS Sale_Details");
        db.execSQL("DROP TABLE IF EXISTS Sales");
        db.execSQL("DROP TABLE IF EXISTS Salespersons");
        db.execSQL("DROP TABLE IF EXISTS Regions");
        onCreate(db);
    }
}
