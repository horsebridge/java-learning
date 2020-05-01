import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import weka.core.Instance;
import weka.core.FastVector;
import weka.core.Attribute;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Analysis {
//    private static final String[] keys={"之其或亦方于即皆因仍故尚呢了的着一不乃呀吗咧" +
//            "啊把让向往是在越再更比很偏别好可便就但儿又也都要这你我他来去道说"};
    private static String[] keys;
    //={"之","其","或","亦","方","于","即","皆","因","仍"};
    private static final String training ="TrainingSet.txt";
    private String trainingset;
    private static final String train="train.txt";
    private static final String testing ="TestingSetDir.txt";
    private String testingset;
    private static final String test="test.txt";
    private HashMap<String,Integer> initial=new HashMap<>();
    private ArrayList<HashMap<String,Integer>> trained=new ArrayList<>();
    private ArrayList<HashMap<String,Integer>> tested=new ArrayList<>();
    ArrayList<Integer> kind=new ArrayList<>();

    Analysis(String trainingset,String testingset,int n) {
        this.trainingset=trainingset;
        this.testingset=testingset;
        keys=Keywords.subKeys(n);
        for(int i=0;i<keys.length;++i) {
            initial.put(keys[i],0);
        }
    }

    public void setKeys(String key) {
        keys =new String[1];
        keys[0]=key;
        initial.clear();
        initial.put(key,0);
        trained.clear();
        tested.clear();
        kind.clear();
    }

    private void analyze(String file, ArrayList<HashMap<String,Integer>> t) {
        BufferedReader reader = null;
        try {
            reader=new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String str=null;
        HashMap<String,Integer> map=new HashMap<>();
        map.putAll(initial);
        while (true) {
            try {
                if ((str = reader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Term> termList= StandardTokenizer.segment(str);
            for(int i=0;i<termList.size();++i) {
                String word=termList.get(i).word;
                if(map.containsKey(word)) {
                    int fre=map.get(word);
                    map.replace(word,fre+1);
                }
            }
        }
        t.add(map);
    }

    public void analyzeAll() {
        BufferedReader reader=null;
        try {
            reader=new BufferedReader(new FileReader(trainingset));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String str=null;
        while (true) {
            try {
                if ((str = reader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            int k=str.charAt(0)-'0';
            kind.add(k);
            String path=str.substring(2);
            analyze(path,trained);
        }
        try {
            reader=new BufferedReader(new FileReader(testingset));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                if ((str = reader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            analyze(str,tested);
        }
    }

    public void output(String outtrain,String outtest) {
        analyzeAll();
//        ArrayList<Attribute> attributes=new ArrayList<>();
//        for(int i=0;i<keys.length;++i) attributes.add(new Attribute(keys[i],real));
        FileWriter fileWriter=null;
        try {
            fileWriter=new FileWriter(outtrain);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.write("@relation guess_kind\n");
            fileWriter.write("\n");
            for(int i=0;i<keys.length;++i) {
                fileWriter.write("@attribute "+keys[i]+" real\n");
            }
            fileWriter.write("@attribute kind {0,1,2} \n");
            fileWriter.write("\n@data\n");
            fileWriter.write("%\n% "+kind.size()+" instances\n%\n");
            for(int i=0;i<kind.size();++i) {
                for(int j=0;j<keys.length;++j) {
                    fileWriter.write(trained.get(i).get(keys[j])+",");
                }
                fileWriter.write(kind.get(i)+"\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter=new FileWriter(outtest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.write("@relation guess_kind\n");
            fileWriter.write("\n");
            for(int i=0;i<keys.length;++i) {
                fileWriter.write("@attribute "+keys[i]+" real\n");
            }
            fileWriter.write("@attribute kind {0,1,2} \n");
            fileWriter.write("\n@data\n");
            fileWriter.write("%\n% "+tested.size()+" instances\n%\n");
            for(int i=0;i<tested.size();++i) {
                for(int j=0;j<keys.length;++j) {
                    fileWriter.write(tested.get(i).get(keys[j])+",");
                }
                fileWriter.write("?\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Analysis analysis=new Analysis(training,testing,10);
        analysis.output(train,test);
    }
}
