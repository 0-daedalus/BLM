import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NameGenerator {
    private File f;
    private Map<String, Integer> bigramMap;
    private Map<Long, String> integerCumProb;
    private Map<String, Double> bigramFreq;
    public Map<String, Double> getBigramFreq() {
        return bigramFreq;
    }
    private Long sum = 0L;
    public NameGenerator(File f) throws FileNotFoundException {
        this.f = f;
        this.bigramMap = new HashMap<>();
        this.bigramFreq = new HashMap<>();
        try{
            populateBigramMap(f);
        }
        catch (IOException ex){
            System.out.println("IOException");
        }
    }

    private void populateBigramMap(File f) throws IOException {
        BufferedReader sc = new BufferedReader(new FileReader(f));
        String currentName = " ";
        String currentBigram;
        double total = 0d;
        while(currentName != null) {
            StringBuilder sb = new StringBuilder();
            currentName = sc.readLine();
            if(currentName == null) break;
            sb.append('^');
            sb.append(currentName);
            sb.append('$');
            String augWord = sb.toString();
            //System.out.println(augWord);
            for(int i = 0; i < augWord.length()-1; i++){
                currentBigram = augWord.substring(i, i+2);
                if (!bigramMap.containsKey(currentBigram)){
                    bigramMap.put(currentBigram, 1);
                }
                else bigramMap.put(currentBigram, bigramMap.get(currentBigram) + 1);
                total++;
            }
        }
        populateCumProb();
        for (Map.Entry<String, Integer> pair:
                bigramMap.entrySet()) {
            bigramFreq.put(pair.getKey(), pair.getValue()/total);
        }
        //System.out.println(bigramMap.toString());
        //System.out.println(bigramFreq.toString());
        //System.out.println(total);
    }

    private void populateCumProb(){
        integerCumProb = new HashMap<>();
        for (Map.Entry<String, Integer> pair:
                bigramMap.entrySet()) {
            sum += pair.getValue();
            integerCumProb.put(sum, pair.getKey());
        }
        //System.out.println(integerCumProb);
    }

    public String generateBigram(){
        double rand = Math.random();
        long index = (long)Math.ceil(rand*sum);
        while(integerCumProb.get(index) == null) index++;
        return integerCumProb.get(index);
    }
    public String generateName(){
        String beg = generateBigram();
        StringBuilder name = new StringBuilder();
        while(beg.charAt(0) != '^') beg = generateBigram();
        name.append(beg);
        String bigram;
        do {
            bigram = generateBigram();
            if (bigram.charAt(0) == '^'){
                continue;
            }
            name.append(bigram);
        }
        while(bigram.charAt(1) != '$');
        return name.substring(1, name.length()-1);
    }
}
