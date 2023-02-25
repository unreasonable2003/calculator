package com.myApp.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    fun numberAction(view: View){
        if(view is Button){
            if(view.text == "."){
                if(canAddDecimal)
                    solution_tv.append(view.text)
                canAddDecimal = false
            }
            else{
                solution_tv.append(view.text)
            }
            canAddOperation = true
        }
    }

    fun operationAction(view: View){
        if(view is Button && canAddOperation){
            solution_tv.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View){
        solution_tv.text = ""
        result_tv.text = ""
    }

    fun backSpaceAction(view: View){
        val length = solution_tv.length()
        if(length > 0)
                solution_tv.text = solution_tv.text.subSequence(0,length-1)
    }
    fun equalsAction(view: View){
        result_tv.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperator()
        if(digitsOperators.isEmpty())   return ""

        val timeDivision = timeDivisionCalculate(digitsOperators)
        if(timeDivision.isEmpty())  return ""

        val result = addSubtractCalculate(timeDivision)

        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if(operator == '+')
                    result += nextDigit
                if(operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timeDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while(list.contains('x') || list.contains('/')){
            list = calcTimeDivision(list)

        }
        return list
    }

    private fun calcTimeDivision(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size
        for( i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex && i< restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float
                when(operator){
                    'x'->{
                        newList.add(prevDigit*nextDigit)
                        restartIndex = i+1
                    }
                    '÷'->{
                        newList.add(prevDigit*nextDigit)
                        restartIndex = i+1
                    }
                    else ->{
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i > restartIndex)
                    newList.add(passedList[i])
        }
        return newList
    }

    private fun digitsOperator(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in solution_tv.text){
            if(character.isDigit() || character == '.')
                    currentDigit += character
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if(currentDigit != "")
                list.add(currentDigit.toFloat())
        return list
    }
}