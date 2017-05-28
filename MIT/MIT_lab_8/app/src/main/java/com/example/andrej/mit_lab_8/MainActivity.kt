package com.example.andrej.mit_lab_8

import android.app.*
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.NotificationCompat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.andrej.mit_lab_8.SqlHelper.Companion.COLUMN_NAME
import com.example.andrej.mit_lab_8.SqlHelper.Companion.TABLE_NAME
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val editTextTitle by lazy {
        findViewById(R.id.editTextTitle) as EditText
    }

    val editTextMessage by lazy {
        findViewById(R.id.editTextMessage) as EditText
    }

    val editTextTime by lazy {
        findViewById(R.id.editTextTime) as EditText
    }

    val editTextDate by lazy {
        findViewById(R.id.editTextDate) as EditText
    }

    val buttonAddNotification by lazy {
        findViewById(R.id.buttonAddNotification) as Button
    }

    val buttonShowNotifications by lazy {
        findViewById(R.id.buttonShowNotifications) as Button
    }

    val buttonRemove by lazy {
        findViewById(R.id.buttonRemove) as Button
    }

    val calendar = Calendar.getInstance()
    val sqlDB by lazy { SqlHelper(this@MainActivity) }
    val newNotification = mutableListOf<NotificationData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PushNotification(this).execute(sqlDB.getAllRecords().toMutableList())


        buttonRemove.setOnClickListener { sqlDB.removeOldNotification() }

        buttonShowNotifications.setOnClickListener {
            Intent(this@MainActivity, TableViewActivity::class.java)
                    .let(this@MainActivity::startActivity)
        }

        buttonAddNotification.setOnClickListener {
            NotificationData(
                    title = editTextTitle.text.toString(),
                    message = editTextMessage.text.toString(),
                    date = calendar.let {
                        Date(it[Calendar.YEAR],
                                it[Calendar.MONTH],
                                it[Calendar.DAY_OF_MONTH],
                                it[Calendar.HOUR_OF_DAY],
                                it[Calendar.MINUTE]
                        )
                    }
            ).let {
                sqlDB.insert(it)
                synchronized(newNotification, { newNotification.add(it) })
            }

            Toast.makeText(this@MainActivity, "Уведомление добавлено", Toast.LENGTH_LONG)
                    .show()
        }

        editTextDate.setOnClickListener {
            DatePickerDialog(this@MainActivity,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        editTextDate.text.let {
                            calendar.set(year, month, dayOfMonth)
                            it.clear()
                            it.append("$year-$month-$dayOfMonth")
                        }
                    },
                    calendar[Calendar.YEAR],
                    calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        editTextTime.setOnClickListener {
            TimePickerDialog(this@MainActivity,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        editTextTime.text.let {
                            with(calendar) {
                                set(get(Calendar.YEAR),
                                        get(Calendar.MONTH),
                                        get(Calendar.DAY_OF_MONTH),
                                        hourOfDay, minute)
                            }
                            it.clear()
                            it.append("$hourOfDay:$minute")
                        }
                    },
                    calendar[Calendar.HOUR_OF_DAY],
                    calendar[Calendar.MINUTE],
                    true
            ).show()
        }
    }

    var notificationId = 0

    fun NotificationData.addNotification() {
        val builder = NotificationCompat.Builder(this@MainActivity).apply {
            setSmallIcon(R.drawable.kotlin)
            setContentTitle(title)
            setContentText(message)
        }

        Intent(this@MainActivity, MainActivity::class.java)
                .let {
                    TaskStackBuilder.create(this@MainActivity).apply {
                        addParentStack(MainActivity::class.java)
                        addNextIntent(it)
                        builder.setContentIntent(getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT))
                    }
                }


        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .notify(notificationId, builder.build())

        notificationId++
    }
}

class PushNotification(val main: MainActivity) : AsyncTask<MutableList<NotificationData>, Unit, Unit>() {
    override fun doInBackground(vararg params: MutableList<NotificationData>?) {
        val mutableList = params[0]!!
        while (true) {
            val currentTime = Date().let { Date(it.year, it.month, it.date, it.hours, it.minutes) }
            mutableList += synchronized(main.newNotification, { main.newNotification })

            mutableList
                    .filter { it.date == currentTime }
                    .forEach {
                        main.apply { it.addNotification() }
                        mutableList.remove(it)
                    }

            synchronized(main.newNotification, { main.newNotification.clear() })
            TimeUnit.SECONDS.sleep(2)
        }
    }
}

data class NotificationData(val id: Int = -1,
                            val title: String,
                            val message: String,
                            val date: Date)

class SqlHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 2) {

    companion object {
        const val DATABASE_NAME = "MIT_lab_8_DB"
        const val TABLE_NAME = "NotificationData"
        val COLUMN_NAME = arrayOf("_id", "title", "message", "date")
        val DEFAULT = arrayOf("", "заголовок не установлин", "сообщение не установлено", "давно")
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

    fun insert(radio: NotificationData) = with(radio) {
        writableDatabase.execSQL("INSERT INTO $TABLE_NAME" +
                "(${COLUMN_NAME[1]}, ${COLUMN_NAME[2]}, ${COLUMN_NAME[3]}) " +
                "VALUES('$title', '$message', '${date.apply { year -= 1900 }}');")
    }

    fun getAllRecords(): List<NotificationData> {
        val cursor = writableDatabase.rawQuery("SELECT ${COLUMN_NAME.joinToString(",")} FROM $TABLE_NAME;", null)
        cursor.moveToFirst()

        return (0..cursor.count - 1).map {
            with(cursor) {
                NotificationData(
                        getInt(0),
                        getString(1),
                        getString(2),
                        Date(getString(3))
                ).apply { moveToNext() }
            }
        }
    }
}

fun SqlHelper.removeOldNotification(date: Date = Date()) = writableDatabase
        .execSQL("DELETE FROM $TABLE_NAME WHERE ${COLUMN_NAME[3]} <= '$date'")