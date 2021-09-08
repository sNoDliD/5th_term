package first.lab;

import javax.swing.*;

public class CustomRunnable implements Runnable{
    private int value;
    private JSlider slider;

    public CustomRunnable(int value, JSlider slider) {
        this.slider = slider;
        this.value = value;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (slider){
                slider.setValue(value);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
