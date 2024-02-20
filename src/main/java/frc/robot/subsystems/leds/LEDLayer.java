package frc.robot.subsystems.leds;

import edu.wpi.first.wpilibj.util.Color;
import frc.robot.util.OpacityColor;
import frc.robot.util.Util;

public class LEDLayer {
    protected final OpacityColor[] colorArray;
    protected final int ledLength;

    public LEDLayer(int length) {
        ledLength = length;
        colorArray = new OpacityColor[length];
    }

    /**
     * Sets an LED at a specified index.
     * @param i The LED index to set.
     * @param color The color to set the LED at index i to (null is equivalent to transparent).
     * @param opacity The opacity to set the LED at index i to
     */
    public void setLED(int i, OpacityColor color) {
        colorArray[i] = color;
    }

    /**
     * Gets the color of the LED at a specified index.
     * @param i The LED index to retrieve.
     * @return The color of the LED at index i.
     */
    public OpacityColor getLEDColor(int i) {
        return colorArray[i];
    }


    /**
     * Moves the leds up by an increment
     * @param inc The number of leds to move up by
     * @param color The color to set at the bottom
     * @param opacity The opacity to set the new leds at
     */
    public void incrementColors(int inc, OpacityColor color) {
        for (int i = 0; i < colorArray.length - inc; i++) {
            setLED(i, getLEDColor(i + inc));
        }
        for (int i = colorArray.length - inc; i < colorArray.length; i++) {
            setLED(i, color);
        }
    }

    /**
     * Fills the layer with a solid color.
     * @param color The color to fill the layer with.
     * @param opacity The opacity to fill the layer with
     */
    public void fillColor(OpacityColor color) {
        for (int i = 0; i < colorArray.length; i++) {
            setLED(i, color);
        }
    }


    /**
     * Fills the layer with alternating groups of "on" and "off" LEDs. "off" leds are set to null (transparent).
     * @param onGroupLength The length of the "on" group.
     * @param offGroupLength The length of the "off" group.
     * @param borderLength The length of a gradient border which fades in at the edge of each "on" length
     * @param color The color to set the "on" LEDs.
     * @param opacity The opacity of the "on" LEDs.
     * @param offset The number of LEDs to offset the base by
     */
    public void fillGrouped(int onGroupLength, int offGroupLength, int borderLength, OpacityColor color, OpacityColor baseColor, int offset) {
        for (int i = 0; i < colorArray.length; i++) {
            int ledNumInSegment = (i + offset) % (2 * borderLength + onGroupLength + offGroupLength);
            if (ledNumInSegment < borderLength){
                setLED(i, OpacityColor.blendColors(baseColor, color, (ledNumInSegment + 1) / (borderLength + 1)));
            } else if (ledNumInSegment < onGroupLength + borderLength) {
                setLED(i, color);
            } else if(ledNumInSegment < onGroupLength + borderLength * 2){
                setLED(i, OpacityColor.blendColors(baseColor, color, (1 - ((ledNumInSegment - onGroupLength - borderLength + 1.) / (borderLength + 1)))));                
            } else {
                setLED(i, baseColor);
            }
        }
    }

    public void fillGrouped(int onGroupLength, int offGroupLength, OpacityColor color){
        fillGrouped(onGroupLength, offGroupLength, 0, color, new OpacityColor(), 0);
    }

    /**
     * Resets the layer by setting all LEDs to null (transparent).
     */
    public void reset() {
        fillColor(new OpacityColor());
    }

    public void scale(double factor){
        for(int i = 0; i < colorArray.length; i++){
            setLED(i, Util.scaleColor(getLEDColor(i), factor));
        }
    }
}