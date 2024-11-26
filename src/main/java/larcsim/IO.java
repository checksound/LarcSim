//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package larcsim;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class IO extends JPanel {
    private Console console = new Console();
    private BufferedReader in;
    private PrintWriter out;

    public void put(int var1) {
        this.out.print(var1);
        this.console.scrollToEnd();
        this.console.repaint();
    }

    public void put(String var1) {
        this.out.print(var1);
        this.console.scrollToEnd();
        this.console.repaint();
    }

    public void clearBuffer() {
        this.console.clearBuffers();
    }

    public void clear() {
        this.console.clear();
    }

    public int getInt() {
        while(true) {
            try {
                String var1 = this.getString().trim();
                int var2 = Integer.parseInt(var1);
                if (var2 >= -32568 && var2 <= 65536) {
                    return var2;
                }

                this.out.print("Input out of legal range -32568 to 65536, try again: ");
            } catch (NumberFormatException var3) {
                this.out.print("Illegal Integer Input, try again: ");
            }
        }
    }

    public String getString() {
        try {
            return this.in.readLine();
        } catch (Exception var2) {
            throw new Error("Internal Error while reading from console", var2);
        }
    }

    public IO() {
        this.in = this.console.inputStream;
        this.out = this.console.outputStream;
        this.setLayout(new BorderLayout(2, 2));
        this.setBackground(Color.GRAY);
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        this.add(this.console, "Center");
        this.add(this.console.scroller, "East");
    }

    public static class Console extends JPanel {
        JScrollBar scroller;
        CIN cin;
        BufferedReader inputStream;
        PrintWriter outputStream;
        String[] lines;
        int topLine;
        int lineCount;
        int rows;
        int columns;
        volatile boolean doingInput;
        volatile String inputBuffer;
        volatile boolean cursorOn = true;
        volatile int inputStartLine;
        volatile int inputStartColumn;
        volatile String typeAheadBuffer = "";
        FontMetrics fontMetrics;
        int lineSkip;
        int charWidth;
        static final int MARGIN = 9;
        static final Color CURSOR_COLOR = new Color(200, 0, 0);
        static final int ROWS = 5;

        Console() {
            Font var1 = new Font("Monospaced", 0, 12);
            this.fontMetrics = this.getFontMetrics(var1);
            this.lineSkip = (int)((double)this.fontMetrics.getHeight() * 1.2);
            this.charWidth = this.fontMetrics.charWidth('W');
            this.setFont(var1);
            this.setPreferredSize(new Dimension(18 + 80 * this.charWidth, 18 + 4 * this.lineSkip + this.fontMetrics.getAscent() + this.fontMetrics.getDescent()));
            this.setBackground(Color.WHITE);
            this.setForeground(Color.BLACK);
            this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3));
            this.addFocusListener(new FocusListener() {
                public void focusLost(FocusEvent var1) {
                    Console.this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3));
                }

                public void focusGained(FocusEvent var1) {
                    Console.this.setBorder(BorderFactory.createLineBorder(new Color(0, 180, 180), 3));
                }
            });
            this.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent var1) {
                    char var2 = var1.getKeyChar();
                    if (var2 != '\uffff') {
                        if (!Console.this.doingInput) {
                            Console var10000 = Console.this;
                            var10000.typeAheadBuffer = var10000.typeAheadBuffer + var2;
                        } else {
                            synchronized(Console.this) {
                                Console.this.doInputChar(var2);
                                Console.this.notify();
                            }
                        }
                    }
                }
            });
            this.lines = new String[2000];
            this.lineCount = 1;
            this.lines[0] = "";
            this.scroller = new JScrollBar(1, 0, 80, 0, 80);
            this.scroller.setEnabled(false);
            this.scroller.addAdjustmentListener(new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent var1) {
                    Console.this.topLine = Console.this.scroller.getValue();
                    Console.this.repaint();
                }
            });
            this.cin = new CIN();
            this.inputStream = new BufferedReader(this.cin);
            this.outputStream = new PrintWriter(new COUT());
        }

        private synchronized void clear() {
            this.lines = new String[2000];
            this.lineCount = 1;
            this.lines[0] = "";
            this.topLine = 0;
            this.scroller.setValues(0, 80, 0, 80);
            this.typeAheadBuffer = "";
            this.cin.buffer = null;
            this.repaint();
            this.doingInput = false;
            this.cursorOn = false;
        }

        public void paintComponent(Graphics var1) {
            super.paintComponent(var1);
            ((Graphics2D)var1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (this.rows == 0) {
                this.columns = (this.getWidth() - 18 + 1) / this.charWidth;
                this.rows = 1 + (this.getHeight() - 18 - this.fontMetrics.getAscent()) / this.lineSkip;
                this.scroller.setBlockIncrement(this.rows - 2);
                this.scrollToEnd();
            }

            for(int var2 = this.topLine; var2 < this.topLine + this.rows && var2 < this.lineCount; ++var2) {
                var1.drawString(this.lines[var2], 9, 9 + (var2 - this.topLine) * this.lineSkip + this.fontMetrics.getAscent());
            }

            if (this.doingInput && this.cursorOn) {
                var1.setColor(CURSOR_COLOR);
                int var5 = 9 + this.fontMetrics.stringWidth(this.lines[this.lineCount - 1]) + 1;
                int var3 = 9 + (this.lineCount - 1 - this.topLine) * this.lineSkip + this.fontMetrics.getAscent() + this.fontMetrics.getDescent();
                int var4 = var3 - this.fontMetrics.getAscent() - this.fontMetrics.getDescent();
                var1.drawLine(var5, var3, var5, var4);
                var1.drawLine(var5 + 1, var3, var5 + 1, var4);
            }

        }

        synchronized void newLine() {
            try {
                Thread.sleep(20L);
            } catch (InterruptedException var2) {
            }

            if (this.lineCount == this.lines.length) {
                for(int var1 = 0; var1 < this.lines.length - 1; ++var1) {
                    this.lines[var1] = this.lines[var1 + 1];
                }

                this.lines[this.lines.length - 1] = "";
                if (this.doingInput) {
                    --this.inputStartLine;
                }
            } else {
                this.lines[this.lineCount] = "";
                ++this.lineCount;
            }

            this.scrollToEnd();
            this.repaint();
        }

        synchronized void putChar(char var1) {
            if (var1 == '\n') {
                this.newLine();
            } else {
                if (var1 == '\t') {
                    var1 = ' ';
                }

                if (Character.isDefined(var1) && !Character.isISOControl(var1)) {
                    if (this.columns > 0 && this.lines[this.lineCount - 1].length() >= this.columns) {
                        this.newLine();
                    }

                    String[] var10000 = this.lines;
                    int var10001 = this.lineCount - 1;
                    var10000[var10001] = var10000[var10001] + var1;
                }
            }
        }

        synchronized void deleteChar() {
            if (this.lineCount != 0) {
                if (this.inputStartLine != this.lineCount - 1 || this.inputStartColumn < this.lines[this.lineCount - 1].length()) {
                    if (this.lines[this.lineCount - 1].length() > 0) {
                        this.lines[this.lineCount - 1] = this.lines[this.lineCount - 1].substring(0, this.lines[this.lineCount - 1].length() - 1);
                    } else {
                        --this.lineCount;
                        this.scrollToEnd();
                    }

                }
            }
        }

        void scrollToEnd() {
            if (this.rows != 0) {
                if (this.lineCount <= this.rows) {
                    this.topLine = 0;
                    this.scroller.setEnabled(false);
                } else {
                    this.topLine = this.lineCount - this.rows;
                    this.scroller.setEnabled(true);
                }

                this.scroller.setValues(this.topLine, this.rows, 0, this.rows + this.topLine);
            }
        }

        synchronized void doInputChar(char var1) {
            if (var1 != '\b' && var1 != 127) {
                if (var1 != '\r' && var1 != '\n') {
                    this.putChar(var1);
                    if (var1 == '\t') {
                        var1 = ' ';
                    }

                    if (Character.isDefined(var1) && !Character.isISOControl(var1)) {
                        this.inputBuffer = this.inputBuffer + var1;
                    }
                } else {
                    this.newLine();
                    this.doingInput = false;
                }
            } else {
                this.deleteChar();
                if (this.inputBuffer.length() > 0) {
                    this.inputBuffer = this.inputBuffer.substring(0, this.inputBuffer.length() - 1);
                }
            }

            this.scrollToEnd();
            this.repaint();
        }

        synchronized void clearBuffers() {
            this.typeAheadBuffer = "";
            this.cin.pos = Integer.MAX_VALUE;
        }

        class CIN extends Reader {
            String buffer;
            volatile int pos;

            CIN() {
            }

            public void close() {
            }

            public int read(char[] var1, int var2, int var3) throws IOException {
                int var4 = 0;

                int var5;
                do {
                    var5 = this.read();
                    var1[var2 + var4] = (char)var5;
                    ++var4;
                } while(var5 != 10);

                return var4;
            }

            public int read() {
                if (this.buffer != null && this.pos < this.buffer.length()) {
                    ++this.pos;
                    return this.buffer.charAt(this.pos - 1);
                } else {
                    synchronized(Console.this) {
                        Console.this.inputStartLine = Console.this.lineCount - 1;
                        Console.this.inputStartColumn = Console.this.lines[Console.this.lineCount - 1].length();
                        char var2 = 0;
                        Console.this.scrollToEnd();
                        Console.this.inputBuffer = "";

                        while(Console.this.typeAheadBuffer.length() > 0) {
                            var2 = Console.this.typeAheadBuffer.charAt(0);
                            Console.this.typeAheadBuffer = Console.this.typeAheadBuffer.substring(1);
                            if (var2 == '\r' || var2 == '\n') {
                                break;
                            }

                            Console.this.doInputChar(var2);
                            Console.this.repaint();

                            try {
                                Console.this.wait(25L);
                            } catch (InterruptedException var6) {
                            }
                        }

                        if (var2 != '\r' && var2 != '\n') {
                            Console.this.doingInput = true;
                            Console.this.cursorOn = true;

                            while(Console.this.doingInput) {
                                Console.this.requestFocusInWindow();

                                try {
                                    Console.this.wait(300L);
                                    Console.this.cursorOn = !Console.this.cursorOn;
                                    Console.this.repaint();
                                } catch (InterruptedException var5) {
                                    Console.this.cursorOn = true;
                                    Console.this.repaint();
                                }
                            }

                            Console.this.cursorOn = false;
                            Console.this.repaint();
                        }

                        this.buffer = Console.this.inputBuffer + "\n";
                        this.pos = 1;
                        return this.buffer.charAt(0);
                    }
                }
            }
        }

        class COUT extends Writer {
            COUT() {
            }

            public void write(int var1) {
                this.write(new char[]{(char)(var1 & '\uffff')}, 0, 1);
            }

            public void write(char[] var1, int var2, int var3) {
                for(int var4 = var2; var4 < var2 + var3; ++var4) {
                    Console.this.putChar(var1[var4]);
                }

            }

            public void write(char[] var1) {
                this.write(var1, 0, var1.length);
            }

            public void close() {
            }

            public void flush() {
            }
        }
    }
}
