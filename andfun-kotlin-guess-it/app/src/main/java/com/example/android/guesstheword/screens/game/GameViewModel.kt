package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    // The current word
    private val _word = MutableLiveData<String>("")
    val word: LiveData<String> get() = _word
    // The current score
    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int> get() = _score

    private val _gameEventFinished = MutableLiveData<Boolean>(false)
    val gameEventFinished: LiveData<Boolean> get() = _gameEventFinished

    private val _currentTime = MutableLiveData<Long>(0L)
    val currentTime: LiveData<Long> get() = _currentTime

    private val _eventCorrect = MutableLiveData<Boolean>()
    val eventCorrect: LiveData<Boolean> get() = _eventCorrect
    
    val currentTimeString: LiveData<String> = Transformations.map(currentTime) {
        DateUtils.formatElapsedTime(it)
    }

    val timer: CountDownTimer

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

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



    init {
        Log.i("GameViewModel", "init")
        resetList()
        nextWord()

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished / ONE_SECOND
            }

            override fun onFinish() {
                _gameEventFinished.value = true
            }
        }.also { it.start() }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }


    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (wordList.isEmpty()) {
            resetList()
        } else {
            _word.value = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = _score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = _score.value?.plus(1)
        nextWord()
        _eventCorrect.value = true
    }

    fun onCorrectFinished(){
        _eventCorrect.value = false
    }

    fun onGameFinishComplete(){
        _gameEventFinished.value = false
    }




    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 6000L
    }
}