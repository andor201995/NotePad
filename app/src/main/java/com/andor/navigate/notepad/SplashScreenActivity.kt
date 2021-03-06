package com.andor.navigate.notepad

import android.graphics.Typeface
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.andor.navigate.notepad.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreenActivity : AppCompatActivity() {
    // Splash screen timer
    private val splashTime = 500L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    private fun startNoteActivity() {
        val i = AuthActivity.intent(this@SplashScreenActivity)
        startActivity(i)
        finish()
    }

    override fun onStart() {
        super.onStart()
        val font = Typeface.createFromAsset(assets, "font/pacifico.ttf")
        splash_text.typeface = font

        val animation = AnimationUtils.loadAnimation(this, R.anim.nav_default_enter_anim)

        animation.duration = splashTime
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                startNoteActivity()
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        splash_text.startAnimation(animation)
    }
}
