package com.lovelybroteam.listenexercise.controller;

import com.lovelybroteam.listenexercise.constant.AppConstant;
import com.lovelybroteam.listenexercise.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Vo Quang Hoa on 12/21/2015.
 */
public class HttpDownloadController implements AppConstant {
    public enum  DownloadFailReason{
        CANCELED,
        NOT_FOUND,
        NO_INTERNET,
        INTERRUPT
    }

    public interface IDownload{
        void onDownloadDone(String url, byte[] data);
        void onDownloadFail(DownloadFailReason reason, String message);
        void onDownloadProgress(int done, int total);
    }

    private boolean isDownloading;
    private boolean isStopped;

    private static HttpDownloadController instance;

    public synchronized static HttpDownloadController getInstance(){
        if(instance==null){
            instance = new HttpDownloadController();
        }
        return instance;
    }

    private HttpDownloadController(){}

    public void startDownload(final String url,final IDownload downloadHandler){
        new Thread(new Runnable() {
            public void run() {
                downloadFile(AppConstant.SERVER_BASE_PATH + url,downloadHandler);
            }
        }).start();
    }

    public synchronized void stopDownload(){
        if(isDownloading){
            isStopped = true;
        }
    }

    private void downloadFile(String downloadUrl, IDownload downloadHandler) {
        isDownloading = true;
        isStopped = false;
        try {
            URL url = new URL(downloadUrl);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if(isStopped){
                isStopped = false;
                downloadHandler.onDownloadFail(DownloadFailReason.CANCELED, "Download cancelled !");
                return;
            }

            int responseCode = httpConn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpConn.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int totalSize = httpConn.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    downloadHandler.onDownloadProgress(downloadedSize, totalSize);

                    if(isStopped){
                        isStopped = false;
                        downloadHandler.onDownloadFail(DownloadFailReason.CANCELED, "Download cancelled !");
                        inputStream.close();
                        byteArrayOutputStream.close();
                        return;
                    }
                }

                inputStream.close();
                byteArrayOutputStream.close();
                downloadHandler.onDownloadDone(downloadUrl, byteArrayOutputStream.toByteArray());
            }else{
                if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
                    downloadHandler.onDownloadFail(DownloadFailReason.NOT_FOUND, "Download error. Can not download this file. Code"+responseCode);
                    Utils.Log(new Exception("Can not download file " + downloadUrl));
                }else {
                    downloadHandler.onDownloadFail(DownloadFailReason.INTERRUPT, "Download error. Can not download this file. Code : "+responseCode);
                    Utils.Log(new Exception("Can not download file " + downloadUrl));
                }
            }
        } catch (MalformedURLException e) {
            downloadHandler.onDownloadFail(DownloadFailReason.INTERRUPT, "Download error. Can not download the data.");
            Utils.Log(e);
        } catch (ProtocolException e) {
            downloadHandler.onDownloadFail(DownloadFailReason.INTERRUPT, "Download error. Can not download the data.");
            Utils.Log(e);
        } catch (IOException e) {
            downloadHandler.onDownloadFail(DownloadFailReason.INTERRUPT, "Download error. Can not download the data.");
            Utils.Log(e);
        }
    }
}

