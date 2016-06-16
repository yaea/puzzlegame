package com.yaea.puzzlegame.model;

import android.graphics.Bitmap;

/**
 * Created by 慧客 on 2016/6/16.
 */
public class ImagePiece {
    /**
     * 图片碎片坐标
     */
    private int index;
    /**
     * 图片碎片
     */
    private Bitmap bitmap;

    public ImagePiece(){

    }

    public ImagePiece(int index, Bitmap bitmap){
        this.index = index;
        this.bitmap = bitmap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
