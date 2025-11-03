package dev.weisz.boba.jfr;

import jdk.jfr.*;

@Name(BobaErrorEvent.NAME)
@Label("Boba Error")
@Category("Boba")
public class BobaErrorEvent extends Event {
    static final String NAME = "dev.weisz.boba.jfr.BobaError";

    @Label("Message")
    public String message;

    @Label("Stacktrace")
    @DataAmount("kB")
    public String stackTrace;
}
