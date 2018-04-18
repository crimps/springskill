package com.crimps;

public class ActionTime {

    public void actionDb() {
        try{
            Thread.sleep(100);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void actionHttp(){
        try{
            Thread.sleep(200);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void actionLong(){
        try {
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
