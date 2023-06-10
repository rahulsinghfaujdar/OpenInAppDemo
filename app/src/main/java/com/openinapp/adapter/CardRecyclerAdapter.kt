package com.openinapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openinapp.R

enum class CARDS {
    TODAYS_CLICKS,
    TOP_LOCATION,
    TOP_SOURCE,
    START_TIME
}

class CardRecyclerAdapter(private val mList: List<Pair<CARDS, String>>) :
    RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_topcard, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.tvCardValue.text = item.second
        if (item.first.equals(CARDS.TODAYS_CLICKS)) {
            holder.ivLogo.setImageResource(R.drawable.today_clicks)
            holder.tvCardTitle.text = "Today’s clicks"
        } else if (item.first.equals(CARDS.TOP_LOCATION)) {
            holder.ivLogo.setImageResource(R.drawable.top_location)
            holder.tvCardTitle.text = "Top Location"
        } else if (item.first.equals(CARDS.TOP_SOURCE)) {
            holder.ivLogo.setImageResource(R.drawable.top_source)
            holder.tvCardTitle.text = "Top source"
        } else if (item.first.equals(CARDS.START_TIME)) {
            holder.ivLogo.setImageResource(R.drawable.graph_time)
            holder.tvCardTitle.text = "Start Time"
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivLogo: ImageView = itemView.findViewById(R.id.ivLogo)
        val tvCardValue: TextView = itemView.findViewById(R.id.tvCardValue)
        val tvCardTitle: TextView = itemView.findViewById(R.id.tvCardTitle)
    }
}