import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

class Common {
    static public final int STRLEN = 5;
    static public String AvailableChars = "ABCD";

    static char GetRandomChar() {
        return AvailableChars.charAt((int)(Math.random() * AvailableChars.length()));
    }

    static String GetRandomString() {
        StringBuilder Str = new StringBuilder();
        for (int i = 0; i < STRLEN; ++i)
            Str.append(GetRandomChar());
        return Str.toString();
    }
}

class StringProcessor extends Thread {

    public String Name;
    public String Str = Common.GetRandomString();
    public AtomicBoolean StartIteration =  new AtomicBoolean(false);
    public AtomicBoolean FinishIteration = new AtomicBoolean(false);

    public StringProcessor(String Name) {
        this.Name = Name;
    }

    @Override
    public void run() {
        while(!interrupted()) {

            while(!StartIteration.get());

            StartIteration.set(false);

            int RandomPos = (int)(Math.random() * Str.length());
            char Char = Str.charAt(RandomPos);
            StringBuilder StrBuild = new StringBuilder(Str);

            if (Char == 'A')
                StrBuild.setCharAt(RandomPos, 'C');
            else if (Char == 'C')
                StrBuild.setCharAt(RandomPos, 'A');
            else if (Char == 'B')
                StrBuild.setCharAt(RandomPos, 'D');
            else if (Char == 'D')
                StrBuild.setCharAt(RandomPos, 'B');

            System.out.println("String " + Name + " before: " + Str + "\n" +
                               "String " + Name + " after:  " + StrBuild.toString() + "\n");
            Str = StrBuild.toString();

            FinishIteration.set(true);
        }
    }

    public int GetABCount() {
        int Sum = 0;
        for (int i = 0; i < Str.length(); ++i)
            if (Str.charAt(i) == 'A' || Str.charAt(i) == 'B')
                ++Sum;
        return Sum;
    }
}


public class Main {



    public static void main(String[] args) {

        StringProcessor A = new StringProcessor("A");
        StringProcessor B = new StringProcessor("B");
        StringProcessor C = new StringProcessor("C");
        StringProcessor D = new StringProcessor("D");

        A.setDaemon(true);
        B.setDaemon(true);
        C.setDaemon(true);
        D.setDaemon(true);

        A.start();
        B.start();
        C.start();
        D.start();

        while (true) {
            A.StartIteration.set(true);
            B.StartIteration.set(true);
            C.StartIteration.set(true);
            D.StartIteration.set(true);

            while(!A.FinishIteration.get());
            A.FinishIteration.set(false);
            while(!B.FinishIteration.get());
            B.FinishIteration.set(false);
            while(!C.FinishIteration.get());
            C.FinishIteration.set(false);
            while(!D.FinishIteration.get());
            D.FinishIteration.set(false);

            int ASum = A.GetABCount();
            int BSum = B.GetABCount();
            int CSum = C.GetABCount();
            int DSum = D.GetABCount();

            System.out.println("A: " + ASum + ", B: " + BSum + ", C: " + CSum + ", " + "D: " + DSum + "\n");

            HashMap<Integer, Integer> Check = new HashMap<>();

            Check.put(ASum, Check.getOrDefault(ASum, 0) + 1);
            Check.put(BSum, Check.getOrDefault(BSum, 0) + 1);
            Check.put(CSum, Check.getOrDefault(CSum, 0) + 1);
            Check.put(DSum, Check.getOrDefault(DSum, 0) + 1);

            for (HashMap.Entry<Integer, Integer> Entry : Check.entrySet()) {
                if (Entry.getValue() >= 3) {
                    A.interrupt();
                    B.interrupt();
                    C.interrupt();
                    D.interrupt();
                    System.out.println("DONE");
                    return;
                }
            }
        }
    }
}
