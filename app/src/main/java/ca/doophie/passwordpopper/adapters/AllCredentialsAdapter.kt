package ca.doophie.passwordpopper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.doophie.passwordpopper.R
import ca.doophie.passwordpopper.data.Credential
import ca.doophie.passwordpopper.extensions.getIconURL
import com.squareup.picasso.Picasso

class AllCredentialsAdapter: RecyclerView.Adapter<AllCredentialsAdapter.ViewHolder>() {

    private var allCredentials: List<Credential> = emptyList()
    private var onSelectionCallback: ((item: Credential, send: Boolean)->Unit) = {_,_->}

    fun setCredentials(credentials: List<Credential>) {
        allCredentials = credentials
        notifyDataSetChanged()
    }

    fun onItemSelected(callback: (item: Credential, send: Boolean)->Unit) {
        onSelectionCallback = callback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView
            get() = itemView.findViewById(R.id.cred_title_text)

        private val iconImageView: ImageView
            get() = itemView.findViewById(R.id.icon_image_view)

        fun setCredential(credential: Credential) {
            titleTextView.text = credential.name
            Picasso.with(itemView.context).load(credential.url!!.getIconURL()).into(iconImageView)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_all_credentials, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setCredential(allCredentials[position])
        holder.itemView.setOnClickListener {
            onSelectionCallback(allCredentials[position], false)
        }

        holder.itemView.findViewById<View>(R.id.send_value_button).setOnClickListener {
            onSelectionCallback(allCredentials[position], true)
        }
    }

    override fun getItemCount(): Int {
        return allCredentials.count()
    }
}