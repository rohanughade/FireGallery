package com.rohan.firebase02storage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rohan.firebase02storage.databinding.ActivityImagesBinding

class images : AppCompatActivity() {

    lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityImagesBinding
    private  var mlist: MutableList<String> = mutableListOf()
    lateinit var adapter:Radapter
    private var imageurl:String? =null
    lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initvar()





    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getImag() {
       firestore.collection("images").get().addOnSuccessListener {
           mlist.clear()
           for (i in it){
               mlist.add(i.data["pic"].toString())
           }
           adapter.notifyDataSetChanged()
       }
    }

    private fun initvar() {
        firestore= FirebaseFirestore.getInstance()
        binding.recyclerView.layoutManager=StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        binding.recyclerView.setHasFixedSize(true)
        adapter = Radapter(mlist){selectedurl->
             deleteimg(selectedurl)
        }
        binding.recyclerView.adapter = adapter
        getImag()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.del->{
                imageurl?.let {
                    deleteimg(it)
                }?:Toast.makeText(this,"no image selected,",Toast.LENGTH_LONG).show()
                true
            }else->super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteimg(imgeurll: String) {
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imgeurll)
        storageReference.delete().addOnSuccessListener {
            firestore.collection("images").whereEqualTo("pic",imgeurll)
                .get().addOnSuccessListener { doc->
                    for (doc in doc){
                        firestore.collection("images").document(doc.id).delete()
                    }
                    mlist.remove(imgeurll)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this,"image is deleted",Toast.LENGTH_LONG).show()
                }.addOnFailureListener{
                    Toast.makeText(this,"failed to delete from firestore",Toast.LENGTH_LONG).show()
                }
        }.addOnFailureListener{
            Toast.makeText(this,"failed to delete image",Toast.LENGTH_LONG).show()
        }


    }
}