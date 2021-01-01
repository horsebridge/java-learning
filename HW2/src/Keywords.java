public class Keywords {
//    private static final String[] keys={"之其或亦方于即皆因仍故尚呢了的着一不乃呀吗咧" +
//                "啊把让向往是在越再更比很偏别好可便就但儿又也都要这你我他来去道说"};
    private static final String[] keys={"之","其","或","亦","方","于","即","皆","因","仍",
            "故","尚","呢","了","的","着","一","不","乃","呀","吗","咧", "啊","把","让","向",
            "往","是","在","越","再","更","比","很","偏","别","好","可","便","就","但","儿",
            "又","也","都","要","这","你","我","他","来","去","道","说"};
    private static int len=keys.length;
    private static final String[] effective={"呢","了","的","着","再","好","便","就","又","都","这","道"};

    public static int getLen() {
        return len;
    }

    public static String key(int n) {
        return keys[n];
    }

    public static String[] subKeys(int n) {
        //return effective;
        String[] sub=new String[n];
        boolean[] chosen=new boolean[len];
        for(int i=0;i<n;++i) {
            int ran;
            while (true) {
                ran = (int) (Math.random() * len);
                if(!chosen[ran]) {
                    chosen[ran]=true;
                    break;
                }
            }
            sub[i]=keys[ran];
        }
        return sub;
    }
}
