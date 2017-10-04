package com.logaritmos;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.io.Serializable;
import java.util.ArrayList;

public class Rectangle implements Serializable {
  private static final long serialVersionUID = 5L;
  private int left;
  private int right;
  private int top;
  private int bottom;

  public Rectangle(int l, int r, int t, int b){
    this.setXaxis(l,r);
    this.setYaxis(t,b);
  }

  // fix rectangle borders if order is not correct
  public static void rectify(Rectangle rect) {
    rect.setXaxis(rect.left, rect.right);
    rect.setYaxis(rect.bottom, rect.top);
  }

  private void setXaxis(int a, int b){
    //axis grows from left to right
    this.left = min(a,b);
    this.right = max(a,b);
  }

  private void setYaxis(int a, int b){
    // axis grows from bottom to top
    this.top = max(a,b);
    this.bottom = min(a,b);
  }

  // getters

  protected int getTop() {
    return this.top;
  }

  protected int getBottom() {
    return this.bottom;
  }

  protected int getLeft() {
    return this.left;
  }

  protected int getRight() {
    return this.right;
  }

  protected int getHeight() {
    return abs(this.top - this.bottom);
  }

  protected int getWidth() {
    return abs(this.right - this.left);
  }

  double area() {
    return (double) this.getHeight()*this.getWidth();
  }

  protected double deltaArea(Rectangle rect1, Rectangle rect2) {
    return abs(rect1.area() - rect2.area());
  }

  boolean intersects(Rectangle aRectangle) {
    Rectangle minorY;
    Rectangle majorY;
    if (this.bottom <= aRectangle.bottom) {
      minorY = this;
      majorY = aRectangle;
    } else {
      minorY = aRectangle;
      majorY = this;
    }
    return minorY.left <= majorY.left && minorY.right >= majorY.right && minorY.top >= majorY.bottom
        || minorY.left <= majorY.left && minorY.left <= majorY.right && majorY.top <= majorY.bottom;
  }

  protected double intersectArea(Rectangle r) {
    if (!this.intersects(r)) {return 0;}
    double dx = 0;
    double dy = 0;
    if(this.right >= r.left) {
      if(r.left >= this.left) {
        dx = min(r.right, this.right) - r.left;
      } else {
        if (r.right >= this.right) {
          dx = this.right - this.left;
        } else {
          dx = this.left - r.right;
        }
      }
    }
    if(this.top >= r.bottom) {
      if(r.bottom >= this.bottom) {
        dy = min(this.bottom, r.top);
      } else {
        if(r.top >= this.top) {
          dy = this.top - this.bottom;
        } else {
          dy = this.bottom - r.top;
        }
      }
    }
    return dx*dy;
  }

  protected static double overlapSum(Rectangle r, ArrayList<Rectangle> recs) {
    double result = 0;
    for (Rectangle rec : recs) {
      result += r.intersectArea(rec);
    }
    return result;
  }

  static Rectangle calculateMBR(ArrayList<Rectangle> rectangles) {
    Rectangle ans = new Rectangle( 500000,0,0,50000);
    for (Rectangle r : rectangles) {
      ans.left = min(ans.left, r.left);
      ans.right = max(ans.right, r.right);
      ans.bottom = min(ans.bottom, r.bottom);
      ans.top = max(ans.top, r.top);
    }
    return ans;
  }

  static Rectangle calculateMBR(Rectangle r1, Rectangle r2) {
    ArrayList<Rectangle> list = new ArrayList<Rectangle>();
    list.add(r1);
    list.add(r2);
    return calculateMBR(list);
  }

  static Rectangle calculateMBR(Rectangle r, ArrayList<Rectangle> rectangles) {
    ArrayList<Rectangle> list = new ArrayList<Rectangle>(rectangles);
    list.add(r);
    return calculateMBR(list);
  }

}
