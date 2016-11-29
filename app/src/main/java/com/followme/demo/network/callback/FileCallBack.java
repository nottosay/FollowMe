package com.followme.demo.network.callback;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by wally.yan on 2015/11/8.
 */
public abstract class FileCallBack extends Callback<File> {
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    /**
     * @param destFileDir  文件夹路径
     * @param destFileName 文件名
     */
    public FileCallBack(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }


    @Override
    public File parseResponse(ResponseBody body) throws Exception {
        return saveFile(body);
    }


    public File saveFile(ResponseBody body) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = body.byteStream();
            final long total = body.contentLength();
            long sum = 0;

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);

            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();

            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                long[] longs = new long[]{sum, total};
                Flowable.just(longs).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<long[]>() {
                    @Override
                    public void accept(long[] longs) throws Exception {
                        onProgress(longs[0], longs[1]);
                    }
                });
            }
            fos.flush();

            return file;

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }

        }
    }


}
