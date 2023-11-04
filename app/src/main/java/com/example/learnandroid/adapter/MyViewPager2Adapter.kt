package com.example.learnandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroid.R
import org.w3c.dom.Text

class MyViewPager2Adapter: RecyclerView.Adapter<MyViewPager2Adapter.MyViewPager2ViewHolder> {

    // 初始化
    constructor() : super() {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewPager2Adapter.MyViewPager2ViewHolder {
//        TODO("Not yet implemented")
        val viewHolder = MyViewPager2ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_pager2,
                parent,
                false)
        )

        return viewHolder
    }

    override fun onBindViewHolder(
        holder: MyViewPager2Adapter.MyViewPager2ViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {

        return 2
    }

    class MyViewPager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        private lateinit var item_text: TextView
        val rLayout = itemView.findViewById<RelativeLayout>(R.id.r_container)
        val text = itemView.findViewById<TextView>(R.id.item_page2_text)

        fun bind(position: Int) {
            text.text = "hello $position"
        }
    }
}