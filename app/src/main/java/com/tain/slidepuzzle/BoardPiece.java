package com.tain.slidepuzzle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class BoardPiece {
    private int oldx, oldy, x, y, boardx, boardy, wantedx, wantedy, originalboardx, originalboardy, width, height, speedx, speedy;
    private IAnimation customAnimation;
    private Bitmap bitmap;
    private Paint paint;
    private boolean isAbsentTile = false;

    public BoardPiece(int boardx, int boardy, int originalboardx, int originalboardy, int width, int height, View view) {
        x = width * boardx;
        y = height * boardy;
        this.width = width;
        this.height = height;
        this.boardx = boardx;
        this.boardy = boardy;
        this.originalboardx = originalboardx;
        this.originalboardy = originalboardy;
        paint = new Paint();
        paint.setColor(view.getResources().getColor(R.color.k));
        paint.setStrokeWidth(15);
    }

    public BoardPiece(int boardx, int boardy, int width, int height, View view) {
        x = width * boardx;
        y = height * boardy;
        this.width = width;
        this.height = height;
        this.boardx = boardx;
        this.boardy = boardy;
        paint = new Paint();
        paint.setColor(view.getResources().getColor(R.color.k));
        paint.setStrokeWidth(15);
        isAbsentTile = true;
    }

    void updateBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public BoardPiece(int boardx, int boardy, int width, int height, IAnimation custom) {
        x = width * boardx;
        y = height * boardy;
        this.width = width;
        this.height = height;
        customAnimation = custom;
        this.boardx = boardx;
        this.boardy = boardy;
    }

    public void scheduleAnimatedMove(int x, int y) {
        wantedx = x * width;
        wantedy = y * height;
        oldx = this.x;
        oldy = this.y;
        autoSpeed();
    }

    public void moveImediate(int x, int y) {
        oldx = this.x;
        oldy = this.y;
        this.x = x * width;
        this.y = y * height;
        wantedx = x;
        wantedy = y;
    }

    void autoSpeed() {
        speedx = 0;
        speedy = 0;
        if (wantedx < x) {
            speedx = -1;
        } else if (wantedx > x) {
            speedx = 1;
        }
        if (wantedy < y) {
            speedy = -1;
        } else if (wantedy > y) {
            speedy = 1;
        }
    }

    void updateSpeed() {
        speedx = getSpeedX();
        speedy = getSpeedY();
    }

    public void onDraw(Canvas canvas) {
        if (isAbsentTile)
            return;
        updateSpeed();
        if (wantedx != x) {
            x += speedx;
        }
        else {
            speedx = 0;
        }
        if (wantedy != y) {
            y += speedy;
        }
        else {
            speedy = 0;
        }
        if (bitmap == null) {
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
        else {
            int left = (bitmap.getWidth() / 4) * originalboardx;
            int top = (bitmap.getHeight() / 4) * originalboardy;
            int right = left + (bitmap.getWidth() / 4);
            int bottom = top + (bitmap.getHeight() / 4);
            canvas.drawBitmap(bitmap, new Rect(left, top, right, bottom), new Rect(x, y, x + width, y + height), null);
        }
    }

    int getSpeedX() {
        if (customAnimation != null) {
            return speedx * (int) customAnimation.speedFactor(oldx, wantedx, x);
        }
        return speedx;
    }

    int getSpeedY() {
        if (customAnimation != null) {
            return speedy * (int) customAnimation.speedFactor(oldy, wantedy, y);
        }
        return speedy;
    }
}
