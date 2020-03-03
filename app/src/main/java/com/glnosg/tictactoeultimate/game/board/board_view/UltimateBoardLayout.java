package com.glnosg.tictactoeultimate.game.board.board_view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class UltimateBoardLayout extends ViewGroup {

    private final String LOG_TAG = UltimateBoardLayout.class.getSimpleName();
    private final int DEFAULT_NUMBER_OF_COLUMNS = 3;
    private final int DEFAULT_NUMBER_OF_ROWS = 3;
    private final int DEFAULT_MARGINS = 12;

    private List<View> mChildViews;

    private float mCellWidth, mCellHeight;
    private int mNumberOfColumns, mNumberOfRows;
    private int mMargins;


    public UltimateBoardLayout(Context context, List<View> childViews) {
        super(context);
        mChildViews = childViews;
        initVariables();
        initChildBoards();
    }

    private void initVariables() {
        mNumberOfColumns = DEFAULT_NUMBER_OF_COLUMNS;
        mNumberOfRows = DEFAULT_NUMBER_OF_ROWS;
        mMargins = DEFAULT_MARGINS;
    }

    private void initChildBoards() {
        ViewGroup.LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        for (View currentChildBoard : mChildViews) {
            if (currentChildBoard.getParent() != null) {
                ((ViewGroup) currentChildBoard.getParent()).removeView(currentChildBoard);
            }
            addView(currentChildBoard, params);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == mNumberOfColumns * mNumberOfRows) {
            for (int row = 0; row < mNumberOfRows; row++)
                for (int column = 0; column < mNumberOfColumns; column++) {
                    int childId = ((row * mNumberOfColumns) + column);

                    View currentChild = getChildAt(childId);

                    int topX = column * (int) mCellWidth;
                    int topY = row * (int) mCellHeight;

                    currentChild.layout(
                            topX + mMargins,
                            topY + mMargins,
                            topX + (int) mCellWidth - mMargins,
                            topY + (int) mCellHeight - mMargins);
                }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        setDimens((float) MeasureSpec.getSize(widthMeasureSpec));
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View currentChild = getChildAt(i);
            measureChild(currentChild, widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void setDimens(float canvasWidth) {
        mCellWidth = mCellHeight = canvasWidth / mNumberOfColumns;
    }
}
