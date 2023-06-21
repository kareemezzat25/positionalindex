import java.io.IOException;
import java.util.*;

    public class Main {
        public static void main(String args[])  throws IOException{
            Scanner input=new Scanner(System.in);
            PositionalIndex positionalIndex=new PositionalIndex();
            String []documents=new String[10];
            //Path of Documents
            documents=new String[]
            {
                    "D:\\Documents/Doc1.txt","D:\\Documents/Doc2.txt","D:\\Documents/Doc3.txt",
                    "D:\\Documents/Doc4.txt","D:\\Documents/Doc5.txt", "D:\\Documents/Doc6.txt",
                    "D:\\Documents/Doc7.txt","D:\\Documents/Doc8.txt","D:\\Documents/Doc9.txt",
                    "D:\\Documents/Doc10.txt"
            };
            positionalIndex.createIndex(documents);//create index
             // positional index
            //positionalIndex.printPositionlIndex();
            System.out.println("Enter The word you want : ");
            String term=input.nextLine();
            System.out.println("if you want the documents that have query with terms in same order Enter : 1");
            System.out.println("if you want the documents that have query with terms Enter : 2 ");
            int num=input.nextInt();
            if(num==1)
                positionalIndex.findQueryPositions(term);
            else if(num==2)
                positionalIndex.findQuery(term);
            else
                System.out.println("invalid Number Enter 1 or 2");
        }
    }


