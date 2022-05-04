package ca.doophie.passwordpopper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ca.doophie.passwordpopper.R
import ca.doophie.passwordpopper.adapters.CredentialDetailsAdapter
import ca.doophie.passwordpopper.data.Credential
import ca.doophie.passwordpopper.databinding.FragmentCredentialDetailsBinding
import ca.doophie.passwordpopper.extensions.getIconURL
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

        return binding.root
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

        binding.editCredentialButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frame_layout, EditCredentialDetailsFragment.withCredential(credential))
                .addToBackStack("EditCred")
                .commit()
        }
    }
}