package dev.mccue.ansi;

@Kitty
public enum KittyMode {
    SET_GIVEN_FLAGS_UNSET_OTHERS(1),
    SET_GIVEN_FLAGS_KEEP_EXISTING(2),
    UNSET_GIVEN_FLAGS_KEEP_EXISTING(3);

    final int value;

    KittyMode(int value) {
        this.value = value;
    }
}
