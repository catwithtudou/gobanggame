package red.rock.gobanggame.entity;

import lombok.Data;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/1 10:58
 **/

@Data
public class UserRecord {

    private int isReady;
    private int isAllow;
    private int color;

    public UserRecord(){

    }

    public UserRecord(int isReady,int isAllow){
        this.isAllow=isAllow;
        this.isReady=isReady;
    }

}
