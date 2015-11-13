package org.gitools.heatmap.header;

import com.google.common.base.Joiner;
import org.apache.commons.math3.util.FastMath;

import java.awt.*;
import java.util.*;


public class HierarchicalClusterNamer {

    public static final int[] DEFAULT_PALETTE = {0x4bb2c5, 0xEAA228, 0xc5b47f, 0x546D61, 0x958c12, 0x953579, 0xc12e2e, 0x4b5de4, 0xd8b83f, 0xff5800, 0x0085cc, 0xc747a3, 0xcddf54, 0xFBD178, 0x26B4E3, 0xbd70c7, 0xabdbeb, 0x40D800, 0x8AFF00, 0xD9EB00, 0xFFFF71, 0x777B00, 0x498991, 0xC08840, 0x9F9274, 0x579575, 0x646C4A, 0x6F6621, 0x6E3F5F, 0x4F64B0, 0xA89050, 0xC45923, 0x187399, 0x945381, 0x959E5C, 0xAF5714, 0x478396, 0x907294, 0x426c7a, 0x878166, 0xAEA480, 0xFFFFD3, 0xE9D5A4, 0xA29877};
    public static final int[] GREY_PALETTE = {0xDCDCDC, 0xD3D3D3, 0xC0C0C0, 0xA9A9A9, 0x808080, 0x696969, 0x778899, 0x708090, 0x2F4F4F, 0x000000};
    public static final int[] REDGREEN_PALETTE = {0xa50026, 0xd73027, 0xf46d43, 0xfdae61, 0xfee08b, 0xffffbf , 0xd9ef8b, 0xa6d96a, 0x66bd63, 0x1a9850, 0x006837};
    public static final int[] BLUES_PALETTE = {0xf7fbff, 0xdeebf7, 0xc6dbef, 0x9ecae1, 0x6baed6, 0x4292c6, 0x2171b5, 0x08519c, 0x08306b};
    public static final int[] FOREST_PALETTE = {0x007789, 0x439d91, 0x3fb5af, 0x64cdb3, 0x9cdcbc, 0xe0dbb1, 0xd3a897, 0x8c5434, 0x613a23}; //http://colrd.com/palette/22706/



    public static final Map<String, int[]> palettes = new HashMap<>();
    static {
        palettes.put("Default", DEFAULT_PALETTE);
        palettes.put("Greys", GREY_PALETTE);
        palettes.put("Red-Green", REDGREEN_PALETTE);
        palettes.put("Blues", BLUES_PALETTE);
        palettes.put("Forest", FOREST_PALETTE);
    }

    private static int colorIndex = -1;
    private static Color nextColor(int[] palette) {
        colorIndex++;
        return new Color(palette[colorIndex % palette.length]);
    }

    public static Color nameClusters(HierarchicalCluster root, int[] palette) {
        return nameClusters(root.getChildren(), 1, palette);
    }

    public static Color nameClusters(HierarchicalCluster root, String palette) {
        return nameClusters(root.getChildren(), 1, HierarchicalClusterNamer.palettes.get(palette));
    }

    private static Color nameClusters(java.util.List<HierarchicalCluster> children, int level, int[] palette) {

        if (children.isEmpty()) {
            return nextColor(palette);
        }

        int digits = calculateDigits(children.size());

        Collections.sort(children, Collections.reverseOrder());

        Color colorParent = null;
        Double weightParent = 0.0;
        for (int i = 0; i < children.size(); i++) {
            HierarchicalCluster child = children.get(i);
            if (child.getChildren().isEmpty()) {
                child.setName(JOINER.join(child.getIdentifiers()));
            } else {
                child.setName(child.getParentName() + createLabel(i, digits));
            }
            child.setSortName(child.getParentName() + createLabel(i, digits));
            Color colorChild = nameClusters(child.getChildren(), level+1, palette);
            child.setColor(colorChild.getRGB());

            if (colorParent == null) {
                colorParent = colorChild;
            } else {
                colorParent = mixColors(colorParent, weightParent, colorChild, child.getWeight());
            }
            weightParent = child.getWeight();

        }

        if (level > 10) {
            return nextColor(palette);
        }

        return colorParent;

    }

    private static Color mixColors(Color c1, double w1, Color c2, double w2) {

        if (w1 == 0.0) {
            return c2;
        }

        if (w2 == 0.0) {
            return c1;
        }

        double wa1 = FastMath.abs(w1);
        double wa2 = FastMath.abs(w2);

        double total = wa1 + wa2;

        double t1 = wa1 / total;
        double t2 = wa2 / total;

        if (t1 < 0.1) {
            t1 = 0.1; t2 = 0.8;
        }

        if (t2 < 0.1) {
            t1 = 0.8; t2 = 0.1;
        }

        int red = (int) (t1*(double)c1.getRed() + t2*(double)c2.getRed());
        int green = (int) (t1*(double)c1.getGreen() + t2*(double)c2.getGreen());
        int blue = (int) (t1*(double)c1.getBlue() + t2*(double)c2.getBlue());

        return new Color(red, green, blue);
    }

    private static Joiner JOINER = Joiner.on("&");
    private static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static int calculateDigits(int size) {
        return ((int) FastMath.log(ALPHABET.length, size)) + 1;
    }

    public static String createLabel(int number, int digits) {

        char[] label = new char[digits];
        label[digits - 1] = ALPHABET[number % ALPHABET.length];

        for (int d = 1; d < digits; d++) {
            int quocient = number / (ALPHABET.length * d);
            label[digits - d - 1] = ALPHABET[quocient % ALPHABET.length];
        }

        return String.valueOf(label);

    }
}
