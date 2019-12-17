# Remote-Config-App-Update-Dialog

Add Firebase to your app

If you haven't already, [add Firebase to your Android project](https://firebase.google.com/docs/android/setup).

<h2>Add dependency</h2>

```
implementation 'com.google.firebase:firebase-config:19.0.4'
```

<h2>Open Remote Config in Console</h2>

![](https://i.ibb.co/YLBKxWc/image.png)


<h2>Add needed parameters and publish</h2>

![](https://i.ibb.co/SrKXS0g/image.png)


<h2>Add default values</h2>

Create `remote_config_defaults.xml` file in `res>xml` folder

add default values like

```
<?xml version="1.0" encoding="utf-8"?>
<defaultMap>
    <entry>
        <key>latestVersion</key>
        <value>1.0</value>
    </entry>
    <entry>
        <key>forceUpdate</key>
        <value>false</value>
    </entry>
    <entry>
        <key>releaseNotes</key>
        <value>- Feature 1</value>
    </entry>
    <entry>
        <key>updateUrl</key>
        <value>https://nandroidex.wordpress.com/</value>    <!--TODO : PLAY STORE URL-->
    </entry>
</defaultMap>
```

<h2>Fetch values from Firebase</h2>

```
val remoteConfig = FirebaseRemoteConfig.getInstance()
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
```

<h2>Check current app version</h2>

```
try {
    val currentVersion = packageManager.getPackageInfo(packageName, 0).versionName
} catch (e: PackageManager.NameNotFoundException) {
    e.printStackTrace()
}
```

<h2>Show dialog if version is different than app version</h2>


```
val dialogUpdate = Dialog(this@MainActivity)
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
```


# Thanks

<b>Your contributions are welcome.</b>
