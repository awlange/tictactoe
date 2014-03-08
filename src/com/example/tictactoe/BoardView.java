package com.example.tictactoe;

import java.util.logging.Logger;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

public class BoardView extends TileView {

    private static Logger log = Logger.getLogger("BoardView.log");

    /**
     * Labels for the drawables that will be loaded into the TileView class
     */
    public static final int OCCUPIED_X = 1;
    public static final int OCCUPIED_O = 2;

    /**
     * Current turn count. Even = X Odd = O
     */
    private int turn;
    private boolean gameOver;
    private boolean gameTie;
    private int scoreX;
    private int scoreO;
    
	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		turn = 0;
		gameOver = false;
		gameTie = false;
		scoreX = 0;
		scoreO = 0;
	}
	
	private void initTiles() {
		// Set up the map of integer->graphic with their current sizes
		Resources r = this.getContext().getResources();
		resetTiles(3);
        loadTile(OCCUPIED_X, r.getDrawable(R.drawable.tictactoe_x));
        loadTile(OCCUPIED_O, r.getDrawable(R.drawable.tictactoe_o));
	}
	
    public void initNewGame() {
    	turn = 0;
    	gameOver = false;
    	gameTie = false;
    	clearTiles();
    }
    
 
    /**
     * Updates the drawn state of the board
     */
    public void update() {
    	// Handle game over
    	if (isGameOver()) {
    		return;
    	}
    	// Next turn
    	turn += 1;
    }

    /**
     * Trigger redrawing
     */
    public void refresh() {
    	invalidate();
    }

    /**
     * When size of view changes need to update the tiles
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	updateTileSize(w, h);
    	initTiles();
    }
    
    private int getPlayer() {
    	return turn % 2 == 0 ? OCCUPIED_X : OCCUPIED_O;
    }
    
    /**
     * Given the chosen tile coordinate, attempt turn if game is not over.
     * If the tile is already occupied, do not set.
     * Otherwise, set the tile depending whose turn it is.
     */
    public boolean attemptTurn(int x, int y) {
    	if (gameOver || isTileOccupied(x, y)) {
    		return false;
    	}
    	setTile(getPlayer(), x, y);
    	return true;
    }
    
    /**
     * Test for winning conditions.
     * If game is won on this turn, return true
     */
    public boolean isGameOver() {
    	if (gameOver) {
    		return gameOver;
    	}
    	
    	int player = getPlayer(); 
    	
    	// Down
    	for (int x = 0; x < 3; x++) {
    		if (getTile(x, 0) == player && getTile(x, 1) == player && getTile(x, 2) == player) {
    			gameOver = true;
    			updateScore(player);
    			return gameOver;
    		}
    	}
    	
    	// Across
    	for (int y = 0; y < 3; y++) {
    		if (getTile(0, y) == player && getTile(1, y) == player && getTile(2, y) == player) {
    			gameOver = true;
    			updateScore(player);
    			return gameOver;
    		}
    	}
    	
    	// Diagonal down to right
    	if (getTile(0, 0) == player && getTile(1, 1) == player && getTile(2,2) == player) {
    		gameOver = true;
    		updateScore(player);
    		return gameOver;
    	}
    	
    	// Diagonal up to right
    	if (getTile(0, 2) == player && getTile(1, 1) == player && getTile(2,0) == player) {
    		gameOver = true;
    		updateScore(player);
    		return gameOver;
    	}
    	
    	// Check for tie
    	gameTie = true;
    	for (int x = 0; x < 3 && gameTie; x++) {
    		for (int y = 0; y < 3; y++) {
    			if (!isTileOccupied(x, y)) {
    				gameTie = false;
    				break;
    			}
    		}
    	}
    	gameOver = gameTie;
    	
    	// Game must not be over then, or it is tied
    	return gameOver;
    }
    
    /**
     * Only should be called when game is over
     * @return name of winner, X or O
     */
    public String getWinner() {
    	if (gameTie) {
    		return "\nTie!";
    	}
    	return String.format("\n%s wins!", getPlayer() == 1 ? "X" : "O");
    }
    
    private void updateScore(int player) {
    	if (player == 1) {
    		scoreX += 1;
    	} else {
    		scoreO += 1;
    	}
    }
    
    public int getScoreX() {
    	return scoreX;
    }
    
    public int getScoreO() {
    	return scoreO;
    }
}
