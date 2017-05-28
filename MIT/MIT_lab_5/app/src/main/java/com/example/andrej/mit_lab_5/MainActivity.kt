package com.example.andrej.mit_lab_5

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.example.andrej.mit_lab_5.SqlHelper.Companion.COLUMN_NAME
import com.example.andrej.mit_lab_5.SqlHelper.Companion.DEFAULT
import com.example.andrej.mit_lab_5.SqlHelper.Companion.TABLE_NAME

class MainActivity : AppCompatActivity() {

    val buttonTableShow by lazy {
        this@MainActivity.findViewById(R.id.buttonTableShow) as Button
    }

    val buttonAddRecord by lazy {
        this@MainActivity.findViewById(R.id.buttonAddRecord) as Button
    }

    val buttonChangeLastRecord by lazy {
        this@MainActivity.findViewById(R.id.buttonChangeLastRecord) as Button
    }

    val txtFirstName by lazy {
        this@MainActivity.findViewById(R.id.txtFirstName) as EditText
    }

    val txtEmail by lazy {
        this@MainActivity.findViewById(R.id.txtEmail) as EditText
    }

    val txtPhoneNumber by lazy {
        this@MainActivity.findViewById(R.id.txtPhoneNumber) as EditText
    }

    val sqlDB by lazy { SqlHelper(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonTableShow.setOnClickListener {
            startActivity(Intent(applicationContext, TableViewActivity::class.java))
        }

        buttonAddRecord.setOnClickListener { sqlDB.insert(getPerson()) }

        buttonChangeLastRecord.setOnClickListener { sqlDB.changeLastOnDefault() }
    }


    fun getPerson() = Person(
            txtFirstName.text.toString(),
            txtEmail.text.toString(),
            txtPhoneNumber.text.toString())
}


data class Person(val firstName: String?,
                  val email: String?,
                  val telephoneNumber: String?)

class SqlHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        const val DATABASE_NAME = "MIT_lab_5_DB"
        const val TABLE_NAME = "Person"
        val COLUMN_NAME = arrayOf("_id", "firstName", "email", "telephoneNumber")
        val DEFAULT = arrayOf("", "Аноним", "не указан", "не указан")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE $TABLE_NAME(" +
                "${COLUMN_NAME[0]} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${COLUMN_NAME[1]} TEXT DEFAULT '${DEFAULT[1]}'," +
                "${COLUMN_NAME[2]} TEXT DEFAULT '${DEFAULT[2]}'," +
                "${COLUMN_NAME[3]} TEXT DEFAULT '${DEFAULT[3]}');")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db.version = newVersion
        onCreate(db)
    }

    fun insert(person: Person) = with(person) {
        writableDatabase.execSQL("INSERT INTO $TABLE_NAME" +
                "(${COLUMN_NAME[1]}, ${COLUMN_NAME[2]}, ${COLUMN_NAME[3]}) " +
                "VALUES('$firstName', '$email', '$telephoneNumber');")
    }

    fun getAllRecords() = writableDatabase
            .rawQuery("SELECT ${COLUMN_NAME.drop(1).joinToString(",")} FROM $TABLE_NAME;", null)
            .let {
                with(it) {
                    moveToFirst()
                    (0..count - 1).map {
                        Person(getString(0), getString(1), getString(2)).apply { moveToNext() }
                    }
                }
            }
}

fun SqlHelper.changeLastOnDefault() {
    writableDatabase.execSQL("UPDATE $TABLE_NAME " +
            "SET ${COLUMN_NAME[1]} = '${DEFAULT[1]}', " +
            "${COLUMN_NAME[2]} = '${DEFAULT[2]}'," +
            "${COLUMN_NAME[3]} = '${DEFAULT[3]}' " +
            "WHERE _id = (" +
            "SELECT MAX(_id) FROM $TABLE_NAME);")
}

