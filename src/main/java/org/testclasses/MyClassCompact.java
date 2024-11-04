package org.testclasses;

import java.util.Random;

public class MyClassCompact {
    public int age;
    public char symbol;
    public boolean isMale;
    private Random rand = new Random();
    public MyClassCompact() {
        age = rand.nextInt(18, 99);
        symbol = (char) rand.nextInt(40, 60);
        isMale = rand.nextBoolean();
    }

}
