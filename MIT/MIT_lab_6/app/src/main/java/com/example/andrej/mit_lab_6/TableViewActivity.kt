package com.example.andrej.mit_lab_6

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import com.example.andrej.mit_lab_6.SqlHelper.Companion.COLUMN_NAME

class TableViewActivity : AppCompatActivity() {

    val buttonReturn by lazy {
        this@TableViewActivity.findViewById(R.id.buttonReturn) as Button
    }

    val gridView by lazy {
        this@TableViewActivity.findViewById(R.id.gridView) as GridView
    }

    val sqlDB by lazy { SqlHelper(this@TableViewActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_view)

        with(gridView) {
            val data = sqlDB.getAllRecords()
            numColumns = COLUMN_NAME.size - 1
            adapter = DBTableAdapter(this@TableViewActivity, COLUMN_NAME.drop(1).toList(), data)
        }

        buttonReturn.setOnClickListener { finish() }
    }
}


class DBTableAdapter(val context: Context, val rawData: List<String>) : BaseAdapter() {

    constructor(context: Context, header: List<String>, data: List<Radio>) :
            this(context, header + (data.flatMap {
                listOf(it.singer,
                        it.trackName,
                        it.recordingTime.toString())
            }))

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?) =
            TextView(context).apply { text = getItem(position) }

    override fun getItem(position: Int) = rawData[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = rawData.size
}