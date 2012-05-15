package de.androidlab.trackme.map;

import android.graphics.Color;
import android.util.Log;

public class ColorGenerator {
    
    private static final float SATURATION = 0.9F;
    private static final float LIGHTNESS = 0.5F;
    private int[] colorList;
    private int posStart;
    private int posEnd;
    private int alternating = 0;
    private double stepSize;
    
    public ColorGenerator(int count) {
        stepSize = 360/count;
        colorList = new int[count];
        posStart = 0;
        posEnd = count - 1;
        for (int i = 0; i < count; i++) {
            colorList[i] = generateColor((float)(i*stepSize));
        }
    }
    
    public int getNewColor() {
        if (posStart <= posEnd) {
            if ((alternating++%2) == 0) {
                return colorList[posStart++];
            } else {
                return colorList[posEnd--];
            }
        } else {
            generateNewColorList();
            posStart = 0;
            posEnd = colorList.length - 1;
            return getNewColor();
        }
    }
    
    private void generateNewColorList() {
        Log.d("Test", "-----------");
        float start = (float) (stepSize/2);
        int count = (int) (1 + (360 - start)/stepSize);
        colorList = new int[count];
        for (int i = 0; i < colorList.length; i++) {
            colorList[i] = generateColor((float) (start + i * stepSize));
        }
        stepSize/=2;
    }
    
    private int generateColor(float hue) {
        float[] color = new float[] {hue,
                                     SATURATION,
                                     LIGHTNESS};
        return Color.HSVToColor(color);
    }
}
