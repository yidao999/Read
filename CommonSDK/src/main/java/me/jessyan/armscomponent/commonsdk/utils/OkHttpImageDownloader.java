package me.jessyan.armscomponent.commonsdk.utils;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.jessyan.autosize.utils.LogUtils;
import okhttp3.Request;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/7/25
 * owspace
 */
public class OkHttpImageDownloader {
    public static void download(String url){
        final Request request = new Request.Builder().url(url).build();
        HttpUtils.client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                me.jessyan.autosize.utils.LogUtils.e(e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                FileUtil.createSdDir();
                String url = response.request().url().toString();
                int index = url.lastIndexOf("/");
                String pictureName = url.substring(index+1);
                if(FileUtil.isFileExist(pictureName)){
                    return;
                }
                LogUtils.d("pictureName="+pictureName);
                FileOutputStream fos = new FileOutputStream(FileUtil.createFile(pictureName));
                InputStream in = response.body().byteStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = in.read(buf))!=-1){
                    fos.write(buf,0,len);
                }
                fos.flush();
                in.close();
                fos.close();
            }
        });
    }
}
