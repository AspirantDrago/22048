package com.example.myapplication

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.controls.OnSwipeTouchListener
import com.example.myapplication.model.DoubleGameModel
import com.example.myapplication.model.MoveType
import com.example.myapplication.views.GameView
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {
    private lateinit var gameView1: GameView
    private lateinit var gameView2: GameView
    private lateinit var gameModel: DoubleGameModel
    private lateinit var textScore: TextView
    private lateinit var textGameOver: TextView
    private lateinit var gameViewLinearLayout: LinearLayout

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editorPreferences: SharedPreferences.Editor

    private var areaSize = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameView1 = findViewById(R.id.gameView1)
        gameView2 = findViewById(R.id.gameView2)
        textScore = findViewById(R.id.textScore)
        gameViewLinearLayout = findViewById(R.id.gameViewLinearLayout)
        textGameOver = findViewById(R.id.textGameOver)
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        editorPreferences = sharedPreferences.edit()

        window.decorView.setOnTouchListener(object: OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeUp() {
                moveUp()
            }
            override fun onSwipeDown() {
                moveDown()
            }
            override fun onSwipeLeft() {
                moveLeft()
            }
            override fun onSwipeRight() {
                moveRight()
            }
        })
        findViewById<Button>(R.id.btnRestart).setOnClickListener() {restart()}
        restart(!intent.getBooleanExtra("reset", false))
    }

    override fun onResume() {
        super.onResume()
        checkOrientation(getResources().configuration.orientation)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        checkOrientation(newConfig.orientation)
    }

    private fun restart(savedGame: Boolean = false) {
        val gameJson = sharedPreferences.getString("saved_game", "") ?: ""
        if (savedGame && gameJson.isNotEmpty()) {
            gameModel = Json.decodeFromString<DoubleGameModel>(gameJson)
        } else {
            gameModel = DoubleGameModel(areaSize)
        }
        gameView1.model = gameModel.game1
        gameView2.model = gameModel.game2
        update()
    }

    private fun checkOrientation(orientation: Int) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            gameViewLinearLayout.orientation = LinearLayout.VERTICAL
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gameViewLinearLayout.orientation = LinearLayout.HORIZONTAL
        }
    }

    public fun update() {
        textScore.setText("Счёт: ${gameModel.score}")
        gameView1.invalidate()
        gameView2.invalidate()
        if (gameModel.isGaming) {
            textGameOver.visibility = INVISIBLE
        } else {
            textGameOver.visibility = VISIBLE
        }
        editorPreferences.putString("saved_game", Json.encodeToString(gameModel))
        editorPreferences.apply()
    }

    public fun moveUp() {
        gameModel.move(MoveType.UP)
        update()
    }

    public fun moveDown() {
        gameModel.move(MoveType.DOWN)
        update()
    }

    public fun moveLeft() {
        gameModel.move(MoveType.LEFT)
        update()
    }

    public fun moveRight() {
        gameModel.move(MoveType.RIGHT)
        update()
    }
}