package ca.doophie.passwordpopper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import ca.doophie.passwordpopper.R

class EditCredentialDetailsAdapter : RecyclerView.Adapter<EditCredentialDetailsAdapter.ViewHolder>() {

    private var fields: MutableList<Pair<String, String>> = mutableListOf()

    fun addNewField() {
        fields += mutableListOf(Pair("", ""))
        notifyItemInserted(fields.count() - 1)
    }

    fun setFields(fields: List<Pair<String, String>>) {
        this.fields = fields.toMutableList()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleEditText: EditText
            get() = itemView.findViewById(R.id.field_title_edit_text)

        private val valueEditText: EditText
            get() = itemView.findViewById(R.id.field_value_edit_text)

        private val completionSpinner: Spinner
            get() = itemView.findViewById(R.id.completion_spinner)

        private val completeValue: String
            get() = valueEditText.text.toString() + when (completionSpinner.getItemAtPosition(completionSpinner.selectedItemPosition) as String) {
                "Enter" -> "\n"
                "Tab" -> "\t"
                else -> ""
            }

        fun setField(pair: Pair<String, String>, onUpdate: (Pair<String, String>)->Unit) {
            titleEditText.setText(pair.first)
            valueEditText.setText(pair.second)

            completionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    onUpdate(Pair(titleEditText.text.toString(), completeValue))
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // do nothing
                }
            }

            titleEditText.doOnTextChanged { _,_,_,_ ->
                onUpdate(Pair(titleEditText.text.toString(), completeValue))
            }

            valueEditText.doOnTextChanged { _,_,_,_ ->
                onUpdate(Pair(titleEditText.text.toString(), completeValue))
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