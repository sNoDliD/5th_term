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
                    while (slider.getValue() != value) {
                        int now = slider.getValue();
                        slider.setValue(now > value ? now - 1 : now + 1);
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
            App.semaphore.set(0);
        }
    }
}
