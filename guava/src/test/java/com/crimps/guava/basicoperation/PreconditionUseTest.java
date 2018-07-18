package com.crimps.guava.basicoperation;

import org.junit.Test;


public class PreconditionUseTest {

    PreconditionUse preconditionUse = new PreconditionUse();

    @Test
    public void preconditonUse() {
        preconditionUse.preconditonUse();
    }

    @Test
    public void checkArgumentTest(){
        preconditionUse.checkArgument();
    }
}