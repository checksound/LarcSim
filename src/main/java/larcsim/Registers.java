//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package larcsim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Registers extends JPanel {
    private int[] value = new int[16];
    private JTextField[] register = new JTextField[16];
    private JComboBox<String> style;
    private boolean runFast;

    public Registers() {
        Font var1 = new Font("Monospaced", 0, 14);
        Font var2 = new Font("Monospaced", 1, 14);
        String var3 = "0                  ";
        JPanel var4 = new JPanel();
        var4.setLayout(new GridLayout(8, 2, 12, 2));

        for(int var5 = 0; var5 < 16; ++var5) {
            this.register[var5] = new JTextField(var3);
            this.register[var5].setOpaque(true);
            this.register[var5].setBackground(Color.WHITE);
            this.register[var5].setForeground(Color.BLACK);
            this.register[var5].setFont(var1);
            this.register[var5].setMargin(new Insets(3, 3, 3, 3));
            JPanel var6 = new JPanel();
            var6.setLayout(new BorderLayout(4, 4));
            JLabel var7 = new JLabel((var5 < 10 ? " " : "") + var5 + ":");
            var7.setFont(var2);
            var6.add(var7, "West");
            var6.add(this.register[var5], "Center");
            var4.add(var6);
            if (var5 > 0) {
                int finalVar = var5;
                this.register[var5].addActionListener((var2x) -> this.checkInput(finalVar));
                int finalVar1 = var5;
                this.register[var5].addFocusListener(new FocusListener() {
                    public void focusLost(FocusEvent var1) {
                        Registers.this.checkInput(finalVar1);
                    }

                    public void focusGained(FocusEvent var1) {
                        Registers.this.clearHilite();
                        Registers.this.register[finalVar1].selectAll();
                    }
                });
                this.register[var5].setToolTipText("Register value can be entered in decimal form, in binary form starting with 0b, in hexadecimal form starting with 0x");
            } else {
                this.register[0].setEditable(false);
                this.register[0].setToolTipText("Register zero always has value 0 and cannot be edited");
            }
        }

        this.style = new JComboBox();
        this.style.addItem("Binary");
        this.style.addItem("Unsigned Decimal");
        this.style.addItem("Signed Decimal");
        this.style.addItem("Hexadecimal");
        this.style.setSelectedIndex(1);
        JPanel var9 = new JPanel();
        var9.setLayout(new FlowLayout(1));
        var9.add(new JLabel("Display Style:"));
        var9.add(this.style);
        this.setLayout(new BorderLayout(10, 10));
        this.add(var9, "North");
        this.add(var4, "Center");
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Registers"), BorderFactory.createEmptyBorder(7, 7, 7, 7)));
        this.style.addActionListener((var1x) -> this.setStyle());
    }

    private void error(int var1, String var2) {
        JOptionPane.showMessageDialog(this, var2, "Error in register number " + var1, 0);
        this.register[var1].requestFocus();
    }

    private void setStyle() {
        String var1 = (String)this.style.getSelectedItem();

        for(int var2 = 0; var2 < 16; ++var2) {
            String var3 = Style.getDisplayText(this.value[var2], var1);
            this.register[var2].setText(var3);
        }

    }

    private void checkInput(int var1) {
        String var2 = (String)this.style.getSelectedItem();
        String var3 = this.register[var1].getText().trim();
        if (var3.length() != 0 && (var3.charAt(0) == '+' || var3.charAt(0) == '-' || Character.isDigit(var3.charAt(0)))) {
            try {
                this.value[var1] = Instruction.stringToCode(var3);
                this.register[var1].setText(Style.getDisplayText(this.value[var1], var2));
            } catch (Exception var5) {
                this.register[var1].setText(Style.getDisplayText(this.value[var1], var2));
                this.error(var1, var5.getMessage());
            }

        } else {
            this.register[var1].setText(Style.getDisplayText(this.value[var1], var2));
            this.error(var1, "Register must be entered as a numeric constant");
        }
    }

    public void setRunFast(boolean var1) {
        this.runFast = var1;
    }

    public void set(int var1, int var2) {
        if (!this.runFast) {
            this.register[var1].setBackground(Style.BG_COLOR_FOR_SET);
        }

        if (var1 > 0) {
            this.value[var1] = var2 & '\uffff';
            String var3 = (String)this.style.getSelectedItem();
            this.register[var1].setText(Style.getDisplayText(this.value[var1], var3));
        }

    }

    public int get(int var1) {
        if (!this.runFast) {
            this.register[var1].setBackground(Style.BG_COLOR_FOR_GET);
        }

        return this.value[var1];
    }

    public void setEditable(boolean var1) {
        for(int var2 = 1; var2 < 16; ++var2) {
            this.register[var2].setEditable(var1);
        }

    }

    public void clearHilite() {
        for(int var1 = 0; var1 < 16; ++var1) {
            this.register[var1].setBackground(Color.WHITE);
        }

    }
}
