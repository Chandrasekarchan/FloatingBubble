package com.example.floatingbubble

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context,workerParams: WorkerParameters):Worker(context,workerParams) {
    private val contextduplicate = context
    override fun doWork(): Result {
        contextduplicate.startService(Intent(contextduplicate, FloatingControlService::class.java))
        return Result.success()
    }
}