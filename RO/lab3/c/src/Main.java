import java.util.concurrent.Semaphore;

class Component {
    static final int Nothing = 0;
    static final int Tobacco = 1 << 0;
    static final int Paper   = 1 << 1;
    static final int Match   = 1 << 2;
    static final int All     = Tobacco | Paper | Match;

    static public String ToString(int _Value) {

        if (_Value == Nothing)
            return "Nothing";
        if (_Value == All)
            return "All";

        String Result = "";
        boolean IsFirst = true;

        if ((_Value & Tobacco) != Nothing) {
            Result += "Tobacco";
            IsFirst = false;
        }
        if ((_Value & Paper) != Nothing) {
            if (!IsFirst)
                Result += ", ";
            Result += "Paper";
            IsFirst = false;
        }
        if ((_Value & Match) != Nothing) {
            if (!IsFirst)
                Result += ", ";
            Result += "Match";
        }

        if (Result.isEmpty())
            return "???";
        return Result;
    }

}

class CSmoker extends Thread {
    private int m_AvailableComponent;

    public CSmoker(int _AvailableComponent) {
        m_AvailableComponent = _AvailableComponent;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                Main.TableSemaphore.acquire();
            } catch (InterruptedException e) { return; }

            if ((Main.TableWithComponents | m_AvailableComponent) == Component.All) {
                System.out.println("Smoker with " + Component.ToString(m_AvailableComponent) + ": 'Oh, let's smoke!'");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) { return; }

                Main.TableWithComponents = Component.Nothing;
                System.out.println("Smoker with " + Component.ToString(m_AvailableComponent) + ": 'That was nice!'");
            }

            Main.TableSemaphore.release();
        }
    }
}

class CHelper extends Thread {

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                Main.TableSemaphore.acquire();
            } catch (InterruptedException e) { return; }

            if (Main.TableWithComponents != Component.Nothing)
                continue;

            double Rand = Math.random();
            Main.TableWithComponents = Rand < 0.333 ?
                                       (Component.Tobacco | Component.Paper) :
                                       Rand < 0.666 ?
                                       (Component.Paper | Component.Match) :
                                       (Component.Tobacco | Component.Match);
            System.out.println("Helper: 'Oh, empty table... Let's put " + Component.ToString(Main.TableWithComponents) + " on table!'");
            Main.TableSemaphore.release();
        }
    }
}

public class Main {

    static CSmoker Smoker1 = new CSmoker(Component.Tobacco);
    static CSmoker Smoker2 = new CSmoker(Component.Paper);
    static CSmoker Smoker3 = new CSmoker(Component.Match);
    static CHelper Helper = new CHelper();
    static int TableWithComponents = Component.Nothing;
    static Semaphore TableSemaphore = new Semaphore(1, true);

    public static void main(String[] args) {
        Smoker1.start();
        Smoker2.start();
        Smoker3.start();
        Helper.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {}
        finally {
            Smoker1.interrupt();
            Smoker2.interrupt();
            Smoker3.interrupt();
            Helper.interrupt();
        }
    }
}
