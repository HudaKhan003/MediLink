package com.example.mad_project

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val database_name = "WELLCURA.DB"
const val database_version = 1

class Database(context: Context) : SQLiteOpenHelper(context, database_name, null, database_version) {

    private val qry1 = "create table users(username text, email text, password text)"
    private val qry2 = "create table cart(username text, product text, price real, otype text)"
    private val qry3 = "create table orderplace(username text, fullname text, address text, contact text, pincode int, date text, amount float, otype text )"
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

        sqLiteDatabase.execSQL(qry1)
        sqLiteDatabase.execSQL(qry2)
        sqLiteDatabase.execSQL(qry3)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users")
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS cart")
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS orderplace")
    }

    fun register(username: String, email: String, password: String) {
        val cv = ContentValues()
        cv.put("username", username)
        cv.put("email", email)
        cv.put("password", password)

        val db = writableDatabase
        db.insert("users", null, cv)
        db.close()
    }

    fun login(username: String, password: String): Boolean {
        val result: Boolean
        val db = readableDatabase
        val selection = "username=? and password=?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query("users", null, selection, selectionArgs, null, null, null)
        result = cursor.moveToFirst()
        cursor.close()
        db.close()

        return result
    }

    fun addCart(username: String, product: String, price: Float, otype: String) {
        val cv = ContentValues().apply {
            put("username", username)
            put("product", product)
            put("price", price)
            put("otype", otype)
        }
        val db = writableDatabase
        db.insert("cart", null, cv)
        db.close()
    }
    fun checkCart(username: String, product: String): Int {
        val str = arrayOf(username, product)
        var result = 0
        val db = readableDatabase
        val c = db.rawQuery("select * from cart where username = ? and product = ?", str)
        if (c.moveToFirst()) {
            result = 1
        }
        c.close()
        db.close()
        return result
    }
    fun removeCart(username: String, otype: String) {
        val str = arrayOf(username, otype)
        val db = writableDatabase
        db.delete("cart", "username=? and otype=?", str)
        db.close()
    }


    fun removeCartItem(username: String, otype: String = "medicine", medicine: String) {
        val str = arrayOf(username, otype, medicine)
        val db = writableDatabase
        db.delete("cart", "username=? and otype=? and product=?", str)
        db.close()
    }
    fun addOrder(username: String,fullName:String, address:String, contact:String,pin:Int,date:String, price:Float, otype:String):Boolean
    {
        val cv = ContentValues().apply {
            put("username", username)
            put("fullname", fullName)
            put("address", address)
            put("contact", contact)
            put("pincode", pin)
            put("date", date)
            put("amount", price)
            put("otype", otype)
        }
        val db = writableDatabase
        val inserted = db.insert("orderplace", null, cv)
        db.close()
        return inserted>-1
    }
    fun getCartData(username: String, otype: String): ArrayList<Pair<String, Float>> {
        val arr = ArrayList<Pair<String, Float>>()
        val db = readableDatabase
        val str = arrayOf(username, otype)
        val c = db.rawQuery("select * from cart where username = ? and otype = ?", str)

        if (c.moveToFirst()) {
            do {
                val product = c.getString(1)
                val price = c.getFloat(2)
                arr.add(Pair(product, price))
            } while (c.moveToNext())
        }
        c.close()
        db.close()
        return arr
    }
    fun getOrderData(username: String): ArrayList<String> {
        val arr = ArrayList<String>()
        val db = readableDatabase
        val str = arrayOf(username)
        val c = db.rawQuery("select * from orderplace where username = ?", str)

        if (c.moveToFirst()) {
            do {
                arr.add(
                    "${c.getString(1)} ${c.getString(2)} ${c.getString(3)} ${c.getString(4)} ${
                        c.getString(
                            5
                        )
                    } ${c.getString(6)} ${c.getString(7)}"
                )
            } while (c.moveToNext())
        }
        c.close()
        db.close()
        return arr
    }
    fun updateOrder(
        username: String,
        fullName: String,
        address: String,
        contact: String,
        pincode: Int,
        otype: String
    ): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("fullname", fullName)
            put("address", address)
            put("contact", contact)
            put("pincode", pincode)
            put("otype", otype)
        }
        val whereClause = "username = ? AND otype = ?"
        val whereArgs = arrayOf(username, otype)
        val numRowsAffected = db.update("orderplace", cv, whereClause, whereArgs)
        db.close()
        return numRowsAffected != 0
    }
    fun updatePrice(username: String, price: Float): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("amount", price)
        }
        val whereClause = "username = ?"
        val whereArgs = arrayOf(username)
        val numRowsAffected = db.update("orderplace", cv, whereClause, whereArgs)
        db.close()
        return numRowsAffected != 0
    }
}
