package org.testclasses;

public class PrimitiveArr {
    public byte[][] numnumInt;
    public byte[] numByte;
    public short[] numShort;
    public int[] numInt;
    public long[] numLong;
    public float[] numFloat;
    public double[] numDouble;
    public boolean[] bool;
    public char[] ch;

    public PrimitiveArr() {
        numnumInt = new byte[][]{{3, 4}, {2, 9}, {8, -1}};
        numByte = new byte[]{-128, -127, -126, -125, -124, -123};
        numShort = new short[]{23, 77, -490};
        numInt = new int[]{5555, 9999, 1222};
        numLong = new long[]{36187, 36828, 99881};
        numFloat = new float[]{32.32f, 34.03f, 4983.1f};
        numDouble = new double[]{984.0, 329.367, 111.99};
        bool = new boolean[]{false, true};
        ch = new char[]{'^', '%', '#', '='};
    }
}


