package GUIs.settings;

import java.awt.GridLayout;

import javax.swing.*;

public class guiSetting {

    private JFrame jFrame;
    private JSlider FPSslider;
    private JSlider FogDensitySlider;

    public void build() {
        // Set up new window called "--SETTINGS--"
        jFrame = new JFrame("--SETTINGS--");
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setSize(300, 400);
        jFrame.setLayout(new GridLayout(10, 10, 0, 0));

        // Create fps (Frame per second) slider with range 0 to 120 and default value 60
        FPSslider = new JSlider(0, 120, 60);
        FPSslider.setLayout(new GridLayout(1, 1, 0, 0));

        // Create fog density slider with range 0 to 1000 and default value 0
        FogDensitySlider = new JSlider(0, 1000, 10);
        FogDensitySlider.setLayout(new GridLayout(1, 2, 0, 0));

        // Add all components to window and show it to a user
        jFrame.add(FPSslider);
        jFrame.add(FogDensitySlider);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public int getFPS() {           // Return value of fps (Frame per second) slider
        return FPSslider.getValue();
    }

    public float getFogDensity() {  // Return value of fog density slider divided by 1000
        return (FogDensitySlider.getValue() / 1000f);
    }
}
