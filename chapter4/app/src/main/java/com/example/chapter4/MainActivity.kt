package com.example.chapter4

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chapter4.ui.theme.Chapter4Theme

class MainActivity : ComponentActivity() , View.OnClickListener {
    val TAG:String = "chapter4"
    lateinit var  btn:Button
    lateinit var  editText:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        btn = findViewById(R.id.button)
        editText = findViewById(R.id.edit_text)
        actionBar?.hide();
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button -> {
//                AlertDialog.Builder(this).apply {
//                    setTitle("this is dialog")
//                    setMessage("some important")
//                    setPositiveButton("OK"){}
//                    setNegativeButton("Cancel"){}
//                    show()
                val inputText = editText.text.toString()
                Log.d(TAG,"CLICK BTN" + inputText)
                }


        }
    }
}


