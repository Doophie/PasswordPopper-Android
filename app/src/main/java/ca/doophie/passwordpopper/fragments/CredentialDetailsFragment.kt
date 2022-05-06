package ca.doophie.passwordpopper.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ca.doophie.passwordpopper.R
import ca.doophie.passwordpopper.adapters.CredentialDetailsAdapter
import ca.doophie.passwordpopper.data.Credential
import ca.doophie.passwordpopper.data.CredentialDatabase
import ca.doophie.passwordpopper.databinding.FragmentCredentialDetailsBinding
import ca.doophie.passwordpopper.extensions.getIconURL
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

class CredentialDetailsFragment: Fragment() {

    companion object {
        fun withCredential(credential: Credential): CredentialDetailsFragment {
            val frag = CredentialDetailsFragment()

            frag.credential = credential

            return frag
        }
    }

    private lateinit var credential: Credential
    private lateinit var binding: FragmentCredentialDetailsBinding
    private val adapter = CredentialDetailsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCredentialDetailsBinding.inflate(inflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_credential_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_button -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frame_layout, EditCredentialDetailsFragment.withCredential(credential))
                    .addToBackStack("EditCred")
                    .commit()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleText.text = credential.name
        binding.urlText.text = credential.url

        credential.url?.let { url ->
            Picasso.with(requireContext()).load(url.getIconURL()).into(binding.iconImagePreview)
        }

        binding.editableFieldsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.editableFieldsRecycler.adapter = adapter

        adapter.setCredential(credential)
    }
}