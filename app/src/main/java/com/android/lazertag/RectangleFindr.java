package com.android.lazertag;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import static android.content.ContentValues.TAG;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.opencv.core.CvType.CV_32FC2;
import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.imgproc.Imgproc.adaptiveThreshold;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class RectangleFindr {

    private Mat findLines(Mat src) {
        // Declare the output variables
        Mat dst = new Mat(), cdst = new Mat(), cdstP;
        // Load an image
        //Mat src = Imgcodecs.imread(default_file, Imgcodecs.IMREAD_COLOR);
        // Check if image is loaded fine
        /*if( src.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- "
                    + default_file +"] \n");
            System.exit(-1);
        }*/
        // Edge detection
        Imgproc.Canny(src, dst, 50, 200, 3, false);
        // Copy edges to the images that will display the results in BGR
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        cdstP = cdst.clone();
        // Standard Hough Line Transform
        /*Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 250); // runs the actual detection
        // Draw the lines
        Log.d("progress", lines.rows() + "");
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }*/
        saveFile(src, "new 3");
        // Probabilistic Line Transform
        Mat linesP = new Mat(); // will hold the results of the detection
        Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 80, 100, 40); // runs the actual detection
        // Draw the lines
        Log.d("progress1", linesP.rows() + "");
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        // Show results
        Imgcodecs.imwrite(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/test/new 3.png", src);
        Imgcodecs.imwrite(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/test/new 3.png", cdstP);
        //Imgcodecs.imwrite(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/test/new 6.png", cdstP);

        // Wait and Exit
        //System.exit(0);
        return  linesP;
    }

    private Mat preprocess(Mat image) {
        Mat bw = new Mat();

        cvtColor(image, bw, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(bw, bw, new Size(3, 3), 0);

        adaptiveThreshold(bw, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                Imgproc.THRESH_BINARY_INV, 75, 10);

        //        dilate(bw, bw, crossKernel);

        return bw;
    }

    private Point2 computeIntersect(double[] a, double [] b) {
        double x1 = a[0], y1 = a[1], x2 = a[2], y2 = a[3], x3 = b[0], y3 = b[1], x4 = b[2], y4 = b[3];
        double d = (x1 - x2) * (y3 - y4) - ((y1 - y2) * (x3 - x4));
        Point2 pt = new Point2(-1, -1);
        if (d != 0) {
            pt.x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
            pt.y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
            //-10 is a threshold, the POI can be off by at most 10 pixels
            if (pt.x < min(x1, x2) - 10 || pt.x > max(x1, x2) + 10 || pt.y < min(y1, y2) - 10 || pt.y > max(y1, y2) + 10) {
                return new Point2(-1, -1);
            }
            if (pt.x < min(x3, x4) - 10 || pt.x > max(x3, x4) + 10 || pt.y < min(y3, y4) - 10 || pt.y > max(y3, y4) + 10) {
                return new Point2(-1, -1);
            }
        }
        return pt;
    }

    public void FindRect(Mat img) {
        //Mat img2 = this.preprocess(img);
        Mat img2 = img.clone();
        //saveFile(img2, "new 12");
        Log.d("progress", "preprocess");
        Mat linesP = this.findLines(img2);
        //saveFile(img2, "lines result");
        Log.d("progress", "linefindr");
        double[][] lines = new double[linesP.rows()][];
        for (int x = 0; x < linesP.rows(); x++) {
            lines[x] = linesP.get(x, 0);
            /*for (int u = 0; u < lines[x].length; u++) {
                System.out.println(lines[x][u]);
            }*/
        }
        int[] poly = new int[lines.length];
        for (int i = 0; i < lines.length; i++) {
            poly[i] = -1;
        }
        int curPoly = 0;
        Log.d("progress", "poly");
        ArrayList<ArrayList<Point2>> corners = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            for (int j = i + 1; j < lines.length; j++) {

                Point2 pt = computeIntersect(lines[i], lines[j]);
                //System.out.println(pt.toString());
                //System.out.println(img.size().width + "  " + img.size().height);
                if (pt.x >= 0 && pt.y >= 0 && pt.x < img.size().width && pt.y < img.size().height) {
                    //System.out.println(poly[i] + " " + poly [j]);
                    if (poly[i] == -1 && poly[j] == -1) {
                        ArrayList<Point2> v = new ArrayList<>();
                        v.add(pt);
                        corners.add(v);
                        poly[i] = curPoly;
                        poly[j] = curPoly;
                        curPoly++;
                        continue;
                    }
                    if (poly[i] == -1 && poly[j] >= 0) {
                        corners.get(poly[j]).add(pt);
                        poly[i] = poly[j];
                        continue;
                    }
                    if (poly[i] >= 0 && poly[j] == -1) {
                        corners.get(poly[i]).add(pt);
                        poly[j] = poly[i];
                        continue;
                    }
                    if (poly[i] >= 0 && poly[j] >= 0) {
                        if (poly[i] == poly[j]) {
                            corners.get(poly[i]).add(pt);
                            continue;
                        }

                        for (int k = 0; k < corners.get(poly[j]).size(); k++) {
                            corners.get(poly[i]).add(corners.get(poly[j]).get(k));
                        }

                        corners.get(poly[j]).clear();
                        poly[j] = poly[i];
                        continue;
                    }
                }
            }
        }

        Log.d("progress", "corners");
        for (int i = 0; i < corners.size(); i++) {
            Point2 center = new Point2(0, 0);
            if (corners.get(i).size() < 4) continue;
            for (int j = 0; j < corners.get(i).size(); j++) {
                center.x += corners.get(i).get(j).x;
                center.y += corners.get(i).get(j).y;
            }
            center.x *= (1. / corners.get(i).size());
            center.y *= (1. / corners.get(i).size());
            System.out.println(center.toString());
            sortCorners(corners.get(i), center);
        }
        for (int k = 0; k < corners.size(); k++){
            System.out.println(corners.get(k));
        }
        Log.d("progress", "sorting");
        Log.d("progress", corners.size() + "");
        int j = 0;
        for(int i=0; i < corners.size(); i++){
            Log.d("corner amt", i + "" + corners.get(i).size());
            if(corners.get(i).size() < 4)continue;
            ArrayList<Point> rec = new ArrayList<>();
            Mat matOfPoints = new MatOfPoint();
            for (int c = 0; c < corners.get(i).size(); c++){
                rec.add(corners.get(i).get(c));
                matOfPoints.push_back(new MatOfPoint(corners.get(i).get(c)));
            }

            //matOfPoints.create(0,0, CV_32FC2);
            //matOfPoints.fromList(rec);
            System.out.println(matOfPoints);

            Rect r = Imgproc.boundingRect(new MatOfPoint(matOfPoints));
            System.out.println(r.toString());
            //if(r.area()<50000)continue;
            // Define the destination image
            Mat quad = Mat.zeros(r.height, r.width, CV_8UC3);
            // Corners of the destination image
            ArrayList<Point> quad_pts = new ArrayList<>();
            //quad_pts.create(4,0, CV_32FC2);
            quad_pts.add(new Point(0, 0));
            quad_pts.add(new Point(quad.cols(), 0));
            quad_pts.add(new Point(quad.cols(), quad.rows()));
            quad_pts.add(new Point(0, quad.rows()));
            System.out.println(quad_pts);
            // Get transformation matrix
            Mat srcMat = Converters.vector_Point2f_to_Mat(rec);
            Mat dstMat = Converters.vector_Point2f_to_Mat(quad_pts);

            //getting the transformation matrix
            Mat perspectiveTransformation = Imgproc.getPerspectiveTransform(srcMat,dstMat);
            //Mat transmtx = Imgproc.getPerspectiveTransform(matOfPoints, quad_pts);
            // Apply perspective transformation



            Mat img3 = img.clone();
            Imgproc.warpPerspective(img3, quad, perspectiveTransformation, quad.size());

            if (r.contains(new Point(img.size().width / 2, img.size().height / 2))){
                saveFile(quad, "img num: " + j);
                j++;
            }
        }
    }
    private void sortCorners(ArrayList<Point2> corners, Point2 center)
    {
        ArrayList<Point2> top = new ArrayList<>(), bot = new ArrayList<>();
        for (int i = 0; i < corners.size(); i++)
        {
            if (corners.get(i).y > center.y)
                top.add(new Point2(Math.round(corners.get(i).x), Math.round(corners.get(i).y)));
            else
                bot.add(new Point2(Math.round(corners.get(i).x), Math.round(corners.get(i).y)));
        }
        Collections.sort(top);
        Collections.sort(bot);
        for (Point x : top) {
            System.out.println(x);
        }
        for (Point x : bot) {
            System.out.println(x);
        }
        Point2 tl = top.get(0);
        Point2 tr = top.get(top.size()-1);
        Point2 bl = bot.get(0);
        Point2 br = bot.get(bot.size()-1);
        corners.clear();
        corners.add(tl);
        corners.add(tr);
        corners.add(br);
        corners.add(bl);
    }
    public void saveFile(Mat img, String fileName) {
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(img, bmp);
        } catch (CvException e) {
            Log.d(TAG, e.getMessage());
        }

        img.release();

        FileOutputStream out = null;

        File sd = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/test");
        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
        }
        if (success) {
            File dest = new File(sd, fileName + ".png");

            try {
                out = new FileOutputStream(dest);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            } finally {
                try {
                    if (out != null) {
                        out.close();
                        Log.d(TAG, "OK!!");
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage() + "Error");
                    e.printStackTrace();
                }
            }
        }
    }
}