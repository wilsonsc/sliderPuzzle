package scottnickamanda.sliderpuzzle;

public class Piece {
    private int number;
    private int imageId;

    public Piece() {
        super();
    }

    public Piece(int number) {
        super();
        this.number = number + 1;
    }

    public Piece(int number, int imageId) {
        super();
        this.number = number;
        this.imageId = imageId;
    }

    public int getNumber() {
        return number;
    }

    public int getimageId() {
        return imageId;
    }

    public void setNumber(int number) {
        this.number = number + 1;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

        public String toString() {
            if (number == 0)
                return "";
            return Integer.toString(number);
    }
}
