package com.company;

import java.util.Arrays;

public class InelVector {
  double[] xyz;
  String definition;

    public InelVector(String[] arrDef){
        int length = arrDef.length-1;
        definition = arrDef[length];
        xyz = new double[length];
        double sum=0.0;
        for(int i=0; i<length;i++){
            xyz[i]=Double.parseDouble(arrDef[i]);
            sum+=xyz[i];
        }
        normalise(xyz);
    }

    public double distance(InelVector two){
        int length = Math.min(xyz.length, two.xyz.length);
        double sum=0;
        for(int i = 0; i < length; i++){
            sum+=Math.pow(xyz[i]-two.xyz[i],2);
        }
        return Math.sqrt(sum);
    }
    public double distance(double[] two){
        int length = Math.min(xyz.length, two.length);
        double sum=0;
        for(int i = 0; i < length; i++){
            sum+=Math.pow(xyz[i]-two[i],2);
        }
        return Math.sqrt(sum);
    }
    public static void normalise(double[] vektor) {
        double len = 0;
        for (double v : vektor) {
            len += Math.pow(v, 2);
        }
        len = Math.sqrt(len);
        if (len != 1)
            for (int i = 0; i < vektor.length; i++) {
                vektor[i] /= len;
            }
    }
}
