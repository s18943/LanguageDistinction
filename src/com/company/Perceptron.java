package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Perceptron {
    private double a;
    private double t;
    private double y;
    private double d;
    private double sum;
    private double[] W;
    private int arrayPoint;
    private ArrayList<String> alphabet;
    private Random r;

    public double[] getW() {
        return W;
    }

    public Perceptron(double a) {
        this.a = a;
        r = new Random();
        sum = 0;
        alphabet = new ArrayList<>();
        t = r.nextDouble()*5;
    }

    public Perceptron(double a, ArrayList<String> alphabet, int arrayPoint) {
        this(a);
        this.alphabet = alphabet;
        this.arrayPoint = arrayPoint;
    }

    public void Train(ArrayList<InelVector> trainSet,int f) {
        System.out.println("----------------"+alphabet.get(arrayPoint)+"----------------");
        W = new double [trainSet.get(0).xyz.length];
        for(int i=0;i<W.length;i++)
        {
            W[i] = r.nextDouble();
        }
        //to fill alphabet if empty
//        alphabet.add(trainSet.get(0).definition);
//        alphabet.add(trainSet.get(trainSet.size()-1).definition);
        double countAll=0, countRight=0, learnCykle = 0;
       // for(int r=0;r<5;r++) {
            do {
                Collections.shuffle(trainSet);
                for (InelVector inelVector : trainSet) {
                    y = Test(inelVector.xyz);
                    d = alphabet.indexOf(inelVector.definition) == arrayPoint ? 1 : 0;

                    System.out.println("think: " + (y == 1 ? alphabet.get(arrayPoint) : "other lang"));
                    System.out.println("real val: " + inelVector.definition);
                    if (d != y) {
                        System.out.println("Learned");
                        Learn(inelVector.xyz);
                        //i--;
                    } else {
                        countRight++;
                    }
                    countAll++;
                    System.out.println("----------------------------------");
                }
                learnCykle++;
            }while((100-Math.round(countRight/countAll*100))>f);
       // }
        System.out.println(Arrays.toString(W));
        InelVector.normalise(W);
        System.out.println(Arrays.toString(W));
        System.out.println("---------!!!!!!--Train Finished-------"+learnCykle+"------!!!!!----------");
    }
    private void Learn(double[] xyz){
        int length = Math.min(xyz.length, W.length);
        for(int i = 0; i < length; i++){
            W[i] += (d-y) * a * xyz[i];
        }
        t -= (d-y) * a;
    }
    private double Test(double[] xyz){
        int length = Math.min(xyz.length, W.length);
        sum=0.0;
        for(int i = 0; i < length; i++){
            sum+=xyz[i]*W[i];
        }
        return sum>t? 1 : 0;

    }
    public double Weight(double[] xyz){
        int length = Math.min(xyz.length, W.length);
        sum=0.0;
        for(int i = 0; i < length; i++){
            sum+=xyz[i]*W[i];
        }
        return 1/(1+Math.pow(Math.E,-sum));

    }

    public String determ(InelVector inelVector) {
        y=Test(inelVector.xyz);
        d=alphabet.indexOf(inelVector.definition);
//        System.out.println(alphabet.get((int)y));
//        System.out.println("______________________");
        return y==1 ? alphabet.get(arrayPoint):"nope" ;
    }
}
