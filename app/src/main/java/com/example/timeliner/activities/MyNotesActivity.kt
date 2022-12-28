package com.example.timeliner.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.timeliner.R
import com.example.timeliner.database.NoteDatabase
import com.example.timeliner.databinding.ActivityMyNotesBinding
import com.example.timeliner.repository.NoteRepository
import com.example.timeliner.viewModel.NoteActivityViewModel
import com.example.timeliner.viewModel.NoteActivityViewModelFactory
import java.lang.Exception

class MyNotesActivity : AppCompatActivity() {

    lateinit var noteActivityViewModel: NoteActivityViewModel
    private lateinit var binding : ActivityMyNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityMyNotesBinding.inflate(layoutInflater)

        try{
            setContentView(binding.root)
            val noteRepository = NoteRepository(NoteDatabase(this))
            val noteActivityViewModelFactory = NoteActivityViewModelFactory(noteRepository)
            noteActivityViewModel = ViewModelProvider(this,
                noteActivityViewModelFactory)[NoteActivityViewModel::class.java]
        }catch (e: Exception){
            Log.d("TAG","Error")
        }

        binding.bottomNavBar1.selectedItemId = R.id.myNotes

        binding.bottomNavBar1.setOnItemSelectedListener {
            when(it.itemId){
                R.id.myNotes -> {
                    //Toast.makeText(this,"My Notes", Toast.LENGTH_SHORT).show()
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
}