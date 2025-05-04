package dev.weisz.boba.tea;

import org.jspecify.annotations.Nullable;

public record UpdateResult<Model>(Model model, @Nullable Cmd cmd) {}
