package com.nagash.appwebbrowser.model.geofencing;

/**
 * Created by nagash on 23/09/16.
 */

public class GeoEvent{

    //public enum Event { ENTER, IN, EXIT, OUT, ALL }

    int flags = 0;

    int ENTERING  = 0b00001000;
    int IN        = 0b00000100;
    int OUT       = 0b00000010;
    int EXITITING = 0b00000001;

    private void toggleFlag(int flag) { flags ^= flag; }
    private void addFlag(int flag)    { flags |= flag; }
    private void remFlag(int flag)    { flags &= ~flag; }

    private boolean readFlag(int flag) { return (flags & flag)!=0; }


    public boolean eventEntering() {
        return readFlag(ENTERING);
    }
    public boolean eventExiting() {
        return readFlag(ENTERING);
    }
    public boolean eventIn() {
        return readFlag(ENTERING);
    }
    public boolean eventOut() {
        return readFlag(ENTERING);
    }



    public GeoEvent enableAllEvent() {
        return enableEnteringEvent().enableInEvent().enableOutEvent().enableExitingEvent();
    }
    public GeoEvent disableAllEvent() {
        return setEnteringEvent(false).setInEvent(false).setOutEvent(false).setExitingEvent(false);
    }


    public GeoEvent toggleEnteringEvent() { toggleFlag(ENTERING); return this; }
    public GeoEvent enableEnteringEvent() { toggleFlag(ENTERING); return this; }
    public GeoEvent setEnteringEvent(boolean value) {
        if(value) addFlag(ENTERING);
        else remFlag(ENTERING);
        return this;
    }


    public GeoEvent toggleExitingEvent() { toggleFlag(EXITITING); return this; }
    public GeoEvent enableExitingEvent() { setExitingEvent(true); return this; }
    public GeoEvent setExitingEvent(boolean value) {
        if(value) addFlag(EXITITING);
        else remFlag(EXITITING);
        return this;
    }



    public GeoEvent toggleInEvent() { toggleFlag(IN); return this; }
    public GeoEvent enableInEvent() { setInEvent(true); return this; }
    public GeoEvent setInEvent(boolean value) {
        if(value) addFlag(IN);
        else remFlag(IN);
        return this;
    }


    public GeoEvent toggleOutEvent() { toggleFlag(OUT); return this; }
    public GeoEvent enableOutEvent() { setOutEvent(true); return this; }
    public GeoEvent setOutEvent(boolean value) {
        if(value) addFlag(OUT);
        else remFlag(OUT);
        return this;
    }

}