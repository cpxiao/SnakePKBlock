//package com.cpxiao.snakepkblock;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import android.graphics.Typeface;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//
//import com.cpxiao.R;
//import com.cpxiao.androidutils.library.utils.PreferencesUtils;
//import com.cpxiao.gamelib.mode.common.Sprite;
//import com.cpxiao.gamelib.views.BaseSurfaceViewFPS;
//import com.cpxiao.snakepkblock.mode.Block;
//import com.cpxiao.snakepkblock.mode.Board;
//import com.cpxiao.snakepkblock.mode.extra.ColorExtra;
//import com.cpxiao.snakepkblock.mode.extra.Extra;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//
///**
// * @author cpxiao on 2017/07/18.
// */
//
//public class GameView2 extends BaseSurfaceViewFPS {
//
//    /**
//     * 游戏状态
//     */
//    private enum GameState {
//        PREPARED, STARTED, PARSED, GAME_OVER
//    }
//
//    private GameState mGameState = GameState.PREPARED;
//
//    /**
//     * 分数与最高分
//     */
//    private int mScore = 0, mBestScore = 0;
//    /**
//     * 蛇
//     */
//    private Snake mSnake;
//    /**
//     * 速度
//     */
//    private float mSpeed;//, speedCommon, speedMin;
//    /**
//     * 蛇是否与方块发生碰撞
//     */
//    private boolean isSnakeCollideWithBlock = false;
//    /**
//     * 精灵列表
//     */
//    //    private List<MovingSprite> mSpriteQueue = new ArrayList<>();
//    //    private CopyOnWriteArrayList<MovingSprite> mSpriteQueue = new CopyOnWriteArrayList<>();
//    private ConcurrentLinkedQueue<MovingSprite> mSpriteQueue = new ConcurrentLinkedQueue<>();
//
//    private long mFrame = 0;//总共绘制的帧数
//
//    private MovingSprite mLastSprite = new MovingSprite();
//
//    public GameView2(Context context) {
//        super(context);
//    }
//
//    public GameView2(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public GameView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    private void restart() {
//        mGameState = GameState.PREPARED;
//    }
//
//    public void pause() {
//        if (mGameState == GameState.STARTED) {
//            //将游戏设置为暂停状态
//            mGameState = GameState.PARSED;
//        }
//    }
//
//    private void resume() {
//        //将游戏设置为运行状态
//        mGameState = GameState.STARTED;
//        postInvalidate();
//    }
//
//    public void destroy() {
//
//    }
//
//    @Override
//    protected void initWidget() {
//        setBgColor(Color.BLACK);
//        mBestScore = PreferencesUtils.getInt(getContext(), Extra.Key.BEST_SCORE, 0);
//
//        //初始化Snake大小
//        mSnake = new Snake();
//        int snakeWH = (int) (0.05F * mViewWidth);
//        mSnake.setWidth(snakeWH);
//        mSnake.setHeight(snakeWH);
//        //        mSnake.setOneAgeWH(snakeWH);
//        mSnake.setMinX(0);
//        mSnake.setMaxX(mViewWidth);
//
//        //初始化速度
//        //        speedCommon = 0.5F * mViewHeight / mFPS;
//        //        speedMin = 0;
//        //        mSpeed = speedCommon;
//        mSpeed = 0.5F * mViewHeight / mFPS;
//        isSnakeCollideWithBlock = false;
//
//        //
//        mLastSprite.setY(0);
//        mLastSprite.setHeight(mViewWidth / 5);
//        mLastSprite.setSpeedAndAngle(mSpeed, 90);
//
//    }
//
//
//    @Override
//    public void drawCache() {
//        mFrame++;
//
//        mSnake.checkX();
//
//        if (mGameState == GameState.PREPARED) {
//            resetSnake();
//            mScore = 0;
//            drawPrepared(mCanvasCache, mPaint);
//            //准备好就直接开始
//        } else if (mGameState == GameState.STARTED) {
//            drawStarted(mCanvasCache, mPaint);
//        } else if (mGameState == GameState.PARSED) {
//            resetSnake();
//
//            drawParsed(mCanvasCache, mPaint);
//        } else if (mGameState == GameState.GAME_OVER) {
//            drawGameOver(mCanvasCache, mPaint);
//        }
//    }
//
//    /**
//     * 初始化Snake坐标
//     */
//    private void resetSnake() {
//
//        mSnake.centerTo(0.5F * mViewWidth, 0.618F * mViewHeight);
//        mSnake.setLife(5);
//        mSnake.setupPosition();
//    }
//
//    private void drawPrepared(Canvas canvas, Paint paint) {
//
//        mSpriteQueue.clear();
//
//        drawTitle(canvas, paint);
//
//        //绘制最高分
//        paint.setTextSize(0.06F * mViewWidth);
//        String best = getResources().getString(R.string.best_score) + ": " + mBestScore;
//        canvas.drawText(best, 0.5f * mViewWidth, 0.5F * mViewHeight, paint);
//
//        //静态绘制snake
//        mSnake.onDraw(canvas, paint);
//
//        //绘制开始按钮
//        drawButton(canvas, getContext().getString(R.string.tap_to_start), paint);
//    }
//
//    private void drawStarted(Canvas canvas, Paint paint) {
//
//        //随机添加Sprite
//        createSpite();
//
//        checkBlockAndBoardCollided();
//
//        mLastSprite.draw(canvas, paint);
//        //遍历sprites，绘制敌机、子弹、奖励、爆炸效果
//        Iterator<MovingSprite> iterator = mSpriteQueue.iterator();
//        while (iterator.hasNext()) {
//            MovingSprite autoSprite = iterator.next();
//
//            //检查Sprite是否超出了Canvas的范围，如果超出，则销毁Sprite
//            RectF canvasRecF = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
//            RectF spriteRecF = autoSprite.getRectF();
//            //在屏幕外且在屏幕底部才destroy
//            if (!RectF.intersects(canvasRecF, spriteRecF) && spriteRecF.top > canvasRecF.bottom) {
//                autoSprite.destroy();
//            }
//
//            if (!autoSprite.isDestroyed()) {
//                //在Sprite的draw方法内有可能会调用destroy方法
//                if (autoSprite instanceof Block) {
//                    paint.setColor(ColorExtra.getBlockColor(getContext(), ((Block) autoSprite).getLife()));
//                }
//                if (isSnakeCollideWithBlock) {
//                    autoSprite.onDraw(canvas, paint);
//                } else {
//                    autoSprite.draw(canvas, paint);
//                }
//            }
//
//            //我们此处要判断Sprite在执行了draw方法后是否被destroy掉了
//            if (autoSprite.isDestroyed()) {
//                //如果Sprite被销毁了，那么从Sprites中将其移除
//                iterator.remove();
//            }
//        }
//
//        mSnake.draw(canvas, paint);
//        drawScore(canvas, paint);
//    }
//
//    private void drawParsed(Canvas canvas, Paint paint) {
//
//        drawSpriteList(canvas, paint, true);
//
//        drawScoreAndBestScoreView(canvas, mScore, mBestScore, paint);
//        drawButton(canvas, getResources().getString(R.string.tap_to_start), paint);
//
//        mSnake.onDraw(canvas, paint);
//        drawScore(canvas, paint);
//    }
//
//    private void drawGameOver(Canvas canvas, Paint paint) {
//        drawSpriteList(canvas, paint, true);
//
//        drawScoreAndBestScoreView(canvas, mScore, mBestScore, paint);
//        drawContinueButton(canvas, getContext().getString(R.string.tap_to_continue), paint);
//        drawButton(canvas, getContext().getString(R.string.tap_to_restart), paint);
//
//        mSnake.onDraw(canvas, paint);
//        drawScore(canvas, paint);
//    }
//
//    private void drawSpriteList(Canvas canvas, Paint paint, boolean isOnDraw) {
//        for (MovingSprite autoSprite : mSpriteQueue) {
//            if (autoSprite instanceof Block) {
//                paint.setColor(ColorExtra.getBlockColor(getContext(), ((Block) autoSprite).getLife()));
//            }
//            if (isOnDraw) {
//                autoSprite.onDraw(canvas, paint);
//            } else {
//                autoSprite.draw(canvas, paint);
//            }
//        }
//    }
//
//    private void drawScore(Canvas canvas, Paint paint) {
//        paint.setColor(Color.WHITE);
//        //绘制title
//        float cX = 0.5f * mViewWidth;
//        paint.setTextSize(0.1F * mViewWidth);
//        paint.setTypeface(Typeface.DEFAULT);
//        String text = "" + mScore;
//        canvas.drawText(text, cX, 0.05F * mViewHeight, paint);
//    }
//
//    private void drawTitle(Canvas canvas, Paint paint) {
//        paint.setColor(Color.WHITE);
//        //绘制title
//        float cX = 0.5f * mViewWidth;
//        paint.setTextSize(0.16F * mViewWidth);
//        paint.setTypeface(Typeface.DEFAULT);
//        canvas.drawText(getContext().getString(R.string.title_snake), cX, 0.12F * mViewHeight, paint);
//        paint.setTypeface(Typeface.DEFAULT_BOLD);
//        canvas.drawText(getContext().getString(R.string.title_pk), cX, 0.24F * mViewHeight, paint);
//        paint.setTypeface(Typeface.DEFAULT);
//        canvas.drawText(getContext().getString(R.string.title_block), cX, 0.36F * mViewHeight, paint);
//
//    }
//
//    /**
//     * 绘制分数
//     */
//    private void drawScoreAndBestScoreView(Canvas canvas, int score, int bestScore, Paint paint) {
//        paint.setColor(Color.WHITE);
//        paint.setAlpha(232);
//        RectF rectF = new RectF();
//        float w = 0.4f * mViewWidth;
//        float h = 1.0f * w;
//        rectF.left = 0.5F * (mViewWidth - w);
//        rectF.right = rectF.left + w;
//        rectF.top = 0.1f * mViewHeight;
//        rectF.bottom = rectF.top + h;
//        canvas.drawRoundRect(rectF, 0.1F * rectF.width(), 0.1F * rectF.height(), paint);
//
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(0.3f * rectF.width());
//        canvas.drawText(String.valueOf(score), rectF.centerX(), rectF.top + 0.5F * rectF.height(), paint);
//
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(0.15f * rectF.width());
//        String best = getResources().getString(R.string.best) + ": " + bestScore;
//        canvas.drawText(best, rectF.centerX(), rectF.top + 0.8F * rectF.height(), paint);
//        paint.setAlpha(255);
//    }
//
//    /**
//     * 绘制新加一条命按钮
//     */
//    private void drawContinueButton(Canvas canvas, String text, Paint paint) {
//        //        RectF rectF = new RectF();
//        //        float r = 0.16F * mViewWidth;
//        //        rectF.left = 0.5f * mViewWidth - r;
//        //        rectF.right = rectF.left + 2 * r;
//        //        rectF.top = 0.5f * mViewHeight - r;
//        //        rectF.bottom = rectF.top + 2 * r;
//        //
//        //        paint.setColor(Color.YELLOW);
//        //        canvas.drawCircle(rectF.centerX(), rectF.centerY(), 0.5f * rectF.height(), paint);
//        //        paint.setTextSize(0.12F * rectF.width());
//        //        paint.setColor(Color.BLACK);
//        //        canvas.drawText(text, rectF.centerX(), rectF.centerY() + 0.08f * rectF.height(), paint);
//    }
//
//    /**
//     * 绘制开始按钮/继续按钮
//     */
//    private void drawButton(Canvas canvas, String text, Paint paint) {
//        paint = new Paint();
//        int frequency = 20;
//        long tmp = mFrame % (frequency * 2);
//        if (tmp >= frequency) {
//            tmp = frequency - tmp % frequency;
//        }
//        paint.setTextAlign(Paint.Align.CENTER);
//        mPaint.setAntiAlias(true);//抗锯齿
//        paint.setTextSize(0.06F * mViewWidth);
//        paint.setColor(Color.WHITE);
//        paint.setAlpha((int) (255 * tmp / frequency));
//        canvas.drawText(text, 0.5F * mViewWidth, 0.8f * mViewHeight, paint);
//    }
//
//    private void resetSpeed(float speed) {
//        this.mSpeed = speed;
//        mLastSprite.setSpeed(speed);
//        //遍历sprites
//        for (MovingSprite autoSprite : mSpriteQueue) {
//            autoSprite.setSpeed(speed);
//        }
//    }
//
//
//    @Override
//    protected void timingLogic() {
//
//    }
//
//
//    private int mLastSpriteIndex = 2;
//
//    private void createSpite() {
//        if (mLastSprite.getY() >= mLastSpriteIndex * mLastSprite.getHeight()) {
//            mLastSpriteIndex = (int) ((Math.random() * 100) % 2 + 2);
//            mLastSprite.setY(0);
//        } else {
//            return;
//        }
//
//
//        int maxCount = 5;
//        int blockWH = mViewWidth / maxCount;
//        int circleWH = mViewWidth / 20;
//        int lineBoardW = mViewWidth / 60;
//        int lineBoardMarginTB = (int) (0.1f * blockWH);
//
//        for (int i = 0; i < maxCount; i++) {
//            double random = Math.random();
//            double blockProbability = 0.5;
//            double circleProbability = 0.1;
//            double line1Probability = 0.1;
//            double line2Probability = 0.1;
//            if (random < blockProbability) {
//                Block block = new Block();
//                int age = (int) (Math.random() * mSnake.getLife() * 3);
//                block.setLife(age);
//                block.setWidth(blockWH);
//                block.setHeight(blockWH);
//                block.setSpeedAndAngle(mSpeed, 90);
//                block.centerTo((0.5f + i) * blockWH, -0.5f * blockWH);
//                mSpriteQueue.add(block);
//            } else if (random < blockProbability + circleProbability) {
//                Food circle = new Food();
//                int age = (int) (Math.random() * 5) + 1;
//                circle.setLife(age);
//                circle.setWidth(circleWH);
//                circle.setHeight(circleWH);
//                circle.setSpeedAndAngle(mSpeed, 90);
//                circle.centerTo((0.5f + i) * blockWH, -0.5f * blockWH);
//                mSpriteQueue.add(circle);
//            } else if (random < blockProbability + circleProbability + line1Probability) {
//                Board lineBoard = new Board();
//                lineBoard.setWidth(lineBoardW);
//                lineBoard.setHeight(blockWH - 2 * lineBoardMarginTB);
//                lineBoard.setSpeedAndAngle(mSpeed, 90);
//                int index = (int) (Math.random() * 100 % (maxCount - 1));
//                lineBoard.centerTo((index + 1) * blockWH, -0.5f * blockWH);
//                mSpriteQueue.add(lineBoard);
//            } else if (random < blockProbability + circleProbability + line1Probability + line2Probability) {
//                Board lineBoard = new Board();
//                lineBoard.setWidth(lineBoardW);
//                lineBoard.setHeight(blockWH * 2 - 2 * lineBoardMarginTB);
//                lineBoard.setSpeedAndAngle(mSpeed, 90);
//                int index = (int) (Math.random() * 100 % (maxCount - 1));
//                lineBoard.centerTo((index + 1) * blockWH, -1.0f * blockWH);
//                mSpriteQueue.add(lineBoard);
//            }
//
//        }
//
//
//    }
//
//
//    float mLastTouchX = 0;
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //        return super.onTouchEvent(event);
//        if (mGameState == GameState.PREPARED) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                mGameState = GameState.STARTED;
//            } else {
//                return true;
//            }
//        }
//        if (mGameState == GameState.PARSED) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                resume();
//            } else {
//                return true;
//            }
//        }
//        if (mGameState == GameState.GAME_OVER) {
//            //TODO游戏结束点击继续可续命
//
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                restart();
//            }
//
//            return true;
//        }
//
//        checkBlockAndBoardCollided();
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            mLastTouchX = event.getX();
//        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            float delta = event.getX() - mLastTouchX;
//
//            mSnake.move(delta * 1.0F, 0);
//
//            mSnake.checkX();
//
//            mLastTouchX = event.getX();
//        }
//        return true;
//    }
//
//    private void checkBlockAndBoardCollided() {
//        synchronized (TAG) {
//            mSnake.setMinX(0);
//            mSnake.setMaxX(mViewWidth);
//
//            checkBlockCollided();
//            checkBoardCollided();
//            checkCircleCollided();
//        }
//    }
//
//    private void checkBlockCollided() {
//        boolean isBlockTouched = false;
//        List<Block> blockList = getBlockList();
//        for (Block block : blockList) {
//            //发生碰撞了
//            if (mSnake.onTheBottomOfOther(block)) {
//                //                resetSpeed(speedMin);
//                isSnakeCollideWithBlock = true;
//                isBlockTouched = true;
//                if (mFrame % 3 == 0) {
//                    block.deleteLife(1);
//                    mSnake.deleteLife(1);
//                    mScore++;
//                    checkScoreAndBestScore();
//                    if (mSnake.getLife() <= 0) {
//                        mGameState = GameState.GAME_OVER;
//                    }
//                }
//                break;
//            }
//        }
//        if (!isBlockTouched) {
//            for (Block block : blockList) {
//                //如果蛇不在方块的顶部，则根据此方块重设边界
//                if (!mSnake.onTheTopOfOther(block)) {
//                    resetMinAndMax(block);
//                }
//            }
//            isSnakeCollideWithBlock = false;
//            //            resetSpeed(speedCommon);
//        }
//
//    }
//
//    private void checkScoreAndBestScore() {
//        if (mScore > mBestScore) {
//            mBestScore = mScore;
//            PreferencesUtils.putInt(getContext(), Extra.Key.BEST_SCORE, mScore);
//        }
//    }
//
//    private void checkBoardCollided() {
//        List<Board> lineBoardList = getBoardList();
//
//        for (Board lineBoard : lineBoardList) {
//            resetMinAndMax(lineBoard);
//        }
//    }
//
//    private void checkCircleCollided() {
//        List<Food> circleList = getFoodList();
//        for (Food circle : circleList) {
//            //发生碰撞了
//            if (mSnake.isCollidedWithOther(circle)) {
//                mSnake.addLife(circle.getLife());
//
//                circle.setLife(0);
//                circle.destroy();
//
//                break;
//            }
//        }
//    }
//
//    private void resetMinAndMax(Sprite sprite) {
//        if (sprite.getY() + sprite.getHeight() >= mSnake.getY()
//                && sprite.getY() <= mSnake.getY() + mSnake.getHeight()) {
//            if (mSnake.getCenterX() < sprite.getCenterX()) {
//                float max = sprite.getX();
//                if (mSnake.getMaxX() > max) {
//                    mSnake.setMaxX(max);
//                }
//            } else {
//                float min = sprite.getX() + sprite.getWidth();
//                if (mSnake.getMinX() < min) {
//                    mSnake.setMinX(min);
//                }
//            }
//        }
//    }
//
//    /**
//     * 获取处于活动状态的Block
//     *
//     * @return list
//     */
//    public List<Block> getBlockList() {
//        List<Block> list = new ArrayList<>();
//        for (MovingSprite autoSprite : mSpriteQueue) {
//            if (!autoSprite.isDestroyed() && autoSprite instanceof Block) {
//                Block block = (Block) autoSprite;
//                list.add(block);
//            }
//        }
//        return list;
//    }
//
//
//    /**
//     * 获取处于活动状态的板子
//     *
//     * @return list
//     */
//    public List<Board> getBoardList() {
//        List<Board> list = new ArrayList<>();
//        for (MovingSprite autoSprite : mSpriteQueue) {
//            if (!autoSprite.isDestroyed() && autoSprite instanceof Board) {
//                Board lineBoard = (Board) autoSprite;
//                list.add(lineBoard);
//            }
//        }
//        return list;
//    }
//
//    /**
//     * 获取处于活动状态的食物
//     *
//     * @return list
//     */
//    public List<Food> getFoodList() {
//        List<Food> list = new ArrayList<>();
//        for (MovingSprite autoSprite : mSpriteQueue) {
//            if (!autoSprite.isDestroyed() && autoSprite instanceof Food) {
//                Food lineBoard = (Food) autoSprite;
//                list.add(lineBoard);
//            }
//        }
//        return list;
//    }
//}
