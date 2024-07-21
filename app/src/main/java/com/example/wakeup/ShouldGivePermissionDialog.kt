package com.example.wakeup

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment

class ShouldGivePermissionDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.app_name)
                .setMessage(getString(R.string.permission_agitation))
                .setIcon(R.drawable.brain_icon_foreground)
                .setPositiveButton(getString(R.string.go_to_settings)) { dialog, id ->
                    openAppPermissionSettings()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun openAppPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(requireContext(), intent, null)
    }
}