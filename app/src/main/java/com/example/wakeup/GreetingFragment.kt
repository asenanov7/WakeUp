package com.example.wakeup

import android.Manifest.permission
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.composition.presentation.WelcomeFragment
import com.example.wakeup.databinding.GreetingFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GreetingFragment : Fragment() {

    private var _binding: GreetingFragmentBinding? = null
    private val binding: GreetingFragmentBinding
        get() = _binding ?: throw RuntimeException("GreetingFragmentBinding == null")

    private val viewModel: GreetingFragmentViewModel by viewModels()

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { binding.avatarImageView.setImageURI(it) }
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.all { it.value }) { chooseImage() }
        else { ShouldGivePermissionDialog().show(parentFragmentManager, PERMISSION_AGITATION) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = GreetingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextName.addTextChangedListener { viewModel.textNotEmpty.value = it.isNullOrEmpty().not() }
        binding.avatarImageView.setOnClickListener { requestPermissions() }
        binding.nextButton.setOnClickListener { launchWelcomeFragment() }
        lifecycleScope.launch {
            viewModel.buttonEnabled.collectLatest {
                binding.nextButton.isEnabled = it
            }
        }
    }

    private fun launchWelcomeFragment() {
        parentFragmentManager.beginTransaction().replace(R.id.main_container, WelcomeFragment()).commit()
    }

    private fun chooseImage() {
        viewModel.imagePicked.value = true
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun requestPermissions() {
        val permissions =
            if (SDK_INT >= TIRAMISU) arrayOf(permission.READ_MEDIA_IMAGES, permission.POST_NOTIFICATIONS)
            else arrayOf(permission.READ_EXTERNAL_STORAGE)
        requestPermissionsLauncher.launch(permissions)
    }

    companion object {

        const val PERMISSION_AGITATION = "PERMISSION_AGITATION"
    }

}
