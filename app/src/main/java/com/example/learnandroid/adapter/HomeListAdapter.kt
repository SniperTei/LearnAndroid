package com.example.learnandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroid.R
import com.example.learnandroid.bean.HomeListItemBean

class HomeListAdapter: RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {

    private val TAG = "HomeListAdapter"

    private var mHomeListData: List<HomeListItemBean> = listOf()

    private lateinit var mContext: Context

    // 设置数据
    fun setData(data: List<HomeListItemBean>) {
        mHomeListData = data
        notifyDataSetChanged()
    }
//
//    // 构造函数
//    constructor(context: Context): super() {
//        mContext = context
//    }
//
//    // constructor
//    constructor(context: Context, homeliest: List<String>): super() {
//        mContext = context
//        mHomeListData = homeliest
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeListAdapter.ViewHolder, position: Int) {
        val itemData = mHomeListData[position]
        holder.bindData(itemData)
    }

    override fun getItemCount(): Int {
        return mHomeListData.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindData(item: HomeListItemBean) {
            val title = itemView.findViewById<TextView>(R.id.home_list_item_title)
            title.text = item.title
        }
    }
}