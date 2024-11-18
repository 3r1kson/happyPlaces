package com.example.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.activities.AddHappyPlaceActivity
import com.example.happyplaces.activities.HappyPlaceDetailActivity
import com.example.happyplaces.activities.MainActivity
import com.example.happyplaces.database.DatabaseHandler
import com.example.happyplaces.databinding.ItemHappyPlaceBinding
import com.example.happyplaces.models.HappyPlaceModel


open class HappyPlacesAdapter(
    private val context: Context,
    private var list: ArrayList<HappyPlaceModel>
) : RecyclerView.Adapter<HappyPlacesAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHappyPlaceBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        holder.binding.apply {
            ivPlaceImage.setImageURI(Uri.parse(model.image))
            tvTitle.text = model.title
            tvDescription.text = model.description

        }

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener?.onClick(position, model)
            }
        }
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

    fun notifyDeleteItem(position: Int, requestCode: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])
        if (isDeleted > 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClicklistener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: HappyPlaceModel)
    }

    inner class MyViewHolder(val binding: ItemHappyPlaceBinding) : RecyclerView.ViewHolder(binding.root)
}