package com.example.floatingbubble

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListeners()
    }

    private fun initListeners() {
        findViewById<View>(R.id.btn_start)?.setOnClickListener {
            if(checkHasDrawOverlayPermissions()) {
                val periodicTask = PeriodicWorkRequest.Builder(MyWorker::class.java,15,TimeUnit.MINUTES)
                    .setConstraints(Constraints.NONE)
                    .build()
                WorkManager.getInstance(this).enqueue(periodicTask)
            }else{
                navigateDrawPermissionSetting()
            }
        }
        findViewById<View>(R.id.btn_stop)?.setOnClickListener {
            val intentStop = Intent(this, FloatingControlService::class.java)
            intentStop.action = ACTION_STOP_FOREGROUND
            startService(intentStop)
        }
    }

    private fun navigateDrawPermissionSetting() {
        val intent1 = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName"))
        startActivityForResult(intent1, REQUEST_CODE_DRAW_PREMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_DRAW_PREMISSION){
            val periodicTask = PeriodicWorkRequest.Builder(MyWorker::class.java,15,TimeUnit.MINUTES)
                .setConstraints(Constraints.NONE)
                .build()
            WorkManager.getInstance(this).enqueue(periodicTask)
        }
    }

    private fun checkAndStartService() {
        if(checkHasDrawOverlayPermissions()) {
            startService(Intent(this, FloatingControlService::class.java))
        }
    }

    private fun checkHasDrawOverlayPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        }else{
            true
        }
    }
    companion object{
        const val  ACTION_STOP_FOREGROUND = "${BuildConfig.APPLICATION_ID}.stopfloating.service"
        const val REQUEST_CODE_DRAW_PREMISSION = 2

    }


}