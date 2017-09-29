package com.logaritmos;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rectangle implements Serializable {
  private static final long serialVersionUID = 5L;
  private int left;
  private int right;
  private int top;
  private int bottom;

  public Rectangle(int l,int r, int t, int b){
    this.setXaxis(l,r);
    this.setYaxis(t,b);
  }

  // corrige los valores de un rectangulo en cada eje en caso de estar desordenados
  public static void rectify(Rectangle rect) {
    rect.setXaxis(rect.left, rect.right);
    rect.setYaxis(rect.bottom, rect.top);
  }

  private void setXaxis(int a, int b){
    //el eje crece de izquierda a derecha
    this.left = min(a,b);
    this.right = max(a,b);
  }

  private void setYaxis(int a, int b){
    //el eje Y crece de abajo hacia arriba
    this.top = max(a,b);
    this.bottom = min(a,b);
  }

  // getters

  public int getTop() {
    return this.top;
  }

  public int getBottom() {
    return this.bottom;
  }

  public int getLeft() {
    return this.left;
  }

  public int getRight() {
    return this.right;
  }

  public int getHeight() {
    return abs(this.top - this.bottom);
  }

  public int getWidth() {
    return abs(this.right - this.left);
  }

  public double area() {
    return (double) this.getHeight()*this.getWidth();
  }

  public double deltaArea(Rectangle rect1, Rectangle rect2) {
    return abs(rect1.area() - rect2.area());
  }

  public boolean intersects(Rectangle aRectangle) {
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

  public double intersectArea(Rectangle r) {
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

  public static double overlapSum(Rectangle r, ArrayList<Rectangle> rects) {
    double result = 0;
    for (Rectangle rec : rects) {
      result += r.intersectArea(rec);
    }
    return result;
  }

  public static Rectangle calculateMBR(ArrayList<Rectangle> rectangles) {
    Rectangle ans = new Rectangle(0,0,0,0);
    for (Rectangle r : rectangles) {
      ans.left = min(ans.left, r.left);
      ans.right = max(ans.right, r.right);
      ans.bottom = min(ans.bottom, r.bottom);
      ans.top = max(ans.top, r.top);
    }
    return ans;
  }

  public static Rectangle calculateMBR(Rectangle r1, Rectangle r2) {
    ArrayList<Rectangle> list = new ArrayList<Rectangle>();
    list.add(r1);
    list.add(r1);
    return calculateMBR(list);
  }

  public static Rectangle calculateMBR(Rectangle r, ArrayList<Rectangle> rectangles) {
    ArrayList<Rectangle> list = new ArrayList<Rectangle>(rectangles);
    list.add(r);
    return calculateMBR(list);
  }

  // --------------------- Old Functions ------------------------------

  public boolean overlaps(Rectangle aRectangle) {
    return (this.inVertical(aRectangle) && this.inHorizontal(aRectangle));
  }
  private boolean inVertical(Rectangle aRectangle){
   return (this.inTop(aRectangle.top) || this.inBottom(aRectangle.bottom));
  }
  private boolean inHorizontal(Rectangle aRectangle){
    return (this.inLeft(aRectangle.left) || this.inRight(aRectangle.right));
  }
  private boolean contains(int number, int min ,int max){
    return number>=min && number<=max;
  }
  private boolean inTop(int aTop){
    return contains(aTop,this.bottom,this.top);
  }
  private boolean inBottom(int aBottom){
    return contains(aBottom,this.bottom,this.top);
  }
  private boolean inLeft(int aLeft){
    return contains(aLeft,this.left,this.right);
  }
  private boolean inRight(int aRight){
    return contains(aRight,this.left,this.right);
  }

  public void multiMax(int i, int i1, int i2, int i3) {
  }
}
