//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package larcsim;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ContentPanel extends JPanel {
    private Memory memory = new Memory();
    private Registers registers = new Registers();
    private IO io = new IO();
    private ControlPanel controls;

    public ContentPanel() {
        this.memory = new Memory();
        this.registers = new Registers();
        this.io = new IO();
        this.controls = new ControlPanel(this.memory, this.registers, this.io);
        JPanel var1 = new JPanel();
        JPanel var2 = new JPanel();
        JPanel var3 = new JPanel();
        this.setLayout(new BorderLayout(8, 8));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.add(var1, "North");
        this.add(var2, "Center");
        var2.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Input/Output for Syscall"), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        var2.add(this.io);
        var3.setLayout(new BorderLayout(8, 8));
        var3.add(this.controls, "Center");
        var3.add(this.registers, "South");
        var1.setLayout(new BorderLayout(8, 8));
        var1.add(var3, "West");
        var1.add(this.memory, "Center");
    }

    void loadFile(String var1) {
        this.memory.doLoadFile(new File(var1));
    }

    void fixSizes() {
        this.memory.setPreferredSize(this.memory.getSize());
        this.io.setPreferredSize(this.io.getSize());
        this.registers.setPreferredSize(this.registers.getSize());
        this.controls.setPreferredSize(this.controls.getSize());
    }
}
