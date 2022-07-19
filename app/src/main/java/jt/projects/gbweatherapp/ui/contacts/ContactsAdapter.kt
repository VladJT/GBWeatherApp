package jt.projects.gbweatherapp.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import jt.projects.gbweatherapp.R

class ContactsAdapter(private var onItemViewClickListener: OnItemViewClickListener?) :
    RecyclerView.Adapter<ContactsAdapter.HomeViewHolder>() {

    interface OnItemViewClickListener {
        fun onItemViewClick(contact: Contact)
    }

    var contactsData: List<Contact> = listOf()

    fun setContacts(data: List<Contact>) {
        contactsData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.contacts_info, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(contactsData[position])
    }

    override fun getItemCount(): Int {
        return contactsData.size
    }

    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(contact: Contact) {
            itemView.findViewById<TextView>(R.id.textViewNameContact).text = contact.name

            itemView.findViewById<MaterialButton>(R.id.buttonCallContact).setOnClickListener {
                onItemViewClickListener?.onItemViewClick(contact)
            }

        }
    }
}