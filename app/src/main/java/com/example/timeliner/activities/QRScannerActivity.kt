package com.example.timeliner.activities

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.timeliner.R
import com.example.timeliner.databinding.ActivityQrScannerBinding
import com.google.zxing.integration.android.IntentIntegrator

class QRScannerActivity : AppCompatActivity(){

    private lateinit var binding : ActivityQrScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.qrscanBtn.setOnClickListener {
            val scanner = IntentIntegrator(this)
            /*scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)*/
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }

        binding.qrCopy.setOnClickListener {
            val qr_scanned_text : String = binding.qrScanText.text.toString()
            copyToClipBoard(qr_scanned_text)
        }

        binding.bottomNavBar2.selectedItemId = R.id.qr_scanner
        binding.bottomNavBar2.setOnItemSelectedListener {
            when(it.itemId){
                R.id.myNotes -> {
                    val intent = Intent(this, MyNotesActivity::class.java)
                    startActivity(intent)
                    finish()
                    overridePendingTransition(0,0)
                    true
                }
                R.id.qr_scanner -> {
                    //Toast.makeText(this,"Qr scanner", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.text_recognization -> {
                    val intent = Intent(this, OCRActivity::class.java)
                    startActivity(intent)
                    finish()
                    overridePendingTransition(0,0)
                    true
                }
                else -> false
            }
        }

    }

    //QR scanner
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
            if(result != null){
                if(result.contents == null){
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                }else{

                    binding.qrScanText.setText(result.contents)
                    binding.qrscanBtn.setText("Rescan")
                    binding.qrCopy.visibility = View.VISIBLE
                    //customDialog(result)

                    Toast.makeText(this, "Scanned:" + result.contents, Toast.LENGTH_SHORT).show()
                }
            }else{
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    //Copied to clipboard
    private fun copyToClipBoard(text : String){
        val cLipBoard : ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip : ClipData = ClipData.newPlainText("Copied Data",text)
        cLipBoard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    //Custom Dialog Box
    /*private fun customDialog(result : IntentResult) {
        val dialogBinding = layoutInflater.inflate(R.layout.my_custom_dialog,null)
        val myDialog = Dialog(this)
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val alert_message = dialogBinding.findViewById<TextView>(R.id.alert_message)
        alert_message.setText(result.contents)

        val alert_ok = dialogBinding.findViewById<Button>(R.id.alert_ok)
        alert_ok.setOnClickListener {
            myDialog.dismiss()
        }

    }
*/
}