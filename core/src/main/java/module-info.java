module dev.weisz.boba {
    exports dev.weisz.boba;
    exports dev.weisz.boba.tea;
    exports dev.weisz.boba.terminal;

    requires org.jspecify;
    requires jdk.unsupported;
    requires dev.weisz.ansi;
    requires dev.weisz.wcwidth;
    requires dev.weisz.color.terminal;
    requires org.apache.commons.lang3;
    requires org.slf4j;
}