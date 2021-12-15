package main;

import java.util.Random;

public class WorkingThread extends Thread {
    private Matrix matrix;
    private int task;

    public WorkingThread(Matrix matrix, int task) {
        this.matrix = matrix;
        this.task = task;
    }
    @Override
    public void run() {
        switch (task) {
            case 1: ChangePrice(); break;
            case 2: AddDeleteWay(); break;
            case 3: ShowPrice(); break;
            case 4: ShowMatrix(); break;
            default:
                System.out.println("Unknown task!");
        }
    }

    public int RandomCity() {
        return (int) (Math.random() * matrix.getN());
    }

    public void ChangePrice() {
        while (true) {
            int i = RandomCity();
            int j = RandomCity();
            int new_price = (int) (Math.random() * 100);

            try {
                while (matrix.getValue(i, j) == -1) {
                    i++;
                    if (i == matrix.getN()) {
                        i = 0;
                        j++;
                        if (j == matrix.getN())
                            j = 0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            matrix.LockWrite(i, j);
            System.out.println("Changing price: Start");
            try {
                var old_price = matrix.getValue(i, j);
                matrix.setValue(i, j, new_price);
                System.out.println("Changing price: way (" + i + ", " + j + "), price changed " + old_price + " -> " + new_price);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Changing price: Finish\n");
            matrix.UnlockWrite(i, j);

            try {
                Thread.sleep(3000 + (int) (Math.random() * 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void AddDeleteWay() {
        while (true) {
            int i = RandomCity();
            int j = RandomCity();
            boolean add = matrix.getValue(i, j) == -1;

            matrix.LockWrite(i, j);
            System.out.println("Add/Delete way: Start");
            try {
                if (add) {
                    int new_price = (int) (Math.random() * 100);
                    try {
                        matrix.setValue(i, j, new_price);
                        System.out.println("Add/Delete way: Added way (" + i + ", " + j + ") with price " + new_price);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        matrix.setValue(i, j, -1);
                        System.out.println("Add/Delete way: Deleted way (" + i + ", " + j + ")");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Add/Delete way: Finish\n");
            matrix.UnlockWrite(i, j);

            try {
                Thread.sleep(3000 + (int) (Math.random() * 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void ShowPrice() {
        while (true) {
            int i = RandomCity();
            int j = RandomCity();
            try {
                while (matrix.getValue(i, j) < 0)
                    j = RandomCity();
            } catch (Exception e) {
                e.printStackTrace();
            }

            matrix.LockRead(i, j);
            System.out.println("Price: Start");
            try {
                System.out.println("Price: from " + i + " to " + j + " costs = " + matrix.getValue(i, j));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Price: End\n");
            matrix.UnlockRead(i, j);

            try {
                Thread.sleep(3000 + (int) (Math.random() * 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ShowMatrix() {
        while (true) {
            matrix.LockRead();
            matrix.PrintMatrix();
            matrix.UnlockRead();
            try {
                Thread.sleep(3000 + (int) (Math.random() * 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}