package com.example.andrej.mit_lab_6

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.andrej.mit_lab_6.SqlHelper.Companion.COLUMN_NAME
import com.example.andrej.mit_lab_6.SqlHelper.Companion.TABLE_NAME
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    val link = "http://maximum.ru/currenttrack.aspx?station=maximum"

    val textView by lazy {
        this@MainActivity.findViewById(R.id.textViewUpdateMain) as TextView
    }

    val buttonTableShow by lazy {
        this@MainActivity.findViewById(R.id.buttonTableShow) as Button
    }

    val buttonTestActivityTask by lazy {
        this@MainActivity.findViewById(R.id.buttonTestActivityTask) as Button
    }

    val sqlDB by lazy { SqlHelper(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonTestActivityTask.setOnClickListener {
            startActivity(Intent(this@MainActivity, AsyncTaskTestActivity::class.java))
        }

        buttonTableShow.setOnClickListener {
            startActivity(Intent(this@MainActivity, TableViewActivity::class.java))
        }

        if (hasInternetConnection()) {
            Server(sqlDB, textView).execute(link)
        } else {
            Toast.makeText(this, "Нет интернета, нет радио :(", Toast.LENGTH_LONG)
                    .show()
        }
    }

}

fun forever(loop: () -> Unit) {
    while (true) loop()
}

fun hasInternetConnection() = true


class Server(val dataBase: SqlHelper,
             val textView: TextView,
             val timeout: Long = 20) :
        AsyncTask<String, Int, Unit>() {

    private val client = OkHttpClient()
    lateinit var lastRecord: Radio

    override fun onPreExecute() {
        super.onPreExecute()
        lastRecord = dataBase.getLastRecord() ?: Radio("", "")
    }

    override fun doInBackground(vararg params: String?) {
        forever {
            for (url in params) {
                if (url != null) {
                    try {
                        val currentRadio = getCurrentRadioInfo(url)

                        if (lastRecord.trackName != currentRadio.trackName) {
                            dataBase.insert(currentRadio)
                            lastRecord = currentRadio
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    delay()
                }
            }
        }
    }

    protected fun getCurrentRadioInfo(url: String) = Request.Builder()
            .url(url)
            .build()
            .let { client.newCall(it).execute() }
            .body()
            .string()
            .let { JSONObject(it).getJSONObject("Current") }
            .let { Radio(it.getString("Artist"), it.getString("Song")) }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        textView.text = "Времени прошло: ${values[0]} с\n" +
                "Период обновления $timeout c"
    }


    private fun delay() = (0..timeout - 1)
            .forEach {
                publishProgress(it.toInt())
                TimeUnit.SECONDS.sleep(1)
            }
}


data class Radio(val singer: String,
                 val trackName: String,
                 val recordingTime: Date = Date())


class SqlHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        const val DATABASE_NAME = "MIT_lab_6_DB"
        const val TABLE_NAME = "Radio"
        val COLUMN_NAME = arrayOf("_id", "singer", "trackName", "recordingTime")
        val DEFAULT = arrayOf("", "Аноним", "не известно", "давно")
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

    fun insert(radio: Radio) = with(radio) {
        writableDatabase.execSQL("INSERT INTO $TABLE_NAME" +
                "(${COLUMN_NAME[1]}, ${COLUMN_NAME[2]}, ${COLUMN_NAME[3]}) " +
                "VALUES('$singer', '$trackName', '$recordingTime');")
    }

    fun getAllRecords(): List<Radio> {
        val cursor = writableDatabase.rawQuery("SELECT ${COLUMN_NAME.drop(1).joinToString(",")} FROM $TABLE_NAME;", null)
        cursor.moveToFirst()

        return (0..cursor.count - 1).map {
            with(cursor) {
                Radio(getString(0), getString(1), Date(getString(2)))
                        .apply { moveToNext() }
            }
        }
    }
}

fun SqlHelper.getLastRecord() = writableDatabase
        .rawQuery("SELECT ${COLUMN_NAME.drop(1).joinToString(",")} " +
                "FROM $TABLE_NAME " +
                "WHERE _id = (SELECT MAX(_id) FROM $TABLE_NAME);", null)
        .let {
            if (it.moveToFirst()) Radio(it.getString(0), it.getString(1), Date(it.getString(2)))
            else null
        }
