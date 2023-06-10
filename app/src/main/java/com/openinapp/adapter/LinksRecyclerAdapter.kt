package com.openinapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openinapp.R
import com.openinapp.datamodel.Item
import java.util.Locale

class LinksRecyclerAdapter(
    private val mList: List<Item>,
    private val helper: LinksRecyclerAdapterHelper
) : RecyclerView.Adapter<LinksRecyclerAdapter.ViewHolder>(), Filterable {

    private var filterList: List<Item> = emptyList()

    init {
        filterList = mList
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_tab, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filterList[position]
        holder.tvClickCounts.text = item.totalClicks.toString()
        holder.tvLink.text = item.webLink
        holder.tvLinkName.text = item.title
        holder.tvLinkTimeStamp.text = item.createdAt
        holder.itemView.setOnClickListener(View.OnClickListener {

        })
        holder.viewLinkCopy.setOnClickListener(View.OnClickListener {
            helper.copyContent(item.webLink)
        })
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = mList
                } else {
                    val resultList = ArrayList<Item>()
                    for (row in mList) {
                        if (row.title.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                            Log.e("title", " : $row.title")
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<Item>
                Log.e("title2", " : $filterList")
                notifyDataSetChanged()
            }

        }
    }

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivStoreLogo: ImageView = itemView.findViewById(R.id.ivStoreLogo)
        val tvLinkName: TextView = itemView.findViewById(R.id.tvLinkName)
        val tvClickCounts: TextView = itemView.findViewById(R.id.tvClickCounts)
        val tvLinkTimeStamp: TextView = itemView.findViewById(R.id.tvLinkTimeStamp)
        val tvLink: TextView = itemView.findViewById(R.id.tvLink)
        val viewLinkCopy: View = itemView.findViewById(R.id.viewLinkCopy)
    }
}

fun interface LinksRecyclerAdapterHelper {
    fun copyContent(link:String)
}