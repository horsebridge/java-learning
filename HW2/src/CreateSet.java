import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CreateSet {
    private static final String hlm="data_utf8_cut\\hlm\\hlm";
    private static final String sgyy="data_utf8_cut\\sgyy\\sgyy";
    private static final String txt=".txt";
    private static final String training="TrainingSet.txt";
    private static final String testing="TestingSetDir.txt";
    private static final String std="std.txt";
    private static boolean[] exists_hlm=new boolean[121];
    private static boolean[] exists_sgyy=new boolean[121];

    public static void createTraining() {

        FileWriter fileWriter=null;
        try{
            fileWriter=new FileWriter(training);
            for(int i=0;i<20;++i) {
                int ran;
                while(true) {
                    ran = 1 + (int) (Math.random() * 80);
                    if(!exists_hlm[ran]) {
                        exists_hlm[ran]=true;
                        break;
                    }
                }
                String chapter="";
                if(ran%10==0) {
                    int t=ran/10;
                    chapter="0"+ t +"_10";
                }
                else {
                    int t=ran/10+1;
                    int rest=ran%10;
                    chapter="0"+ t +"_"+ rest;
                }
                fileWriter.write("0|"+hlm+chapter+txt+"\n");
            }
            for(int i=0;i<20;++i) {
                int ran;
                while(true) {
                    ran = 81 + (int) (Math.random() * 40);
                    if(!exists_hlm[ran]) {
                        exists_hlm[ran]=true;
                        break;
                    }
                }
                String chapter="";
                if(ran%10==0) {
                    int t=ran/10;
                    if(t<10) chapter="0";
                    chapter+= t +"_10";
                }
                else {
                    int t=ran/10+1;
                    int rest=ran%10;
                    if(t<10) chapter="0";
                    chapter+= t +"_"+ rest;
                }
                fileWriter.write("1|"+hlm+chapter+txt+"\n");
            }
            exists_sgyy=new boolean[121];
            for(int i=0;i<40;++i) {
                int ran;
                while(true) {
                    ran = 1 + (int) (Math.random() * 120);
                    if(!exists_sgyy[ran]) {
                        exists_sgyy[ran]=true;
                        break;
                    }
                }
                String chapter="";
                if(ran%10==0) {
                    int t=ran/10;
                    if(t<10) chapter="0";
                    chapter+= t +"_10";
                }
                else {
                    int t=ran/10+1;
                    int rest=ran%10;
                    if(t<10) chapter="0";
                    chapter+= t +"_"+ rest;
                }
                fileWriter.write("2|"+sgyy+chapter+txt+"\n");
            }
            //fileWriter.write("\n");
            //fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTesting() {
        FileWriter fileWriter=null;
        try{
            fileWriter=new FileWriter(testing);
            for(int i=1;i<=120;++i) {
                if(exists_hlm[i]) continue;
                String chapter="";
                if(i%10==0) {
                    int t=i/10;
                    if(t<10) chapter="0";
                    chapter+= t +"_10";
                }
                else {
                    int t=i/10+1;
                    int rest=i%10;
                    if(t<10) chapter="0";
                    chapter+= t +"_"+ rest;
                }
                fileWriter.write(hlm+chapter+txt+"\n");
            }
            for(int i=1;i<=120;++i) {
                if(exists_sgyy[i]) continue;
                String chapter="";
                if(i%10==0) {
                    int t=i/10;
                    if(t<10) chapter="0";
                    chapter+= t +"_10";
                }
                else {
                    int t=i/10+1;
                    int rest=i%10;
                    if(t<10) chapter="0";
                    chapter+= t +"_"+ rest;
                }
                fileWriter.write(sgyy+chapter+txt+"\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createStd() {
        FileWriter fileWriter=null;
        try{
            fileWriter=new FileWriter(std);
            for(int i=1;i<=120;++i) {
                if(exists_hlm[i]) continue;
                if(i<=80) fileWriter.write(Integer.toString(0)+'\n');
                else fileWriter.write(Integer.toString(1)+'\n');
            }
            for(int i=1;i<=120;++i) {
                if(exists_sgyy[i]) continue;
                fileWriter.write(Integer.toString(2)+'\n');
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createAll() {
        createTraining();
        createTesting();
        createStd();
    }

    public static void main(String[] args) {
        //CreateSet createSet=new CreateSet();

    }
}
