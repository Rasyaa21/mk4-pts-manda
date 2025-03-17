package com.example.mk4b_pas

import DatabaseHelper
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.mk4b_pas.DataClass.User

class UserRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addUser(user: User): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NAME, user.name)
            put(DatabaseHelper.COLUMN_EMAIL, user.email)
            put(DatabaseHelper.COLUMN_PASSWORD, user.password)
        }
        return db.insert(DatabaseHelper.TABLE_USERS, null, values)
    }

    fun getUserByEmail(email: String): User? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            null,
            "${DatabaseHelper.COLUMN_EMAIL} = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    fun checkUserExists(email: String): Boolean {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COLUMN_ID),
            "${DatabaseHelper.COLUMN_EMAIL} = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun checkUserCredentials(email: String, password: String): Boolean {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COLUMN_ID),
            "${DatabaseHelper.COLUMN_EMAIL} = ? AND ${DatabaseHelper.COLUMN_PASSWORD} = ?",
            arrayOf(email, password),
            null,
            null,
            null
        )
        val valid = cursor.count > 0
        cursor.close()
        return valid
    }
}