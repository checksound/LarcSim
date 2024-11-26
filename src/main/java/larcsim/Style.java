//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package larcsim;

import java.awt.Color;

public class Style {
    static final Color BG_COLOR_FOR_GET = new Color(210, 230, 255);
    static final Color BG_COLOR_FOR_SET = new Color(255, 255, 200);
    public static final String BINARY = "Binary";
    public static final String UNSIGNED = "Unsigned Decimal";
    public static final String SIGNED = "Signed Decimal";
    public static final String HEXADECIMAL = "Hexadecimal";
    public static final String CHARACTER = "Character";
    public static final String ASSEMBLY = "Assembly Command";

    public Style() {
    }

    public static String getDisplayText(int var0, String var1) {
        String var2;
        switch (var1) {
            case "Binary":
                var2 = "0b";

                for(int var7 = 15; var7 >= 0; --var7) {
                    if ((var0 & 1 << var7) == 0) {
                        var2 = var2 + "0";
                    } else {
                        var2 = var2 + "1";
                    }
                }
                break;
            case "Unsigned Decimal":
                var2 = "" + (var0 & '\uffff');
                break;
            case "Signed Decimal":
                var2 = "" + (short)var0;
                break;
            case "Hexadecimal":
                var2 = "0x";

                for(int var5 = 12; var5 >= 0; var5 -= 4) {
                    int var6 = var0 >> var5 & 15;
                    if (var6 < 10) {
                        var2 = var2 + var6;
                    } else {
                        var2 = var2 + (char)(65 + var6 - 10);
                    }
                }
                break;
            case "Character":
                if ((var0 < 0 || var0 > 32) && (var0 < 127 || var0 > 159)) {
                    var2 = "'" + (char)var0;
                } else {
                    var2 = "'<" + var0 + ">";
                }
                break;
            case "Assembly Command":
                var2 = Instruction.toString(var0);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized didplay style");
        }

        return var2;
    }
}
