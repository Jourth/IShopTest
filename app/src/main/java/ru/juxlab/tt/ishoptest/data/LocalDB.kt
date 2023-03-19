package ru.juxlab.tt.ishoptest.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDB(context: Context) : SQLiteOpenHelper(context, "IShopDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table users (id integer primary key autoincrement," +
                "name text not null, " +
                "last_name text not null, " +
                "email text not null, " +
                "photo_image_id int not null default -1" +
                ");")

        db.execSQL("create table password_hash(id integer primary key autoincrement," +
                "login text not null, " +
                "password_hash text not null, " +
                "user_id int not null" +
                ");")
        db.execSQL("create table images (id integer primary key autoincrement, image blob not null)")
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    //User

    fun writeNewUser(cv: ContentValues) : Long {
        val crDb = this.writableDatabase
        return crDb.insert("users", null, cv)
    }

    fun updateUser(cv: ContentValues, num: Int) {
        val crDb = this.writableDatabase
        crDb.update("users", cv, "id = ?", arrayOf(num.toString()))
    }

    fun getUserData(id: Int): Cursor {
        val crDb = this.writableDatabase
        val selectionArgs = arrayOf(id.toString())
        return crDb.rawQuery("select * from users where id = ?", selectionArgs)
    }

    //Ищем только по имени (логину)
    fun findUser(name: String): Cursor {
        val crDb = this.writableDatabase
        val selectionArgs = arrayOf(name)
        return crDb.rawQuery("select * from users where name = ?", selectionArgs)
    }

    //Password
    fun writeNewUserPasswordHash(cv: ContentValues) : Long {
        val crDb = this.writableDatabase
        return crDb.insert("password_hash", null, cv)
    }

    fun findUserPasswordHash(login: String, passwordHash: String) : Cursor {
        val crDb = this.writableDatabase
        val selectionArgs = arrayOf(login, passwordHash)
        return crDb.rawQuery("select user_id from password_hash where login = ? and password_hash = ?", selectionArgs)
    }


    //Images

    fun writeNewUserPhoto(cv: ContentValues) : Long {
        val crDb = this.writableDatabase
        return crDb.insert("images", null, cv)
    }

    fun updateUserPhoto(cv: ContentValues, num: Int) {
        val crDb = this.writableDatabase
        crDb.update("images", cv, "id = ?", arrayOf(num.toString()))
    }

    fun getUserPhoto(id: Int): ByteArray {
        val crDb = this.writableDatabase
        val selectionArgs = arrayOf(id.toString())

        val crImgSize = crDb.rawQuery("select length(image) from images where id = ?", selectionArgs)
        if (crImgSize.moveToFirst()){
            val imgSize = crImgSize.getInt(0)
            val output = ByteArray(imgSize)
            var pos = 1;
            while (pos < imgSize) {
                val chunkSelectionArgs = arrayOf(pos.toString(), listOf<Int>(999999, imgSize-pos).minOrNull().toString(), id.toString())
                val crImgChunk = crDb.rawQuery("select substr(image,?,?) from images where id = ?", chunkSelectionArgs)
                crImgChunk.moveToFirst();
                val chunk = crImgChunk.getBlob(0)
                System.arraycopy(chunk, 0, output, pos-1, chunk.size)
                pos += 999999
            }
            return output
        } else return ByteArray(0)
        return ByteArray(0)
    }
}