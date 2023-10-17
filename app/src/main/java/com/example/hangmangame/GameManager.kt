package com.example.hangmangame

class GameManager {

    fun getDrawable(tries:Int):Int{
        return when(tries){
            0->R.drawable.game01
            1->R.drawable.game02
            2->R.drawable.game03
            3->R.drawable.game04
            4->R.drawable.game05
            5->R.drawable.game06
            6->R.drawable.game07
            7->R.drawable.game08
            else -> {R.drawable.game08}
        }
    }

    fun generateUnderScore(wordsize: Int):String{
        val sb = StringBuilder()
        for (index in 0 until wordsize){
            sb.append("-");
        }
        return sb.toString();
    }

}