package com.android.lazertag;

import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

//import static org.lasarobotics.vision.android.Util.getContext;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.opencv.imgproc.Imgproc.adaptiveThreshold;
import static org.opencv.imgproc.Imgproc.cvtColor;

/**
 * Created by tommy on 12/6/16.
 */

public class RectangleFindr {

    private Mat findLines(String default_file) {
        // Declare the output variables
        Mat dst = new Mat(), cdst = new Mat(), cdstP;
        // Load an image
        Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_COLOR);
        // Check if image is loaded fine
        if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- "
                    + default_file +"] \n");
            System.exit(-1);
        }
        // Edge detection
        Imgproc.Canny(src, dst, 50, 200, 3, false);
        // Copy edges to the images that will display the results in BGR
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        cdstP = cdst.clone();
        // Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        // Probabilistic Line Transform
        Mat linesP = new Mat(); // will hold the results of the detection
        Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 10); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        /* Show results
        HighGui.imshow("Source", src);
        HighGui.imshow("Detected Lines (in red) - Standard Hough Line Transform", cdst);
        HighGui.imshow("Detected Lines (in red) - Probabilistic Line Transform", cdstP);
        // Wait and Exit
        HighGui.waitKey();*/
        System.exit(0);
        return  linesP;
    }

    private Mat preprocess(Mat image) {
        Mat bw = new Mat();

        cvtColor(image, bw, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(bw, bw, new Size(11, 11), 0);

        adaptiveThreshold(bw, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY_INV, 5, 2);

        //        dilate(bw, bw, crossKernel);

        return bw;
    }

    private Point computeIntersect(double[] a, double [] b) {
        double x1 = a[0], y1 = a[1], x2 = a[2], y2 = a[3], x3 = b[0], y3 = b[1], x4 = b[2], y4 = b[3];
        double d = (x1 - x2) * (y3 - y4) - ((y1 - y2) * (x3 - x4));
        Point pt = new Point(-1, -1);
        if (d != 0) {
            pt.x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
            pt.y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
            //-10 is a threshold, the POI can be off by at most 10 pixels
            if (pt.x < min(x1, x2) - 10 || pt.x > max(x1, x2) + 10 || pt.y < min(y1, y2) - 10 || pt.y > max(y1, y2) + 10) {
                return new Point(-1, -1);
            }
            if (pt.x < min(x3, x4) - 10 || pt.x > max(x3, x4) + 10 || pt.y < min(y3, y4) - 10 || pt.y > max(y3, y4) + 10) {
                return new Point(-1, -1);
            }
        }
        return pt;
    }

    private void FindRect(Mat linesP, Mat img2) {
        double[][] lines = new double[linesP.rows()][];
        for (int x = 0; x < linesP.rows(); x++) {
            lines[x] = linesP.get(x, 0);
        }
        int[] poly = new int[lines.length];
        for(int i=0; i<lines.length; i++){
            poly[i] = - 1;
        }
        int curPoly = 0;
        ArrayList<ArrayList<Point>> corners = new ArrayList<>();
        for (int i = 0; i < lines.length; i++)
        {
            for (int j = i+1; j < lines.length; j++)
            {

                Point pt = computeIntersect(lines[i], lines[j]);
                if (pt.x >= 0 && pt.y >= 0 && pt.x < img2.size().width && pt.y < img2.size().height){

                    if(poly[i]==-1&&poly[j] == -1){
                        ArrayList<Point> v = new ArrayList<>();
                        v.add(pt);
                        corners.add(v);
                        poly[i] = curPoly;
                        poly[j] = curPoly;
                        curPoly++;
                        continue;
                    }
                    if(poly[i]==-1&&poly[j]>=0){
                        corners.get(poly[j]).add(pt);
                        poly[i] = poly[j];
                        continue;
                    }
                    if(poly[i]>=0&&poly[j]==-1){
                        corners.get(poly[i]).add(pt);
                        poly[j] = poly[i];
                        continue;
                    }
                    if(poly[i]>=0&&poly[j]>=0){
                        if(poly[i]==poly[j]){
                            corners.get(poly[i]).add(pt);
                            continue;
                        }

                        for(int k=0; k < corners.get(poly[j]).size();k++){
                            corners.get(poly[i]).add(corners.get(poly[j]).get(k));
                        }

                        corners.get(poly[j]).clear();
                        poly[j] = poly[i];
                        continue;
                    }
                }
            }
        }
    }
    public boolean comparator(Point a,Point b){
        return a.x < b.x;
    }
    public void sortCorners(ArrayList<Point> corners, Point center)
    {
        ArrayList<Point> top = new ArrayList<>(), bot = new ArrayList<>();
        for (int i = 0; i < corners.size(); i++)
        {
            if (corners.get(i).y < center.y)
                top.add(corners.get(i));
            else
                bot.add(corners.get(i));
        }/*
        Collections.sort(top, );
        (top.begin(),top.end(),comparator);
        sort(bot.begin(),bot.end(),comparator);
        cv::Point2f tl = top[0];
        cv::Point2f tr = top[top.size()-1];
        cv::Point2f bl = bot[0];
        cv::Point2f br = bot[bot.size()-1];
        corners.clear();
        corners.push_back(tl);
        corners.push_back(tr);
        corners.push_back(br);
        corners.push_back(bl);*/
    }
}


