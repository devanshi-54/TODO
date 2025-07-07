package com.example.todo.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME="todo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME="user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CNO = "cno";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TODO_table = "todo";

    private static final String ID ="id";
    private static final String TASK ="task";
    private static final String STATUS ="status";

    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_table + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TASK + " TEXT, " +
            STATUS + " INTEGER) ";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "CREATE TABLE "+TABLE_NAME+"("+
                COLUMN_ID +" INTEGER PRIMARY KEY autoincrement, "+
                COLUMN_NAME + " TEXT, "+
                COLUMN_EMAIL + " TEXT, "+
                COLUMN_CNO + " TEXT, "+
                COLUMN_PASSWORD + " TEXT );";
        db.execSQL(CREATE_TODO_TABLE);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TODO_table);
        onCreate(db);
    }

    public boolean registerUser(String name, String email, String cno ,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME + " WHERE "+ COLUMN_EMAIL + " = ? ", new String[]{email});
        if(cursor.getCount() > 0){
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_CNO, cno);
        contentValues.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_NAME , null,contentValues);
        return result != -1;
    }

    public boolean loginUser(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email , password});


        boolean isSuccess = cursor.getCount() > 0;
        cursor.close();
        return isSuccess;
    }

    public void insertTask(ToDomodel task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, task.getTask());
        contentValues.put(STATUS, 0);
        db.insert(TODO_table, null, contentValues);

    }

    public List<ToDomodel> getALLTask(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<ToDomodel> tasklist = new ArrayList<>();
        Cursor cursor = null ;

        db.beginTransaction();
        try {
            cursor = db.query(TODO_table,null,null,null,null,null,null,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        ToDomodel toDoModel= new ToDomodel();
                        toDoModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                        toDoModel.setTask(cursor.getString(cursor.getColumnIndexOrThrow(TASK)));
                        toDoModel.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(STATUS)));
                        tasklist.add(toDoModel);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return tasklist;
    }

    public void openDatabase(){
        db=this.getWritableDatabase();
    }

    public void updateStatus(int id , int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, "status");
        db.update(TODO_table , contentValues , ID + "= ?" , new String[]{Integer.toString(id)});
    }


    public void updateTask(int id , String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK,task);
        db.update(TODO_table ,contentValues, ID + " = ? ", new String[]{Integer.toString(id)});
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODO_table, ID + " = ? " , new String[]{Integer.toString(id)});
    }

}
