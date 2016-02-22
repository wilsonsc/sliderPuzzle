package scottnickamanda.sliderpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Controls the board for the Slider puzzle game
 *
 * @Author Scott Wilson
 */
public class GameBoard {
    /* The number of pieces on the current puzzle, default value initialized*/
    private int gameSize;
    /* The number of columns on the current puzzle, default value initialized*/
    private int columns;
    /* True if the game is currently in progress, false if the game has not started or has ended*/
    private boolean gameInProgress;
    /* A representation of the index of the "blank" piece*/
    private int blankPiece;
    /* The number of moves the player has made*/
    private int moveCounter = 0;
    /* An array to hold all of the pieces that the puzzle is made of*/
    private Piece[] pieces;
    /* An array holding the individual chopped up image pieces*/
    private Bitmap[] choppedImage;

    /**
     * Default constructor without parameters
     * default 3x3 board will be used
     */
    public GameBoard() {
        gameSize = 9;
        columns = 3;
    }

    /**
     * Constructor with custom board size
     *
     * @param size the number of columns and rows of the board (size x size)
     */
    public GameBoard(int size) {
        gameSize = size * size;
        columns = size;
        blankPiece = gameSize-1;
        choppedImage = new Bitmap[gameSize];
    }

    /**
     * Obtain an array of the current game pieces
     * @return the current array of pieces
     */
    public Piece[] getPieces() {
        return pieces;
    }

    /**
     * The size of the columns and rows of the board
     * @return the length of a column or row
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Shuffles the board for the user
     * Simulates a random "roll" from 1-4 (0-3)
     * Each value of the roll corresponds to a direction in which we attempt to brute force move
     * the piece to.
     * This will be repeated equal to the number of tiles squared * 2 times
     * ****This implementation ensures the game will ALWAYS have a valid solution****
     * A pure random shuffling would occur in unsolvable puzzles
     */
    public void shuffleBoard() {
        /* The value of the random roll*/
        int roll;
        for (int i = 0; i < gameSize*gameSize*2; i++) {
            //Roll
            roll = (int) (Math.random() * 4);
            //Force move based off what was rolled
            switch (roll) {
                case 0:
                    if (checkMove(blankPiece - columns))
                        movePieces(blankPiece - columns);
                    break;
                case 1:
                    if (checkMove(blankPiece + columns))
                        movePieces(blankPiece + columns);
                    break;
                case 2:
                    if (checkMove(blankPiece - 1))
                        movePieces(blankPiece - 1);
                    break;
                case 3:
                    if (checkMove(blankPiece + 1))
                        movePieces(blankPiece + 1);
                    break;
                default:
                    break;

            }
        }
        //Reset move counter
        moveCounter = 0;
    }

    /**
     * Checks to see if the board is in a state which is won
     * @return true if the game is won, false otherwise
     */
    public boolean checkIfWon() {
        //Check bottom right corner for validity
        if (blankPiece != gameSize-1)
            return false;
        //Check pieces, starting with upper left for validity
        for (int i = 0; i < gameSize - 1; i++) {
            if (pieces[i].getNumber() != i+1)
                return false;
        }
        //Game is over, set variable appropriately
        gameInProgress = false;
        return true;
    }


    /**
     * User is ready to play a new game, create the game with the appropriate size
     */
    public void newGame() {
        //Declare pieces array size based off current game size
        pieces = new Piece[gameSize];
        //Initialize pieces and declare text and image
        for (int i = 0; i < gameSize - 1; i++) {
            pieces[i] = new Piece(i, choppedImage[i]);
        }
        //Final piece does not get an image and gets a number that will not be displayed
        //-1 indicates the piece is to not be shown to the user
        pieces[gameSize-1] = new Piece(-1);
        //Begin the game
        gameInProgress = true;
        //Reset move counter
        moveCounter = 0;
    }

    /**
     * Checks to see if this is a valid move
     * If the piece that was clicked on is adjacent to the blank piece, it is
     */
    public boolean checkMove(int pieceNumber) {
        //Game must be in progress to be a valid move
        if (!gameInProgress)
            return false;
        //Must be clicking on a piece that is not the blank piece
        if (pieceNumber == blankPiece)
            return false;
        //Check to see if piece is adjacent to the blank piece
        if (pieceNumber + columns == blankPiece ||
                pieceNumber - columns == blankPiece ||
                (pieceNumber + 1 == blankPiece || pieceNumber - 1 == blankPiece) &&
                        pieceNumber / columns == blankPiece / columns)
            return true;
        return false;
    }

    /**
     * Swaps the values of the two pieces
     * @param pieceNumber the index of the piece that was clicked
     */
    public void movePieces(int pieceNumber) {
        //Make sure the piece exists
        if (pieceNumber >= gameSize || pieceNumber < 0)
            return;
        //Sets the blank piece to the values which were in the piece the user clicked
        pieces[blankPiece].setNumber(pieces[pieceNumber].getNumber() - 1);
        pieces[blankPiece].setImage(pieces[pieceNumber].getImage());
        //Sets the piece the user clicked to the values of the blank piece
        pieces[pieceNumber].setNumber(-1);
        pieces[pieceNumber].setImage(choppedImage[gameSize-1]);
        //Locally save the index of the blank piece
        blankPiece = pieceNumber;
        //Increase the move counter and update the view
        moveCounter++;

    }

    /**
     * Getter for move counter
     * @return the current number of moves made by the user
     */
    public int getMoveCounter() {
        return moveCounter;
    }

    /**
     * Setter for images to be used on pieces
     * @param images an array of images to set on the pieces
     */
    public void setImages(Bitmap[] images) {
        choppedImage = images;
    }

    /**
     * Getter for is the game in progress variable
     * @return true if the game is in progress
     */
    public boolean getGameProgress() {
        return gameInProgress;
    }

    /**
     * Sets the game in progress variable
     * @param bool true if game is to be in progress
     */
    public void setGameProgress(boolean bool) {
        gameInProgress = bool;
    }

    /**
     * Setter for the blank piece on the board
     * @param piece the index of the blank piece
     */
    public void setBlankPiece(int piece) {
        blankPiece = piece;
    }

    /**
     * Getter for board size (column x row)
     * @return value of the size of the game
     */
    public int getBoardSize() {
        return gameSize;
    }

}
