package com.adr57.netdemo.network;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileDownloader {

    public interface DownloadListener {
        void onProgress(int progress);
        void onSuccess(File file);
        void onError(String error);
    }

    public static void downloadFile(String fileUrl, String destinationPath, DownloadListener listener) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(fileUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    listener.onError("Download failed: " + response.code());
                    return;
                }

                InputStream inputStream = null;
                FileOutputStream outputStream = null;

                try {
                    File destinationFile = new File(destinationPath);

                    inputStream = response.body().byteStream();
                    outputStream = new FileOutputStream(destinationFile);

                    byte[] buffer = new byte[4096];
                    long total = response.body().contentLength();
                    long downloaded = 0;
                    int read;

                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                        downloaded += read;

                        final int progress = (int) (downloaded * 100 / total);
                        // Update progress on UI thread
                        new Handler(Looper.getMainLooper()).post(() ->
                                listener.onProgress(progress));
                    }

                    outputStream.flush();

                    new Handler(Looper.getMainLooper()).post(() ->
                            listener.onSuccess(destinationFile));

                } catch (IOException e) {
                    listener.onError(e.getMessage());
                } finally {
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                }
            }
        });
    }
}
