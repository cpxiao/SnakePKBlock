package com.cpxiao.snakepkblock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import android.view.MotionEvent;

import com.cpxiao.R;
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

    private int mGameMode = Extra.mode_default;

    /**
     * 游戏状态
     */
    private enum GameState {
        PREPARED,
        STARTED,
        PARSED,
        GAME_OVER
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

    private volatile SnakeHead mSnakeHead;

    private static final int mSnakeBodyStartCount = 8;
    private static int mBlockCount = 5;
    private float mCircleWH;
    private float mBlockWH;
    private float mBoardW;
    private float mSnakeTargetX, mSnakeTargetY;


    private RectF mCanvasRectF;

    public GameView(Context context, int gameMode) {
        this(context);
        mBlockCount = gameMode;
        mGameMode = gameMode;
    }

    public GameView(Context context) {
        super(context);
    }

//    public GameView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }

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

        mBestScore = Extra.getBestScore(getContext(), mGameMode);

        //初始化snake初始目标位置
        mSnakeTargetX = 0.5F * mViewWidth;
        mSnakeTargetY = 0.6F * mViewHeight;
//        mSnakeTargetY = 0.8F * mViewHeight;

        mCanvasRectF = new RectF(0, 0, mViewWidth, mViewHeight);

    }


    @Override
    public void drawCache() {
        if (mSnakeHead != null) {
            mSnakeHead.setY(mSnakeTargetY);
        }
        removeDestroyedSprite();
        checkBlockAndBoardCollided(mSnakeHead);

        if (mGameState == GameState.PREPARED) {
            mScore = 0;
            //初始化速度
            mSpeedY = 0.4F * mViewHeight / mFPS;
            isSnakeCollideWithBlock = false;

            mSpriteQueue.clear();
            //初始化蛇
            createSnakeHead();

            if (DEBUG) {
                createSnakeBody(5);
            } else {
                createSnakeBody(mSnakeBodyStartCount);
            }
            drawPrepared(mCanvasCache, mPaint);
        } else if (mGameState == GameState.STARTED) {
            // 每十秒速度增加
            if (mFrame % (10 * mFPS) == 0) {
                mSpeedY *= 1.01F;
                for (Sprite sprite : mSpriteQueue) {
                    sprite.setSpeedY(mSpeedY);
                }
            }
            //随机添加Sprite
            if (!isSnakeCollideWithBlock) {
                boolean isBlockCreateSuccess = createBlockAndFood();
                if (isBlockCreateSuccess) {
                    createBoard();
                }
            }
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

        // 绘制游戏模式文本
        paint.setTextSize(0.06F * mViewWidth);
        String gameModeStr = Extra.getGameModeStr(getContext(), mGameMode);
        canvas.drawText(gameModeStr, 0.5f * mViewWidth, 0.45F * mViewHeight, paint);

        //绘制最高分
        paint.setTextSize(0.06F * mViewWidth);
        String best = getResources().getString(R.string.best_score) + ": " + mBestScore;
        canvas.drawText(best, 0.5f * mViewWidth, 0.5F * mViewHeight, paint);

        //绘制开始按钮
        drawButton(canvas, getContext().getString(R.string.tap_to_start), paint);

    }

    private void drawStarted(Canvas canvas, Paint paint) {
        drawSprite(canvas, paint, false);
        drawTitleBarScore(canvas, paint);
    }

    private void drawParsed(Canvas canvas, Paint paint) {
        drawSprite(canvas, paint, true);

        drawScoreAndBestScoreView(canvas, mScore, mBestScore, paint);
        drawButton(canvas, getResources().getString(R.string.tap_to_start), paint);

//        drawTitleBarScore(canvas, paint);
    }

    private void drawGameOver(Canvas canvas, Paint paint) {
        drawSprite(canvas, paint, true);

        drawScoreAndBestScoreView(canvas, mScore, mBestScore, paint);
        drawContinueButton(canvas, getContext().getString(R.string.tap_to_continue), paint);
        drawButton(canvas, getContext().getString(R.string.tap_to_restart), paint);

//        drawTitleBarScore(canvas, paint);
    }


    private void drawTitleBarScore(Canvas canvas, Paint paint) {

        // 绘制游戏模式文本
        paint.setColor(Color.WHITE);
        paint.setTextSize(0.06F * mViewWidth);
        String gameModeStr = Extra.getGameModeStr(getContext(), mGameMode);
        canvas.drawText(gameModeStr, 0.5f * mViewWidth, 0.05F * mViewHeight, paint);


        //绘制得分
        paint.setTextSize(0.15F * mViewWidth);
        paint.setTypeface(Typeface.DEFAULT);
        String text = "" + mScore;
        canvas.drawText(text, 0.5f * mViewWidth, 0.15F * mViewHeight, paint);


    }

    private void drawTitle(Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE);
        //绘制title
        float cX = 0.5f * mViewWidth;
        paint.setTextSize(0.15F * mViewWidth);
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
        float w = 0.5f * mViewWidth;
        float h = 0.88f * w;
        rectF.left = 0.5F * (mViewWidth - w);
        rectF.right = rectF.left + w;
        rectF.top = 0.2f * mViewHeight;
        rectF.bottom = rectF.top + h;
        canvas.drawRoundRect(rectF, 0.1F * rectF.width(), 0.1F * rectF.height(), paint);

        // 绘制游戏模式String
        paint.setColor(Color.BLACK);
        paint.setTextSize(0.12f * rectF.width());
        canvas.drawText(Extra.getGameModeStr(getContext(), mGameMode), rectF.centerX(), rectF.top + 0.2F * rectF.height(), paint);


        // 绘制得分
        paint.setColor(Color.BLACK);
        paint.setTextSize(0.35f * rectF.width());
        canvas.drawText(String.valueOf(score), rectF.centerX(), rectF.top + 0.6F * rectF.height(), paint);

        // 绘制最高分
        paint.setColor(Color.BLACK);
        paint.setTextSize(0.08f * rectF.width());
        String best = getResources().getString(R.string.best_score) + ": " + bestScore;
        canvas.drawText(best, rectF.centerX(), rectF.top + 0.82F * rectF.height(), paint);
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
        paint.setTextSize(0.08F * mViewWidth);
        paint.setColor(Color.WHITE);
        paint.setAlpha((int) (255 * tmp / frequency));
        canvas.drawText(text, 0.5F * mViewWidth, 0.78f * mViewHeight, paint);
    }


    private int createSnakeBodyIndex = 0;

    @Override
    protected void timingLogic() {
        if (needCreateBodyCount > 0) {
            // 0.5秒创建一个
            if (createSnakeBodyIndex > 0.1f * mFPS) {
                createSnakeBodyIndex = 0;

                createSnakeBody(1);
                needCreateBodyCount--;
            } else {
                createSnakeBodyIndex++;
            }
        }
    }

    private void createSnakeHead() {
        mSnakeHead = (SnakeHead) new SnakeHead.Build()
                .setX(mSnakeTargetX)
                .setY(mSnakeTargetY + mCircleWH * 5)
                .setW(mCircleWH)
                .setH(mCircleWH)
                .setMovingRangeRectF(new RectF(0, 0, mViewWidth, mViewHeight))
                .build();
    }

    private int needCreateBodyCount = 0;

    private synchronized void createSnakeBody(int life) {
        if (life < 0 || mSnakeHead == null) {
            return;
        }
        for (int i = 0; i < life; i++) {
            SnakeBody snakeBody = (SnakeBody) new SnakeBody.Build()
                    .setX(mSnakeHead.getX())
                    .setY(mSnakeHead.getY() + 3000)
                    .setW(mCircleWH)
                    .setH(mCircleWH)
                    .build();
            mSpriteQueue.add(snakeBody);
        }
    }

    private float mNextBlockAndFoodDeltaY = 0;
    private float mPreBlockAndFoodY = 0;

    /***
     * 创建方块和食物
     * @return boolean
     */
    private boolean createBlockAndFood() {
        if (mPreBlockAndFoodY < mNextBlockAndFoodDeltaY) {
            mPreBlockAndFoodY += mSpeedY;
            return false;
        }
        mPreBlockAndFoodY = 0;
        mNextBlockAndFoodDeltaY = mBlockWH * (int) (2 + Math.random() * 2);


        float blockProbability = 0.6F;
        float circleProbability = 0.1F;
        for (int i = 0; i < mBlockCount; i++) {
            double random = Math.random();
            float centerY;
            if (Math.random() < 0.05) {
                centerY = -1.5f * mBlockWH;
            } else {
                centerY = -0.5f * mBlockWH;
            }
            if (random < blockProbability) {
                int life = (int) (Math.random() * 10
                        + Math.random() * Math.max(0.35f * getSnakeBodyList().size(), 8));

                Block block = (Block) new Block.Build()
                        .setW(mBlockWH)
                        .setH(mBlockWH)
                        .centerTo((0.5f + i) * mBlockWH, -0.5f * mBlockWH)
                        .setSpeedY(mSpeedY)
                        .setLife(life)
                        .build();
                mSpriteQueue.add(block);
            } else if (random < blockProbability + circleProbability) {
                int age = (int) (Math.random() * 5) + 2;
                Food food = (Food) new Food.Build()
                        .setW(mCircleWH)
                        .setH(mCircleWH)
                        .centerTo((0.5f + i) * mBlockWH, centerY)
                        .setSpeedY(mSpeedY)
                        .setLife(age)
                        .build();
                mSpriteQueue.add(food);
            }
        }
        return true;
    }


    /**
     * * 创建障碍挡板
     */
    private void createBoard() {
        if (Math.random() >= 0.8f) {
            return;
        }

        float board1Probability = 0.4F;
        float board2Probability = 0.1F;
        float board3Probability = 0.05F;
        for (int i = 0; i < mBlockCount; i++) {
            double random = Math.random();
            int index = (int) (Math.random() * 100 % (mBlockCount - 1));
            int height;

            if (random < board1Probability) {
                height = 1;
            } else if (random < board1Probability + board2Probability) {
                height = 2;
            } else if (random < board1Probability + board2Probability + board3Probability) {
                height = 3;
            } else {
                return;
            }
            Board board = (Board) new Board.Build()
                    .setW(mBoardW)
                    .setH(mBlockWH * height)
                    .centerTo((index + 1) * mBlockWH, -0.5f * height * mBlockWH)
                    .setSpeedY(mSpeedY)
                    .build();
            board.setCollideRectFPercent(0, 1);
            mSpriteQueue.add(board);
        }
    }


    private float mLastTouchX = 0;

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
        //        checkBlockAndBoardCollided();

        SnakeHead snakeHead = mSnakeHead;
        if (action == MotionEvent.ACTION_DOWN) {
            mLastTouchX = event.getX();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float delta = event.getX() - mLastTouchX;
            RectF snakeMovingRangeRectF = snakeHead.getMovingRangeRectF();
            if (delta > 0) {
                if (snakeHead.getX() + snakeHead.getWidth() + delta > snakeMovingRangeRectF.right) {
                    delta = snakeMovingRangeRectF.right - (snakeHead.getX() + snakeHead.getWidth());
                }
            } else {
                if (snakeHead.getX() + delta < snakeMovingRangeRectF.left) {
                    delta = snakeMovingRangeRectF.left - snakeHead.getX();
                }
            }
            snakeHead.moveBy(delta, 0);

            mLastTouchX = event.getX();
            //            mLastTouchX = mLastTouchX + delta;
        }

        return true;
    }

    private synchronized void checkBlockAndBoardCollided(SnakeHead snakeHead) {
        if (snakeHead == null) {
            return;
        }
        checkSnakeHeadMovingRangeRectF(snakeHead);
        setupSnakePosition(snakeHead);
        checkBlockCollided(snakeHead);
        checkFoodCollided(snakeHead);
        snakeHead.setLife(getSnakeBodyList().size());
    }

    private synchronized void checkSnakeHeadMovingRangeRectF(SnakeHead snakeHead) {
        if (snakeHead == null) {
            return;
        }
        snakeHead.setMovingRangeRectFL(0);
        snakeHead.setMovingRangeRectFR(mViewWidth);

        //        float delta = 0.5F * mCircleWH;
        float delta = 0;
        //        float delta = 0.03F * mBlockWH;
        /* 设置SnakeHead左右边界 */
        for (Sprite sprite : getBlockAndBoardList()) {
            if (sprite.isDestroyed()) {
                continue;
            }

            if (sprite instanceof Block) {
                delta = 0.5F * mCircleWH;
            } else {
                delta = 0.02F * mBlockWH;
            }
            //与某个方块接触并且在其下方
            if (sprite instanceof Block
                    && SpriteControl.isCollidedByTwoSprite(sprite, snakeHead)
                    && snakeHead.getCenterX() >= sprite.getX()
                    && snakeHead.getCenterX() <= sprite.getX() + sprite.getWidth()) {
                continue;
            }

            if ((snakeHead.getY() + snakeHead.getHeight()) - sprite.getY() > delta
                    && (sprite.getY() + sprite.getHeight()) - snakeHead.getY() > delta) {

                RectF snakeRangeRectF = snakeHead.getMovingRangeRectF();
                if (sprite.getCenterX() < snakeHead.getCenterX()) {
                    float rangeRectFL = Math.max(snakeRangeRectF.left, sprite.getX() + sprite.getWidth());
                    snakeHead.setMovingRangeRectFL(rangeRectFL);
                } else {
                    float rangeRectFR = Math.min(snakeRangeRectF.right, sprite.getX());
                    snakeHead.setMovingRangeRectFR(rangeRectFR);
                }

            }
        }


    }

    private void setupSnakePosition(SnakeHead snakeHead) {
        for (Sprite sprite : getBlockAndBoardList()) {
            checkupSnakeBody(sprite, snakeHead);
            for (Sprite snakeBody : getSnakeBodyList()) {
                checkupSnakeBody(sprite, snakeBody);
            }
        }

        /* 根据Snake的上一个位置计算SnakeBody坐标 */
        List<Sprite> snakeBodyList = getSnakeBodyList();
        Sprite preBody = snakeHead;
        for (int i = 0; i < snakeBodyList.size(); i++) {
            Sprite currentBody = snakeBodyList.get(i);
            currentBody.setY(currentBody.getY() + 0.1F * mCircleWH);
            //            currentBody.setY(preBody.getY() + 0.1F * mCircleWH);
            float distance = getDistance(preBody, currentBody);
            float length = distance / (mCircleWH);


            if (length > 0) {
                currentBody.setX(preBody.getX() + (currentBody.getX() - preBody.getX()) / length);
                currentBody.setY(preBody.getY() + (currentBody.getY() - preBody.getY()) / length);
            } else {
                currentBody.setY(preBody.getY() + mCircleWH);
                currentBody.setX(preBody.getX());
            }
            preBody = currentBody;
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

    private synchronized void checkBlockCollided(SnakeHead snakeHead) {
        boolean isBlockTouched = false;
        for (Sprite block : getBlockList()) {
            if (block.isDestroyed() || block.getLife() <= 0) {
                continue;
            }
            //发生碰撞了
            RectF snakeRectF = snakeHead.getCollideRectF();
            if (SpriteControl.isCollidedByTwoSprite(snakeHead, block)
                    && block.getX() <= snakeHead.getCenterX()
                    && block.getX() + block.getWidth() >= snakeHead.getCenterX()
                    && snakeRectF.bottom >= block.getY() + block.getHeight()) {
                isSnakeCollideWithBlock = true;
                isBlockTouched = true;
                if (mFrame % (int) (0.15F * mFPS) == 0) {
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

    private void gameOver() {
        mGameState = GameState.GAME_OVER;
    }

    private void checkScoreAndBestScore() {
        if (mScore > mBestScore) {
            mBestScore = mScore;
            Extra.setBestScore(getContext(), mGameMode, mBestScore);
        }
    }

    private synchronized void checkFoodCollided(SnakeHead snakeHead) {
        List<Sprite> foodList = getFoodList();
        for (Sprite food : foodList) {
            //发生碰撞了
            if (SpriteControl.isCollidedByTwoSprite(snakeHead, food)) {
                needCreateBodyCount += food.getLife();
//                createSnakeBody(food.getLife());
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


    private float getDistance(Sprite last, Sprite current) {
        float _x = Math.abs(last.getX() - current.getX());
        float _y = Math.abs(last.getY() - current.getY());
        return (float) Math.sqrt(_x * _x + _y * _y);
    }
}
