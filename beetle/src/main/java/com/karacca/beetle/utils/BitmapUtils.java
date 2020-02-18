package com.karacca.beetle.utils;

/**
 * @user: omerkaraca
 * @date: 2019-06-12
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public final class BitmapUtils {
    private static final String TAG = BitmapUtils.class.getSimpleName();
    private static final String FILE_NAME_TEMPLATE = "%s_%s.jpg";
    private static final String BITMAP_PREFIX = "bitmap";
    private static final String FILE_PROVIDER_SUFFIX = ".fileprovider";

    // prevent instantiation
    private BitmapUtils() {}

    /**
     * Create a unique file name starting with the prefix.
     */
    @NonNull
    public static String createUniqueFilename(String prefix) {
        String randomId = Long.toString(System.currentTimeMillis());
        return String.format(Locale.US, FILE_NAME_TEMPLATE, prefix, randomId);
    }

    /**
     * Writes the bitmap the directory, creating the directory if it doesn't exist.
     */
    @Nullable
    @WorkerThread
    public static File writeBitmapToDirectory(@NonNull Bitmap bitmap, @NonNull File directory) {
        if (!directory.mkdirs() && !directory.exists()) {
            Log.e(TAG, "Failed to create directory for bitmap.");
            return null;
        }
        return writeBitmapToFile(bitmap, new File(directory, createUniqueFilename(BITMAP_PREFIX)));
    }

    /**
     * Writes the bitmap to disk and returns the new file.
     *
     * @param bitmap Bitmap the bitmap to write
     * @param file   the file to write to
     */
    @Nullable
    @WorkerThread
    // suppress lint check for AGP 3.2 https://issuetracker.google.com/issues/116776070
    @SuppressLint("WrongThread")
    public static File writeBitmapToFile(@NonNull Bitmap bitmap, @NonNull File file) {
        FileOutputStream fileStream = null;
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
            fileStream = new FileOutputStream(file);
            fileStream.write(byteStream.toByteArray());
            return file;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        return null;
    }

    /**
     * Saves the view as a Bitmap screenshot.
     */
    @Nullable
    public static Bitmap capture(View view) {
        if (view.getWidth() == 0 || view.getHeight() == 0) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * Get the file provider Uri, so that internal files can be temporarily shared with other apps.
     *
     * Requires AndroidManifest permission: android.support.v4.content.FileProvider
     */
    @NonNull
    public static Uri getProviderUri(@NonNull Context context, @NonNull File file) {
        String authority = context.getPackageName() + FILE_PROVIDER_SUFFIX;
        return FileProvider.getUriForFile(context, authority, file);
    }

    /**
     * Get the file provider Uri, so that internal files can be temporarily shared with other apps.
     *
     * Requires AndroidManifest permission: android.support.v4.content.FileProvider
     */
    @NonNull
    public static Uri getProviderUri(@NonNull Context context, @NonNull Uri uri) {
        File file = new File(uri.getPath());
        return getProviderUri(context, file);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}

