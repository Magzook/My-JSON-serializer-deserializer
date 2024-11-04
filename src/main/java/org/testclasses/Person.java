package org.testclasses;

public class Person {
    public StringBuilder name;
    public Integer age;
    public double weight;
    public boolean isMale;

    public String[] strArr;
    public Integer[] intArr;
    public double[] doubleArr;
    public boolean[] boolArr;

    public int[][] intMultiDimArr;

    public FullName fullName;

    public Person() {
        name = new StringBuilder("Maxim");
        age = 20;
        weight = 63.398;
        isMale = true;
        strArr = new String[]{"Skinny Pete", "Jackson", "Taiga-man"};
        intArr = new Integer[4];
        doubleArr = new double[]{4.3, 9.6, -3.2};
        boolArr = new boolean[]{true, false, false};
        intMultiDimArr = new int[2][3];
        fullName = new FullName();
        fullName.first_name = "Walter";
        fullName.middle_name = "Hartwell";
        fullName.last_name = "White";
    }
}
