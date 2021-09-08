package first.lab;

import first.lab.b.App;

import javax.swing.*;

public class MyRunnable implements Runnable{
    private int value;
    private JSlider slider;

    public MyRunnable(int value, JSlider slider) {
        this.slider = slider;
        this.value = value;
    }

    @Override
    public void run() {
        if(App.semaphore.compareAndSet(0,1)) {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (slider) {
                    slider.setValue(value);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            App.semaphore.set(0);
        }
    }
}
