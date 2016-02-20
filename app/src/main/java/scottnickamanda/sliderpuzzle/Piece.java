package scottnickamanda.sliderpuzzle;

import android.graphics.Bitmap;

/**
 * A custom piece that contains various information used for the game
 *
 * @author Scott Wilson
 */
public class Piece {
    /* The number in which the piece will display*/
    private int number;
    /* The image in which the piece will display*/
    private Bitmap image;

    /**
     * Constructor that takes one parameter, used in initializing the blank piece
     *
     * @param number the number that should be held as data for the piece
     */
    public Piece(int number) {
        super();
        //Incremented +1 for user visualization of starting at 1 instead of 0
        this.number = number + 1;
    }

    /**
     * Constructor that takes two parameters
     * These will be held as the data in the piece
     *
     * @param number the number in which to set the piece to
     * @param image the image in which to set the piece to
     */
    public Piece(int number, Bitmap image) {
        super();
        //Incremented +1 for user visualization of starting at 1 instead of 0
        this.number = number + 1;
        this.image = image;
    }

    /**
     * Allow the number contained in the piece to be retrieved
     *
     * @return the number held by the piece
     */
    public int getNumber() {
        return number;
    }

    /**
     * Allow the image contained in the piece to be retrieved
     *
     * @return the image held by the piece
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Change the number contained in the piece
     *
     * @param number the number in which the piece should be set to
     */
    public void setNumber(int number) {
        //Increment by 1 for user visualization starting at 1 instead of 0
        this.number = number + 1;
    }

    /**
     * Change the image contained in the piece
     *
     * @param image the image in which the piece should be set to
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * A string representation of the number contained in the piece
     *
     * @return a string representation of the number
     */
    public String toString() {
        //If the number is zero, no number should be visible so we return an empty string
        if (number == 0)
            return "";
        //Otherwise return a string representation of the number
        return Integer.toString(number);
    }
}
