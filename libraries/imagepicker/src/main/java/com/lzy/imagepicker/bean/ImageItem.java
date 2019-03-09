package com.lzy.imagepicker.bean;

import java.io.Serializable;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：图片信息
 * 修订历史：
 * ================================================
 */
public class ImageItem implements Serializable {


    public static final int NOT_UPLOAD = 0;
    public static final int SUCCESS = 1;
    public static final int FAIL= 2;
    public static final int UPLOADING= 3;



    public String name;       //图片的名字
    public String path;       //图片的路径
    public long size;         //图片的大小
    public int width;         //图片的宽度
    public int height;        //图片的高度
    public String mimeType;   //图片的类型
    public long addTime;      //图片的创建时间
    public String date="";
    public String location="";
    public String url; //服务器上的地址
    public int uploadStatus = NOT_UPLOAD;

    public int progress = 0;

    /** 图片的路径和创建时间相同就认为是同一张图片 */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ImageItem) {
            ImageItem item = (ImageItem) o;
            return this.path.equalsIgnoreCase(item.path) && this.addTime == item.addTime;
        }
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", width=" + width +
                ", height=" + height +
                ", mimeType='" + mimeType + '\'' +
                ", addTime=" + addTime +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", url='" + url + '\'' +
                ", uploadStatus=" + uploadStatus +
                ", progress=" + progress +
                '}';
    }
}
