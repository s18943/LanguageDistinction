package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LanguageDeterminator {
    ArrayList<Perceptron> perceptrons;
    ArrayList<String> langugesName;

    public LanguageDeterminator(ArrayList<Perceptron> perceptrons, ArrayList<String> langugesName) {
        this.perceptrons = perceptrons;
        this.langugesName = langugesName;
        System.out.println(langugesName);
    }

    public String determ(InelVector inelVector) {
        double []distaces = new double[perceptrons.size()];
        for(int i=0;i<perceptrons.size();i++){
            distaces[i] = inelVector.distance(perceptrons.get(i).getW());
        }
        int min = 0;
        for(int i=1;i<distaces.length;i++){
            if(distaces[i]<distaces[min])
                min=i;
        }
//        y=Test(inelVector.xyz);
        System.out.println(Arrays.toString(distaces));
        System.out.println(inelVector.definition);
        System.out.println(min+":"+langugesName.get(min));
        System.out.println("______________________");
        return langugesName.get(min);
    }
}
