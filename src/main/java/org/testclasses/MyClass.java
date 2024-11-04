package org.testclasses;

public class MyClass {
    public byte f_byte = -100;
    public short f_short = 4398;
    public int f_int = 838383;
    public long f_long = 123456789;
    public float f_float = 3.14152f;
    public double f_double = 2.718281828459045;
    public boolean f_boolean = true;
    public char f_char = '%';
    public Byte f_Byte = 32;
    public Short f_Short = 4387;
    public Integer f_Integer = null;
    public Long f_Long = -9999999L;
    public Float f_Float = 846.32f;
    public Double f_Double = 9494.971;
    public Boolean f_Boolean = false;
    public Character f_Char = null;
    public int[] f_arr_int = new int[]{55, 88, -12, 47};
    public int[][] f_arr2d_int = new int[][]{{12, 13},{22, 23},{33, 34}};
    public byte[] f_arr_byte = new byte[]{-128, -127, -126, -125, -124, -123};
    public Byte[] f_arr_Byte = new Byte[]{-34, -33, -32, -31, -30, -29, -28};
    public double[] f_arr_double = new double[3];
    public char[] f_arr_char = new char[]{'$', '@', '?', '!'};
    public Character[] f_arr_Char = new Character[]{'>', '=', '-', '*'};
    public Boolean[] f_arr_Boolean;
    public Boolean[] f_arr_Boolean2 = new Boolean[3];
    public Boolean[] f_arr_Boolean3 = new Boolean[]{true, true, false, false};
    public String str = "Hey! What's up, bro?";
    public String[] str_arr = new String[]{"This", "Is", "An", "Array", "Of", "Strings"};
    public String str3;
    public MyClassCompact mcc = new MyClassCompact();
    public MyClassCompact mcc2 = null;
    private final int length = 4;
    public MyClassCompact[] mcc_arr = new MyClassCompact[length];
    public static int static_field = 1111111;
    public transient String transient_field = "WWWWWWWWWWWWWWWWWWWW";
    public Day day = Day.MONDAY;

    public MyClass() {
        for (int i = 0; i < length; i++) mcc_arr[i] = new MyClassCompact();
    }
}

