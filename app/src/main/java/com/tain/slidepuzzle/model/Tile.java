// $Id: Tile.java,v 1.1 2014/12/01 00:33:19 cheon Exp $

package com.tain.slidepuzzle.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.tain.slidepuzzle.IAnimation;



public class Tile {
    private static final int TEXT_SIZE = 48;
    /** The number of this tile. */
    private final int number, x, y;
    private int wantedx, wantedy, width, height, oldx, oldy, posxscreen, posyscreen, speedx, speedy, left, right, top, bottom;
    private Bitmap bitmap;
    private Paint paint, paint2;
    private IAnimation customAnimation;
    private Place place;
    private Rect screenPosition, bitmapCut;

    public Tile(int number, int origX, int origY, Place p) {
        this.number = number;
        x = origX - 1;
        y = origY - 1;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(15);
        paint2 = new Paint();
        paint2.setColor(Color.RED);
        paint2.setStrokeWidth(5);
        paint2.setTextSize(48);
        bitmap = null;
        this.place = p;
        bitmapCut = new Rect(left, top, right, bottom);
        screenPosition = new Rect(posxscreen, posyscreen, posxscreen + width, posyscreen + height);
    }

    void setPlace(Place p) {
        place = p;
    }

    public void setWidth(int width) {
        this.width = width;
        posxscreen = width * (place.getX() - 1);
        wantedx = posxscreen;
    }

    public void setHeight(int height) {
        this.height = height;
        posyscreen = height * (place.getY() - 1);
        wantedy = posyscreen;
    }

    public void updateBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        calculateBitmap();
    }

    public void setCustomAnimation(IAnimation animation) {
        customAnimation = animation;
    }

    /** Return the number of this tile. */
    public int number() {
        return number;
    }

    public void scheduleAnimatedMove(int x, int y) {
        wantedx = y * width;
        wantedy = x * height;
        oldx = this.posxscreen;
        oldy = this.posyscreen;
        autoSpeed();
    }

    public void moveImediate(int x, int y) {
        oldx = this.posxscreen;
        oldy = this.posyscreen;
        this.posxscreen = y * width;
        this.posyscreen = x * height;
        wantedx = x;
        wantedy = y;
    }

    void snapToPlaceImediate() {
        oldx = this.posxscreen;
        oldy = this.posyscreen;
        posxscreen = (place.getX() - 1) * width;
        posyscreen = (place.getY() - 1) * height;
        wantedx = posxscreen;
        wantedy = posyscreen;
    }

    void autoSpeed() {
        speedx = 0;
        speedy = 0;
        if (wantedx < posxscreen) {
            speedx = -1;
        } else if (wantedx > posxscreen) {
            speedx = 1;
        }
        if (wantedy < posyscreen) {
            speedy = -1;
        } else if (wantedy > posyscreen) {
            speedy = 1;
        }
    }

    void updateSpeed() {
        speedx = getSpeedX();
        speedy = getSpeedY();
    }

    public void onDraw(Canvas canvas) {
        if (width == 0 && height == 0)
            return;
        updateSpeed();
        if (wantedx != posxscreen) {
            posxscreen += speedx;
        }
        else {
            speedx = 0;
        }
        if (wantedy != posyscreen) {
            posyscreen += speedy;
        }
        else {
            speedy = 0;
        }
        if (bitmap == null) {
            canvas.drawRect(posxscreen, posyscreen, posxscreen + width, posyscreen + height, paint);
            canvas.drawText(String.valueOf(number), posxscreen, posyscreen + 48, paint2);
        }
        else {
            screenPosition.set(posxscreen, posyscreen, posxscreen + width, posyscreen + height);
            canvas.drawBitmap(bitmap, bitmapCut, screenPosition, null);
        }
    }

    int getSpeedX() {
        if (customAnimation != null) {
            return speedx * (int) customAnimation.speedFactor(oldx, wantedx, posxscreen);
        }
        return speedx;
    }

    int getSpeedY() {
        if (customAnimation != null) {
            return speedy * (int) customAnimation.speedFactor(oldy, wantedy, posyscreen);
        }
        return speedy;
    }

    private void calculateBitmap() {
        left = (bitmap.getWidth() / 4) * y;
        top = (bitmap.getHeight() / 4) * x;
        right = left + (bitmap.getWidth() / 4);
        bottom = top + (bitmap.getHeight() / 4);
        bitmapCut.set(left, top, right, bottom);
    }
}
