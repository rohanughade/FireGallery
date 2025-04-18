package com.rohan.firebase02storage

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.rohan.firebase02storage.databinding.ActivitySigninBinding

class signin : AppCompatActivity() {
    lateinit var binding: ActivitySigninBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initv()
        binding.btn.setOnClickListener {
            val emai = binding.email.text.toString().trim()
            val pass = binding.pass.text.toString().trim()
            if (emai.isNotEmpty()&& pass.isNotEmpty()){
                signinn(emai,pass)

            }else{
                Toast.makeText(this,"enter credentials",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun signinn(emai: String, pass: String) {
        auth.signInWithEmailAndPassword(emai,pass).addOnCompleteListener {
            if (it.isSuccessful){
                startActivity(Intent(this,MainActivity::class.java))
                finish()

            }else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }


    }


    private fun initv() {
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }
    }
}