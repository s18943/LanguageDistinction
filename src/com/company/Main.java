package com.company;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        double a = Double.parseDouble(args[0]);
        String TrainSet = args[1];
        String TestSet = args[2];
        ArrayList<String> alphabet = new ArrayList<>();
        ArrayList<InelVector> trainLanguges = new ArrayList<>();
        ArrayList<Perceptron> perceptronLanguges = new ArrayList<>();



        try {
            List<String> directoryes = getFromFolderByFilter(TrainSet,Files::isDirectory);
            directoryes.remove(0);
            directoryes.forEach(x->{
                List<String> files = getFromFolderByFilter(x,Files::isRegularFile);
                String langugeName = x.substring(TestSet.length() + 2);
                alphabet.add(langugeName);
                files.forEach(y->{
                    try {
                        String data = new String(Files.readAllBytes(Paths.get(y)));
                        String[] defArr = (getLetterProportion(data.toCharArray())+ "," + langugeName).split(",");
                        trainLanguges.add(new InelVector(defArr));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
            directoryes.forEach(x->{
                Collections.shuffle(trainLanguges);
                Perceptron tmp = new Perceptron(a,alphabet,alphabet.indexOf(x.substring(TestSet.length() + 2)));
                tmp.Train(trainLanguges,20);
                perceptronLanguges.add(tmp);
            });
            LanguageDeterminator determinator = new LanguageDeterminator(perceptronLanguges,alphabet);
            //determinator.determ(trainLanguges.get((int)(Math.random()*5)));
            //Perceptron proc = new Perceptron(a,alphabet,trainLanguges);
            //proc.Train(trainLanguges);

            double countAll=0, countRight=0;
            List<String> directoryesTest = getFromFolderByFilter(TestSet,Files::isDirectory);
            directoryesTest.remove(0);
            for(String x : directoryesTest) {
                List<String> files = getFromFolderByFilter(x, Files::isRegularFile);
                for (String y : files) {
                    try {
                        String data = new String(Files.readAllBytes(Paths.get(y)));
                        String[] defArr = (getLetterProportion(data.toCharArray()) + "," + x.substring(TestSet.length() + 1)).split(",");
                        countAll++;
                        if (defArr[defArr.length - 1].equals(determinator.determ(new InelVector(defArr))))
                            countRight++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println(countRight+" "+countAll);
            System.out.println("Accuracy: " + (Math.round(countRight/countAll*100))+"%");
            Scanner in = new Scanner(System.in);
            String str="";
            while(!str.equals("e")){
                System.out.println("input - i");
                System.out.println("exit - e");
                str = in.nextLine();
                switch (str) {
                    case"i":
                        System.out.println("input 4 parameters in format 0.0 /n anything else to stop");
                        StringBuilder tmp = new StringBuilder();
                        str=in.nextLine();
                        while(!str.matches("//stop//!")){
                            tmp.append(str);
                            str=in.nextLine();
                        }
                        determinator.determ(new InelVector((getLetterProportion(tmp.toString().toCharArray())+",none").split(",")));
                        break;
                    case"s":
                        System.out.println("save percepron state");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String> getFromFolderByFilter(String folder, Predicate<Path> pr){
        try (Stream<Path> walk = Files.walk(Paths.get(folder))) {
            return walk.filter(pr).map(x -> x.toString()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getLetterProportion(char[] text){
        double[] tmpRes = new double[26];
        int counter = 0;
        for (char c : text) {
            if (c >= 97 && c <= 122) {
                tmpRes[c - 97]++;
                counter++;
            }
        }
        for (int i = 0; i < tmpRes.length; i++) {
            tmpRes[i]/=counter;
        }
        String res = Arrays.toString(tmpRes);
        return res.substring(1,res.length()-1);
    }
}
