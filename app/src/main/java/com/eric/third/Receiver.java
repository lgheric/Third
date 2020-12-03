package com.eric.third;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

//注册一个广播接收器，当下载完毕后会收到一个android.intent.action.DOWNLOAD_COMPLETE
//的广播,在这里取出队列里下载任务，进行安装
public class Receiver extends BroadcastReceiver {

    //这里我们用的是git提交版本的前九位作为表示
    private static final String FILE_NAME = "ABCDEFGHI";

    public void onReceive(Context context, Intent intent) {
        final DownloadManager _DownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final long _DownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        final DownloadManager.Query _Query = new DownloadManager.Query();
        _Query.setFilterById(_DownloadId);
        final Cursor _Cursor = _DownloadManager.query(_Query);
        if (_Cursor.moveToFirst()) {
            final int _Status = _Cursor.getInt(_Cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
            final String _Name = _Cursor.getString(_Cursor.getColumnIndexOrThrow("local_filename"));
            if (_Status == DownloadManager.STATUS_SUCCESSFUL && _Name.indexOf(FILE_NAME) != 0) {
                Intent _Intent = new Intent(Intent.ACTION_VIEW);
                _Intent.setDataAndType(Uri.parse(_Cursor.getString(_Cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI))), "application/vnd.android.package-archive");
                _Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(_Intent);
            }
        }
        _Cursor.close();
    }
}
