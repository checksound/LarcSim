//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package larcsim;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class ControlPanel extends JPanel {
    private Memory memory;
    private Registers registers;
    private IO io;
    private JTextField pc = new JTextField("0", 6);
    private JButton runStopButton = new JButton(" Run ");
    private JButton stepButton = new JButton(" Step ");
    private JButton resetButton = new JButton("Reset CPU");
    private JCheckBox stopAtBP = new JCheckBox("Stop at Breakpoints");
    private JCheckBox runFastSelect = new JCheckBox("Run Fast");
    private JTextField pokeValue = new JTextField(14);
    private JTextField pokeLocation = new JTextField("0", 6);
    private JButton pokeButton = new JButton("Poke");
    private JButton loadButton = new JButton("Load Memory From File");
    private JButton saveButton = new JButton("Save Memory");
    private Timer runTimer;
    private JFileChooser fileDialog;
    private boolean running;

    public ControlPanel(Memory var1, Registers var2, IO var3) {
        this.memory = var1;
        this.registers = var2;
        this.io = var3;
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Controls"), BorderFactory.createEmptyBorder(7, 7, 7, 7)));
        this.setLayout(new GridLayout(5, 1, 5, 5));
        this.add(this.makeRow(this.runStopButton, Box.createHorizontalStrut(10), this.stepButton, Box.createHorizontalStrut(20), this.resetButton));
        this.add(this.makeRow(this.stopAtBP, Box.createHorizontalStrut(10), this.runFastSelect));
        this.add(this.makeRow(new JLabel("Program Counter:"), this.pc));
        this.add(this.makeRow(this.pokeButton, this.pokeValue, new JLabel("into"), this.pokeLocation));
        this.add(this.makeRow(this.loadButton, this.saveButton));
        this.stopAtBP.setSelected(true);
        this.runStopButton.addActionListener((var1x) -> this.setRunning(!this.running));
        this.stepButton.addActionListener((var1x) -> this.doStep(false));
        this.pokeButton.addActionListener((var1x) -> this.doPoke());
        this.loadButton.addActionListener((var1x) -> this.doLoadFile());
        this.saveButton.addActionListener((var1x) -> this.doSaveFile());
        this.resetButton.addActionListener((var1x) -> this.doReset());
        this.runFastSelect.addActionListener((var1x) -> this.doRunFast());
        this.resetButton.setToolTipText("Click to set PC and all registers to zero.  Also clears input/output area.");
        this.pokeButton.setToolTipText("Click to store specified value into specified memory location.");
        this.pokeValue.setToolTipText("Specify value to be poked, in any input format. Then press return or click button.");
        this.pokeLocation.setToolTipText("Specify location number, in any numeric format. Then press return or click button.");
        this.pokeValue.addActionListener((var1x) -> this.doPoke());
        this.pokeLocation.addActionListener((var1x) -> this.doPoke());
        Font var4 = new Font("Monospaced", 0, 14);
        this.pc.setFont(var4);
        this.pokeValue.setFont(var4);
        this.pokeLocation.setFont(var4);
        this.runTimer = new Timer(200, (var1x) -> this.doStep(false));
    }

    private JPanel makeRow(Component... var1) {
        JPanel var2 = new JPanel();

        for(Component var6 : var1) {
            var2.add(var6);
        }

        return var2;
    }

    private void error(String var1) {
        this.setRunning(false);
        JOptionPane.showMessageDialog(this, var1, "Error!", 0);
    }

    private void setRunning(boolean var1) {
        if (var1 != this.running) {
            this.running = var1;
            if (var1) {
                boolean var2 = this.runFastSelect.isSelected();
                if (var2) {
                    this.registers.clearHilite();
                    this.memory.clearHilite();
                }

                this.runStopButton.setText("Stop");
                this.memory.setRunFast(var2);
                this.registers.setRunFast(var2);
                this.doStep(true);
                this.runTimer.setDelay(var2 ? 3 : 200);
                this.runTimer.start();
            } else {
                this.runTimer.stop();
                this.runStopButton.setText("Run");
                this.memory.setRunFast(false);
                this.registers.setRunFast(false);
            }

            this.pc.setEditable(!var1);
            this.pokeValue.setEditable(!var1);
            this.pokeLocation.setEditable(!var1);
            this.pokeButton.setEnabled(!var1);
            this.saveButton.setEnabled(!var1);
            this.loadButton.setEnabled(!var1);
            this.stepButton.setEnabled(!var1);
            this.resetButton.setEnabled(!var1);
            this.registers.setEditable(!var1);
            this.memory.setEnableMouse(!var1);
        }
    }

    private void doRunFast() {
        if (this.running) {
            boolean var1 = this.runFastSelect.isSelected();
            if (var1) {
                this.registers.clearHilite();
                this.memory.clearHilite();
                this.runTimer.setDelay(3);
            } else {
                this.runTimer.setDelay(200);
            }

            this.memory.setRunFast(var1);
            this.registers.setRunFast(var1);
        }
    }

    private int getNumeric(JTextField var1, String var2) {
        String var3 = var1.getText().trim();
        if (var3.length() != 0 && (var3.charAt(0) == '+' || var3.charAt(0) == '-' || Character.isDigit(var3.charAt(0)))) {
            try {
                return Instruction.stringToCode(var3);
            } catch (Exception var5) {
                throw new IllegalArgumentException("Input error in " + var2 + ": " + var5.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Input error in " + var2 + ": input must be numeric");
        }
    }

    private void doStep(boolean var1) {
        int var2;
        try {
            var2 = this.getNumeric(this.pc, "PC");
            if (var2 < 0 || var2 >= 65535) {
                throw new Exception("Input error in PC: Value must be in the range 0 to 65535");
            }
        } catch (Exception var4) {
            this.error(var4.getMessage());
            this.pc.requestFocus();
            this.pc.selectAll();
            return;
        }

        if (this.running && !var1 && this.memory.isBreakpoint(var2) && this.stopAtBP.isSelected()) {
            this.setRunning(false);
            JOptionPane.showMessageDialog(this, "Stopped at Breakpoint at memory location " + var2);
        } else {
            int var3 = this.memory.get(var2);
            ++var2;
            if (var2 >= 65535) {
                var2 = 0;
            }

            this.pc.setText("" + var2);
            this.executeInstruction(var2, new Instruction(var3));
        }
    }

    private void doReset() {
        this.pc.setText("0");

        for(int var1 = 1; var1 < 16; ++var1) {
            this.registers.set(var1, 0);
        }

        this.registers.clearHilite();
        this.memory.clearHilite();
        this.io.clear();
    }

    private void doPoke() {
        this.registers.clearHilite();

        int var1;
        try {
            var1 = Instruction.stringToCode(this.pokeValue.getText());
        } catch (Exception var5) {
            this.error("Illegal poke value: " + var5.getMessage());
            this.pokeValue.requestFocus();
            this.pokeValue.selectAll();
            return;
        }

        int var2;
        try {
            var2 = this.getNumeric(this.pokeLocation, "Poke location");
        } catch (Exception var4) {
            this.error(var4.getMessage());
            this.pokeLocation.requestFocus();
            this.pokeValue.selectAll();
            return;
        }

        this.memory.set(var2, var1);
        ++var2;
        if (var2 >= 65535) {
            var2 = 0;
        }

        this.pokeLocation.setText("" + var2);
        this.pokeValue.requestFocus();
        this.pokeValue.selectAll();
    }

    private void doLoadFile() {
        this.registers.clearHilite();
        if (this.fileDialog == null) {
            this.fileDialog = new JFileChooser();
        }

        this.fileDialog.setDialogTitle("Select File for Loading into Memory");
        this.fileDialog.setSelectedFile((File)null);
        int var1 = this.fileDialog.showOpenDialog(this);
        if (var1 == 0 && this.memory.doLoadFile(this.fileDialog.getSelectedFile())) {
            this.pc.setText("0");
            this.io.clearBuffer();
        }

    }

    private void doSaveFile() {
        this.registers.clearHilite();
        if (this.fileDialog == null) {
            this.fileDialog = new JFileChooser();
        }

        this.fileDialog.setDialogTitle("Select File for Saving Memory");
        this.fileDialog.setSelectedFile(new File("larc-memory.txt"));
        int var1 = this.fileDialog.showSaveDialog(this);
        if (var1 == 0) {
            File var2 = this.fileDialog.getSelectedFile();
            if (var2.exists()) {
                int var3 = JOptionPane.showConfirmDialog(this, "The file \"" + var2.getName() + "\" already exists.\nDo you want to replace it?", "Confirm Save", 0, 2);
                if (var3 != 0) {
                    return;
                }
            }

            this.memory.doSaveFile(var2);
        }
    }

    private void executeInstruction(int var1, Instruction var2) {
        this.registers.clearHilite();
        switch (var2.opcode) {
            case 0:
                this.registers.set(var2.ra, this.registers.get(var2.rb) + this.registers.get(var2.rc));
                break;
            case 1:
                this.registers.set(var2.ra, this.registers.get(var2.rb) - this.registers.get(var2.rc));
                break;
            case 2:
                this.registers.set(var2.ra, this.registers.get(var2.rb) * this.registers.get(var2.rc));
                break;
            case 3:
                int var5 = this.registers.get(var2.rc);
                if (var5 == 0) {
                    this.setRunning(false);
                    --var1;
                    if (var1 < 0) {
                        var1 = 65535;
                    }

                    JOptionPane.showMessageDialog(this, "Program treminated by Divisio-by-Zero at PC=" + var1);
                } else {
                    this.registers.set(var2.ra, this.registers.get(var2.rb) / var5);
                }
                break;
            case 4:
                this.registers.set(var2.ra, this.registers.get(var2.rb) << Math.min(16, this.registers.get(var2.rc)));
                break;
            case 5:
                this.registers.set(var2.ra, this.registers.get(var2.rb) >>> Math.min(16, this.registers.get(var2.rc)));
                break;
            case 6:
                this.registers.set(var2.ra, ~(this.registers.get(var2.rb) | this.registers.get(var2.rc)));
                break;
            case 7:
                this.registers.set(var2.ra, (short)this.registers.get(var2.rb) < (short)this.registers.get(var2.rc) ? 1 : 0);
                break;
            case 8:
                this.registers.set(var2.ra, (byte)var2.limm);
                break;
            case 9:
                this.registers.set(var2.ra, var2.limm << 8);
                break;
            case 10:
            case 11:
                if (var2.opcode == 10 && this.registers.get(var2.ra) == 0 || var2.opcode == 11 && this.registers.get(var2.ra) != 0) {
                    int var3 = var1 + var2.limm & '\uffff';
                    this.pc.setText("" + var3);
                }
                break;
            case 12:
                this.registers.set(var2.ra, this.memory.get(this.registers.get(var2.rb) + var2.simm));
                break;
            case 13:
                this.memory.set(this.registers.get(var2.rb) + var2.simm, this.registers.get(var2.ra));
                break;
            case 14:
                JTextField var10000 = this.pc;
                int var10001 = this.registers.get(var2.rb);
                var10000.setText("" + var10001);
                this.registers.set(var2.ra, var1);
                break;
            case 15:
                this.executeSyscall();
        }

    }

    private void executeSyscall() {
        switch (this.registers.get(1)) {
            case 0:
                this.setRunning(false);
                JOptionPane.showMessageDialog(this, "Program treminated by Halt syscall.");
                break;
            case 1:
                int var6 = this.registers.get(2) & '\uffff';
                int var7 = this.registers.get(3) & '\uffff';
                StringBuilder var3 = new StringBuilder();

                for(int var4 = 0; var4 < var7; ++var4) {
                    char var5 = (char)this.memory.getWithoutHilite(var6);
                    if (var5 == 0) {
                        break;
                    }

                    var3.append(var5);
                    ++var6;
                    if (var6 >= 65535) {
                        var6 = 0;
                    }
                }

                this.io.put(var3.toString());
                break;
            case 2:
                this.io.put(this.registers.get(2));
                break;
            case 3:
                if (this.running) {
                    this.runTimer.stop();
                }

                int var1 = this.registers.get(2);
                int var2 = this.registers.get(3);
                this.pc.setEditable(false);
                this.pokeValue.setEditable(false);
                this.pokeLocation.setEditable(false);
                this.pokeButton.setEnabled(false);
                this.saveButton.setEnabled(false);
                this.loadButton.setEnabled(false);
                this.stepButton.setEnabled(false);
                this.resetButton.setEnabled(false);
                this.registers.setEditable(false);
                this.memory.setEnableMouse(false);
                this.runFastSelect.setEnabled(false);
                this.runStopButton.setEnabled(false);
                (new Thread(() -> {
                    String var3A = this.io.getString();
                    int var4 = var1;

                    int var5;
                    for(var5 = 0; var5 < var2 && var5 < var3A.length(); ++var5) {
                        this.memory.setWithoutHilite(var4, var3A.charAt(var5));
                        ++var4;
                        if (var4 >= 65535) {
                            var4 = 0;
                        }
                    }

                    this.memory.setWithoutHilite(var4, 0);
                    ++var5;
                    if (this.running) {
                        this.runTimer.start();
                        this.runStopButton.requestFocus();
                    } else {
                        this.stepButton.requestFocus(this.running);
                    }

                    this.pc.setEditable(!this.running);
                    this.pokeValue.setEditable(!this.running);
                    this.pokeLocation.setEditable(!this.running);
                    this.pokeButton.setEnabled(!this.running);
                    this.saveButton.setEnabled(!this.running);
                    this.loadButton.setEnabled(!this.running);
                    this.stepButton.setEnabled(!this.running);
                    this.resetButton.setEnabled(!this.running);
                    this.registers.setEditable(!this.running);
                    this.memory.setEnableMouse(!this.running);
                    this.runFastSelect.setEnabled(true);
                    this.runStopButton.setEnabled(true);
                    if (var5 < var2) {
                        this.registers.set(3, var5);
                    }

                })).start();
                break;
            case 4:
                if (this.running) {
                    this.runTimer.stop();
                }

                this.pc.setEditable(false);
                this.pokeValue.setEditable(false);
                this.pokeLocation.setEditable(false);
                this.pokeButton.setEnabled(false);
                this.saveButton.setEnabled(false);
                this.loadButton.setEnabled(false);
                this.stepButton.setEnabled(false);
                this.resetButton.setEnabled(false);
                this.registers.setEditable(false);
                this.memory.setEnableMouse(false);
                this.runStopButton.setEnabled(false);
                this.runFastSelect.setEnabled(false);
                (new Thread(() -> {
                    this.registers.set(1, this.io.getInt());
                    if (this.running) {
                        this.runTimer.start();
                        this.runStopButton.requestFocus();
                    } else {
                        this.stepButton.requestFocus(this.running);
                    }

                    this.pc.setEditable(!this.running);
                    this.pokeValue.setEditable(!this.running);
                    this.pokeLocation.setEditable(!this.running);
                    this.pokeButton.setEnabled(!this.running);
                    this.saveButton.setEnabled(!this.running);
                    this.loadButton.setEnabled(!this.running);
                    this.stepButton.setEnabled(!this.running);
                    this.resetButton.setEnabled(!this.running);
                    this.registers.setEditable(!this.running);
                    this.memory.setEnableMouse(!this.running);
                    this.runStopButton.setEnabled(true);
                    this.runFastSelect.setEnabled(true);
                })).start();
                break;
            default:
                this.error("Program halted becasue of illegal system call number!");
        }

    }
}
