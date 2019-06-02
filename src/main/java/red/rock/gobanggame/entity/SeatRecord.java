package red.rock.gobanggame.entity;

import lombok.Data;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/1 10:59
 **/
@Data
public class SeatRecord {

    private int x;
    private int y;
    private int real;
    private int color;

    public SeatRecord(){

    }

    public SeatRecord(int x,int y,int color){
        this.x=x;
        this.y=y;
        this.color=color;
    }

}
