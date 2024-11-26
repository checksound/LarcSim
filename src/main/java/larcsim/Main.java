//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package larcsim;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Main {
    public Main() {
    }

    public static void main(String[] var0) {
        System.setProperty("sun.java2d.opengl", "true");
        JFrame var1 = new JFrame("Larc Simulator/Debugger");
        var1.setDefaultCloseOperation(3);
        ContentPanel var2 = new ContentPanel();
        var1.setContentPane(var2);
        var1.pack();
        var1.setResizable(false);
        Dimension var3 = Toolkit.getDefaultToolkit().getScreenSize();
        Insets var4 = Toolkit.getDefaultToolkit().getScreenInsets(var1.getGraphicsConfiguration());
        int var5 = (var3.height - var1.getHeight()) / 2;
        int var6 = (var3.width - var1.getWidth()) / 2;
        if (var5 < var4.top + 5) {
            var5 = var4.top + 5;
        }

        if (var6 < var4.left + 5) {
            var6 = var4.left + 5;
        }

        var1.setLocation(var6, var5);
        var1.setVisible(true);
        var2.fixSizes();
        if (var0.length > 0) {
            var2.loadFile(var0[0]);
        }

    }
}
