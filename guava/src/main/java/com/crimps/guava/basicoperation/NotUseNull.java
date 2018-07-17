package com.crimps.guava.basicoperation;

import com.google.common.base.Optional;

public class NotUseNull {

    public void notUseNull(){
        String temp = null;
        Optional<String> possible = Optional.of(temp);
        System.out.println(possible.isPresent()); // returns true
        System.out.println(possible.get());
    }
}
