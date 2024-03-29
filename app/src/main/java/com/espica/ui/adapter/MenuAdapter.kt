package com.espica.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.espica.R
import com.espica.data.model.MenuItem
import com.espica.ui.home.OnMenuItemClickListener

class MenuAdapter(context: Context, internal var data: List<MenuItem>,val listener:OnMenuItemClickListener) :
    ArrayAdapter<MenuItem>(context, R.layout.item_menu, data) {

    internal class ViewHolder(val view: View) {
        var title: TextView
        var icon: ImageView? = null

        init {
            title = view.findViewById(R.id.title)
            icon = view.findViewById(R.id.icon)

        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        var viewHolder: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false)
            viewHolder = ViewHolder(convertView!!)
            convertView.tag = viewHolder
        }
        viewHolder = convertView.tag as ViewHolder
        val menuItem = data[position]


        viewHolder.title.text = context.getString(menuItem.title)
        viewHolder.icon!!.setImageResource(menuItem.icon)
        viewHolder.view.setOnClickListener {
            listener.OnMenuItemClick(menuItem)
        }
        return convertView

    }
}
