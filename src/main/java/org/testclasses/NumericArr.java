package org.testclasses;

public class NumericArr {
    public Byte[][] numnumInt;
    public Byte[] numByte;
    public Short[] numShort;
    public Integer[] numInt;
    public Long[] numLong;
    public Float[] numFloat;
    public Double[] numDouble;
    public Boolean[] bool;
    public Character[] ch;

    public NumericArr() {
        numnumInt = new Byte[][]{{3, 4}, {2, 9}, {8, -1}};
        numByte = new Byte[]{-128, -127, -126, -125, -124, -123};
        numShort = new Short[]{23, 77, -490};
        numInt = new Integer[3];
        numLong = new Long[]{36187L, 36828L, 99881L};
        numFloat = new Float[]{32.32f, 34.03f, 4983.1f};
        numDouble = new Double[]{984.0, 329.367, 111.99};
        bool = new Boolean[]{false, true};
        ch = new Character[]{'^', '%', '#', '='};
    }
}
