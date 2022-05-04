package ca.doophie.passwordpopper.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ca.doophie.passwordpopper.R
import ca.doophie.passwordpopper.adapters.AllCredentialsAdapter
import ca.doophie.passwordpopper.data.Credential
import ca.doophie.passwordpopper.data.CredentialDatabase
import ca.doophie.passwordpopper.databinding.FragmentAllCredentialsBinding

class AllCredentialsFragment : Fragment() {

    private lateinit var binding: FragmentAllCredentialsBinding

    private lateinit var credentialsAdapter: AllCredentialsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllCredentialsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addCredentialFab.setOnClickListener {
            addNewCredential()
        }

        credentialsAdapter = AllCredentialsAdapter()

        credentialsAdapter.onItemSelected { item, toSend ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frame_layout, CredentialDetailsFragment.withCredential(item))
                .addToBackStack("ViewDetails")
                .commit()
        }

        binding.allCredentialRecycler.adapter = credentialsAdapter
        binding.allCredentialRecycler.layoutManager = LinearLayoutManager(requireContext())

        Thread {
            val db = Room.databaseBuilder(requireContext(), CredentialDatabase::class.java, "creds").build()

            val credentialsDao = db.credentialDao()
            val allCredentials = credentialsDao.getAll()

            Handler(Looper.getMainLooper()).post {
                credentialsAdapter.setCredentials(allCredentials)
            }
        }.start()
    }

    private fun addNewCredential() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, EditCredentialDetailsFragment())
            .addToBackStack("ViewDetails")
            .commit()
    }

}