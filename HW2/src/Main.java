import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class Main {
    private static final String training ="TrainingSet.txt";
    private static final String testing ="TestingSetDir.txt";
    private static final String train="train.txt";
    private static final String test="test.txt";
    private static final String prediction="prediction.txt";

    public static void main(String[] args) {
        CreateSet.createAll();
        Analysis analysis=new Analysis(training,testing,10);
        //ArrayList<Double> rates=new ArrayList<>();
        //for(int i=0;i<Keywords.getLen();++i) {
            //analysis.setKeys(Keywords.key(i));
            analysis.output(train,test);
            Training training=new Training(train,test);
            training.test(prediction);
            double rate=training.judge();
            System.out.println(rate);
//            rates.add(rate);
//        }
//        rates.sort(new Comparator<Double>() {
//            @Override
//            public int compare(Double o1, Double o2) {
//                if(o1<o2) return 1;
//                if(o1>o2) return -1;
//                return 0;
//            }
//        });
//        System.out.println(rates);
    }
}
