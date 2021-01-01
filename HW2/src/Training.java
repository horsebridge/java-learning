import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.*;
import weka.classifiers.Evaluation;
import weka.classifiers.*;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;

public class Training {
    private static final String trainpath="train.txt";
    private static final String testpath="test.txt";
    private static final String prediction="prediction.txt";
    private static final String std="std.txt";
    private String trainfile;
    private String testfile;
    Instances trained=null;
    Instances totest=null;

    Training(String trainfile,String testfile) {
        this.trainfile=trainfile;
        this.testfile=testfile;
        BufferedReader reader = null;
        try {
            reader=new BufferedReader(new FileReader(trainfile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            trained=new Instances(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        trained.setClassIndex(trained.numAttributes()-1);
        try {
            reader=new BufferedReader(new FileReader(testfile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            totest=new Instances(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        totest.setClassIndex(totest.numAttributes()-1);
    }

    void test(String prediction) {
        Classifier model=new J48();
        FileWriter fileWriter=null;
        try {
            fileWriter=new FileWriter(prediction);
            try {
                model.buildClassifier(trained);
                for(Instance instance:totest) {
                    int pre= (int) model.classifyInstance(instance);
                    fileWriter.write(Integer.toString(pre)+'\n');
                    //System.out.print(pre);
                }
                //int pre= (int) models[i].classifyInstance(totest.get(0));
                //models[i].classifyInstance(totest.get())
                //System.out.println(pre);
                //Evaluation eval = new Evaluation(trained);
//                eval.evaluateModel(models[i], totest);
//                System.out.println(eval.errorRate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    double judge() {
        int all=0;
        int correct=0;
        BufferedReader readstd=null;
        BufferedReader readpre=null;
        try {
            readstd=new BufferedReader(new FileReader(std));
            readpre=new BufferedReader(new FileReader(prediction));
            String strstd=null;
            String strpre=null;
            while (true) {
                try {
                    if((strstd=readstd.readLine())==null) break;
                    if((strpre=readpre.readLine())==null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                all++;
                int kindstd=Integer.parseInt(strstd);
                int kindpre=Integer.parseInt(strpre);
                if(kindstd==kindpre) correct++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //System.out.println(all+" "+correct);
        double rate=(double)(correct)/all;
        return rate;
    }

    public static void main(String[] args) {
        Training training=new Training(trainpath,testpath);
        training.test(prediction);
        double rate=training.judge();
        System.out.println(rate);
    }
}
