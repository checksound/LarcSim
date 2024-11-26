//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package larcsim;

public class Instruction {
    public static final String[] name = new String[]{"add", "sub", "mul", "div", "sll", "srl", "nor", "slt", "li", "lui", "beqz", "bnez", "lw", "sw", "jalr", "syscall"};
    public final int ins;
    public final int opcode;
    public final int ra;
    public final int rb;
    public final int rc;
    public final int simm;
    public final int limm;

    public Instruction(int var1) {
        this.ins = var1;
        this.opcode = var1 >> 12 & 15;
        this.ra = var1 >> 8 & 15;
        this.rb = var1 >> 4 & 15;
        this.rc = var1 & 15;
        this.limm = (byte)var1;
        this.simm = var1 << 28 >> 28;
    }

    public Instruction(String var1) {
        this(stringToCode(var1));
    }

    public String toString() {
        if (this.opcode < 8) {
            return String.format("%s $%d $%d $%d", name[this.opcode], this.ra, this.rb, this.rc);
        } else if (this.opcode < 12) {
            return String.format("%s $%d %d", name[this.opcode], this.ra, this.limm);
        } else if (this.opcode < 14) {
            return String.format("%s $%d $%d %d", name[this.opcode], this.ra, this.rb, this.simm);
        } else {
            return this.opcode == 14 ? String.format("%s $%d $%d", name[this.opcode], this.ra, this.rb) : name[this.opcode];
        }
    }

    public static String toString(int var0) {
        int var1 = var0 >> 12 & 15;
        int var2 = var0 >> 8 & 15;
        int var3 = var0 >> 4 & 15;
        int var4 = var0 & 15;
        byte var5 = (byte)var0;
        int var6 = var0 << 28 >> 28;
        if (var1 < 8) {
            return String.format("%s $%d $%d $%d", name[var1], var2, var3, var4);
        } else if (var1 < 12) {
            return String.format("%s $%d %d", name[var1], var2, Integer.valueOf(var5));
        } else if (var1 < 14) {
            return String.format("%s $%d $%d %d", name[var1], var2, var3, var6);
        } else {
            return var1 == 14 ? String.format("%s $%d $%d", name[var1], var2, var3) : name[var1];
        }
    }

    public static int stringToCode(String var0) {
        var0 = var0.toLowerCase().trim();
        if (var0.length() == 0) {
            throw new IllegalArgumentException("Empty input");
        } else {
            int var1;
            if (var0.startsWith("0x")) {
                try {
                    var1 = Integer.parseInt(var0.substring(2), 16);
                } catch (NumberFormatException var14) {
                    throw new IllegalArgumentException("Invalid hexadecimal constant");
                }

                if (var1 < 0 || var1 > 65535) {
                    throw new IllegalArgumentException("Hexadecimal constant must be in the range 0 to 0xFFFF");
                }
            } else if (var0.startsWith("0b")) {
                try {
                    var1 = Integer.parseInt(var0.substring(2), 2);
                } catch (NumberFormatException var13) {
                    throw new IllegalArgumentException("Invalid binary constant");
                }

                if (var1 < 0 || var1 > 65535) {
                    throw new IllegalArgumentException("Hexadecimal constant must be in the range 0 to 0b1111111111111111");
                }
            } else if (var0.startsWith("'")) {
                if (var0.length() != 2) {
                    throw new IllegalArgumentException("A character constant must be a single character");
                }

                var1 = var0.charAt(1);
            } else if ((var0.length() <= 0 || var0.charAt(0) != '+') && var0.charAt(0) != '-' && !Character.isDigit(var0.charAt(0))) {
                String[] var2 = var0.split(" +");
                int var3 = -1;

                for(int var4 = 0; var4 < 16; ++var4) {
                    if (name[var4].equals(var2[0])) {
                        var3 = var4;
                        break;
                    }
                }

                switch (var3) {
                    case -1:
                        throw new IllegalArgumentException(var2[0] + " is not a legal assembly language instruction");
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    default:
                        if (var2.length != 4) {
                            throw new IllegalArgumentException(var2[0] + " command must have exactly three operands");
                        }

                        int var22 = checkReg(var2[1]);
                        int var25 = checkReg(var2[2]);
                        int var27 = checkReg(var2[3]);
                        var1 = var3 << 12 | var22 << 8 | var25 << 4 | var27;
                        break;
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        if (var2.length != 3) {
                            throw new IllegalArgumentException(var2[0] + " command must have exactly two operands");
                        }

                        int var21 = checkReg(var2[1]);
                        String var26 = var2[2].toLowerCase();
                        int var24;
                        if (var26.startsWith("0x")) {
                            try {
                                var24 = Integer.parseInt(var26.substring(2), 16);
                            } catch (NumberFormatException var10) {
                                throw new IllegalArgumentException("Invalid hexadecimal constant for LIMM");
                            }

                            if (var24 < 0 || var24 > 255) {
                                throw new IllegalArgumentException("Hexadecimal LIMM must be in the range 0 to 0xFF");
                            }
                        } else if (var26.startsWith("0b")) {
                            try {
                                var24 = Integer.parseInt(var26.substring(2), 2);
                            } catch (NumberFormatException var9) {
                                throw new IllegalArgumentException("Invalid binary constant for LIMM");
                            }

                            if (var24 < 0 || var24 > 255) {
                                throw new IllegalArgumentException("Binary LIMM must be in the range 0 to 0b11111111");
                            }
                        } else {
                            try {
                                var24 = Integer.parseInt(var26);
                                if (var24 < -128 || var24 > 255) {
                                    throw new Exception();
                                }
                            } catch (Exception var17) {
                                throw new IllegalArgumentException("LIMM must be an integer in the range -128 to 255");
                            }
                        }

                        var1 = var3 << 12 | var21 << 8 | var24 & 255;
                        break;
                    case 12:
                    case 13:
                        if (var2.length != 3 && var2.length != 4) {
                            throw new IllegalArgumentException(var2[0] + " command must have exactly two or three operands");
                        }

                        int var20 = checkReg(var2[1]);
                        int var23 = checkReg(var2[2]);
                        int var6;
                        if (var2.length == 3) {
                            var6 = 0;
                        } else {
                            String var7 = var2[3].toLowerCase();
                            if (var7.startsWith("0x")) {
                                try {
                                    var6 = Integer.parseInt(var7.substring(2), 16);
                                } catch (NumberFormatException var12) {
                                    throw new IllegalArgumentException("Invalid hexadecimal constant for SIMM");
                                }

                                if (var6 < 0 || var6 > 15) {
                                    throw new IllegalArgumentException("Hexadecimal SIMM must be in the range 0 to 0xF");
                                }
                            } else if (var7.startsWith("0b")) {
                                try {
                                    var6 = Integer.parseInt(var7.substring(2), 2);
                                } catch (NumberFormatException var11) {
                                    throw new IllegalArgumentException("Invalid binary constant for SIMM");
                                }

                                if (var6 < 0 || var6 > 15) {
                                    throw new IllegalArgumentException("Binary SIMM must be in the range 0 to 0b1111");
                                }
                            } else {
                                try {
                                    var6 = Integer.parseInt(var7);
                                    if (var6 < -128 || var6 > 255) {
                                        throw new Exception();
                                    }
                                } catch (Exception var16) {
                                    throw new IllegalArgumentException("SIMM must be an integer in the range -8 to 7");
                                }
                            }
                        }

                        var1 = var3 << 12 | var20 << 8 | var23 << 4 | var6 & 15;
                        break;
                    case 14:
                        if (var2.length != 3) {
                            throw new IllegalArgumentException("jalr command must have exactly two operands");
                        }

                        int var19 = checkReg(var2[1]);
                        int var5 = checkReg(var2[2]);
                        var1 = var3 << 12 | var19 << 8 | var5 << 4;
                        break;
                    case 15:
                        if (var2.length != 1) {
                            throw new IllegalArgumentException("syscall command does not allow operands");
                        }

                        var1 = 61440;
                }
            } else {
                try {
                    var1 = Integer.parseInt(var0);
                } catch (NumberFormatException var15) {
                    throw new IllegalArgumentException("Invalid decimal constant");
                }

                if (var1 < -32768 || var1 > 65535) {
                    throw new IllegalArgumentException("Decimal constant must be in the range -32768 to 65535");
                }
            }

            return var1;
        }
    }

    private static int checkReg(String var0) {
        if (!var0.startsWith("$")) {
            throw new IllegalArgumentException("Register name must start witht a $");
        } else {
            try {
                int var1 = Integer.parseInt(var0.substring(1));
                if (var1 >= 0 && var1 <= 15) {
                    return var1;
                } else {
                    throw new Exception();
                }
            } catch (Exception var2) {
                throw new IllegalArgumentException("Register name must be a $ followed by an integer in the range 0 to 15");
            }
        }
    }
}
