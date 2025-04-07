module dev.mccue.boba {
    requires com.ibm.icu;
    requires org.fusesource.jansi;
    exports dev.mccue.boba.crossterm to dev.mccue.boba.test;
    requires org.jspecify;
    requires jdk.unsupported;
    requires dev.mccue.ansi;
    requires dev.mccue.wcwidth;
}