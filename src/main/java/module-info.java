module dev.mccue.boba {
    requires com.ibm.icu;
    requires org.fusesource.jansi;
    exports dev.mccue.boba.ansi to dev.mccue.boba.test;
    exports dev.mccue.boba.crossterm to dev.mccue.boba.test;
    requires dev.mccue.wcwidth;
    requires org.jspecify;
    requires jdk.unsupported;
}