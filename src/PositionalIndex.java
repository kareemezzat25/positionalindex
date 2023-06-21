import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PositionalIndex {
    HashMap<String, DictEntry> termIndex;
    int collection=10;
    PositionalIndex() {
        termIndex = new HashMap<>();
    }
    public void createIndex(String[] files) throws IOException {
        int documentId = 1;
        try {
            for (String filename : files) {
                int wordCount = 0;
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] words = line.split("\\W+");//(//W+)
                    for (String word : words) {
                        wordCount += 1;
                        word = word.toLowerCase();
                        if (!termIndex.containsKey(word))//search for term in inverted index
                        {
                            termIndex.put(word, new DictEntry(documentId));
                            termIndex.get(word).term_freq++;
                            termIndex.get(word).doc_freq++;
                            termIndex.get(word).plist.positions.add(wordCount);
                        } else {
                            termIndex.get(word).term_freq++;
                            Poisting poistinglist = termIndex.get(word).plist;
                            while (poistinglist != null) {
                                if (poistinglist.docId == documentId) {
                                    poistinglist.positions.add(wordCount);
                                    poistinglist.termFrequency++;
                                    break;
                                }
                                if (poistinglist.next == null) {
                                    poistinglist.next = new Poisting(documentId);
                                    poistinglist.next.positions.add(wordCount);
                                    termIndex.get(word).plist.documentfrequency++;
                                    break;
                                }
                                poistinglist = poistinglist.next;
                            }
                        }
                    }
                }
                documentId++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void printPoistinglist() {
        TreeMap<String, DictEntry> sortedDict = new TreeMap<>();
        sortedDict.putAll(termIndex);
        for (Map.Entry<String, DictEntry> term : sortedDict.entrySet()) {
            Poisting poistlist = term.getValue().plist;
            System.out.print(term.getKey());
            System.out.print("-------->{");
            while (poistlist != null) {
                if (poistlist.next == null) {
                    System.out.print(poistlist.docId+" : "+ poistlist.termFrequency);
                    break;
                }
                //term frequency
                System.out.print(poistlist.docId + " : "+ poistlist.termFrequency+",");
                poistlist = poistlist.next;
            }
            System.out.println("}---------->DocumentFrequncey : " + term.getValue().plist.documentfrequency);
        }
    }
    public void print() {
        TreeMap<String, DictEntry> sortedDict = new TreeMap<>();
        sortedDict.putAll(termIndex);
        for (Map.Entry<String, DictEntry> dictonary : sortedDict.entrySet()) {
            System.out.println(dictonary.getKey() + "    DocumentFrequency : " + dictonary.getValue().plist.documentfrequency + "   TermFrequency : " + dictonary.getValue().term_freq);
        }
    }
    public void printPositionlIndex() {
        TreeMap<String, DictEntry> sortedDict = new TreeMap<>();
        sortedDict.putAll(termIndex);

        for (Map.Entry<String, DictEntry> term : sortedDict.entrySet()) {
            Poisting poistlist = term.getValue().plist;
            System.out.print(term.getKey());
            System.out.print("-------->{");
            while (poistlist != null) {

                System.out.print(poistlist.docId + ":");
                System.out.print(poistlist.positions+" ");
                poistlist = poistlist.next;
            }
            System.out.println("}");
        }
    }
    public void findQuery(String phrase)
    {
        String result="";
        //ArrayList<Integer>results=new ArrayList<>();
        String []terms=phrase.split("\\W+");
        int len=terms.length;
        Poisting poisting=termIndex.get(terms[0].toLowerCase()).plist;
        HashSet<Integer>term=new HashSet<>();
        while (poisting!=null)
        {
            term.add(poisting.docId);
            poisting=poisting.next;
        }
        int i=1;
        while(i<len)
        {
            Poisting poisting2=termIndex.get(terms[i].toLowerCase()).plist;
            HashSet<Integer>termhash=new HashSet<>();

            while (poisting2!=null)
            {
                termhash.add(poisting2.docId);
                poisting2=poisting2.next;
            }
            term=intersect(term,termhash);
            i++;
        }
        if(!term.isEmpty()) {
            System.out.print("The query " + phrase + " is exist in documents :[");
            int count=0;
            for (int num : term) {
            if (count==term.size()-1)
                System.out.print(num);
            else
                System.out.print(num + ",");
            count++;
            }
            System.out.print("]");
        }
        else
        {
            System.out.print("The query " + phrase + " is not exist");
        }

    }
    // intersect for the documents that have this query
    HashSet<Integer>intersect(HashSet<Integer>pos1,HashSet<Integer>pos2)
    {
        HashSet<Integer>Answer=new HashSet<>();
        Iterator<Integer> itPl1=pos1.iterator();
        Iterator<Integer> itPl2= pos2.iterator();
        int docId1=0; int docId2=0;
        if(itPl1.hasNext())
            docId1=itPl1.next();
        if(itPl2.hasNext())
            docId2=itPl2.next();
        while(itPl1.hasNext()&& itPl2.hasNext())
        {
            if(docId1 == docId2) {
                Answer.add(docId1);
                docId1=itPl1.next();
                docId2=itPl2.next();
            }
            else if(docId1<docId2)
            {if(itPl1.hasNext())
                    docId1=itPl1.next();
                else
                    return Answer;
            }
            else
            {if(itPl2.hasNext())
                {
                    docId2=itPl2.next();
                }
                else
                    return Answer;
            }
        }
        if(docId1==docId2)
            Answer.add(docId1);
     return Answer;
    }

    public void findQueryPositions(String phrase) {
        ArrayList<ArrayList<Integer>> matchList = new ArrayList<>();
        // number of documents
        int num=10;
       for(int i=0;i<num;i++)
       {
           ArrayList<Integer> test=new ArrayList<>();
           matchList.add(test);
       }
        String[] terms = phrase.split("\\W+");
        int len=terms.length;
        for (String term : terms) {
            if (termIndex.containsKey(term)) {
                Poisting poistinglist = termIndex.get(term).plist;
                while (poistinglist != null) {
                    if (matchList.get(poistinglist.docId - 1).size() != 0) {
                        if (matchList.get(poistinglist.docId - 1).get(matchList.get(poistinglist.docId - 1).size() - 1) == poistinglist.positions.get(0)-1) {
                            matchList.get(poistinglist.docId - 1).add(poistinglist.positions.get(0));
                        }
                    } else {

                        matchList.get(poistinglist.docId - 1).add(poistinglist.positions.get(0));
                    }
                    poistinglist=poistinglist.next;
                }
            }
        }
        int i=1;
        ArrayList<Integer>query=new ArrayList<>();
        for(ArrayList matchlist:matchList)
        {
            if(matchlist.size()==len)
                query.add(i);
            i++;
        }
        if(query.size()==0)
            System.out.println("The query : "+phrase+" is not exist");
        else
            System.out.println("The query : "+phrase+" in the documents:"+query);
    }
}
