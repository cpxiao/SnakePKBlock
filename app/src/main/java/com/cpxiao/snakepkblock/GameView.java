package com.cpxiao.snakepkblock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.gamelib.mode.common.Sprite;
import com.cpxiao.gamelib.mode.common.SpriteControl;
import com.cpxiao.gamelib.views.BaseSurfaceViewFPS;
import com.cpxiao.snakepkblock.mode.Block;
import com.cpxiao.snakepkblock.mode.Board;
import com.cpxiao.snakepkblock.mode.Food;
import com.cpxiao.snakepkblock.mode.SnakeBody;
import com.cpxiao.snakepkblock.mode.SnakeHead;
import com.cpxiao.snakepkblock.mode.extra.ColorExtra;
import com.cpxiao.snakepkblock.mode.extra.Extra;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * @author cpxiao on 2017/07/18.
 */

public class GameView extends BaseSurfaceViewFPS {

    /**
     * 游戏状态
     */
    private enum GameState {
        PREPARED, STARTED, PARSED, GAME_OVER
    }

    private GameState mGameState = GameState.PREPARED;

    /**
     * 分数与最高分
     */
    private int mScore = 0, mBestScore = 0;
    /**
     * 速度
     */
    private float mSpeedY;
    /**
     * 蛇是否与方块发生碰撞
     */
    private boolean isSnakeCollideWithBlock = false;
    /**
     * 精灵列表
     */
    private ConcurrentLinkedQueue<Sprite> mSpriteQueue = new ConcurrentLinkedQueue<>();
    //    private SnakeHead mSnakeHead;
    private volatile SnakeHead mSnakeHead;

    private static final int mBlockCount = 5;
    private float mCircleWH;
    private float mBlockWH;
    private float mBoardW;
    private float mBoardMarginTB;
    private float mSnakeTargetX, mSnakeTargetY;


    private RectF mCanvasRectF;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void restart() {
        mGameState = GameState.PREPARED;
    }

    /**
     * 将游戏设置为暂停状态
     */
    public void pause() {
        if (mGameState == GameState.STARTED) {

            mGameState = GameState.PARSED;
        }
    }

    /**
     * 将游戏设置为运行状态
     */
    private void resume() {

        mGameState = GameState.STARTED;
        postInvalidate();
    }

    public void destroy() {

    }

    @Override
    protected void initWidget() {
        setBgColor(Color.BLACK);

        mBlockWH = mViewWidth / mBlockCount;
        //初始化Circle(包含蛇和食物)宽高
        mCircleWH = 0.05F * mViewWidth;
        mBoardW = 0.015F * mViewWidth;
        mBoardMarginTB = 0.01f * mBlockWH;

        mBestScore = PreferencesUtils.getInt(getContext(), Extra.Key.BEST_SCORE, 0);

        //初始化速度
        mSpeedY = 0.05F * mViewHeight / mFPS;
        mSpeedY = 0.5F * mViewHeight / mFPS;
        isSnakeCollideWithBlock = false;

        //初始化snake初始目标位置
        mSnakeTargetX = 0.5F * mViewWidth;
        mSnakeTargetY = 0.6F * mViewHeight;

        mCanvasRectF = new RectF(0, 0, mViewWidth, mViewHeight);


    }


    @Override
    public void drawCache() {
        if (mGameState == GameState.PREPARED) {
            mScore = 0;
            mSpriteQueue.clear();
            //初始化蛇
            createSnakeHead();
            createSnakeBody(5);
            checkupSnakePosition();
            drawPrepared(mCanvasCache, mPaint);
        } else if (mGameState == GameState.STARTED) {
            //随机添加Sprite
            if (!isSnakeCollideWithBlock) {
                createBlockAndFood();
                createBoard();
            }
            checkBlockAndBoardCollided();
            drawStarted(mCanvasCache, mPaint);
        } else if (mGameState == GameState.PARSED) {
            drawParsed(mCanvasCache, mPaint);
        } else if (mGameState == GameState.GAME_OVER) {
            drawGameOver(mCanvasCache, mPaint);
        }
    }

    private void removeDestroyedSprite() {
        //遍历sprites
        Iterator<Sprite> iterator = mSpriteQueue.iterator();
        while (iterator.hasNext()) {
            Sprite sprite = iterator.next();
            if (sprite == null) {
                continue;
            }
            //检查Sprite是否超出了Canvas的范围，如果超出，则销毁Sprite
            RectF spriteRecF = sprite.getSpriteRectF();
            //在屏幕外且在屏幕底部才destroy并且非SnakeBody
            if (mCanvasRectF != null && spriteRecF != null) {
                if (!RectF.intersects(mCanvasRectF, spriteRecF)
                        && spriteRecF.top > mCanvasRectF.bottom
                        && !(sprite instanceof SnakeBody)) {
                    sprite.destroy();
                }
            }

            //我们此处要判断Sprite在执行了draw方法后是否被destroy掉了
            if (sprite.isDestroyed()) {
                //如果Sprite被销毁了，那么从Sprites中将其移除
                iterator.remove();
            }
        }
    }

    /**
     * 初始化Snake坐标
     */
    private void resetSnake() {

    }

    /**
     * @param canvas   canvas
     * @param paint    paint
     * @param isOnDraw 是否是静态绘制
     */
    private void drawSprite(Canvas canvas, Paint paint, boolean isOnDraw) {
        if (isOnDraw) {
            for (Sprite block : getBlockList()) {
                paint.setColor(ColorExtra.getBlockColor(getContext(), block.getLife()));
                block.onDraw(canvas, paint);
            }
            for (Sprite board : getBoardList()) {
                board.onDraw(canvas, paint);
            }
            for (Sprite food : getFoodList()) {
                food.onDraw(canvas, paint);
            }
            for (Sprite snakeBody : getSnakeBodyList()) {
                snakeBody.onDraw(canvas, paint);
            }
            mSnakeHead.onDraw(canvas, paint);
        } else {
            for (Sprite block : getBlockList()) {
                paint.setColor(ColorExtra.getBlockColor(getContext(), block.getLife()));
                if (isSnakeCollideWithBlock) {
                    block.onDraw(canvas, paint);
                } else {
                    block.draw(canvas, paint);
                }
            }
            for (Sprite board : getBoardList()) {
                if (isSnakeCollideWithBlock) {
                    board.onDraw(canvas, paint);
                } else {
                    board.draw(canvas, paint);
                }
            }
            for (Sprite food : getFoodList()) {
                if (isSnakeCollideWithBlock) {
                    food.onDraw(canvas, paint);
                } else {
                    food.draw(canvas, paint);
                }
            }
            for (Sprite snakeBody : getSnakeBodyList()) {
                snakeBody.draw(canvas, paint);
            }
            mSnakeHead.draw(canvas, paint);
        }

    }

    private void drawPrepared(Canvas canvas, Paint paint) {
        //绘制mSpriteQueue
        drawSprite(canvas, paint, true);

        //绘制标题
        drawTitle(canvas, paint);

        //绘制最高分
        paint.setTextSize(0.06F * mViewWidth);
        String best = getResources().getString(R.string.best_score) + ": " + mBestScore;
        canvas.drawText(best, 0.5f * mViewWidth, 0.5F * mViewHeight, paint);

        //绘制开始按钮
        drawButton(canvas, getContext().getString(R.string.tap_to_start), paint);

    }

    private void drawStarted(Canvas canvas, Paint paint) {
        drawSprite(canvas, paint, false);
        drawScore(canvas, paint);
    }

    private void drawParsed(Canvas canvas, Paint paint) {
        drawSprite(canvas, paint, true);

        drawScoreAndBestScoreView(canvas, mScore, mBestScore, paint);
        drawButton(canvas, getResources().getString(R.string.tap_to_start), paint);

        drawScore(canvas, paint);
    }

    private void drawGameOver(Canvas canvas, Paint paint) {
        drawSprite(canvas, paint, true);

        drawScoreAndBestScoreView(canvas, mScore, mBestScore, paint);
        drawContinueButton(canvas, getContext().getString(R.string.tap_to_continue), paint);
        drawButton(canvas, getContext().getString(R.string.tap_to_restart), paint);

        drawScore(canvas, paint);
    }


    private void drawScore(Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE);
        //绘制title
        float cX = 0.5f * mViewWidth;
        paint.setTextSize(0.1F * mViewWidth);
        paint.setTypeface(Typeface.DEFAULT);
        String text = "" + mScore;
        canvas.drawText(text, cX, 0.05F * mViewHeight, paint);
    }

    private void drawTitle(Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE);
        //绘制title
        float cX = 0.5f * mViewWidth;
        paint.setTextSize(0.16F * mViewWidth);
        paint.setTypeface(Typeface.DEFAULT);
        canvas.drawText(getContext().getString(R.string.title_snake), cX, 0.12F * mViewHeight, paint);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText(getContext().getString(R.string.title_pk), cX, 0.24F * mViewHeight, paint);
        paint.setTypeface(Typeface.DEFAULT);
        canvas.drawText(getContext().getString(R.string.title_block), cX, 0.36F * mViewHeight, paint);
    }

    /**
     * 绘制分数
     */
    private void drawScoreAndBestScoreView(Canvas canvas, int score, int bestScore, Paint paint) {
        paint.setColor(Color.WHITE);
        paint.setAlpha(232);
        RectF rectF = new RectF();
        float w = 0.4f * mViewWidth;
        float h = 1.0f * w;
        rectF.left = 0.5F * (mViewWidth - w);
        rectF.right = rectF.left + w;
        rectF.top = 0.1f * mViewHeight;
        rectF.bottom = rectF.top + h;
        canvas.drawRoundRect(rectF, 0.1F * rectF.width(), 0.1F * rectF.height(), paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(0.3f * rectF.width());
        canvas.drawText(String.valueOf(score), rectF.centerX(), rectF.top + 0.5F * rectF.height(), paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(0.15f * rectF.width());
        String best = getResources().getString(R.string.best) + ": " + bestScore;
        canvas.drawText(best, rectF.centerX(), rectF.top + 0.8F * rectF.height(), paint);
        paint.setAlpha(255);
    }

    /**
     * 绘制新加一条命按钮
     */
    private void drawContinueButton(Canvas canvas, String text, Paint paint) {
        //        RectF rectF = new RectF();
        //        float r = 0.16F * mViewWidth;
        //        rectF.left = 0.5f * mViewWidth - r;
        //        rectF.right = rectF.left + 2 * r;
        //        rectF.top = 0.5f * mViewHeight - r;
        //        rectF.bottom = rectF.top + 2 * r;
        //
        //        paint.setColor(Color.YELLOW);
        //        canvas.drawCircle(rectF.centerX(), rectF.centerY(), 0.5f * rectF.height(), paint);
        //        paint.setTextSize(0.12F * rectF.width());
        //        paint.setColor(Color.BLACK);
        //        canvas.drawText(text, rectF.centerX(), rectF.centerY() + 0.08f * rectF.height(), paint);
    }

    /**
     * 绘制开始按钮/继续按钮
     */
    private void drawButton(Canvas canvas, String text, Paint paint) {
        paint = new Paint();
        int frequency = mFPS * 2;
        long tmp = mFrame % (frequency * 2);
        if (tmp >= frequency) {
            tmp = frequency - tmp % frequency;
        }
        paint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);//抗锯齿
        paint.setTextSize(0.06F * mViewWidth);
        paint.setColor(Color.WHITE);
        paint.setAlpha((int) (255 * tmp / frequency));
        canvas.drawText(text, 0.5F * mViewWidth, 0.8f * mViewHeight, paint);
    }

    @Override
    protected void timingLogic() {
        mSnakeHead.setY(mSnakeTargetY);
        removeDestroyedSprite();
    }

    private void createSnakeHead() {
        mSnakeHead = (SnakeHead) new SnakeHead.Build()
                .setX(mSnakeTargetX)
                .setY(mSnakeTargetY + 100)
                .setW(mCircleWH)
                .setH(mCircleWH)
                .setMovingRangeRectF(new RectF(0, 0, mViewWidth, mViewHeight))
                .build();
    }

    private void createSnakeBody(int life) {
        if (life < 0) {
            return;
        }
        for (int i = 0; i < life; i++) {
            SnakeBody snakeBody = (SnakeBody) new SnakeBody.Build()
                    .setX(mSnakeHead.getX())
                    .setY(mSnakeHead.getY())
                    .setW(mCircleWH)
                    .setH(mCircleWH)
                    .build();
            mSpriteQueue.add(snakeBody);
        }
    }

    private float mBlockAndFoodDeltaY = 0;
    private float mLastBlockAndFoodY = 0;

    private void createBlockAndFood() {
        if (mLastBlockAndFoodY < mBlockAndFoodDeltaY) {
            mLastBlockAndFoodY += mSpeedY;
            return;
        }
        mLastBlockAndFoodY = 0;
        mBlockAndFoodDeltaY = mBlockWH * (int) (2 + Math.random() * 2);


        for (int i = 0; i < mBlockCount; i++) {
            double random = Math.random();
            double blockProbability = 0.5;
            double circleProbability = 0.1;
            if (random < blockProbability) {
                int age = (int) (Math.random() * 10);
                Block block = (Block) new Block.Build()
                        .setW(mBlockWH)
                        .setH(mBlockWH)
                        .centerTo((0.5f + i) * mBlockWH, -0.5f * mBlockWH)
                        .setSpeedY(mSpeedY)
                        .setLife(age)
                        .build();
                mSpriteQueue.add(block);
            } else if (random < blockProbability + circleProbability) {
                int age = (int) (Math.random() * 5) + 1;
                Food food = (Food) new Food.Build()
                        .setW(mCircleWH)
                        .setH(mCircleWH)
                        .centerTo((0.5f + i) * mBlockWH, -0.5f * mBlockWH)
                        .setSpeedY(mSpeedY)
                        .setLife(age)
                        .build();
                mSpriteQueue.add(food);
            }
        }
    }

    private float mBoardDeltaY = 0;
    private float mLastBoardY = 0;

    private void createBoard() {
        if (mLastBoardY < mBoardDeltaY) {
            mLastBoardY += mSpeedY;
            return;
        }
        mLastBoardY = 0;
        mBoardDeltaY = mBlockWH * (int) (2 + Math.random() * 2);

        for (int i = 0; i < mBlockCount; i++) {
            double random = Math.random();
            double line1Probability = 0.1;
            double line2Probability = 0.1;
            if (random < line1Probability) {
                int index = (int) (Math.random() * 100 % (mBlockCount - 1));
                Board board = (Board) new Board.Build()
                        .setW(mBoardW)
                        .setH(mBlockWH - 2 * mBoardMarginTB)
                        .centerTo((index + 1) * mBlockWH, -0.5f * mBlockWH)
                        .setSpeedY(mSpeedY)
                        .build();
                mSpriteQueue.add(board);
            } else if (random < line1Probability + line2Probability) {
                int index = (int) (Math.random() * 100 % (mBlockCount - 1));
                Board board = (Board) new Board.Build()
                        .setW(mBoardW)
                        .setH(mBlockWH * 2 - 2 * mBoardMarginTB)
                        .centerTo((index + 1) * mBlockWH, -1.0f * mBlockWH)
                        .setSpeedY(mSpeedY)
                        .build();
                mSpriteQueue.add(board);
            }
        }
    }


    float mLastTouchX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (mGameState == GameState.PREPARED) {
            if (action == MotionEvent.ACTION_DOWN) {
                mGameState = GameState.STARTED;
            } else {
                return true;
            }
        }
        if (mGameState == GameState.PARSED) {
            if (action == MotionEvent.ACTION_DOWN) {
                resume();
            } else {
                return true;
            }
        }
        if (mGameState == GameState.GAME_OVER) {
            //TODO 游戏结束点击继续可续命
            if (action == MotionEvent.ACTION_DOWN) {
                restart();
            }
            return true;
        }


        //重置snake位置，防止越界
        checkBlockAndBoardCollided();

        if (action == MotionEvent.ACTION_DOWN) {
            mLastTouchX = event.getX();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float delta = event.getX() - mLastTouchX;
            RectF snakeMovingRangeRectF = mSnakeHead.getMovingRangeRectF();
            if (delta > 0) {
                if (mSnakeHead.getX() + mSnakeHead.getWidth() + delta > snakeMovingRangeRectF.right) {
                    delta = snakeMovingRangeRectF.right - (mSnakeHead.getX() + mSnakeHead.getWidth());
                }
            } else {
                if (mSnakeHead.getX() + delta < snakeMovingRangeRectF.left) {
                    delta = snakeMovingRangeRectF.left - mSnakeHead.getX();
                }
            }
            mSnakeHead.moveBy(delta, 0);

            mLastTouchX = event.getX();
            //            mLastTouchX = mLastTouchX + delta;
        }

        return true;
    }

    private synchronized void checkBlockAndBoardCollided() {
        //        synchronized (TAG) {
        checkupSnakePosition();
        checkBlockCollided();
        checkFoodCollided();
        mSnakeHead.setLife(getSnakeBodyList().size());
        //        }
    }

    private synchronized void checkupSnakePosition() {
        mSnakeHead.setMovingRangeRectFL(0);
        mSnakeHead.setMovingRangeRectFR(mViewWidth);

        /* 设置SnakeHead左右边界 */
        for (Sprite sprite : getBlockAndBoardList()) {
            if ((mSnakeHead.getY() + mSnakeHead.getHeight()) - sprite.getY() > 2
                    && (sprite.getY() + sprite.getHeight()) - mSnakeHead.getY() > 2) {

                RectF snakeRangeRectF = mSnakeHead.getMovingRangeRectF();
                if (sprite.getCenterX() < mSnakeHead.getCenterX()) {
                    float rangeRectFL = Math.max(snakeRangeRectF.left, sprite.getX() + sprite.getWidth());
                    mSnakeHead.setMovingRangeRectFL(rangeRectFL);
                } else {
                    float rangeRectFR = Math.min(snakeRangeRectF.right, sprite.getX());
                    mSnakeHead.setMovingRangeRectFR(rangeRectFR);
                }

            }
        }

        for (Sprite sprite : getBlockAndBoardList()) {
            checkupSnakeBody(sprite, mSnakeHead);
            for (Sprite snakeBody : getSnakeBodyList()) {
                checkupSnakeBody(sprite, snakeBody);
            }
        }



        /* 根据Snake的上一个位置计算SnakeBody坐标 */
        List<Sprite> snakeBodyList = getSnakeBodyList();
        Sprite last = mSnakeHead;
        for (int i = 0; i < snakeBodyList.size(); i++) {
            Sprite current = snakeBodyList.get(i);
            current.setY(current.getY() + 0.1F * mCircleWH);
            //            current.setY(last.getY() + 0.1F * mCircleWH);
            float distance = getDistance(last, current);
            float length = distance / (mCircleWH);


            if (length > 0) {
                current.setX(last.getX() + (current.getX() - last.getX()) / length);
                current.setY(last.getY() + (current.getY() - last.getY()) / length);
            } else {
                current.setY(last.getY() + mCircleWH);
                current.setX(last.getX());
            }
            last = current;
        }
    }

    private void checkupSnakeBody(Sprite sprite, Sprite snakeBody) {
        if (SpriteControl.isCollidedByTwoSprite(sprite, snakeBody)) {
                    /*
                    * 如下图所示拆分sprite
                    *
                    * \ 4/
                    * 3\/1
                    *  /\
                    * / 2\
                    /* 以sprite中心为原点重置snake中心坐标，再根据x=y及x=-y两条线四等分sprite，用于碰撞检查*/
            float cX = snakeBody.getCenterX() - sprite.getCenterX();
            //            float cY = snakeBody.getCenterY() - sprite.getCenterX();
            //            float cY = snakeBody.getCenterY() - sprite.getCenterY();
            float cY = -(snakeBody.getCenterY() - sprite.getCenterY());//注意Y轴坐标方向的转化
            float padding = 1;
            if (cX - cY >= 0) {
                if (cX + cY >= 0) {
                    //区域1
                    snakeBody.setX(sprite.getX() + sprite.getWidth() - padding);
                } else {
                    //区域2
                    snakeBody.setY(sprite.getY() + sprite.getHeight() - padding);
                }
            } else {
                if (cX + cY >= 0) {
                    //区域4
                    snakeBody.setY(sprite.getY() - snakeBody.getHeight() + padding);
                } else {
                    //区域3
                    snakeBody.setX(sprite.getX() - snakeBody.getHeight() + padding);
                }
            }
        }
    }

    private synchronized void checkBlockCollided() {
        boolean isBlockTouched = false;
        for (Sprite block : getBlockList()) {
            if (block.isDestroyed() || block.getLife() <= 0) {
                continue;
            }
            //发生碰撞了
            RectF snakeRectF = mSnakeHead.getCollideRectF();
            if (SpriteControl.isCollidedByTwoSprite(mSnakeHead, block)
                    && block.getX() <= mSnakeHead.getCenterX()
                    && block.getX() + block.getWidth() >= mSnakeHead.getCenterX()
                    && snakeRectF.bottom >= block.getY() + block.getHeight()) {
                isSnakeCollideWithBlock = true;
                isBlockTouched = true;
                if (mFrame % (int) (0.2F * mFPS) == 0) {
                    if (!getSnakeBodyList().isEmpty()) {
                        mScore++;
                        block.deleteLife(1);
                        deleteOneLife();
                    } else {
                        gameOver();
                    }
                    checkScoreAndBestScore();
                }
                break;
            }
        }
        if (!isBlockTouched) {
            isSnakeCollideWithBlock = false;
        }
    }

    private void checkScoreAndBestScore() {
        if (mScore > mBestScore) {
            mBestScore = mScore;
            PreferencesUtils.putInt(getContext(), Extra.Key.BEST_SCORE, mScore);
        }
    }

    private synchronized void checkFoodCollided() {
        List<Sprite> foodList = getFoodList();
        for (Sprite food : foodList) {
            //发生碰撞了
            if (SpriteControl.isCollidedByTwoSprite(mSnakeHead, food)) {
                createSnakeBody(food.getLife());
                food.setLife(0);
                food.destroy();
                break;
            }
        }
    }

    /**
     * 获取处于活动状态的Block
     *
     * @return list
     */
    public List<Sprite> getBlockAndBoardList() {
        List<Sprite> list = new ArrayList<>();
        for (Sprite sprite : mSpriteQueue) {
            if ((sprite instanceof Block || sprite instanceof Board) && !sprite.isDestroyed()) {
                list.add(sprite);
            }
        }
        return list;
    }


    /**
     * 获取处于活动状态的Block
     *
     * @return list
     */
    public List<Sprite> getBlockList() {
        List<Sprite> list = new ArrayList<>();
        for (Sprite sprite : mSpriteQueue) {
            if (sprite instanceof Block && !sprite.isDestroyed()) {
                list.add(sprite);
            }
        }
        return list;
    }

    /**
     * 获取处于活动状态的板子
     *
     * @return list
     */
    public List<Sprite> getBoardList() {
        List<Sprite> list = new ArrayList<>();
        for (Sprite sprite : mSpriteQueue) {
            if (sprite instanceof Board && !sprite.isDestroyed()) {
                list.add(sprite);
            }
        }
        return list;
    }

    /**
     * 获取处于活动状态的食物
     *
     * @return list
     */
    public List<Sprite> getFoodList() {
        List<Sprite> list = new ArrayList<>();
        for (Sprite sprite : mSpriteQueue) {
            if (sprite instanceof Food && !sprite.isDestroyed()) {
                list.add(sprite);
            }
        }
        return list;
    }

    /**
     * 获取SnakeBody
     *
     * @return list
     */
    public List<Sprite> getSnakeBodyList() {
        List<Sprite> list = new ArrayList<>();
        for (Sprite sprite : mSpriteQueue) {
            if (sprite instanceof SnakeBody && !sprite.isDestroyed()) {
                list.add(sprite);
            }
        }
        return list;
    }

    /**
     * 删除一条命
     *
     * @return true:删除成功；false:删除失败，没命了
     */
    private boolean deleteOneLife() {
        for (Sprite sprite : mSpriteQueue) {
            if (sprite instanceof SnakeBody && !sprite.isDestroyed()) {
                sprite.destroy();
                return true;
            }
        }
        return false;
    }


    private void gameOver() {
        mGameState = GameState.GAME_OVER;
    }

    private float getDistance(Sprite last, Sprite current) {
        float _x = Math.abs(last.getX() - current.getX());
        float _y = Math.abs(last.getY() - current.getY());
        return (float) Math.sqrt(_x * _x + _y * _y);
    }
}
