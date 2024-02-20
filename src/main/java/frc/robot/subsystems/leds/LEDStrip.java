package frc.robot.subsystems.leds;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.util.OpacityColor;
import frc.robot.util.Util;

public class LEDStrip {
    private final AddressableLED led;
    private final AddressableLEDBuffer ledBuffer;

    private int ledLength;

    public LEDStrip(int ledPort, int ledLength) {
        this.ledLength = ledLength;
        led = new AddressableLED(ledPort);
        ledBuffer = new AddressableLEDBuffer(ledLength);
        led.setLength(ledBuffer.getLength());
        led.start();
    }

    /**
     * Sets the LED data to the current buffer.
     */
    public void setBuffer() {
        led.setData(ledBuffer);
    }

    public void setBuffer(double brightnessFactor){
        for(int i = 0; i < ledLength; i++){
            ledBuffer.setLED(i, Util.scaleColor(ledBuffer.getLED(i), brightnessFactor));
        }
        led.setData(ledBuffer);
    }

    /**
     * Applies an `LEDLayer` on top of the LED buffer.
     * @param layer The layer to be added on top of the buffer. Null leds are considered transparent and "fall through" to the previous layer's color.
     */
    public void addLayer(LEDLayer layer) {
        for (int i = 0; i < ledLength; i++) {
            if (layer.getLEDColor(i) != null) {
                ledBuffer.setLED(i, calcColorWithOpacity(ledBuffer.getLED(i), layer.getLEDColor(i)));
            }
        }
    }

    /**
     * Calculates the desired color when led layering
     * @param baseColor The color of the lower layer led
     * @param topColor The color of the led being added
     * @param opacity The opacity of the top led
     * @return The color of the led layers combined
     */
    public Color calcColorWithOpacity(Color baseColor, OpacityColor topColor){
        double r = (((1 - topColor.opacity) * baseColor.red) + (topColor.opacity * topColor.red));
        double g = (((1 - topColor.opacity) * baseColor.green) + (topColor.opacity * topColor.green));
        double b = (((1 - topColor.opacity) * baseColor.blue) + (topColor.opacity * topColor.blue));

        return(new Color(r, g, b));
    }
}