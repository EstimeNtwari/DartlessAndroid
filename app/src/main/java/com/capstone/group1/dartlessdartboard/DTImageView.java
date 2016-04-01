package com.capstone.group1.dartlessdartboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class DTImageView extends View {

    public Bitmap imageBitmap;

    public DTImageView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(imageBitmap != null)
            canvas.drawBitmap(imageBitmap, 0, 0, null);
    }
}