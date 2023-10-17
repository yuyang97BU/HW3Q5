package com.example.hangmangame

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.hangmangame.databinding.ActivityGameBinding

const val TAG="Test Point"
class GameActivity: AppCompatActivity() {
    private var hintString: String = ""
    private lateinit var binding:ActivityGameBinding
    private val gameViewModel: GameViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.newGameButton.setOnClickListener {
            startNewGame();
        }
        updateUI(gameViewModel.currentGameState)
        for(views in binding.lettersLayout.children){
            if(views is TextView){
                views.setOnClickListener{
                    gameViewModel.playGame(views.text[0])
                    views.visibility=View.GONE
                    gameViewModel.setLetterIsVisibility(views.id,false);
                    updateUI(gameViewModel.currentGameState)
                }
            }
        }
        binding.hintButton.setOnClickListener {
            showHint();
            updateUI(gameViewModel.currentGameState)
        }

    }

    private fun showHint() {
        when(gameViewModel.currentHintCount){

            0-> {
                when (gameViewModel.currentWordToGuess) {
                    "banana" -> {
                        hintString = "A yellow fruit with a peel."
                    }
                    "puzzle" -> {
                        hintString = "A challenging problem to solve."
                    }
                    "guitar" -> {
                        hintString = "A musical instrument with strings."
                    }
                    "forest" -> {
                        hintString = "A  dense area filled with trees and wildlife."
                    }
                    "rocket" -> {
                        hintString = "A vehicle that goes to space."
                    }
                    "wallet" -> {
                        hintString = "It holds your money and credit cards"
                    }
                    else -> {
                        hintString = gameViewModel.currentWordToGuess
                    }
                }
                Toast.makeText(this, hintString, Toast.LENGTH_SHORT).show()
                gameViewModel.currentHintCount+=1
            }

            1-> {
                if ((gameViewModel.currentTries+1) >= GameConstants.maxTries){
                    Toast.makeText(this, "Hints not available", Toast.LENGTH_SHORT).show()
                    binding.hintButton.isEnabled = false
                    gameViewModel.setHintButton(false)
                    return;
                }

                hint2DisableLetters();
            }

            2-> {
                if ((gameViewModel.currentTries+1) >= GameConstants.maxTries){
                    Toast.makeText(this, "Hints not available", Toast.LENGTH_SHORT).show()
                    binding.hintButton.isEnabled = false
                    gameViewModel.setHintButton(false)
                    return;
                }

                hint3ShowVowels();
            }
            else-> {
                Toast.makeText(this, "No more hints", Toast.LENGTH_SHORT).show()
                binding.hintButton.isEnabled = false
                gameViewModel.setHintButton(false)
            }
        }

    }

    private fun hintsUpdateViewModels() {
        gameViewModel.currentTries+=1
        gameViewModel.currentHintCount+=1
        gameViewModel.currentDrawable=GameManager().getDrawable(gameViewModel.currentTries)
        binding.imageView.setImageDrawable(ContextCompat.getDrawable(this,gameViewModel.currentDrawable))
    }

    private fun hint2DisableLetters(){
        hintsUpdateViewModels()
        val hintLetters = ArrayList<TextView>();
        for (views in binding.lettersLayout.children){
            if (views is TextView && views.visibility==0 &&!gameViewModel.currentWordToGuess.contains(views.text[0], ignoreCase = true)){
                hintLetters.add(views);
            }
        }
        for (i in 0 until hintLetters.size step 2){
            hintLetters[i].visibility=View.GONE
            gameViewModel.setLetterIsVisibility(hintLetters[i].id,false);
        }

    }
    private fun hint3ShowVowels() {

        hintsUpdateViewModels()
        val hintLetters=ArrayList<TextView>();

        val vowels=charArrayOf('A', 'E', 'I', 'O', 'U')

        for (views in binding.lettersLayout.children){
            if (views is TextView && views.visibility == 0 && gameViewModel.currentWordToGuess.contains(views.text[0], ignoreCase = true) && vowels.contains(views.text[0])){
                hintLetters.add(views)
                views.setBackgroundResource(R.color.red);
            }
        }
        updateUI(gameViewModel.currentGameState)
        Log.d("test"," ${hintLetters.size}")

        for (letter in binding.lettersLayout.children){
            if (letter is TextView){
                letter.setOnClickListener{
                    letter.visibility=View.GONE;
                    gameViewModel.playGame(letter.text[0])
                    updateUI(gameViewModel.currentGameState)

                    for (i in hintLetters){
                        i.setBackgroundResource(R.color.teal_700);
                    }
                    updateUI(gameViewModel.currentGameState)
                }
            }

        }
    }

    private fun startNewGame() {
        binding.gameLostTextView.visibility= View.GONE
        binding.gameWonTextView.visibility=View.GONE
        binding.hintButton.isEnabled=true;
        gameViewModel.setHintButton(true)
        gameViewModel.startNewGame()
        binding.lettersLayout.visibility=View.VISIBLE
        for (views in binding.lettersLayout.children){
            gameViewModel.setLetterIsVisibility(views.id,true);
            views.visibility=View.VISIBLE
            views.setBackgroundResource(R.color.red)
        }
        updateUI(gameViewModel.currentGameState)
    }

    private fun updateUI(state: Int) {
        when (state){
            0-> { //loses the game
                binding.hintButton.isEnabled = false
                binding.wordTextView.text = gameViewModel.currentWordToGuess.uppercase()
                binding.gameLostTextView.visibility = View.VISIBLE
                binding.lettersLayout.visibility = View.GONE
                binding.imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.game08))
            }
            1-> { //wins the game
                binding.hintButton.isEnabled = false
                binding.wordTextView.text = gameViewModel.currentWordToGuess.uppercase();
                binding.gameWonTextView.visibility = View.VISIBLE
                binding.lettersLayout.visibility = View.GONE
            }
            2-> showGameRunningUI(gameViewModel.currentUnderscoreWord,gameViewModel.currentLettersUsed,gameViewModel.currentDrawable,
                gameViewModel.letterVisibility());
        }
    }

    private fun showGameRunningUI(underScoreWords:String, lettersUsed: String, drawable:Int,lettersVisible:HashMap<Int,Boolean>){
        binding.wordTextView.text = underScoreWords
        binding.lettersUsedTextView.text = lettersUsed
        binding.imageView.setImageDrawable(ContextCompat.getDrawable(this,drawable))
        for (views in binding.lettersLayout.children){
            if (lettersVisible[views.id] == true){
                views.visibility=View.VISIBLE
            } else{
                views.visibility=View.GONE
            }
        }
        binding.hintButton.isEnabled = gameViewModel.getHintButton()
    }



}