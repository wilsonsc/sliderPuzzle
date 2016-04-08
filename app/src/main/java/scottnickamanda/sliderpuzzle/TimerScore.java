package scottnickamanda.sliderpuzzle;

/**
 * Comparable class to sort times in high score list
 *
 * Created by amandabuhr on 4/7/16.
 */
public class TimerScore implements Comparable<TimerScore> {

    private String scoreDate;
    public int scoreTime;
    public String boardSize;

    public TimerScore(String date, int time){
        scoreDate = date;
        scoreTime = time;
//        boardSize = size;
    }

    public int compareTo(TimerScore sc){
        //return 0 if equal
        //1 if passed greater than this
        //-1 if this greater than passed
        return sc.scoreTime<scoreTime? 1 : sc.scoreTime>scoreTime? -1 : 0;
    }

    public String getScoreText() {
        return scoreDate+" - "+scoreTime;
    }
}
