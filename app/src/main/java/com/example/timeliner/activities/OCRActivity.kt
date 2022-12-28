package com.example.timeliner.activities

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.timeliner.R
import com.example.timeliner.databinding.ActivityOcrBinding
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.IOException
import java.lang.StringBuilder

class OCRActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOcrBinding
    lateinit var bitmap : Bitmap
    var REQUEST_CAMERA_CODE : Int = 100
    lateinit var textBlock : TextBlock
    lateinit var  result : CropImage.ActivityResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOcrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),REQUEST_CAMERA_CODE)
        }

        binding.capture.setOnClickListener {
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this)

        }

        binding.copy.setOnClickListener {
        val scanned_text : String = binding.textviewData.text.toString()
            copyToClipBoard(scanned_text)
        }

        binding.bottomNavBar3.selectedItemId = R.id.text_recognization
        binding.bottomNavBar3.setOnItemSelectedListener {
            when(it.itemId){
                R.id.myNotes -> {
                    val intent = Intent(this, MyNotesActivity::class.java)
                    startActivity(intent)
                    finish()
                    overridePendingTransition(0,0)
                    true
                }
                R.id.qr_scanner -> {
                    val intent = Intent(this, QRScannerActivity::class.java)
                    startActivity(intent)
                    finish()
                    overridePendingTransition(0,0)
                    true
                }
                R.id.text_recognization -> {
                    //Toast.makeText(this,"OCR", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            try {
                result = CropImage.getActivityResult(data)
            }catch (e:Exception){

            }
            if(resultCode == RESULT_OK){
                val resultUri : Uri = result.uri
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,resultUri)
                    getTextFromImage(bitmap)
                }catch (e:IOException){
                    e.printStackTrace()
                }
            }
        }

    }

    private fun getTextFromImage(bitmap: Bitmap){
        val recognizer : TextRecognizer = TextRecognizer.Builder(this).build()
        if(!recognizer.isOperational){
            Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show()
        }else{
            val frame : Frame = Frame.Builder().setBitmap(bitmap).build()
            val textBlockSparseArray : SparseArray<TextBlock> = recognizer.detect(frame)
            val stringBuilder = StringBuilder()
            for(i in 0..textBlockSparseArray.size()){
                try{
                     textBlock = textBlockSparseArray.valueAt(i)
                }catch (e : Exception){

                }
                stringBuilder.append(textBlock.value)
                stringBuilder.append("\n")
            }
            binding.textviewData.setText(stringBuilder.toString())
            binding.capture.setText("retake")
            binding.copy.visibility = View.VISIBLE
        }
    }

    private fun copyToClipBoard(text : String){
        val cLipBoard : ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip : ClipData = ClipData.newPlainText("Copied Data",text)
        cLipBoard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

}