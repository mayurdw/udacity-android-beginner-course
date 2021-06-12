package com.example.aboutme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var myName: MyName = MyName( "Mayur Wadhwani", "" )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DataBindingUtil.setContentView( this, R.layout.activity_main )
        this.binding.myName = myName
        this.binding.nicknameDoneButton.setOnClickListener {
            this.addNickname( it )
        }
    }

    private fun addNickname( view: View){
        this.binding.apply {
            myName?.nickName = editTextTextPersonName.text.toString()
            invalidateAll()
            nicknameTextView.visibility = View.VISIBLE;

            nicknameDoneButton.visibility = View.GONE;
            editTextTextPersonName.visibility = View.GONE;

        }

        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow( view.applicationWindowToken, 0 )
    }
}