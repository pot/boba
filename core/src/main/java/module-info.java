module dev.mccue.boba {
    exports dev.mccue.boba;
    exports dev.mccue.boba.tea;
    exports dev.mccue.boba.terminal;

    requires org.jspecify;
    requires jdk.unsupported;
    requires dev.mccue.ansi;
    requires dev.mccue.wcwidth;
    requires dev.mccue.color.terminal;
    requires org.apache.commons.lang3;
}