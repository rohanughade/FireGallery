package com.rohan.firebase02storage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rohan.firebase02storage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var storageRef: StorageReference
    lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initvar();
        onClickevents()

        binding.logout.setOnClickListener {
            auth.signOut()
           startActivity(Intent(this,signin::class.java))
        }

    }

    private fun onClickevents() {
        binding.upload.setOnClickListener{
            uploadImage()

        }
        binding.button2.setOnClickListener {
            startActivity(Intent(this,images::class.java))
        }
        binding.img.setOnClickListener {
            register.launch("image/*")

        }
    }

    private fun uploadImage() {
        binding.progressBar.visibility = View.VISIBLE
        imageUri?.let {
          val imageRef=  storageRef.child(System.currentTimeMillis().toString())

            imageRef.putFile(it).addOnCompleteListener {task->
                if (task.isSuccessful){
                    imageRef.downloadUrl.addOnSuccessListener { uri->
                        val map = HashMap<String,Any>()
                        map["pic"] = uri.toString()
                        firebaseFirestore.collection("images").add(map).addOnCompleteListener {ftask->
                            if (ftask.isSuccessful){
                                Toast.makeText(this,"Upload Sucessfully",Toast.LENGTH_LONG).show()

                            }else{
                                Toast.makeText(this,ftask.exception?.message,Toast.LENGTH_LONG).show()
                            }

                        }

                    }
                    binding.progressBar.visibility = View.GONE
                    binding.img.setImageResource(R.drawable.upload)

                }else{
                    Toast.makeText(this, task.exception?.message,Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                    binding.img.setImageResource(R.drawable.upload)
                }
            }
        }
    }

    private val register  = registerForActivityResult(
        ActivityResultContracts.GetContent()){
        imageUri = it
        binding.img.setImageURI(it)

    }

    private fun initvar() {
        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }
}