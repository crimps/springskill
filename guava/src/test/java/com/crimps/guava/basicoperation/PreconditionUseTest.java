package com.crimps.guava.basicoperation;

import org.junit.Test;

import static org.junit.Assert.*;

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