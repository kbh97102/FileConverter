package com.example.fileconverter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    private val requestActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){activityResult ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testButton.setOnClickListener {
            openFile()
        }
    }

    fun openFile(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        requestActivity.launch(intent)
    }

}