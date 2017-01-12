package com.wangly.okhttputil.bean;

/**
 * Created by wangly on 2017/1/12.
 */

public class Flower {

    /**
     * flowernum : 0
     * lastvalueb : 100
     * lastvaluey : 365
     * userid : 40
     */

    public int flowernum;
    public int lastvalueb;
    public int lastvaluey;
    public int userid;

    public int getFlowernum() {
        return flowernum;
    }

    public void setFlowernum(int flowernum) {
        this.flowernum = flowernum;
    }

    public int getLastvalueb() {
        return lastvalueb;
    }

    public void setLastvalueb(int lastvalueb) {
        this.lastvalueb = lastvalueb;
    }

    public int getLastvaluey() {
        return lastvaluey;
    }

    public void setLastvaluey(int lastvaluey) {
        this.lastvaluey = lastvaluey;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }


    @Override
    public String toString() {
        return "Flower{" +
                "flowernum=" + flowernum +
                ", lastvalueb=" + lastvalueb +
                ", lastvaluey=" + lastvaluey +
                ", userid=" + userid +
                '}';
    }
}
