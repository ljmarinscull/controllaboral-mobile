package com.cdsg.ficheaqui.ui.worktime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.data.OptionType
import kotlinx.android.synthetic.main.custom_option_view.view.*

class OptionTypeAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val options: List<OptionType>):
ArrayAdapter<OptionType>(context, layoutResource, options) {



    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)

        val text = view.findViewById<TextView>(R.id.text)
        text.text = options[position].getTextToShow()

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        imageView.setBackgroundResource(options[position].getIcon())

        return view
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return createViewFromResource(position, convertView, parent)
    }
}