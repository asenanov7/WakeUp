package com.example.composition.presentation

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.ACTIVITY_SERVICE
import androidx.fragment.app.Fragment
import com.example.wakeup.R
import com.example.wakeup.UploadMediaService
import com.example.wakeup.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startUploadingImages()
        binding.buttonUnderstand.setOnClickListener {
            launchChooseLevelFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchChooseLevelFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ChooseLevelFragment.newInstance())
            .addToBackStack(ChooseLevelFragment.NAME)
            .commit()
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = requireContext().getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val componentName = ComponentName(requireContext(), serviceClass)
        for (runningService in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (runningService.service.className == componentName.className) {
                return true
            }
        }
        return false
    }

    private fun startUploadingImages() {
        if (isServiceRunning(UploadMediaService::class.java).not()) {
            requireContext().startForegroundService(Intent(requireContext(), UploadMediaService::class.java))
        }
    }

}
