package dev.weisz.boba.jfr;

import jdk.jfr.*;

@Name(BobaFrameRenderEvent.NAME)
@Label("Boba Frame Render")
@Category({"Boba", "Boba Renderer"})
public class BobaFrameRenderEvent extends Event {
    static final String NAME = "dev.weisz.boba.jfr.BobaFrameRender";

    @Label("Active Renderer")
    public String activeRenderer;

    @Label("Frame")
    public String frameContent; // TODO: do we really want to do all this?

    @Label("Frame Render Duration")
    @Description("The time that Program#view took to render the current frame")
    @Timespan(Timespan.MICROSECONDS)
    public long frameRenderDuration;
}
