package ca.doophie.passwordpopper.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ca.doophie.passwordpopper.R
import ca.doophie.passwordpopper.adapters.EditCredentialDetailsAdapter
import ca.doophie.passwordpopper.data.Credential
import ca.doophie.passwordpopper.data.CredentialDatabase
import ca.doophie.passwordpopper.databinding.FragmentEditCredentialDetailsBinding
import ca.doophie.passwordpopper.extensions.getIconURL
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

class EditCredentialDetailsFragment : Fragment() {

    companion object{
        fun withCredential(credential: Credential): EditCredentialDetailsFragment {
            val frag = EditCredentialDetailsFragment()

            frag.credential = credential

            return frag
        }
    }

    private lateinit var binding: FragmentEditCredentialDetailsBinding

    private var adapter = EditCredentialDetailsAdapter()

    private var credential: Credential = Credential("", "", emptyList())

    private var database: CredentialDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditCredentialDetailsBinding.inflate(inflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_credential_edit_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_button -> saveCredential()
            R.id.delete_button -> deleteCredential()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addCredFieldFab.setOnClickListener {
            adapter.addNewField()
        }

        binding.titleEditText.setText(credential.name)
        binding.urlEditText.setText(credential.url)

        binding.urlEditText.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    credential.url = binding.urlEditText.text.toString()
                    credential.url?.let { url ->
                        Picasso.with(requireContext()).load(url.getIconURL()).into(binding.iconImageViewEdit)
                    }
                }
            }
        }

        credential.url?.let { url ->
            Picasso.with(requireContext()).load(url.getIconURL()).into(binding.iconImageViewEdit)
        }

        binding.editableFieldsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.editableFieldsRecycler.adapter = adapter

        adapter.setFields(credential.fields)

        database = Room.databaseBuilder(requireContext(), CredentialDatabase::class.java, "creds").build()
    }

    private fun saveCredential() {
        Thread {
            credential.url = binding.urlEditText.text.toString()
            credential.name = binding.titleEditText.text.toString()
            credential.fields = adapter.getAllFields()

            database?.credentialDao()?.insertAll(credential)
        }.start()

        activity?.onBackPressed()
    }

    private fun deleteCredential() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Credential")
            .setMessage("Are you sure you wish to delete ${credential.name}? You will not be able to recover it.")
            .setPositiveButton("Delete") { _, _ ->
                Thread {
                    database?.credentialDao()?.delete(credential)
                }.start()

                parentFragmentManager.popBackStack("AllCreds", 0)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}