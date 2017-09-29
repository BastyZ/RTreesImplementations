package com.logaritmos;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.io.Serializable;

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
    return false;
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
