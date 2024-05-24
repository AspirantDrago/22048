package com.example.myapplication

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import com.example.myapplication.controls.OnSwipeTouchListener
import com.example.myapplication.model.DoubleGameModel
import com.example.myapplication.model.MoveType
import com.example.myapplication.views.GameView
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.AccessController.getContext

class MainActivity : AppCompatActivity() {
    private lateinit var gameView1: GameView
    private lateinit var gameView2: GameView
    private lateinit var gameModel: DoubleGameModel
    private lateinit var textScore: TextView
    private lateinit var textGameOver: TextView
    private lateinit var gameViewLinearLayout: LinearLayout
    private lateinit var gameSeparator: Space

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
        gameSeparator = findViewById(R.id.gameSeparator)
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
        gameViewLinearLayout.addOnLayoutChangeListener() {_, _, _, _, _, _, _, _, _ -> resizeGameViews()}
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

    private fun resizeGameViews() {
        var current_width = gameViewLinearLayout.width
        var current_height = gameViewLinearLayout.height
        if (gameViewLinearLayout.orientation == LinearLayout.VERTICAL) {
            current_height -= gameSeparator.width
            current_height /= 2
        } else if (gameViewLinearLayout.orientation == LinearLayout.HORIZONTAL) {
            current_width -= gameSeparator.height
            current_width /= 2
        }
        var current_size = Math.min(current_height, current_width) - 3
        if (current_size == gameView1.layoutParams.width)
            return
        gameView1.layoutParams.height = current_size
        gameView1.layoutParams.width = current_size
        gameView2.layoutParams.height = current_size
        gameView2.layoutParams.width = current_size
        if (gameViewLinearLayout.orientation == LinearLayout.VERTICAL) {
            val offset = (gameViewLinearLayout.height - current_size * 2 - gameSeparator.width) / 2
            (gameView1.layoutParams as MarginLayoutParams).setMargins(0, offset, 0, 0)
        } else {
            val offset = (gameViewLinearLayout.width - current_size * 2 - gameSeparator.width) / 2
            (gameView1.layoutParams as MarginLayoutParams).setMargins(offset, 0, 0, 0)
        }
        gameView1.requestLayout()
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
        resizeGameViews()
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