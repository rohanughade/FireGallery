package com.rohan.firebase02storage

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.rohan.firebase02storage.databinding.TempBinding
import com.squareup.picasso.Picasso

class Radapter(private var list: List<String>,
   var onimageSelected:(String)->Unit): RecyclerView.Adapter<Radapter.ViewHolder>() {
    class ViewHolder(var binding: TempBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TempBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
      return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageurl = list[position]
        with(holder){


                Picasso.get().load(imageurl).into(binding.imgv)


            binding.menubtn.setOnClickListener {view->
                showPopup(view, imageurl)
            }
        }
    }
    private fun showPopup(view: View,imageurl:String){
        val pp = PopupMenu(view.context,view)
        pp.menuInflater.inflate(R.menu.menu,pp.menu)
        pp.setOnMenuItemClickListener { item:MenuItem->
            when(item.itemId){
                R.id.del->{
                    onimageSelected(imageurl)
                    true
                }
               else-> false
            }
        }
        pp.show()

    }
}