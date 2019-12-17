package com.nandroidex.remoteconfigappupdatedialog

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {

    private lateinit var dialogUpdate: Dialog
    private var currentVersion: String = ""
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(10)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()

        val latestVersion = remoteConfig.getDouble("latestVersion")
        val forceUpdate = remoteConfig.getBoolean("forceUpdate")
        val updateUrl = remoteConfig.getString("updateUrl")
        val releaseNotes = remoteConfig.getString("releaseNotes")
        Log.e("TAG", "latestVersion : $latestVersion")
        Log.e("TAG", "forceUpdate : $forceUpdate")
        Log.e("TAG", "updateUrl : $updateUrl")
        Log.e("TAG", "releaseNotes : $releaseNotes")


        try {
            currentVersion = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        dialogUpdate = Dialog(this@MainActivity)

        if (currentVersion.toFloat() != latestVersion.toFloat()) {
            dialogUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogUpdate.setContentView(R.layout.dialog_update)
            dialogUpdate.setCancelable(false)

            dialogUpdate.tvReleaseNotes.text = releaseNotes
            dialogUpdate.btnUpdate.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(updateUrl)
                startActivity(intent)
            }

            if (forceUpdate) {
                dialogUpdate.btnCancel.visibility = INVISIBLE
            } else {
                dialogUpdate.btnCancel.visibility = VISIBLE
            }
            dialogUpdate.btnCancel.setOnClickListener {
                dialogUpdate.dismiss()
            }
            dialogUpdate.show()
        } else {
            if (dialogUpdate.isShowing) {
                dialogUpdate.dismiss()
            }
        }
    }
}
