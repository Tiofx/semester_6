package com.example.andrej.mit_lab_6

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import java.util.concurrent.TimeUnit

class AsyncTaskTestActivity : AppCompatActivity() {
    val textView by lazy {
        this@AsyncTaskTestActivity.findViewById(R.id.textView) as TextView
    }

    val buttonReturn by lazy {
        this@AsyncTaskTestActivity.findViewById(R.id.buttonReturn) as Button
    }

    val buttonStart by lazy {
        this@AsyncTaskTestActivity.findViewById(R.id.buttonStart) as Button
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_task_test)

        buttonStart.setOnClickListener {
            TestAsyncTask(textView).executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR,
                    "первый", "второй", "третий")
        }

        buttonReturn.setOnClickListener { finish() }
    }
}


class TestAsyncTask(val txt: TextView) : AsyncTask<String, Int, Unit>() {
    override fun doInBackground(vararg params: String?) =
            params.forEachIndexed { index, _ ->
                publishProgress(index)
                TimeUnit.SECONDS.sleep(1)
            }

    override fun onPreExecute() {
        super.onPreExecute()
        txt.text = "Работа начинается"
        TimeUnit.SECONDS.sleep(1)
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        txt.text = "обработано ${values[0]} параметров"
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        txt.text = "Работа закончена"
    }
}