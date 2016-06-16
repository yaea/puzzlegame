package com.yaea.puzzlegame.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yaea.puzzlegame.R;
import com.yaea.puzzlegame.model.Game;
import com.yaea.puzzlegame.model.ImagePiece;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by 慧客 on 2016/6/16.
 */
public class GameLayout extends RelativeLayout implements View.OnClickListener {
    /**
     * 图片内边距
     */
    private int mPadding;
    /**
     * 每个碎片之间点距离（DP)
     */
    private int mMargin = 3;
    /**
     * imageview集合
     */
    private ImageView[] imageViews;
    /**
     * 每项大宽度
     */
    private int itemWidth;
    /**
     * 第一次初始化标记
     */
    private boolean once = true;
    /**
     * 布局宽度
     */
    private int mWidth;
    /**
     * 游戏
     */
    private Game game;
    /**
     * 第一次点击的item
     */
    private ImageView ivFirst;
    /**
     * 第二次点击item
     */
    private ImageView ivSecond;
    /**
     * 动画层
     */
    private RelativeLayout mAnimLayout;
    /**
     * 动画是否正在进行
     */
    private boolean isAniming = false;

    public GameLayout(Context context) {
        this(context, null);
    }

    public GameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        if(once){
            initGame(1, 3, BitmapFactory.decodeResource(getResources(), R.mipmap.image));
            initItem();
            once = false;
        }
        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    public void onClick(View v) {
        if(isAniming){
            return;
        }
        if(ivFirst == v){
            ivFirst.setColorFilter(null);
            ivFirst = null;
            return;
        }
        if(ivFirst == null){
            ivFirst = (ImageView) v;
            ivFirst.setColorFilter(Color.parseColor("#55000000"));
        }else {
            ivSecond = (ImageView) v;
            exchangeItem();
        }
    }

    private void init(){
        mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
    }

    /**
     * 获取最小值
     */
    private int min(int... params){
        int min = params[0];
        for(int param : params){
            if(param < min){
                min = param;
            }
        }
        return min;
    }

    /**
     * 初始化碎片
     */
    private void initGame(int level, int colume, Bitmap bitmap){
        game = new Game(level, colume, bitmap);
        Collections.sort(game.getListImagePiece(), new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece lhs, ImagePiece rhs) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
    }

    /**
     * 初始化 imageview item
     */
    private void initItem(){
        itemWidth = (mWidth - mPadding * 2 - mMargin * (game.getColumn() - 1)) / game.getColumn();
        imageViews = new ImageView[game.getColumn() * game.getColumn()];
        for(int i = 0; i < imageViews.length; i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setOnClickListener(this);
            imageView.setImageBitmap(game.getListImagePiece().get(i).getBitmap());
            imageViews[i] = imageView;
            imageView.setId(i + 1);
            imageView.setTag(i + "_" + game.getListImagePiece().get(i).getIndex());
            LayoutParams lp = new LayoutParams(itemWidth, itemWidth);
            if((i + 1) % game.getColumn() != 0){
                lp.rightMargin = mMargin;
            }
            // 不是第一列
            if (i % game.getColumn() != 0)
            {
                lp.addRule(RelativeLayout.RIGHT_OF,
                        imageViews[i - 1].getId());
            }
            // 如果不是第一行 , 设置topMargin和rule
            if ((i + 1) > game.getColumn())
            {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW, imageViews[i - game.getColumn()].getId());
            }
            addView(imageView, lp);
        }
    }

    /**
     * 变化位置
     */
    private void exchangeItem(){
        ivFirst.setColorFilter(null);
        setUpAnimLayout();

        ImageView ivFirstTemp = new ImageView(getContext());
        final Bitmap firstBitmap = game.getListImagePiece().get(getImageViewIdByTag((String) ivFirst.getTag())).getBitmap();
        ivFirstTemp.setImageBitmap(firstBitmap);
        LayoutParams lp = new LayoutParams(itemWidth, itemWidth);
        lp.leftMargin = ivFirst.getLeft() - mPadding;
        lp.topMargin = ivFirst.getTop() - mPadding;
        ivFirstTemp.setLayoutParams(lp);
        mAnimLayout.addView(ivFirstTemp);

        ImageView ivSecondTemp = new ImageView(getContext());
        final Bitmap secondBitmap = game.getListImagePiece().get(getImageViewIdByTag((String) ivSecond.getTag())).getBitmap();
        ivSecondTemp.setImageBitmap(secondBitmap);
        LayoutParams lp2 = new LayoutParams(itemWidth, itemWidth);
        lp2.leftMargin = ivSecond.getLeft() - mPadding;
        lp2.topMargin = ivSecond.getTop() - mPadding;
        ivSecondTemp.setLayoutParams(lp2);
        mAnimLayout.addView(ivSecondTemp);

        TranslateAnimation animFirst = new TranslateAnimation(0, ivSecond.getLeft() - ivFirst.getLeft(), 0, ivSecond.getTop() - ivFirst.getTop());
        animFirst.setDuration(300);
        animFirst.setFillAfter(true);
        ivFirstTemp.startAnimation(animFirst);

        TranslateAnimation animSecond = new TranslateAnimation(0, ivFirst.getLeft() - ivSecond.getLeft(), 0, ivFirst.getTop() - ivSecond.getTop());
        animSecond.setDuration(300);
        animSecond.setFillAfter(true);
        ivSecondTemp.startAnimation(animSecond);

        animFirst.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ivFirst.setVisibility(INVISIBLE);
                ivSecond.setVisibility(INVISIBLE);
                isAniming = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String firstTag = (String) ivFirst.getTag();
                String secondTag = (String) ivSecond.getTag();
                ivFirst.setImageBitmap(secondBitmap);
                ivSecond.setImageBitmap(firstBitmap);
                ivFirst.setTag(secondTag);
                ivSecond.setTag(firstTag);
                ivFirst.setVisibility(VISIBLE);
                ivSecond.setVisibility(VISIBLE);
                ivFirst = ivSecond = null;
                mAnimLayout.removeAllViews();
                checkSuccess();
                isAniming = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private int getImageViewIdByTag(String tag){
        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);
    }

    private int getImageViewIndexByTag(String tag){
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);
    }

    private void setUpAnimLayout(){
        if(mAnimLayout == null){
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }
    }

    private void checkSuccess(){
        boolean isSuccess = true;
        for(int i = 0; i < imageViews.length; i++){
            ImageView imageView = imageViews[i];
            if(getImageViewIndexByTag((String) imageView.getTag()) != i){
                isSuccess = false;
            }
        }

        if(isSuccess){
            nextLevel();
            Toast.makeText(getContext(), "拼图成功，进入下一关！", Toast.LENGTH_SHORT).show();
        }
    }

    private void nextLevel(){
        this.removeAllViews();
        mAnimLayout = null;
        game.setColumn(game.getColumn() + 1);
        game.setLevel(game.getLevel() + 1);
        initGame(game.getLevel(), game.getColumn(), BitmapFactory.decodeResource(getResources(), R.mipmap.image));
        initItem();
    }
}
