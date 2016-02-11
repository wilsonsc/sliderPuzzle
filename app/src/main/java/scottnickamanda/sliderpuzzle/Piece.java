package scottnickamanda.sliderpuzzle;

import android.graphics.Bitmap;

public class Piece {
    private int number;
    private int imageId;
    private Bitmap image;

    public Piece() {
        super();
    }

    public Piece(int number) {
        super();
        this.number = number + 1;
    }

    public Piece(int number, Bitmap image) {
        super();
        this.number = number + 1;
        this.image = image;
    }

    public int getNumber() {
        return number;
    }

    public int getimageId() {
        return imageId;
    }
    public Bitmap getImage() {
        return image;
    }

    public void setNumber(int number) {
        this.number = number + 1;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String toString() {
            if (number == 0)
                return "";
            return Integer.toString(number);
    }
}
