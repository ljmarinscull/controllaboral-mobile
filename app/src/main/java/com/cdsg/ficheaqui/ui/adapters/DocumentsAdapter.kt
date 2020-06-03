package com.cdsg.ficheaqui.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cdsg.ficheaqui.data.Document

class DocumentsAdapter(private val mContext: Context, private val mDocumentList: List<Document>) : BaseAdapter(){

    private val inflater: LayoutInflater
            = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return mDocumentList.size
    }

    override fun getItem(position: Int): Document {
        return mDocumentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        val docName = getItem(position)
        val docNameTextView = rowView.findViewById(android.R.id.text1) as TextView
        docNameTextView.text = docName.name
        return rowView
    }
}