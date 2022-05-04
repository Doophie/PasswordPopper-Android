package ca.doophie.passwordpopper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import ca.doophie.passwordpopper.R

class EditCredentialDetailsAdapter : RecyclerView.Adapter<EditCredentialDetailsAdapter.ViewHolder>() {

    private var fields: MutableList<Pair<String, String>> = mutableListOf()

    fun addNewField() {
        fields += mutableListOf(Pair("", ""))
        notifyItemInserted(fields.count() - 1)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleEditText: EditText
            get() = itemView.findViewById(R.id.field_title_edit_text)

        private val valueEditText: EditText
            get() = itemView.findViewById(R.id.field_value_edit_text)

        fun setField(pair: Pair<String, String>, onUpdate: (Pair<String, String>)->Unit) {
            titleEditText.setText(pair.first)
            valueEditText.setText(pair.second)

            titleEditText.doOnTextChanged { _,_,_,_ ->
                onUpdate(Pair(titleEditText.text.toString(), valueEditText.text.toString()))
            }

            valueEditText.doOnTextChanged { _,_,_,_ ->
                onUpdate(Pair(titleEditText.text.toString(), valueEditText.text.toString()))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_edit_credential_details, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val field = fields[position]
        holder.setField(field) { updatedPair ->
            fields[position] = updatedPair
        }
    }

    override fun getItemCount(): Int {
        return fields.count()
    }

    fun getAllFields(): List<Pair<String, String>> {
        return fields
    }
}