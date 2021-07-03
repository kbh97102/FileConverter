package com.example.fileconverter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val requestActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->

    }

    private var isAuto = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setButtonEvent()
    }

    private fun setButtonEvent() {
        buttonGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                autoButton.id -> isAuto = true
                selectMode.id -> isAuto = false
            }
        }
        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (Objects.isNull(uri)) {
                    return@registerForActivityResult
                }
                val cursor = contentResolver.query(uri!!, null, null, null)
                var name = ""

                if (cursor != null) {
                    cursor.moveToFirst()
                    val idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                    name = cursor.getString(idx)
                    cursor.close()
                }

                if (name != "") {
                    selectedFileName.text = name
                }
            }
        fileOpenButton.setOnClickListener { getContent.launch("text/*") }
    }


    fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        requestActivity.launch(intent)
    }

}