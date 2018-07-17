package com.crimps.guava.basicoperation;

import com.google.common.base.Preconditions;

public class PreconditionUse {

    public void preconditonUse(){
        try{
            String projectLicence = "12306";
            System.out.println(Preconditions.checkNotNull(projectLicence));
            String emptyStr = null;
            System.out.println(Preconditions.checkNotNull(emptyStr));
            System.out.println("end");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void checkArgument(){
        try{
            String trueStr = "true";
            boolean trueBoolean = true;
            Preconditions.checkArgument(trueBoolean);
            Preconditions.checkArgument(false, "返回失败");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
