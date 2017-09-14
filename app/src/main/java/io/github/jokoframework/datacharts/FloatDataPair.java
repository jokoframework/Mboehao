package io.github.jokoframework.datacharts;

/**
 * Created by joaquin on 11/09/17.
 */

public class FloatDataPair {

    private float x;
    private float y;
    private int xi;
    private int yi;


    public FloatDataPair(float x, float y) {this.x=x;this.y=y;}
    public FloatDataPair(int x, float y) {this.xi=x;this.y=y;}
    public FloatDataPair(float x, int y) {this.x=x;this.yi=y;}
    public FloatDataPair(int x, int y) {this.xi=x;this.yi=y;}


    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public int getYi() {
        return yi;
    }

    public void setYi(int yi) {
        this.yi = yi;
    }

    public int getXi() {
        return xi;
    }

    public void setXi(int xi) {
        this.xi = xi;
    }
}
