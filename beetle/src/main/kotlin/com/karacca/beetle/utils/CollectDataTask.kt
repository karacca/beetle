package com.karacca.beetle.utils

/**
 * @author karacca
 * @date 17.07.2022
 */

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import java.io.File

@Suppress("DEPRECATION")
@SuppressLint("StaticFieldLeak")
internal class CollectDataTask(
    private var activity: AppCompatActivity?,
    private var listener: OnCollectDataTaskListener
) : AsyncTask<Bitmap, Void, Uri?>() {

    @Deprecated("Deprecated in Java")
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
        val bitmap = if (params.isEmpty()) null else params[0]

        bitmap?.let {
            val screenShotFile = BitmapUtils.writeBitmapToFile(bitmap, directory)
            uri = Uri.fromFile(screenShotFile)
        }

        return uri
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Uri?) {
        super.onPostExecute(result)
        listener.onDataReady(result)
    }

    interface OnCollectDataTaskListener {
        fun onDataReady(data: Uri?)
    }

    companion object {
        const val TAG = "CollectDataTask"
        private const val SCREENSHOT_DIRECTORY = "/screenshots"

        @WorkerThread
        fun getScreenshotDirectoryRoot(context: Context) =
            if (context.filesDir != null) "${context.filesDir}$SCREENSHOT_DIRECTORY" else null
    }
}
