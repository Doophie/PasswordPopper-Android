package ca.doophie.passwordpopper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.doophie.passwordpopper.R
import ca.doophie.passwordpopper.data.Credential
import ca.doophie.passwordpopper.databinding.ListItemCredentialDetailsBinding
import org.w3c.dom.Text

class CredentialDetailsAdapter : RecyclerView.Adapter<CredentialDetailsAdapter.ViewHolder>() {

    private var fields: List<Pair<String, String>> = emptyList()

    fun setCredential(credential: Credential) {
        fields = credential.fields
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleText: TextView
            get() = itemView.findViewById(R.id.field_title_text)

        private val valueText: TextView
            get() = itemView.findViewById(R.id.field_value_text)

        private val completionIconView: ImageView
            get() = itemView.findViewById(R.id.completion_icon)

        fun setItem(pair: Pair<String, String>) {
            val valueParts = pair.second.split("\\")

            titleText.text = pair.first
            valueText.text = valueParts[0]

            when {
                valueParts.count() == 1 -> completionIconView.setImageDrawable(null)
                valueParts[1] == "n" -> completionIconView.setImageResource(R.drawable.ic_baseline_keyboard_return_24)
                valueParts[2] == "t" -> completionIconView.setImageResource(R.drawable.ic_baseline_keyboard_tab_24)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_credential_details, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItem(fields[position])
    }

    override fun getItemCount(): Int {
        return fields.count()
    }
}