package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.myapplication.MainActivity

class MenuActivity : AppCompatActivity() {
    private lateinit var menuStart: View
    private lateinit var menuNewGame: View
    private lateinit var menuRecords: View
    private lateinit var menuAbout: View
    private lateinit var menuQuit: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        menuStart = findViewById(R.id.menu_start)
        menuStart.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        menuNewGame = findViewById(R.id.menu_new_game)
        menuNewGame.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("reset", true)
            startActivity(intent)
        }

        menuQuit = findViewById(R.id.menu_quit)
        menuQuit.setOnClickListener{
            finish()
            System.out.close()
        }
    }
}