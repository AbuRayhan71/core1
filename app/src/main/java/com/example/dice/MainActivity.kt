package com.example.dice
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random


private const val TAG = "MainActivity"
private const val SCORE_KEY = "score_key"
private const val DICE_KEY = "dice_key"
private const val GAME_STATE_KEY = "game_state_key"
private const val ROLL_COUNT = "roll_count"



class MainActivity : AppCompatActivity() {
    // create all the android widget variables
    private lateinit var rollingButton: Button
    private lateinit var addtionButton: Button
    private lateinit var subtractionButton: Button
    private lateinit var textViewScore: TextView
    private lateinit var textViewDice: TextView
    private lateinit var resetButton: Button
    private var score = 0
    private var diceValue = 0
    private var canRoll = true
    private var random = Random(1)
    private var rollCount = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // assign all the android widget variables by ID
        rollingButton = findViewById(R.id.rollButton)

        addtionButton = findViewById(R.id.addButton)
        subtractionButton = findViewById(R.id.subtractButton)
        textViewScore = findViewById(R.id.textViewScore)
        resetButton = findViewById(R.id.resetButton)
        textViewDice= findViewById(R.id.textViewDice)


        // Check if there's a saved state
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY, 0)
            diceValue = savedInstanceState.getInt(DICE_KEY, 0)
            canRoll = savedInstanceState.getBoolean(GAME_STATE_KEY, true)
            rollCount = savedInstanceState.getInt(ROLL_COUNT, 0)
            for(i in 1..rollCount){
                random.nextInt(1, 7)
            }


            textViewScore.text = score.toString()

            // check if the game is in the roll state or arr/sub state
            rollingButton.isEnabled = canRoll
            canAddSub(!canRoll)

            if (score > 20) {
                textViewScore.setTextColor(Color.RED)
            } else if (score == 20) {
                textViewScore.setTextColor(Color.GREEN)
                rollingButton.isEnabled =false
                canAddSub(false)
                Log.i(TAG, "gameOver") // Log the game over even
            } else {
                textViewScore.setTextColor(Color.BLACK)
            }
        }

        // roll button action
        rollingButton.setOnClickListener {
            rollDice()

            canAddSub(true)
            rollingButton.isEnabled = false
            canRoll = false

            textViewDice.text=getUnicodeForRoll(diceValue)

        }
        addtionButton.setOnClickListener {
            score += diceValue
            updateScore()
            Log.i(TAG, "numAdded") // Log add event

            textViewDice.text=" "
        }
        subtractionButton.setOnClickListener {
            score -= diceValue
            updateScore()
            Log.i(TAG, "numSubtracted") // Log subtract event

            textViewDice.text=" "
        }

        // reset button action
        resetButton.setOnClickListener {
            score = 0
            updateScore()
            Log.i(TAG, "scoreReset") // Log the reset even
           
            textViewDice.text=" "

        }

        if(diceValue==0)
        {
            rollingButton.isEnabled = true
            canAddSub(false)

        }


    }

    // update the score with the current score and disable add, sub button. then enable roll
    private fun updateScore() {
        textViewScore.text = score.toString()
        canAddSub(false)
        rollingButton.isEnabled = true
        canRoll = true

        if (score > 20) {
            textViewScore.setTextColor(Color.RED)
        } else if (score == 20) {
            textViewScore.setTextColor(Color.GREEN)
            Log.i(TAG, "gameOver") // Log the game over even
        } else if (score < 0) {
            score = 0
            textViewScore.text = score.toString()
        } else {
            textViewScore.setTextColor(Color.BLACK)
        }
    }

    // make the add, sub buttons clickable or hidden
    private fun canAddSub(b: Boolean) {
        addtionButton.isEnabled = b
        subtractionButton.isEnabled = b
    }

    private fun rollDice() {
        // random with seed: 1
        diceValue = random.nextInt(1, 7)
        rollCount ++
        // built in random function
        // diceValue = (1..6).random() // Generates a random number between 1 and 6 (inclusive)
        Log.i(TAG, "diceRolled: $diceValue") // Log the number in logcat
    }
    // save the current state when changing states
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current score to the bundle
        outState.putInt(SCORE_KEY, score)
        outState.putInt(DICE_KEY, diceValue)
        outState.putBoolean(GAME_STATE_KEY, canRoll)
        outState.putInt(ROLL_COUNT, rollCount)
    }

    fun getUnicodeForRoll(rollValue: Int): String {
        val unicodeDiceFaces = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
        return unicodeDiceFaces[rollValue - 1]
    }
}