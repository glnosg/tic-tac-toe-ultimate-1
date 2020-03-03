package com.glnosg.tictactoeultimate.game.board.board_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.glnosg.tictactoeultimate.R;

import java.util.LinkedList;

import androidx.core.content.ContextCompat;

public class StandardBoardView extends View implements BoardView {

    public static final float COLUMNS_ON_SCREEN_AT_MAX_ZOOM = 5;

    private final String LOG_TAG = StandardBoardView.class.getSimpleName();
    private final int X_INDEX = 0, Y_INDEX = 1;

    private Context mContext;
    private LinkedList<OnBoardEventListener> mOnBoardEventListeners;

    private float mBoardWidth, mBoardHeight;
    private float mCellWidth, mCellHeight;
    private int mNumberOfColumns, mNumberOfRows;

    private Bitmap mImageOnTopOfBoard;
    private Bitmap[][] mShapes;
    private int mBackgroundColor, mCurrentGridColor, mGridColorSolid, mGridColorTransparent;
    private Paint mGridPaint;

    private boolean mIsPanningFunctionActive;
    private float[] mDownClickPosition;
    private float[] mCurrentImagePosition;
    private float[] mFirstTouchPosition, mSecondTouchPosition;

    private final int INVALID_POINTER_ID = -1;
    private int mActivePointerId;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mCurrentScaleFactor, mPendingScaleFactor;
    private float mMinZoom, mMaxZoom;

    private float mBaseColumnScreenRatio;

    public StandardBoardView(Context context, int numberOfColumns, int numberOfRows) {
        super(context);
        mContext = context;
        mNumberOfColumns = numberOfColumns;
        mNumberOfRows = numberOfRows;
        setWillNotDraw(false);
        initVariables();
    }

    private void initVariables() {
        mScaleGestureDetector = new ScaleGestureDetector(mContext, new BoardScaleListener());
        mCurrentScaleFactor = 1.0f;
        initShapesWithEmptyBitmaps();
        initColors();
        initGridPaint();
        mOnBoardEventListeners = new LinkedList<>();
        mIsPanningFunctionActive = true;
        mActivePointerId = INVALID_POINTER_ID;
        mDownClickPosition = new float[2];
        mFirstTouchPosition = new float[2];
        mSecondTouchPosition = new float[2];
        setZoomLimits();
        setInitialBoardPosition();
    }

    private void initShapesWithEmptyBitmaps() {
        mShapes = new Bitmap[mNumberOfColumns][mNumberOfRows];
        for (int row = 0; row < mNumberOfRows; row++)
            for (int column = 0; column < mNumberOfColumns; column++)
                mShapes[column][row] = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }

    private void initColors() {
        mBackgroundColor = ContextCompat.getColor(mContext, R.color.colorBoard);
        mGridColorSolid = ContextCompat.getColor(mContext, R.color.colorChalk);
        mGridColorTransparent = ContextCompat.getColor(mContext, R.color.colorBoardLight);
        mCurrentGridColor = mGridColorSolid;
    }

    private void initGridPaint() {
        mGridPaint = new Paint();
        mGridPaint.setColor(mCurrentGridColor);
        mGridPaint.setStrokeWidth(0);
    }

    private void setZoomLimits() {
        mMinZoom = 1.0f;
        mMaxZoom = mNumberOfColumns / COLUMNS_ON_SCREEN_AT_MAX_ZOOM;
    }

    private void setInitialBoardPosition() {
        mCurrentImagePosition = new float[2];
        mCurrentImagePosition[X_INDEX] = 0;
        mCurrentImagePosition[Y_INDEX] = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBoardBaseDimens(canvas.getWidth());
        correctBoardPosition(canvas.getWidth(), canvas.getHeight());
        canvas.save();
        canvas.translate(mCurrentImagePosition[X_INDEX], mCurrentImagePosition[Y_INDEX]);
        canvas.scale(mCurrentScaleFactor, mCurrentScaleFactor);
        drawBoard(canvas);
        drawImageOnTopOfBoard(canvas);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void setBoardBaseDimens(float canvasWidth) {
        if (mBoardWidth == 0) {
            mBoardWidth = canvasWidth;
            mCellWidth = mBoardWidth / mNumberOfColumns;
            mCellHeight = mCellWidth;
            mBoardHeight = mCellHeight * mNumberOfRows;
            if (!(mBaseColumnScreenRatio > 0 || mBaseColumnScreenRatio < 0)) {
                mBaseColumnScreenRatio = mCellWidth / getResources().getDisplayMetrics().widthPixels;
                notifyOnColumnScreenRatioMeasuredListeners();
            }
        }
    }

    private void correctBoardPosition(float canvasWidth, float canvasHeight) {
        if (mCurrentImagePosition[X_INDEX] > 0) {
            mCurrentImagePosition[X_INDEX] = 0;
        }
        if ((mCurrentImagePosition[X_INDEX] + (mBoardWidth * mCurrentScaleFactor)) < canvasWidth) {
            mCurrentImagePosition[X_INDEX] = canvasWidth - (mBoardWidth * mCurrentScaleFactor);
        }
        if (mCurrentImagePosition[Y_INDEX] > 0) {
            mCurrentImagePosition[Y_INDEX] = 0;
        }
        if ((mCurrentImagePosition[Y_INDEX] + (mBoardHeight * mCurrentScaleFactor)) < canvasHeight) {
            mCurrentImagePosition[Y_INDEX] = canvasHeight - (mBoardHeight * mCurrentScaleFactor);
        }
    }

    private void drawBoard(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
        fillBoardWithShapes(canvas);
        drawGridOnBoard(canvas);
    }

    private void fillBoardWithShapes(Canvas canvas) {
        for (int row = 0; row < mNumberOfRows; row++)
            for (int column = 0; column < mNumberOfColumns; column++) {
                RectF whereToDraw = new RectF(
                        column * mCellWidth, row * mCellWidth,
                        (column + 1) * mCellWidth, (row + 1) * mCellHeight);
                canvas.drawBitmap(mShapes[column][row], null, whereToDraw, null);
            }
    }

    private void drawGridOnBoard(Canvas canvas) {
        for (int i = 1; i < mNumberOfColumns; i++) {
            canvas.drawLine(
                    i * mCellWidth, 0, i * mCellWidth, mBoardHeight, mGridPaint);
        }
        for (int i = 1; i < mNumberOfRows; i++) {
            canvas.drawLine(
                    0, i * mCellHeight, mBoardWidth, i * mCellHeight, mGridPaint);
        }
    }

    private void drawImageOnTopOfBoard(Canvas canvas) {
        if (mImageOnTopOfBoard != null) {
            float left = (mCurrentImagePosition[X_INDEX] * -1) / mCurrentScaleFactor;
            float top = (mCurrentImagePosition[Y_INDEX] * -1) / mCurrentScaleFactor;
            float width = mBoardWidth / mCurrentScaleFactor;
            float height = mBoardWidth / mCurrentScaleFactor;
            RectF whereToDraw = new RectF(left, top, left + width, top + height);
            canvas.drawBitmap(mImageOnTopOfBoard, null, whereToDraw, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case (MotionEvent.ACTION_DOWN):
                setFirstTouchPosition(event);
                mActivePointerId = event.getPointerId(0);
                break;
            case (MotionEvent.ACTION_MOVE):
                calculateNewBoardPosition(event);
                break;
            case (MotionEvent.ACTION_UP):
                if (isSamePositionAsDownClick(event))
                    clickBoard(mDownClickPosition[X_INDEX], mDownClickPosition[Y_INDEX]);
                mIsPanningFunctionActive = true;
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case (MotionEvent.ACTION_CANCEL):
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case (MotionEvent.ACTION_POINTER_DOWN):
                setSecondTouchPosition(event);
                mIsPanningFunctionActive = false;
                break;
            case (MotionEvent.ACTION_POINTER_UP):
                resetSecondTouchPosition(event);
                setActivePointerId(event);
                break;
        }
        return true;
    }

    private void setFirstTouchPosition(MotionEvent event) {
        mFirstTouchPosition[X_INDEX] = event.getX();
        mFirstTouchPosition[Y_INDEX] = event.getY();
        mDownClickPosition[X_INDEX] = mFirstTouchPosition[X_INDEX];
        mDownClickPosition[Y_INDEX] = mFirstTouchPosition[Y_INDEX];
    }

    private void calculateNewBoardPosition(MotionEvent event) {
        if (!mScaleGestureDetector.isInProgress() && mIsPanningFunctionActive) {
            int activePointerIndex = event.findPointerIndex(mActivePointerId);
            float movePositionX = event.getX(activePointerIndex);
            float movePositionY = event.getY(activePointerIndex);
            float distanceX = movePositionX - mFirstTouchPosition[X_INDEX];
            float distanceY = movePositionY - mFirstTouchPosition[Y_INDEX];
            mCurrentImagePosition[X_INDEX] += distanceX;
            mCurrentImagePosition[Y_INDEX] += distanceY;
            mFirstTouchPosition[X_INDEX] = movePositionX;
            mFirstTouchPosition[Y_INDEX] = movePositionY;
            invalidate();
        }
    }

    private boolean isSamePositionAsDownClick(MotionEvent event) {
        boolean isSamePositionX = event.getX() == mDownClickPosition[X_INDEX];
        boolean isSamePositionY = event.getY() == mDownClickPosition[Y_INDEX];
        return isSamePositionX && isSamePositionY;
    }

    private void setSecondTouchPosition(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                    >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
            mSecondTouchPosition[X_INDEX] = event.getX(pointerIndex);
            mSecondTouchPosition[Y_INDEX] = event.getY(pointerIndex);
        }
    }

    private void resetSecondTouchPosition(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            mSecondTouchPosition[X_INDEX] = 0;
            mSecondTouchPosition[Y_INDEX] = 0;
        }
    }

    private void setActivePointerId(MotionEvent event) {
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        int pointerId = event.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mFirstTouchPosition[X_INDEX] = event.getX(newPointerIndex);
            mFirstTouchPosition[Y_INDEX] = event.getY(newPointerIndex);
            mActivePointerId = event.getPointerId(newPointerIndex);
        }
    }

    private class BoardScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mPendingScaleFactor = mCurrentScaleFactor * detector.getScaleFactor();
            mPendingScaleFactor = Math.max(mMinZoom, Math.min(mPendingScaleFactor, mMaxZoom));
            setNewBoardPosition(X_INDEX);
            setNewBoardPosition(Y_INDEX);
            mCurrentScaleFactor = mPendingScaleFactor;
            invalidate();
            notifyOnScaleListeners();
            return true;
        }
    }

    private void setNewBoardPosition (int axisIndex) {
        float focusPointOnScreen =
                (mFirstTouchPosition[axisIndex] + mSecondTouchPosition[axisIndex]) / 2;
        float focusPointOnObject =
                ((focusPointOnScreen - mCurrentImagePosition[axisIndex]) / mCurrentScaleFactor);
        mCurrentImagePosition[axisIndex] =
                focusPointOnScreen - (mPendingScaleFactor * focusPointOnObject);
    }

    private void clickBoard(float positionX, float positionY) {
        float currentCellWidth = mCellWidth * mCurrentScaleFactor;
        float currentCellHeight = mCellHeight * mCurrentScaleFactor;
        int column = (int) ((positionX - mCurrentImagePosition[X_INDEX]) / currentCellWidth);
        int row = (int) ((positionY - mCurrentImagePosition[Y_INDEX]) / currentCellHeight);
        if (isInBounds(column, row))
            for(OnBoardEventListener eventListener : mOnBoardEventListeners) {
                eventListener.onCellClick(column, row);
            }
    }

    private boolean isInBounds(int column, int row) {
        boolean isColumnInBounds = (column >= 0) && (column <= mNumberOfColumns);
        boolean isRowInBounds = (row >= 0) && (row <= mNumberOfRows);
        return (isColumnInBounds && isRowInBounds);
    }

    @Override
    public void updateCell(int column, int row, int shapeId) {
        mShapes[column][row] = BitmapFactory.decodeResource(getResources(), shapeId);
    }

    @Override
    public void clearCell(int column, int row) {
        mShapes[column][row] = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }

    @Override
    public void drawImageOnTopOfBoard(int imageId) {
        mImageOnTopOfBoard = BitmapFactory.decodeResource(getResources(), imageId);
        invalidate();
    }

    @Override
    public void removeImageOnTopOfBoard() {
        mImageOnTopOfBoard = null;
        invalidate();
    }

    @Override
    public void registerOnBoardEventListener(OnBoardEventListener onBoardEventListener) {
        if (!mOnBoardEventListeners.contains(onBoardEventListener)) {
            mOnBoardEventListeners.add(onBoardEventListener);
        }
    }

    @Override
    public void removeOnBoardEventListener(OnBoardEventListener onBoardEventListener) {
        if (mOnBoardEventListeners.contains(onBoardEventListener)) {
            mOnBoardEventListeners.remove(onBoardEventListener);
        }
    }

    private void notifyOnScaleListeners() {
        for (OnBoardEventListener onBoardEventListener : mOnBoardEventListeners) {
            onBoardEventListener.onBoardScale(mCurrentScaleFactor);
        }
    }

    private void notifyOnColumnScreenRatioMeasuredListeners() {
        for (OnBoardEventListener onBoardEventListener : mOnBoardEventListeners) {
            onBoardEventListener.onColumnScreenRatioMeasured();
        }
    }

    @Override
    public void setBoardBackgroundColor(int color) {
        mBackgroundColor = color;
        invalidate();
    }

    @Override
    public void setGridColorSolid(int color) {
        mGridColorSolid = color;
        invalidate();
    }

    @Override
    public void setGridColorTransparent(int color) {
        mGridColorTransparent = color;
        invalidate();
    }

    @Override
    public float getBaseColumnScreenRatio() {
        return mBaseColumnScreenRatio;
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void makeGridTransparent() {
        mCurrentGridColor = mGridColorTransparent;
        initGridPaint();
        invalidate();
    }

    @Override
    public void makeGridSolid() {
        mCurrentGridColor = mGridColorSolid;
        initGridPaint();
        invalidate();
    }

    @Override
    public void zoomOut() {
        mCurrentScaleFactor = 1;
        setInitialBoardPosition();
        invalidate();
    }
}
