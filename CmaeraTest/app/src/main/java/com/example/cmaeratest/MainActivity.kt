package com.example.cmaeratest

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {
    val takePhoto = 1
    val fromalbum = 2
    lateinit var imageUrl:Uri
    lateinit var outputFile: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_take = findViewById<Button>(R.id.takePhotoBtn)
        btn_take.setOnClickListener {
            //创建File对象，用于存储照片
            outputFile = File(externalCacheDir,"output_image.jpg")
            if (outputFile.exists())
                outputFile.delete()
            imageUrl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                FileProvider.getUriForFile(this,"com.example.cmaeratest.fileprovider",outputFile)
                else
                    Uri.fromFile(outputFile)
                //启动相机程序
               val intent = Intent("android.media.action.IMAGE_CAPTURE")
               intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUrl)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent,takePhoto)
            }
        val btn_from = findViewById<Button>(R.id.fromAlbum)
        btn_from.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type ="image/*"
            startActivityForResult(intent,fromalbum)
        }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            takePhoto -> if(resultCode == Activity.RESULT_OK) {
                //显示图片
                val bitmp = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUrl))
                val imageview = findViewById<ImageView>(R.id.imageView)
                imageview.setImageBitmap(bitmp)
            }
             fromalbum -> {
                 if (resultCode == Activity.RESULT_OK && data != null) {
                     data.data?.let {    uri -> val bitmap  = getBitmap(uri)
                         val imageview = findViewById<ImageView>(R.id.imageView)
                         imageview.setImageBitmap(bitmap)}
                 }
                }

        }
    }
    private fun getBitmap(uri:Uri) = contentResolver.openFileDescriptor(uri,"r").use { BitmapFactory.decodeFileDescriptor(it?.fileDescriptor) }
    }
