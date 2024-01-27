package com.example.home_module.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.home_module.R
import com.example.home_module.data.bean.HomeListItemBean

class HomeListAdapter: RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {

    private val TAG = "HomeListAdapter"

    private var mHomeListData: ArrayList<HomeListItemBean>? = null

    fun setData(data: ArrayList<HomeListItemBean>) {
        mHomeListData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.home_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mHomeListData?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mHomeListData?.get(position))
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val TAG = "HomeListAdapter"

        private val mHomeListItemTitle: TextView = itemView.findViewById(R.id.home_list_item_title)
//        private val mHomeListItemAuthor: TextView = itemView.findViewById(R.id.home_list_item_author)
//        private val mHomeListItemTime: TextView = itemView.findViewById(R.id.home_list_item_time)

        fun bind(item: HomeListItemBean?) {
            Log.d(TAG, "bind item: $item")
           mHomeListItemTitle.text = item?.title
//            mHomeListItemAuthor.text = item?.author
//            mHomeListItemTime.text = item?.niceDate
        }
    }
}