/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetworkstask2;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Artur
 */
public class Perseptron {
    
    private double err=0;
    private double s=0;
    private double f=0;
    private List<Double> w=new ArrayList<>();

    public Perseptron() {
    }

    public Perseptron(double err, double s, double f, List<Double> w) {
        this.err = err;
        this.s = s;
        this.f = f;
        this.w = w;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }
    
    public double getErr() {
        return err;
    }

    public double getS() {
        return s;
    }

    public List<Double> getW() {
        return w;
    }

    public void setErr(double err) {
        this.err = err;
    }

    public void setS(double output) {
        this.s = output;
    }

    public void setW(List<Double> w) {
        this.w = w;
    }
    
}
