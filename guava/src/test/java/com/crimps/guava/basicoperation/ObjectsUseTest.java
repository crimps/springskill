package com.crimps.guava.basicoperation;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectsUseTest {

    private ObjectsUse objectsUse = new ObjectsUse();

    @Test
    public void equals() {
        objectsUse.equals();
    }

    @Test
    public void hashCodeUseTest(){
        objectsUse.hashCodeUse();
    }

    @Test
    public void compareTest(){
        ObjectsUse o1 = new ObjectsUse();
        o1.setP1("p3");
        o1.setP2(9);
        ObjectsUse o2 = new ObjectsUse();
        o2.setP1("p1");
        o2.setP2(8);
        System.out.println(o1.compareTo(o2));
    }
}