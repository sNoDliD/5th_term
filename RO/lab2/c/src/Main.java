import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.RecursiveTask;

enum FighterSide {
    YIN,
    YANG
}

class Fighter {

    private static int Counter;
    private int Idx;
    private int Power;
    private FighterSide Side;

    public Fighter() {
        Idx = ++Counter;
        Power = (int)(Math.random() * 100);
        Side = (Counter % 2 == 1 ? FighterSide.YIN : FighterSide.YANG);
    }

    @Override
    public String toString() {
        return "Fighter #" + Idx + ", Side: " + Side + ", Power: " + Power + ";";
    }

    static public Fighter Fight(Fighter _Left, Fighter _Right) {
        Fighter Winner = (_Left.Power > _Right.Power ? _Left : _Right);
        System.out.println(_Left + "\nVS\n" + _Right + "\nWINNER: " + Winner + "\n");
        return Winner;
    }

}

class Battle extends RecursiveTask<Fighter> {

    static private ArrayList<Fighter> Fighters;
    private int LeftIdx;
    private int RightIdx;

    public Battle(int _N) {
        Fighters = new ArrayList<>();
        for (int i = 0; i < _N; i++) {
            Fighters.add(new Fighter());
        }
        LeftIdx = 0;
        RightIdx = Fighters.size();
    }

    private Battle(int _LeftIdx, int _RightIdx) {
        LeftIdx = _LeftIdx;
        RightIdx = _RightIdx;
    }

    @Override
    protected Fighter compute() {
        if (RightIdx - LeftIdx == 1)
            return Fighters.get(LeftIdx);
        else if (RightIdx - LeftIdx == 2)
            return Fighter.Fight(Fighters.get(LeftIdx), Fighters.get(LeftIdx + 1));
        else {
            int MiddleIdx = (LeftIdx + RightIdx) / 2;
            Battle LeftPartBattle = new Battle(LeftIdx, MiddleIdx);
            Battle RightPartBattle = new Battle(MiddleIdx, RightIdx);

            LeftPartBattle.fork();
            RightPartBattle.fork();

            Fighter LeftWinner = LeftPartBattle.join();
            Fighter RightWinner = RightPartBattle.join();

            return Fighter.Fight(LeftWinner, RightWinner);
        }
    }

    public Fighter GetWinner() {
        return compute();
    }
}

public class Main {

    public static void main(String[] args) {
        Battle MainBattle = new Battle(100);
        System.out.println("!!! COMPETITION WINNER !!!\n" + MainBattle.GetWinner());
    }
}
