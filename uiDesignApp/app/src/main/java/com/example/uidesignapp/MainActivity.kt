package com.example.uidesignapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setListeners()
    }

    private fun setListeners() {
        val clickableViews: List<View> =
            listOf(
                findViewById(R.id.box_one),
                findViewById(R.id.box_two),
                findViewById(R.id.box_three),
                findViewById(R.id.box_four),
                findViewById(R.id.box_five),
                findViewById(R.id.base_layout))

        for( fn in clickableViews){
            fn.setOnClickListener {
                this.changeColour( it )
            }
        }
    }

    private fun changeColour( view: View){
        when( view.id ){
            R.id.box_one -> view.setBackgroundColor(Color.DKGRAY)
            R.id.box_two -> view.setBackgroundColor(Color.GRAY)

            // Boxes using Android color resources for background
            R.id.box_three -> view.setBackgroundResource(android.R.color.holo_green_light)
            R.id.box_four -> view.setBackgroundResource(android.R.color.holo_green_dark)
            R.id.box_five -> view.setBackgroundResource(android.R.color.holo_green_light)

            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }
}