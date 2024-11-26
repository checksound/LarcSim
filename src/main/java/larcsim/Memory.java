//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package larcsim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.border.Border;

public class Memory extends JPanel {
    private int[] value = new int[65536];
    private JLabel[] display = new JLabel[21];
    private MouseListener[] listener = new MouseListener[21];
    private JPanel labelHolder;
    private MouseWheelListener wheelListener;
    private boolean listenersInstalled;
    private JComboBox<String> displaySelect;
    private static final int DISPLAY_COUNT = 21;
    private static final int DISPLAY_CENTER = 10;
    private static final Border CLEAR_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2);
    private static final Border BLUE_BORDER = BorderFactory.createLineBorder(new Color(0, 0, 180), 2);
    private JScrollBar scroller;
    private int currentScroll;
    private ArrayList<Integer> breakpoints = new ArrayList();
    private JPopupMenu popupMenu;
    private JMenuItem setBP;
    private JMenuItem clearBP;
    private int popupMenuIndex;
    private boolean runFast;

    public Memory() {
        Font var1 = new Font("Monospaced", 0, 14);
        this.labelHolder = new JPanel();
        this.labelHolder.setLayout(new GridLayout(21, 1));

        for(int var2 = 0; var2 < 21; ++var2) {
            String var3 = String.format("%5d", var2);
            this.display[var2] = new JLabel(var3 + ": add $0 $0 $0       ");
            this.display[var2].setFont(var1);
            this.display[var2].setBorder(CLEAR_BORDER);
            this.display[var2].setOpaque(true);
            this.display[var2].setBackground(Color.WHITE);
            this.display[var2].setForeground(Color.BLACK);
            this.labelHolder.add(this.display[var2]);
            int finalVar = var2;
            this.listener[var2] = new MouseAdapter() {
                public void mousePressed(MouseEvent var1) {
                    Memory.this.doPopupMenu(finalVar, var1.getX(), var1.getY());
                }
            };
            this.display[var2].addMouseListener(this.listener[var2]);
        }

        this.wheelListener = (var1x) -> {
            int var2 = this.scroller.getModel().getValue();
            var2 += 5 * var1x.getWheelRotation();
            if (var2 < 0) {
                var2 = 0;
            } else if (var2 > this.scroller.getMaximum()) {
                var2 = this.scroller.getMaximum();
            }

            this.clearHilite();
            this.scroller.getModel().setValue(var2);
        };
        this.labelHolder.addMouseWheelListener(this.wheelListener);
        this.listenersInstalled = true;
        this.labelHolder.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.scroller = new JScrollBar(1, 10, 18, 0, 65553);
        this.currentScroll = 10;
        this.scroller.addAdjustmentListener((var1x) -> {
            this.display[10].setBackground(Color.WHITE);
            this.unmarkBreakpoints();
            int var2 = this.scroller.getValue();

            for(int var3 = 0; var3 < 21; ++var3) {
                int var4 = var2 - 10 + var3;
                this.display[var3].setText(this.displayText(var4));
            }

            this.currentScroll = var2;
            this.markBreakpoints();
        });
        JPanel var5 = new JPanel();
        var5.setLayout(new BorderLayout(3, 3));
        var5.add(this.labelHolder, "Center");
        var5.add(this.scroller, "East");
        this.displaySelect = new JComboBox();
        this.displaySelect.addItem("Binary");
        this.displaySelect.addItem("Signed Decimal");
        this.displaySelect.addItem("Unsigned Decimal");
        this.displaySelect.addItem("Hexadecimal");
        this.displaySelect.addItem("Character");
        this.displaySelect.addItem("Assembly Command");
        this.displaySelect.setSelectedIndex(5);
        this.displaySelect.addActionListener((var1x) -> this.doDisplayStyle());
        this.setLayout(new BorderLayout());
        this.add(new JLabel("Display Style:"), "Center");
        this.add(this.displaySelect, "South");
        this.add(var5, "North");
        var5.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Memory"), BorderFactory.createEmptyBorder(7, 7, 7, 7)));
        this.popupMenu = new JPopupMenu();
        this.setBP = new JMenuItem("Set Breakpoint");
        this.setBP.addActionListener((var1x) -> this.setBreakpoint(this.popupMenuIndex));
        this.clearBP = new JMenuItem("Clear Breakpoint");
        this.clearBP.addActionListener((var1x) -> this.clearBreakpoint(this.popupMenuIndex));
        JMenuItem var6 = new JMenuItem("Clear All Breakpoints");
        var6.addActionListener((var1x) -> this.clearAllBreakpoints());
        this.popupMenu.add(this.setBP);
        this.popupMenu.add(this.clearBP);
        this.popupMenu.addSeparator();
        this.popupMenu.add(var6);
    }

    private void doDisplayStyle() {
        for(int var1 = 0; var1 < 21; ++var1) {
            int var2 = var1 - 10 + this.currentScroll;
            this.display[var1].setText(this.displayText(var2));
        }

    }

    private void doPopupMenu(int var1, int var2, int var3) {
        int var4 = var1 - 10 + this.currentScroll;
        if (this.breakpoints.contains(var4)) {
            this.setBP.setEnabled(false);
            this.clearBP.setEnabled(true);
        } else {
            this.setBP.setEnabled(true);
            this.clearBP.setEnabled(false);
        }

        this.popupMenuIndex = var4;
        this.popupMenu.show(this.display[var1], var2, var3);
    }

    private String displayText(int var1) {
        if (var1 >= 0 && var1 <= 65535) {
            String var2 = String.format("%5d: ", var1);
            return var2 + Style.getDisplayText(this.value[var1], (String)this.displaySelect.getSelectedItem());
        } else {
            return "";
        }
    }

    public void set(int var1, int var2) {
        if (this.runFast) {
            this.setWithoutHilite(var1, var2);
        } else {
            var1 &= 65535;
            this.value[var1] = var2 & '\uffff';
            this.scroller.getModel().setValue(var1);
            this.display[10].setText(this.displayText(var1));
            this.display[10].setBackground(Style.BG_COLOR_FOR_SET);
        }

    }

    public void setWithoutHilite(int var1, int var2) {
        var1 &= 65535;
        this.value[var1] = var2 & '\uffff';
        int var3 = var1 + 10 - this.currentScroll;
        if (var3 >= 0 && var3 < 21) {
            this.display[var3].setText(this.displayText(var1));
        }

    }

    public int get(int var1) {
        var1 &= 65535;
        if (!this.runFast) {
            this.scroller.getModel().setValue(var1);
            this.display[10].setBackground(Style.BG_COLOR_FOR_GET);
        }

        return this.value[var1];
    }

    public int getWithoutHilite(int var1) {
        return this.value[var1 & '\uffff'];
    }

    public void clearHilite() {
        this.display[10].setBackground(Color.WHITE);
    }

    private void markBreakpoints() {
        for(int var2 : this.breakpoints) {
            int var3 = var2 + 10 - this.currentScroll;
            if (var3 >= 0 && var3 < 21) {
                this.display[var3].setBorder(BLUE_BORDER);
            }
        }

    }

    private void unmarkBreakpoints() {
        for(int var2 : this.breakpoints) {
            int var3 = var2 + 10 - this.currentScroll;
            if (var3 >= 0 && var3 < 21) {
                this.display[var3].setBorder(CLEAR_BORDER);
            }
        }

    }

    public void setBreakpoint(int var1) {
        this.breakpoints.add(var1);
        this.markBreakpoints();
    }

    public boolean isBreakpoint(int var1) {
        return this.breakpoints.contains(var1);
    }

    public void clearBreakpoint(int var1) {
        if (this.breakpoints.contains(var1)) {
            this.unmarkBreakpoints();
            this.breakpoints.remove(var1);
            this.markBreakpoints();
        }

    }

    public void clearAllBreakpoints() {
        this.unmarkBreakpoints();
        this.breakpoints.clear();
    }

    public void setEnableMouse(boolean var1) {
        if (var1 != this.listenersInstalled) {
            this.listenersInstalled = var1;
            if (this.listenersInstalled) {
                for(int var2 = 0; var2 < 21; ++var2) {
                    this.display[var2].addMouseListener(this.listener[var2]);
                }

                this.labelHolder.addMouseWheelListener(this.wheelListener);
            } else {
                for(int var3 = 0; var3 < 21; ++var3) {
                    this.display[var3].removeMouseListener(this.listener[var3]);
                }

                this.labelHolder.removeMouseWheelListener(this.wheelListener);
            }

        }
    }

    public void setRunFast(boolean var1) {
        this.runFast = var1;
    }

    public void error(String var1) {
        JOptionPane.showMessageDialog(this, var1, "File Error", 0);
    }

    public boolean doLoadFile(File var1) {
        int var2 = 0;

        BufferedReader var3;
        try {
            var3 = new BufferedReader(new FileReader(var1));
        } catch (Exception var9) {
            this.error("Can't open file " + var1.getAbsolutePath() + " for reading.");
            return false;
        }

        int[] var4 = new int[65536];
        int var5 = 0;

        try {
            while(true) {
                String var6 = var3.readLine();
                ++var5;
                if (var6 == null) {
                    var3.close();
                    break;
                }

                if (!var6.trim().startsWith("\"")) {
                    int var15 = var6.indexOf(35);
                    if (var15 >= 0) {
                        var6 = var6.substring(0, var15);
                    }

                    var6 = var6.trim();
                    if (var6.length() != 0) {
                        if (var6.charAt(0) == '@') {
                            try {
                                var2 = Integer.parseInt(var6.substring(1).trim());
                                if (var2 < 0 || var2 >= 65535) {
                                    throw new Exception();
                                }
                            } catch (NumberFormatException var10) {
                                throw new Exception("@ must be followed by a location number in the range 0 to 65535");
                            }
                        } else {
                            var4[var2] = Instruction.stringToCode(var6);
                            ++var2;
                            if (var2 >= 65535) {
                                var2 = 0;
                            }
                        }
                    }
                } else {
                    int var7;
                    for(var7 = 0; var6.charAt(var7) != '"'; ++var7) {
                    }

                    ++var7;

                    for(; var7 < var6.length(); ++var7) {
                        char var8 = var6.charAt(var7);
                        if (var8 == '\\' && var7 < var6.length() - 1) {
                            if (var6.charAt(var7 + 1) == 'n') {
                                var4[var2] = 10;
                                ++var7;
                            } else if (var6.charAt(var7 + 1) == '\\') {
                                var4[var2] = 92;
                                ++var7;
                            } else {
                                var4[var2] = 92;
                            }
                        } else {
                            var4[var2] = var8;
                        }

                        ++var2;
                        if (var2 >= 65535) {
                            var2 = 0;
                        }
                    }

                    var4[var2] = 0;
                    ++var2;
                    if (var2 >= 65535) {
                        var2 = 0;
                    }
                }
            }
        } catch (Exception var11) {
            this.error("Error on line " + var5 + " while reading file: " + var11.getMessage());
            return false;
        }

        this.clearAllBreakpoints();
        this.display[10].setBackground(Color.WHITE);
        this.value = var4;

        for(int var13 = 0; var13 < 21; ++var13) {
            this.display[var13].setText(this.displayText(var13));
        }

        this.currentScroll = 10;
        this.scroller.getModel().setValue(this.currentScroll);
        return true;
    }

    public void doSaveFile(File var1) {
        PrintWriter var2;
        try {
            var2 = new PrintWriter(var1);
        } catch (Exception var5) {
            this.error("Can't open file " + var1.getAbsolutePath() + " for writing.");
            return;
        }

        int var3 = 0;

        for(int var4 = 0; var4 < 65535; ++var4) {
            if (this.value[var4] == 0) {
                ++var3;
            } else {
                if (var3 <= 5) {
                    while(var3 > 0) {
                        var2.println("0x0000");
                        --var3;
                    }
                } else {
                    var2.println();
                    var2.print("@");
                    var2.println(var4);
                    var3 = 0;
                }

                var2.println(String.format("0x%04x", this.value[var4]));
            }
        }

        if (var3 == 65535) {
            var2.println("# empty memory!");
        }

        var2.flush();
        var2.close();
        if (var2.checkError()) {
            this.error("Some error occurred while writing file.  Check the output!");
        }

    }
}
