package dev.weisz.boba.jfr;

import jdk.jfr.*;

@Name(BobaRenderSettingsEvent.NAME)
@Label("Boba Render Settings")
@Description("Triggered whenever the settings of the renderer are updated")
@Category({"Boba", "Boba Renderer"})
public class BobaRenderSettingsEvent extends Event {
    static final String NAME = "dev.weisz.boba.jfr.BobaRenderSettings";
}
