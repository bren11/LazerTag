package com.android.lazertag;

import org.opencv.core.Point;

/**
 * Created by Bren on 1/29/2018.
 */

public class Point2 extends Point implements Comparable{
    public Point2(double x, double y){
        super(x,y);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Point2))
            throw new ClassCastException();

        Point2 e = (Point2) o;

        if (x == e.x){return 0;}
        return x < e.x? 1: -1;
    }
}
