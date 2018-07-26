package com.tain.slidepuzzle;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.tain.slidepuzzle.A_Star_Class_Solver.AStarClassSolver;
import com.tain.slidepuzzle.A_Star_Class_Solver.EuclidianDistanceHeuristicStrategy;
import com.tain.slidepuzzle.A_Star_Class_Solver.Helper;
import com.tain.slidepuzzle.A_Star_Class_Solver.ParsedBoard;
import com.tain.slidepuzzle.A_Star_Class_Solver.SolverStepCallback;
import com.tain.slidepuzzle.model.Board;
import com.tain.slidepuzzle.model.Place;
import com.tain.slidepuzzle.model.Tile;


public class BoardView extends View implements SolverStepCallback {

	/** The board. */
	private Board board;

	/** The width. */
	public float width;

	/** The height. */
	public float height;

	Bitmap bitmap;

	/**
	 * Instantiates a new board view.
	 *
	 * @param context
	 *            the context
	 * @param board
	 *            the board
	 */
	public BoardView(Context context, Board board) {
		super(context);
		this.board = board;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

    void setBitmap(Bitmap bitmap) {
	    this.bitmap = bitmap;
	    for (Place p : board.places()) {
	        p.updateBitmap(bitmap);
        }
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		this.width = w / this.board.size();
		this.height = h / this.board.size();
		super.onSizeChanged(w, h, oldw, oldh);
		for (Place p : board.places()) {
			if (p.hasTile()) {
				Tile t = p.getTile();
				t.setWidth((int)this.width);
				t.setHeight((int)this.height);
			}
		}
	}

	/**
	 * Locate place.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the place
	 */
	private Place locatePlace(float x, float y) {
		int ix = (int) (x / width);
		int iy = (int) (y / height);

		return board.at(ix + 1, iy + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		Place p = locatePlace(event.getX(), event.getY());
		if (p != null && p.slidable() && !board.solved()) {
			p.slide();
			invalidate();
		}
		return true;
	}

    @Override
    public void onStepCallback(int b1, int b2) {
	    b1++;
	    b2++;
	    int old1 = 0, old2 = 0;
	    for (Place p : board.places()) {
	        if (!p.hasTile()) {
	            old1 = p.getX();
	            old2 = p.getY();
            }
        }
        int delta1 = b2 - old1, delta2 = b1 - old2;
	    if (delta1 == 0 && delta2 == 0)
	        return;
	    Place p = board.at( old1 + delta1, old2 + delta2);
        if (p != null && p.slidable() && !board.solved()) {
            p.slide();
            invalidate();
        }
    }

	public void autoSolve() {
        ParsedBoard k = Helper.convertToSolverRepresentation(board);
        AStarClassSolver solver = new AStarClassSolver(k.table, this, new EuclidianDistanceHeuristicStrategy());
		ArrayList<Integer> sol = solver.solve(k.start.x, k.start.y);
		int x = k.start.x, y = k.start.y;
		for (Integer i : sol) {
			onStepCallback(x + AStarClassSolver.dy[i], y + AStarClassSolver.dx[i]);
			x = x + AStarClassSolver.dy[i];
			y = y + AStarClassSolver.dx[i];
		}
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.tile_color));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);

		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.tile_color));
		dark.setStrokeWidth(15);

		// Draw the major grid lines
//		for (int i = 0; i < this.board.size(); i++) {
//			canvas.drawLine(0, i * height, getWidth(), i * height, dark);
//			canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
//		}

		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.tile_color));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height * 0.75f);
		foreground.setTextScaleX(width / height);
		foreground.setTextAlign(Paint.Align.CENTER);

		float x = width / 2;
		FontMetrics fm = foreground.getFontMetrics();
		float y = (height / 2) - (fm.ascent + fm.descent) / 2;

		for (Place p : board.places()) {
		    p.onDraw(canvas);
        }

		invalidate();
	}
}
