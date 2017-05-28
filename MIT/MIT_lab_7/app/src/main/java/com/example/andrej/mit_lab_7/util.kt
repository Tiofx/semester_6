package com.example.andrej.mit_lab_7

import android.app.Activity
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.support.v4.content.FileProvider
import android.util.Log
import android.widget.Toast
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpHead
import org.apache.http.impl.client.DefaultHttpClient
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL

object Util {
    fun getFile(context: Context, fileName: String) = getPath(context)
            .let { File(it, "$fileName.pdf") }

    fun getPath(context: Context) = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

    fun downloadFile(main: Context, uri: String, fileName: String) =
            DownloadManager.Request(Uri.parse(uri))
                    .apply {
                        setDestinationInExternalFilesDir(main, Environment.DIRECTORY_DOWNLOADS,
                                "$fileName.pdf")
                        setMimeType("application/pdf")
                        allowScanningByMediaScanner()
                        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or
                                DownloadManager.Request.NETWORK_WIFI)
                    }
                    .let {
                        (main.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager)
                                .enqueue(it)
                    }

    fun showPdf(main: Activity, fileName: String) {
        try {
            val uri = getFile(main, fileName)
                    .let {
                        FileProvider.getUriForFile(main,
                                "${main.applicationContext.packageName}.provider",
                                it)
                    }
            val intent = Intent(Intent.ACTION_VIEW)
                    .apply {
                        setDataAndType(uri, "application/pdf")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

            main.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(main, "Приложение для открытия pdf файлов отсутствует\n$e", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(main, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun httpExists(url: String): Boolean {
        val client = DefaultHttpClient()
        val request = HttpGet(url)

        val response = try {
            client.execute(request)
        } catch (e: ClientProtocolException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

        return response?.statusLine?.statusCode == HttpURLConnection.HTTP_OK
    }


    fun setPreference(context: Context, name: String, value: Boolean) = with(context) {
        this.getSharedPreferences(this.javaClass.toString(), Context.MODE_PRIVATE)
                .edit()
                .putBoolean(name, value)
                .apply()
    }


    fun getPreference(context: Context, name: String) = with(context) {
        this.getSharedPreferences(this.javaClass.toString(), Context.MODE_PRIVATE)
                .getBoolean(name, false)
    }

    fun clearPreference(c: Context) = with(c) {
        this.getSharedPreferences(this.javaClass.toString(), Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
    }
}

object TaskTwo {
    fun downloadUrl(myUrl: String, length: Int = 500): String = try {
        with(URL(myUrl).openConnection() as HttpURLConnection) {
            readTimeout = 1e4.toInt()
            connectTimeout = 1.5e4.toInt()
            requestMethod = "GET"
            doInput = true
            Log.d("DEBUG_TAG", "The response is: $responseCode")
            inputStream.use { readIt(it, length) }
        }
    } catch (e: Exception) {
        e.toString()
    }

    fun readIt(stream: InputStream, length: Int) = CharArray(length).let {
        stream.reader(charset("UTF-8")).read(it, 0, length)
        String(it)
    }

    fun httpGET(url: String): String {
        val client = DefaultHttpClient()
        val request = HttpHead(url)
//        val request = HttpGet(url)

        val response = try {
            client.execute(request)
        } catch (e: ClientProtocolException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

        Log.d("Response of GET request", response?.statusLine?.toString() ?: "Произошла ошибка")
        return response?.statusLine?.toString() ?: "Произошла ошибка"
    }


    class Requester() : Thread() {
        val h by lazy { Handler() }

        override fun run() {
            try {
                Socket("remote.servername.com", 13).use { requestSocket ->
                    h.sendMessage(Message().apply {
                        obj = InputStreamReader(requestSocket.getInputStream(), "ISO-8859-1")
                                .readText()
                        what = 0
                    })
                }
            } catch (e: Exception) {
                Log.d("sample application", "failed to read data ${e.message}")
            }
        }
    }
}