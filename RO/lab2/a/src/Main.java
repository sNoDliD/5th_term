import java.awt.geom.Area;
import java.util.ArrayList;

class AreaData {
    public int Idx;
    public boolean IsWinnie;

    public AreaData(int _Idx, boolean _IsWinnie) {
        Idx = _Idx;
        IsWinnie = _IsWinnie;
    }
}

class ForestAreas {
    private volatile boolean[] UsedAreas;
    private boolean[]          Areas;

    public ForestAreas(int _AreasCount) {
        if (_AreasCount == 0)
            return;

        Areas     = new boolean[_AreasCount];
        UsedAreas = new boolean[_AreasCount];

        int IdxWithWinnie = (int)(Math.random() * (_AreasCount - 1));
        Areas[IdxWithWinnie] = true;
    }

    public synchronized AreaData GetArea() {
        for (int i = 0; i < Areas.length; i++) {
            if (!UsedAreas[i]) {
                UsedAreas[i] = true;
                return new AreaData(i, Areas[i]);
            }
        }
        return new AreaData(-1, false);
    }

    public void FreeArea(int _AreaIdx) {
        UsedAreas[_AreaIdx] = false;
    }

    public void Wait() {
        boolean NeedToWait = true;
        while (NeedToWait) {
            NeedToWait = false;
            for (boolean IsAreaUsed: UsedAreas) {
                if (IsAreaUsed) {
                    NeedToWait = true;
                    break;
                }
            }
        }
    }

}

class BeeWork extends Thread {

    static int Counter;
    int Idx;
    AreaData CheckedArea;
    ForestAreas Forest;

    public BeeWork(ForestAreas _Forest) {
        Forest = _Forest;
        Idx = Counter++;
    }

    @Override
    public void run() {
        try {
            CheckedArea = Forest.GetArea();
            System.out.println("Bees #" + Idx + ": started");
            sleep(5000);
        } catch (InterruptedException Unused) {
            CheckedArea.IsWinnie = false;
        }
        finally {
            System.out.println("Bees #" + Idx + ": " + (CheckedArea.IsWinnie ? "!!! SUCCESS !!!" : "FAILURE"));
            Forest.FreeArea(CheckedArea.Idx);
        }
    }
}

public class Main {

    public static void main(String[] args) {
        int N = 100;
        ArrayList<BeeWork> Bees = new ArrayList<>();
        ForestAreas Forest = new ForestAreas(N);
        for (int i = 0; i < N; ++i) {
            Bees.add(new BeeWork(Forest));
        }
        for (BeeWork Bee : Bees) {
            Bee.start();
        }
        Forest.Wait();
    }
}
