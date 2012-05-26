package de.androidlab.trackme.map;

import android.graphics.Color;

public class ColorGenerator {
    
    private static final float SATURATION = 0.7F;
    private static final float LIGHTNESS = 0.5F;
    private int[] colorList;
    private int posStart;
    private int posEnd;
    private int posMid;
    private int alternating = 0;
    private double stepSize;
    
    public ColorGenerator(int count) {
        if (count != 0) {
	    	stepSize = 360/count;
	        colorList = new int[count];
	        posStart = 0;
	        posMid = (count+1)/2;
	        posEnd = posMid;
	        for (int i = 0; i < count; i++) {
	            colorList[i] = generateColor((float)(i*stepSize));
	        }
        }
    }
    
    public int getNewColor() {
        if (posStart < posMid && posEnd < colorList.length) {
            if ((alternating++%2) == 0) {
                return colorList[posStart++];
            } else {
                return colorList[posEnd++];
            }
        } else {
            generateNewColorList();
            posStart = 0;
            posMid = (colorList.length+1)/2;
            posEnd = posMid;
            return getNewColor();
        }
    }
    
    private void generateNewColorList() {
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
                                     (float) (SATURATION + Math.random() * 0.3F),
                                     (float) (LIGHTNESS + Math.random() * 0.5F)};
        return Color.HSVToColor(color);
    }
}
