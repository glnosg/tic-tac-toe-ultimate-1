package com.glnosg.tictactoeultimate.main_menu.standard_game_settings.instructions;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.glnosg.tictactoeultimate.R;

import androidx.fragment.app.Fragment;

public class InstructionsPageFragment extends Fragment {

    private final String LOG_TAG = InstructionsPageFragment.class.getSimpleName();

    private TextView mInstructionsDisplay, mPageNumberDisplay;
    private ImageView mInstructionImageDisplay;

    private String mInstructions, mFormattedPageNumber;
    private int mImageResourceId;

    public InstructionsPageFragment(String instructions, String formattedPageNumber, int imageId) {
        mInstructions = instructions;
        mFormattedPageNumber = formattedPageNumber;
        mImageResourceId = imageId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout =
                inflater.inflate(R.layout.fragment_instructions_page, container, false);
        mInstructionsDisplay = layout.findViewById(R.id.tv_instructions);
        mInstructionsDisplay.setText(mInstructions);
        mPageNumberDisplay = layout.findViewById(R.id.tv_page_number);
        mPageNumberDisplay.setText(mFormattedPageNumber);
        initImageView(layout);
        return layout;
    }

    private void initImageView(View fragmentLayout) {
        final ImageView mInstructionImageDisplay =
                fragmentLayout.findViewById(R.id.iv_instruction_image);
        ViewTreeObserver vto = mInstructionImageDisplay.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int imageWidth = mInstructionImageDisplay.getWidth();
                Bitmap image =
                        decodeBitmapFromResource(getResources(), mImageResourceId, imageWidth);
                mInstructionImageDisplay.setImageBitmap(image);
                mInstructionImageDisplay.getViewTreeObserver()
                        .removeOnGlobalLayoutListener(this);
            }
        });
    }

    private Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public void setInstructionText(String text) {
        if (text != null) {
            mInstructionsDisplay.setText(text);
        }
    }

    public void setPageNumberText(String text) {
        if (text != null) {
            mPageNumberDisplay.setText(text);
        }
    }

    public void setImage(int resourceId) {
        if (resourceId != 0) {
            mInstructionImageDisplay.setImageResource(resourceId);
        }
    }
}
