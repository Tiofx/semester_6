package com.example.andrej.mit_lab_7

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class PdfFileInterface : AppCompatActivity() {

    val idText by lazy {
        findViewById(R.id.idText) as EditText
    }

    val buttonDownload by lazy {
        findViewById(R.id.buttonDownload) as Button
    }

    val buttonShow by lazy {
        findViewById(R.id.buttonShow) as Button
    }

    val buttonRemove by lazy {
        findViewById(R.id.buttonRemove) as Button
    }


    val baseLink = "http://ntv.ifmo.ru/file/journal"

    val fileId
        get() = idText.text.toString()

    var buttonSwitch: Boolean
        get() = true
        set(value) {
            buttonShow.isEnabled = value
            buttonRemove.isEnabled = value
            buttonDownload.isEnabled = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_interface)

        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                buttonSwitch = true

                Toast.makeText(this@PdfFileInterface,
                        "Файл скачался",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        buttonDownload.setOnClickListener {
            if (!Util.getFile(this@PdfFileInterface, fileId).exists()) {
                if (Util.httpExists("$baseLink/$fileId.pdf")) {
                    Util.downloadFile(this@PdfFileInterface, "$baseLink/$fileId.pdf", fileId)
                    buttonSwitch = false

                    "Подождите... происходит скачивание файла"
                } else {
                    "Файл с таким id не существует"
                }
            } else {
                "Файл [$fileId] уже скачан"
            }
                    .let {
                        Toast.makeText(this@PdfFileInterface,
                                it,
                                Toast.LENGTH_SHORT
                        ).show()
                    }
        }

        buttonShow.setOnClickListener {
            if (Util.getFile(this@PdfFileInterface, fileId).exists()) {
                Util.showPdf(this@PdfFileInterface, fileId)
            } else {
                Toast.makeText(this@PdfFileInterface,
                        "Файла [$fileId] нет",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }

        buttonRemove.setOnClickListener {
            (Util.getFile(this@PdfFileInterface, fileId)
                    .takeIf { it.exists() }
                    ?.delete()
                    ?.let { if (it) "Файл удален" else "Файл не удален" }
                    ?: "Файла с id [$fileId] не существует"
                    )
                    .let {
                        Toast.makeText(this@PdfFileInterface,
                                it,
                                Toast.LENGTH_SHORT)
                                .show()
                    }
        }
    }
}
