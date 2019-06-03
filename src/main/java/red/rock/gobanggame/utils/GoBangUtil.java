package red.rock.gobanggame.utils;

import red.rock.gobanggame.entity.SeatRecord;

import java.util.*;

/**
 * TODO
 *
 * @author tudou
 * @version 1.0
 * @date 2019/6/1 11:07
 **/
public class GoBangUtil {

    /**
     * 随机选取开场顺序
     */
    public static   int randomStart(){
        Random random=new Random();
        int i=random.nextInt(2);
        return i==1?1:-1;
    }

    /**
     * 人人五子棋算法
     */
    public static boolean goBang(List<SeatRecord> seatRecords,SeatRecord seatRecord){
        boolean flag=false;
        int a=4;
        int m_color=seatRecord.getColor();
        int m_x=seatRecord.getX();
        int m_y=seatRecord.getY();
        List<Integer> heng=new ArrayList<>();
        List<Integer> shu=new ArrayList<>();
        List<Integer> zuo=new ArrayList<>();
        List<Integer> you=new ArrayList<>();
            for(SeatRecord seatRecord1:seatRecords){
                int up=seatRecord1.getY()-m_y;
                int down=seatRecord1.getX()-m_x;
                if(up==0&&down==0){
                    zuo.add(seatRecord1.getY());
                    you.add(seatRecord1.getX());
                    heng.add(seatRecord1.getY());
                    shu.add(seatRecord1.getX());
                }else {
                    if (seatRecord1.getY() >= m_y - a && seatRecord1.getY() <= m_y + a && seatRecord1.getColor() == m_color) {
                        heng.add(seatRecord1.getY());
                    }
                    if (seatRecord1.getX() <= m_x + a && seatRecord1.getX() >= m_x - a && seatRecord1.getColor() == m_color) {
                        shu.add(seatRecord1.getX());
                    }
                    if (up!=0&&down!=0&&up/down == -1
                            && seatRecord1.getColor() == m_color && seatRecord1.getX() <= m_x + a && seatRecord1.getX() >= m_x - a &&
                            seatRecord1.getY() >= m_y - a && seatRecord1.getY() <= m_y + a) {
                        zuo.add(seatRecord1.getY());
                    }
                    if (up!=0&&down!=0&&up/down == 1
                            && seatRecord1.getColor() == m_color && seatRecord1.getX() <= m_x + a && seatRecord1.getX() >= m_x - a &&
                            seatRecord1.getY() >= m_y - a && seatRecord1.getY() <= m_y + a) {
                        you.add(seatRecord1.getX());
                    }
                }
            }
            if(isList(you)||isList(zuo)||isList(heng)||isList(shu)){
                flag=true;
            }
        return flag;
    }

    private static boolean isList(List<Integer> list){
        int[] a=new int[list.size()];
        int length=list.size();
        int b=0;
        for(Integer integer:list){
            a[b++]=integer;
        }
        boolean flag=false;
        for(int i=0;i<length-1;i++){
            for(int j=0;j<length-1-i;j++){
                if(a[j]>a[j+1]){
                    int temp=a[j];
                    a[j]=a[j+1];
                    a[j+1]=temp;
                }
            }
        }
        for(int i=0;i<length;i++){
            int count=1;
            for(int j=i;j<length-1;j++){
                if(a[j+1]-a[j]!=1){
                    break;
                }
                count++;
                if(count==5){
                    flag=true;
                    break;
                }
            }
        }
        return flag;
   }

    public static void main(String[] args) {
        Map<String,String> map=new HashMap<>();
        List<SeatRecord> seatRecords=new ArrayList<>();
        System.out.println(seatRecords.size());
        if(seatRecords == null){
            System.out.println(11);
        }
//        map.put("1","11");
//        map.put("1","21");
//        System.out.println(map.get("1"));
//
//        if("11".equals(null)){
//            System.out.println(11);
//        }
    }

}
