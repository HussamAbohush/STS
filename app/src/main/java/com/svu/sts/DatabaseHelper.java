package com.svu.sts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
            "date TEXT," +
            "FOREIGN KEY (salesperson_id) REFERENCES Salespersons(id)" +
            ");";

    private static final String CREATE_TABLE_SALE_DETAILS = "CREATE TABLE Sale_Details (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "sale_id INTEGER," +
            "region_id INTEGER," +
            "amount BIGINT ," +
            "FOREIGN KEY (sale_id) REFERENCES Sales(id)," +
            "FOREIGN KEY (region_id) REFERENCES Regions(id)" +
            ");";

    private static final String CREATE_TABLE_COMMISSIONS = "CREATE TABLE Commissions (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "salesperson_id INTEGER," +
            "month INTEGER," +
            "year INTEGER," +
            "amount BIGINT," +
            "FOREIGN KEY (salesperson_id) REFERENCES Salespersons(id)" +
            ");";
    private static final String CREATE_SALE_DETAILS_INDEX ="CREATE INDEX idx_sales_salesperson_date ON Sales(salesperson_id, date);";

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
        db.execSQL(CREATE_SALE_DETAILS_INDEX);
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
    public int getRegionIdByName(String regionName) {
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the query to get the regionId for a given regionName
        String query = "SELECT id FROM Regions WHERE name = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{regionName});

        int regionId = -1; // Default value in case region is not found

        // Check if the cursor contains any data
        if (cursor.moveToFirst()) {
            // Extract the regionId from the first row
            regionId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        // Return the regionId (or -1 if not found)
        return regionId;
    }

    public int getSaleId(int salespersonId, int year, int month) throws SQLException {
        // Create a variable for the SQLite database and call the readable method
        SQLiteDatabase db = this.getReadableDatabase();

        // Format the date filter as "YYYY-MM"
        String dateFilter = year + "-" +  month;

        // Define the query to retrieve the sale ID
        String query = "SELECT id FROM Sales WHERE salesperson_id = ? AND date LIKE ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(salespersonId), dateFilter + "%"});

        int saleId = -1; // Default value if no sale is found
        if (cursor.moveToFirst()) {
            try {
                saleId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            } finally {
                cursor.close();
            }
        }

        // Close the database
        db.close();

        return saleId;
    }
    public void updateSaleDetail(int saleId, int regionId, long amount) throws SQLException {
        // Create a variable for the SQLite database and call the writable method
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a variable for the ContentValues
        ContentValues values = new ContentValues();

        // Add the updated values to ContentValues
        values.put("amount", amount);

        // Define the WHERE clause and arguments
        String whereClause = "sale_id = ? AND region_id = ?";
        String[] whereArgs = {String.valueOf(saleId), String.valueOf(regionId)};

        // Execute the update on the Sale_Details table
        int rowsAffected = db.update("Sale_Details", values, whereClause, whereArgs);

        // If no rows were updated, insert a new record
        if (rowsAffected == 0) {
            values.put("sale_id", saleId);
            values.put("region_id", regionId);
            // Insert the new Sale_Detail record
            long newSaleDetailId = db.insert("Sale_Details", null, values);
            if (newSaleDetailId != -1) {
                System.out.println("New Sale Detail added with ID: " + newSaleDetailId);
            } else {
                System.out.println("Failed to add a new Sale Detail.");
            }
        } else {
            System.out.println("Rows Updated: " + rowsAffected);
        }

        // Close the database
        db.close();
    }
    public void addSaleDetail(int saleId, int regionId, long amount) throws SQLException {
        // Create a variable for the SQLite database and call the writable method
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a variable for the ContentValues
        ContentValues values = new ContentValues();


        // If checks passed, proceed with inserting the Sale Detail
        values.put("sale_id", saleId);
        values.put("region_id", regionId);
        values.put("amount", amount);

        // Insert into Sale_Details table
        long newSaleDetailId = db.insert("Sale_Details", null, values);

        // Close the cursors and database
        db.close();

        // Log or return the ID of the new Sale Detail record (cast to int)
        // You can return the newSaleDetailId if you need to know the ID
        System.out.println("New Sale Detail ID: " + newSaleDetailId);
    }

    public int addSale(int salespersonId, int year, int month) throws SQLException {
        // Create a variable for the SQLite database and call the writable method
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a variable for the ContentValues
        ContentValues values = new ContentValues();

        // Format the date as needed (e.g., "YYYY-MM")
        String date = year + "-" + month;

        // Check if a sale for this salesperson and date already exists
        String query = "SELECT * FROM Sales WHERE salesperson_id = ? AND date = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(salespersonId), date});

        if (cursor.getCount() > 0) {
            // If a record already exists, throw an error
            cursor.close();
            db.close();
            throw new SQLException("Sale record for this salesperson and date already exists.");
        }

        // If no existing record found, proceed with inserting the new record
        values.put("salesperson_id", salespersonId);
        values.put("date", date);

        // Insert the record into the Sales table and get the ID of the new record
        long newRecordId = db.insert("Sales", null, values);

        // Close the cursor and database
        cursor.close();
        db.close();

        // Return the ID of the newly inserted record (cast to int)
        return (int) newRecordId;
    }

    public List<SaleDetailModel> getSaleDetailsBySaleDate(int year, int month, int salespersonId) throws SQLException {
        // Create a variable for the SQLite database and call the readable method
        SQLiteDatabase db = this.getReadableDatabase();

        // Format the date filter as "YYYY-MM"
        String dateFilter = year + "-" +  month;

        // Define the query to retrieve sale details for the specified year, month, and salesperson
        String query = "SELECT sd.id, sd.sale_id, sd.region_id, sd.amount " +
                "FROM Sale_Details sd " +
                "JOIN Sales s ON sd.sale_id = s.id " +
                "WHERE s.salesperson_id = ? AND s.date LIKE ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(salespersonId), dateFilter + "%"});

        // Create a list to hold SaleDetail objects
        List<SaleDetailModel> saleDetails = new ArrayList<>();

        // Iterate over the cursor and extract data
        if (cursor.moveToFirst()) {
            try {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    int regionId = cursor.getInt(cursor.getColumnIndexOrThrow("region_id"));
                    long amount = cursor.getLong(cursor.getColumnIndexOrThrow("amount"));

                    // Add the SaleDetail object to the list
                    saleDetails.add(new SaleDetailModel(id, regionId, amount));
                } while (cursor.moveToNext());
            } finally {
                // Close the cursor in a try-finally block
                cursor.close();
            }
        }

        // Close the database
        db.close();

        // Return the list of sale details
        return saleDetails;
    }



    //region Salesperson
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
    public void addSalesperson(String name, String phoneNumber, int region, byte[] image) throws SQLException {

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

    public void updateSalesperson(int id, String name, String phoneNumber, int region, byte[] image) throws SQLException {

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Create ContentValues to store new values
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone_number", phoneNumber);
        values.put("image", image);
        values.put("main_region_id", region);

        // Execute update query
        int rowsAffected = db.update("Salespersons", values, "id = ?", new String[]{String.valueOf(id)});

        // Check if update was successful
        if (rowsAffected == 0) {
            throw new SQLException("No salesperson found with ID: " + id);
        }

        // Close database
        db.close();
    }
    public void deleteSalesperson(int id) throws SQLException {

        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Execute delete query
        int rowsAffected = db.delete("Salespersons", "id = ?", new String[]{String.valueOf(id)});

        // Check if delete was successful
        if (rowsAffected == 0) {
            throw new SQLException("No salesperson found with ID: " + id);
        }

        // Close database
        db.close();
    }
    //endregion

    public void addOrUpdateCommission(int salespersonId, int month, int year, long amount) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the commission already exists for the given salesperson, month, and year
        String query = "SELECT * FROM Commissions WHERE salesperson_id = ? AND month = ? AND year = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(salespersonId), String.valueOf(month), String.valueOf(year)});

        if (cursor.moveToFirst()) {
            // If a record exists, update the commission amount
            ContentValues values = new ContentValues();
            values.put("amount", amount);

            // Define the where clause for updating the existing commission
            String whereClause = "salesperson_id = ? AND month = ? AND year = ?";
            String[] whereArgs = {String.valueOf(salespersonId), String.valueOf(month), String.valueOf(year)};

            // Execute the update
            db.update("Commissions", values, whereClause, whereArgs);
            System.out.println("Commission updated for salesperson: " + salespersonId + ", month: " + month + ", year: " + year);
        } else {
            // If no record exists, insert a new commission record
            ContentValues values = new ContentValues();
            values.put("salesperson_id", salespersonId);
            values.put("month", month);
            values.put("year", year);
            values.put("amount", amount);

            // Insert the new commission record
            db.insert("Commissions", null, values);
            System.out.println("Commission added for salesperson: " + salespersonId + ", month: " + month + ", year: " + year);
        }

        // Close the cursor and the database
        cursor.close();
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
