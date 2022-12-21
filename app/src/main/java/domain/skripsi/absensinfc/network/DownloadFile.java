package domain.skripsi.absensinfc.network;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import domain.skripsi.absensinfc.utils.Constant;

public class DownloadFile {

    public void startDownload(Context context, String filename, String url, String token) {

        try {

            Log.e("DownloadManager", "startDownload: url : "+url );
            Log.e("DownloadManager", "startDownload: filename : "+filename );

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url+".pdf"));
            request.addRequestHeader("Authorization", "Bearer " + token);
            request.setAllowedOverMetered(true);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setTitle(filename+".pdf");
            request.setDescription("Download file....");
            request.setMimeType("pdf");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename+".pdf" );

            try {
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

                Toast.makeText(context, "Download file....", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(context, "Gagal : " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Toast.makeText(context, "Gagal : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}
