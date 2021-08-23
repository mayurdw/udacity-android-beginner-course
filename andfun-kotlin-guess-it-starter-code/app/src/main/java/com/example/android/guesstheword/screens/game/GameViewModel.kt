package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object{
        // These represent different important times
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 5000L
    }

    private var _time = MutableLiveData<Long>()
    private val time : LiveData<Long>
        get() = _time

    val timeString = Transformations.map( this.time) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The current word
    private var _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    private var _gameFinished = MutableLiveData<Boolean>()
    val gameFinished : LiveData<Boolean>
        get() = _gameFinished

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>
    private var timer: CountDownTimer
    private var currentTime = COUNTDOWN_TIME


    init {
        Log.d( "GameViewModel", "GameViewModel created" )
        this.resetList()
        this.nextWord()
        this._score.value = 0
        this._gameFinished.value = false
        timer = object : CountDownTimer( COUNTDOWN_TIME, ONE_SECOND ){
            override fun onTick(millisUntilFinished: Long) {
                currentTime--;
                _time.value = currentTime
            }

            override fun onFinish() {
                _gameFinished.value = true
            }
        }.start()
    }

    private fun formatTime( time : Long ): String? {
        return DateUtils.formatElapsedTime( time )
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Log.d( "GameViewModel", "GameViewModel cleared" )
    }


    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        this._score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        this._score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishedCompleted() {
        this._gameFinished.value = false
    }

}