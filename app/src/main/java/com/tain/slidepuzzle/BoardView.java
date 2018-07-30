package com.tain.slidepuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tain.slidepuzzle.A_Star_Class_Solver.EuclidianDistanceHeuristicStrategy;
import com.tain.slidepuzzle.A_Star_Class_Solver.Helper;
import com.tain.slidepuzzle.A_Star_Class_Solver.IDAStarClassSolver;
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

	public BoardView(Context context) {
		super(context);
	}

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

    void setBitmap(Bitmap bitmap) {
	    this.bitmap = bitmap;
	    IAnimation anim = new POGAnimationStrategy();
	    for (Place p : board.places()) {
	        p.updateBitmap(bitmap);
	        p.updateCustomAnimation(anim);
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
//        AStarClassSolver solver = new AStarClassSolver(k.table, this, new EuclidianDistanceHeuristicStrategy());
//		ArrayList<Integer> sol = solver.solve(k.start.x, k.start.y);
//		int x = k.start.x, y = k.start.y;
//		for (Integer i : sol) {
//			onStepCallback(x + AStarClassSolver.dy[i], y + AStarClassSolver.dx[i]);
//			x = x + AStarClassSolver.dy[i];
//			y = y + AStarClassSolver.dx[i];
//		}
        IDAStarClassSolver solver = new IDAStarClassSolver(k.table, this, new EuclidianDistanceHeuristicStrategy());
        solver.solve(k.start.x, k.start.y);
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
		for (Place p : board.places()) {
		    p.onDraw(canvas);
        }
		invalidate();
	}
}
