package com.example.tictactoe;

import java.util.logging.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;


public class TicTacToe extends Activity {

	private static final Logger log = Logger.getLogger("TicTacToe.log");
	
	private BoardView boardView;
	private TextView scoreTextView;
	private TextView winnerTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		scoreTextView = (TextView) findViewById(R.id.textViewScore);
		winnerTextView = (TextView) findViewById(R.id.textViewWinner);
		boardView = (BoardView) findViewById(R.id.boardView);
		
		boardView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Transform the touch coordinates into tile index
				int tileX = boardView.getTileIndex(event.getX());
				int tileY = boardView.getTileIndex(event.getY());
				
				if (boardView.attemptTurn(tileX, tileY)) {
					// Refresh the screen and update the state of the game
					boardView.refresh();
					boardView.update();
				}
				
				if (boardView.isGameOver()) {
					showWinner();
					updateScore();
				}
				
				return false;
			}
		});
		
		boardView.initNewGame();
	}
	
	/**
	 * Respond to button click
	 * @param v
	 */
	public void newGame(View v) {
		clearWinner();
		boardView.initNewGame();
		boardView.refresh();
	}

	private void showWinner() {
		winnerTextView.setText(boardView.getWinner());
	}
	
	private void clearWinner() {
		winnerTextView.setText("");
	}
	
	private void updateScore() {
		scoreTextView.setText(String.format("Score\n X: %d  O: %d", 
				boardView.getScoreX(), boardView.getScoreO()));
	}
}
