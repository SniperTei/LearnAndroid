package com.example.learnandroid.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroid.R
import com.example.learnandroid.bean.MyABean

class MyAAdapter: RecyclerView.Adapter<MyAAdapter.MyAViewHolder> {

    private var data: List<MyABean>

    private var context: Context

    constructor(context: Context, data: List<MyABean>) : super() {
        this.data = data
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_a, parent, false)
        return MyAViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: MyAViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
    
    inner class MyAViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: MyABean) {
            itemView.findViewById<TextView>(R.id.item_my_a_name_tv).text = item.name
            itemView.findViewById<TextView>(R.id.item_my_a_age_tv).text = item.age.toString()
        }

        // 点击事件
        init {
            itemView.setOnClickListener {
//                Log.i("Sniper", "item clicked")
                mOnItemClickListener.onItemClick(it, adapterPosition)
            }
        }
    }

    // 点击事件接口
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
    
    private lateinit var mOnItemClickListener: OnItemClickListener

    public fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mOnItemClickListener = listener
    }
}

