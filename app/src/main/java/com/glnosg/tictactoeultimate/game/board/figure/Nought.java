package com.glnosg.tictactoeultimate.game.board.figure;

import com.glnosg.tictactoeultimate.R;

import java.util.ArrayList;
import java.util.Random;

public class Nought implements Figure {

    private static final Object LOCK = new Object();
    private static Nought sInstance;

    private static ArrayList<Integer> sSmallSolidIds;
    private static ArrayList<Integer> sSmallTransparentIds;
    private static ArrayList<Integer> sSmallWinningIds;
    private static ArrayList<Integer> sSmallWinningTransparentIds;

    private static ArrayList<Integer> sMediumSolidIds;
    private static ArrayList<Integer> sMediumTransparentIds;
    private static ArrayList<Integer> sMediumWinningIds;
    private static ArrayList<Integer> sMediumWinningTransparentIds;

    private static ArrayList<Integer> sBigSolidIds;
    private static ArrayList<Integer> sBigTransparentIds;
    private static ArrayList<Integer> sBigWinningIds;
    private static ArrayList<Integer> sBigWinningTransparentIds;

    private static int mHowManyAvailableFigures;

    private Nought() {
        mHowManyAvailableFigures = 0;
        initIdLists();
        addFiguresToLists();
    }

    public static Nought getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Nought();
            }
        }
        return sInstance;
    }

    private void initIdLists() {
        sSmallSolidIds = new ArrayList<>();
        sSmallTransparentIds = new ArrayList<>();
        sSmallWinningIds = new ArrayList<>();
        sSmallWinningTransparentIds = new ArrayList<>();
        sMediumSolidIds = new ArrayList<>();
        sMediumTransparentIds = new ArrayList<>();
        sMediumWinningIds = new ArrayList<>();
        sMediumWinningTransparentIds = new ArrayList<>();
        sBigSolidIds = new ArrayList<>();
        sBigTransparentIds = new ArrayList<>();
        sBigWinningIds = new ArrayList<>();
        sBigWinningTransparentIds = new ArrayList<>();
    }

    private void addFiguresToLists() {
        addShape(
                R.drawable.shape_nought1_small_solid,
                R.drawable.shape_nought1_small_transparent20,
                R.drawable.shape_nought1_small_winning,
                R.drawable.shape_nought1_small_winning_transparent20,
                R.drawable.shape_nought1_medium_solid,
                R.drawable.shape_nought1_medium_transparent20,
                R.drawable.shape_nought1_medium_winning,
                R.drawable.shape_nought1_medium_winning_transparent20,
                R.drawable.shape_nought1_big_solid,
                R.drawable.shape_nought1_big_transparent20,
                R.drawable.shape_nought1_big_winning,
                R.drawable.shape_nought1_big_winning_transparent20);
    }

    private void addShape(
            int smallSolidId,
            int smallTransparentId,
            int smallWinningId,
            int smallWinningTransparentId,
            int mediumSolidId,
            int mediumTransparentId,
            int mediumWinningId,
            int mediumWinningTransparentId,
            int bigSolidId,
            int bigTransparentId,
            int bigWinningId,
            int bigWinningTransparentId) {
        sSmallSolidIds.add(smallSolidId);
        sSmallTransparentIds.add(smallTransparentId);
        sSmallWinningIds.add(smallWinningId);
        sSmallWinningTransparentIds.add(smallWinningTransparentId);
        sMediumSolidIds.add(mediumSolidId);
        sMediumTransparentIds.add(mediumTransparentId);
        sMediumWinningIds.add(mediumWinningId);
        sMediumWinningTransparentIds.add(mediumWinningTransparentId);
        sBigSolidIds.add(bigSolidId);
        sBigTransparentIds.add(bigTransparentId);
        sBigWinningIds.add(bigWinningId);
        sBigWinningTransparentIds.add(bigWinningTransparentId);
        mHowManyAvailableFigures++;
    }

    @Override
    public int getHowManyAvailableShapes() {
        return mHowManyAvailableFigures;
    }

    @Override
    public int getRandomShapeIndex() {
        Random random = new Random();
        return random.nextInt(mHowManyAvailableFigures);
    }

    @Override
    public int getSmallBitmapId(int shapeIndex, boolean isSolid, boolean isWinning) {
        int validShapeIndex = validateShapeIndex(shapeIndex);
        if (isSolid && !isWinning) return sSmallSolidIds.get(validShapeIndex);
        else if (isSolid) return  sSmallWinningIds.get(validShapeIndex);
        else if (!isWinning) return sSmallTransparentIds.get(validShapeIndex);
        else return sSmallWinningTransparentIds.get(validShapeIndex);
    }

    @Override
    public int getMediumBitmapId(int shapeIndex, boolean isSolid, boolean isWinning) {
        int validShapeIndex = validateShapeIndex(shapeIndex);
        if (isSolid && !isWinning) return sMediumSolidIds.get(validShapeIndex);
        else if (isSolid) return  sMediumWinningIds.get(validShapeIndex);
        else if (!isWinning) return sMediumTransparentIds.get(validShapeIndex);
        else return sMediumWinningTransparentIds.get(validShapeIndex);
    }

    @Override
    public int getBigBitmapId(int shapeIndex, boolean isSolid, boolean isWinning) {
        int validShapeIndex = validateShapeIndex(shapeIndex);
        if (isSolid && !isWinning) return sBigSolidIds.get(validShapeIndex);
        else if (isSolid) return  sBigWinningIds.get(validShapeIndex);
        else if (!isWinning) return sBigTransparentIds.get(validShapeIndex);
        else return sBigWinningTransparentIds.get(validShapeIndex);
    }

    @Override
    public int getBigSolidBitmapId(int shapeIndex) {
        if(shapeIndex < 0 || shapeIndex >= mHowManyAvailableFigures)
            shapeIndex = getRandomShapeIndex();
        return sBigSolidIds.get(shapeIndex);
    }

    @Override
    public int getBigWinningBitmapId(int shapeIndex) {
        if(shapeIndex < 0 || shapeIndex >= mHowManyAvailableFigures)
            shapeIndex = getRandomShapeIndex();
        return sBigWinningIds.get(shapeIndex);
    }

    private int validateShapeIndex(int validatedShapeIndex) {
        boolean isShapeIndexValid =
                validatedShapeIndex >= 0 || validatedShapeIndex < mHowManyAvailableFigures;
        if(isShapeIndexValid) return validatedShapeIndex;
        else return getRandomShapeIndex();
    }
}

