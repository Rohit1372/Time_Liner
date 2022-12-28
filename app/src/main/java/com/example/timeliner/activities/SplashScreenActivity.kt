package com.example.timeliner.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.airbnb.lottie.LottieAnimationView
import com.example.timeliner.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        val splashcreenTimeout = 3000
        val intent = Intent(this,MyNotesActivity::class.java)
        Handler().postDelayed({
            startActivity(intent)
            finish()
        },splashcreenTimeout.toLong())

    }
}