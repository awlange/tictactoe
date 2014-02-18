/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tictactoe;

import java.util.logging.Logger;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * TileView: a View-variant designed for handling arrays of "icons" or other drawables.
 *
 * This is based on the TileView from the example Snake game but with several modifications.
 */
public class TileView extends View {

	private static final Logger log = Logger.getLogger("TileView.logger");
	
    /**
     * Parameters controlling the size of the tiles and their range within view. Width/Height are in
     * pixels, and Drawables will be scaled to fit to these dimensions. X/Y Tile Counts are the
     * number of tiles that will be drawn.
     */

    protected static int mTileSize;

    protected static final int TILE_COUNT_X = 3;
    protected static final int TILE_COUNT_Y = 3;

    private static int mXOffset;
    private static int mYOffset;

    private final Paint mPaint = new Paint();

    /**
     * A hash that maps integer handles specified by the subclasser to the drawable that will be
     * used for that reference
     */
    private Bitmap[] mTileArray;

    /**
     * A two-dimensional array of integers in which the number represents the index of the tile that
     * should be drawn at that locations
     */
    private final int[][] mTileGrid = new int[TILE_COUNT_X][TILE_COUNT_Y];

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTileSize = 1;
    }

    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTileSize = 1;
    }

    /**
     * Resets all tiles to 0 (empty)
     *
     */
    public void clearTiles() {
        for (int x = 0; x < TILE_COUNT_X; x++) {
            for (int y = 0; y < TILE_COUNT_Y; y++) {
                setTile(0, x, y);
            }
        }
    }

    /**
     * Function to set the specified Drawable as the tile for a particular integer key.
     *
     * @param key
     * @param tile
     */
    public void loadTile(int key, Drawable tile) {
    	log.info(String.format("loadTile: mTileSize = %d", mTileSize));
    	
        Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, mTileSize, mTileSize);
        tile.draw(canvas);

        mTileArray[key] = bitmap;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < TILE_COUNT_X; x += 1) {
            for (int y = 0; y < TILE_COUNT_Y; y += 1) {
                if (mTileGrid[x][y] > 0) {
                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]], 
                    		mXOffset + x * mTileSize,
                            mYOffset + y * mTileSize, mPaint);
                }
            }
        }

    }

    /**
     * Rests the internal array of Bitmaps used for drawing tiles, and sets the maximum index of
     * tiles to be inserted
     *
     * @param tilecount
     */

    public void resetTiles(int tilecount) {
        mTileArray = new Bitmap[tilecount];
    }

    /**
     * Used to indicate that a particular tile (set with loadTile and referenced by an integer)
     * should be drawn at the given x/y coordinates during the next invalidate/draw cycle.
     *
     * @param tileindex
     * @param x
     * @param y
     */
    protected void setTile(int tileindex, int x, int y) {
        mTileGrid[x][y] = tileindex;
    }
    
    protected boolean isTileOccupied(int x, int y) {
    	return mTileGrid[x][y] > 0;
    }
    
    protected int getTile(int x, int y) {
    	return mTileGrid[x][y];
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	updateTileSize(w, h);
    }
    
    protected void updateTileSize(int w, int h) {
    	int smallDimension = w < h ? w : h;
    	mTileSize = (int) Math.floor(smallDimension / 3.0);
        mXOffset = ((w - (mTileSize * TILE_COUNT_X)) / 2);
        mYOffset = ((h - (mTileSize * TILE_COUNT_Y)) / 2);
        clearTiles();	
    }

    /** because the tile size is the side of a square, this function can be used for both x and y positions */
    public int getTileIndex(float x) {
    	return (int) Math.floor(x / mTileSize);
    }
}
