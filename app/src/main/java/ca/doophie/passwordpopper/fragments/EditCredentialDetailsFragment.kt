package ca.doophie.passwordpopper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ca.doophie.passwordpopper.R
import ca.doophie.passwordpopper.adapters.EditCredentialDetailsAdapter
import ca.doophie.passwordpopper.data.Credential
import ca.doophie.passwordpopper.data.CredentialDatabase
import ca.doophie.passwordpopper.databinding.FragmentEditCredentialDetailsBinding

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addCredFieldFab.setOnClickListener {
            adapter.addNewField()
        }

        binding.editableFieldsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.editableFieldsRecycler.adapter = adapter

        binding.saveButton.setOnClickListener {
            saveCredential()
        }

        binding.deleteButton.setOnClickListener {
            deleteCredential()
        }

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
        Thread {
            database?.credentialDao()?.delete(credential)
        }.start()

        activity?.onBackPressed()
    }
}