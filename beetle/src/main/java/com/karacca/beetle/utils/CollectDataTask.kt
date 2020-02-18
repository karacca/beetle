package com.karacca.beetle.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import java.io.File

/**
 * @user: omerkaraca
 * @date: 2019-06-12
 */

@SuppressLint("StaticFieldLeak")
class CollectDataTask(private var activity: AppCompatActivity?,
                      private var listener: OnCollectDataTaskListener
): AsyncTask<Bitmap, Void, Uri?>() {

    interface OnCollectDataTaskListener {
        fun onDataReady(data: Uri?)
    }

    companion object {
        const val TAG = "CollectDataTask"
        private const val SCREENSHOT_DIRECTORY = "/screenshots"

        @WorkerThread
        fun getScreenshotDirectoryRoot(context: Context) = if (context.filesDir != null) "${context.filesDir}$SCREENSHOT_DIRECTORY" else null
    }

    override fun doInBackground(vararg params: Bitmap?): Uri? {
        if (activity == null)
            return null

        val root = getScreenshotDirectoryRoot(activity!!) ?: return null
        val directory = File(root)
        if (directory.exists()) {
            val oldFiles = directory.listFiles()
            if (oldFiles != null && oldFiles.isNotEmpty()) {
                for (oldScreenShot in oldFiles) {
                    if (!oldScreenShot.delete()) {
                        Log.e(TAG, "Could not delete old screenshot: $oldScreenShot")
                    }
                }
            }
        }

        var uri: Uri? = null
        val bitmap = if (params.isNullOrEmpty()) null else params[0]
        bitmap?.let {
            val screenShotFile = BitmapUtils.writeBitmapToFile(bitmap, directory)
            uri = Uri.fromFile(screenShotFile)
        }

        return uri
    }

    override fun onPostExecute(result: Uri?) {
        super.onPostExecute(result)
        listener.onDataReady(result)
    }
}