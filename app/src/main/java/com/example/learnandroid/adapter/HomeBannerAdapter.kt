package com.example.learnandroid.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
import com.example.learnandroid.R
import com.example.learnandroid.bean.HomeBannerItemBean
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

class HomeBannerAdapter: BannerAdapter<HomeBannerItemBean, HomeBannerAdapter.HomeBannerViewHolder> {
    private val TAG = "HomeBannerAdapter"

    constructor(mDatas: MutableList<HomeBannerItemBean>?) : super(mDatas)

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): HomeBannerViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.home_banner_item, parent, false)
        return HomeBannerViewHolder(view)
    }

    override fun onBindView(holder: HomeBannerViewHolder?, data: HomeBannerItemBean?, position: Int, size: Int) {
        Log.d(TAG, "onBindView data: $data")
        holder?.bind(data)
    }

    class HomeBannerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val TAG = "HomeBannerViewHolder"

        private val mHomeBannerImage: ImageView = itemView.findViewById(R.id.home_banner_item_image)
        private val mHomeBannerTitle: TextView = itemView.findViewById(R.id.home_banner_item_title)

        fun bind(item: HomeBannerItemBean?) {
            Log.d(TAG, "bind item: $item")
            mHomeBannerTitle.text = item?.title
//            Glide.with(itemView.context).load(item?.imagePath).into(mHomeBannerImage)
        }
    }
}
