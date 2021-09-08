package first.lab.a;

import first.lab.CustomRunnable;

import javax.swing.*;

public class App {
    private static Thread firstThread;
    private static Thread secondThread;
    private static final JSlider slider = new JSlider();
    private static boolean isStarted = false;

    public static void main(String[] args) {
        setUI();

        firstThread = new Thread(new CustomRunnable(10, slider));
        secondThread = new Thread(new CustomRunnable(90, slider));
        firstThread.setDaemon(true);
        secondThread.setDaemon(true);
        firstThread.setPriority(1);
        secondThread.setPriority(1);
    }

    private static void clickedStartButton() {
        if(!isStarted) {
            firstThread.start();
            secondThread.start();
            isStarted = true;
        }
    }

    private static void setUI() {
        JFrame frame = new JFrame("A");

        slider.setBounds(50,30,300,50);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JButton startButton = new JButton("Start!");
        startButton.setBounds(100,200,200,40);
        startButton.addActionListener(e -> clickedStartButton());

        SpinnerModel firstModel = new SpinnerNumberModel(1, 0, 10, 1);
        JSpinner firstSpinner = new JSpinner(firstModel);
        firstSpinner.addChangeListener(e -> {
            int changedValue = (int) firstSpinner.getValue();
            firstThread.setPriority(changedValue);
        });
        firstSpinner.setBounds(80, 100, 100, 50);

        SpinnerModel secondModel = new SpinnerNumberModel(1, 0, 10, 1);
        JSpinner secondSpinner = new JSpinner(secondModel);
        secondSpinner.addChangeListener(e -> {
            int changedValue = (int) secondSpinner.getValue();
            secondThread.setPriority(changedValue);
        });
        secondSpinner.setBounds(240, 100, 100, 50);

        frame.add(slider);
        frame.add(startButton);
        frame.add(firstSpinner);
        frame.add(secondSpinner);
        frame.setSize(400,500);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
