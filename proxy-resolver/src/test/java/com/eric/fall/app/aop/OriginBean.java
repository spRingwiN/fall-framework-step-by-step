package com.eric.fall.app.aop;

public class OriginBean {

    public String name;

    @Polite
    public String hello() {
        return "Hello, " + this.name + ".";
    }
    public String morning() {
        return "Morning, " + this.name + ".";
    }
}
