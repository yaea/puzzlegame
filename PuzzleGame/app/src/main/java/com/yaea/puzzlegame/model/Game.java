package com.yaea.puzzlegame.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yaea.puzzlegame.R;

import java.util.ArrayList;

/**
 * Created by 慧客 on 2016/6/16.
 */
public class Game {
    /**
     * 游戏关卡
     */
    private int level;
    /**
     * 列数
     */
    private int column;
    /**
     * 游戏图片
     */
    private Bitmap imageBitmap;
    /**
     * 图片碎片集合
     */
    private ArrayList<ImagePiece> listImagePiece;

    public Game(int level, int colume, Bitmap bitmap){
        this.level = level;
        this.column = colume;
        this.imageBitmap = bitmap;
        splitImage();
    }

    /**
     * 分解图片为碎片
     */
    public void splitImage(){
        this.listImagePiece = new ArrayList<>();
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();
        int pieceWidth = Math.min(width, height) / column;
        for(int i = 0; i < column; i++){
            for(int j = 0; j < column; j++){
                ImagePiece piece = new ImagePiece();
                piece.setIndex(j + i * column);
                int x = j * pieceWidth;
                int y = i * pieceWidth;
                Bitmap pieceBitmap = Bitmap.createBitmap(imageBitmap, x, y, pieceWidth, pieceWidth);
                piece.setBitmap(pieceBitmap);
                listImagePiece.add(piece);
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public ArrayList<ImagePiece> getListImagePiece() {
        return listImagePiece;
    }

    public void setListImagePiece(ArrayList<ImagePiece> listImagePiece) {
        this.listImagePiece = listImagePiece;
    }
}
