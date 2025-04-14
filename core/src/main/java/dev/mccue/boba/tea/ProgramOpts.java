package dev.mccue.boba.tea;

import org.jspecify.annotations.Nullable;

import java.io.InputStream;
import java.io.OutputStream;

// TODO: add filters (needs to be more thought through) we could consider something like the spring web filter chains
public record ProgramOpts(
        OutputStream output,
        @Nullable InputStream input, // provide null to disable receiving input
        String[] env,
        boolean useDefaultSignalHandler,
        boolean handleExceptions,
        boolean useAltScreen, // this means it will open a separate full screen terminal
        boolean useBracketedPaste,
        boolean enableMouseCellMotion, // this can be enabled by default and enabled and disabled at runtime
        boolean withoutRenderer,
        int fps,
        boolean reportFocusChange,
        String startupTitle

) {
    private static final int MIN_FPS = 1;
    private static final int DEFAULT_FPS = 60;
    private static final int MAX_FPS = 120;

    public static Builder builder() {
        return new Builder();
    }

    static public class Builder {
        private OutputStream output = System.out;
        private @Nullable InputStream input = System.in;
        private String[] env = new String[0];
        private boolean useDefaultSignalHandler = true;
        private boolean handleExceptions = true;
        private boolean useAltScreen = false;
        private boolean useBracketedPaste = true;
        private boolean enableMouseCellMotion = true;
        private boolean withoutRenderer = false;
        private int fps = DEFAULT_FPS;
        private boolean reportFocusChange = true;
        private String startupTitle = "";

        public Builder output(OutputStream output) {
            this.output = output;
            return this;
        }

        public Builder input(InputStream input) {
            this.input = input;
            return this;
        }

        public Builder env(String... env) {
            this.env = env;
            return this;
        }

        public Builder useDefaultSignalHandler(boolean useDefaultSignalHandler) {
            this.useDefaultSignalHandler = useDefaultSignalHandler;
            return this;
        }

        public Builder handleExceptions(boolean handleExceptions) {
            this.handleExceptions = handleExceptions;
            return this;
        }

        public Builder useAltScreen(boolean useAltScreen) {
            this.useAltScreen = useAltScreen;
            return this;
        }

        public Builder useBracketedPaste(boolean useBracketedPaste) {
            this.useBracketedPaste = useBracketedPaste;
            return this;
        }

        public Builder enableMouseCellMotion(boolean enableMouseCellMotion) {
            this.enableMouseCellMotion = enableMouseCellMotion;
            return this;
        }

        public Builder withoutRenderer(boolean withoutRenderer) {
            this.withoutRenderer = withoutRenderer;
            return this;
        }

        public Builder fps(int fps) {
            if (fps < MIN_FPS) {
                this.fps = MIN_FPS;
            } else this.fps = Math.min(fps, MAX_FPS);
            return this;
        }

        public Builder reportFocusChange(boolean reportFocusChange) {
            this.reportFocusChange = reportFocusChange;
            return this;
        }

        public Builder startupTitle(String startupTitle) {
            this.startupTitle = startupTitle;
            return this;
        }

        public ProgramOpts build() {
            return new ProgramOpts(
                    output,
                    input,
                    env,
                    useDefaultSignalHandler,
                    handleExceptions,
                    useAltScreen,
                    useBracketedPaste,
                    enableMouseCellMotion,
                    withoutRenderer,
                    fps,
                    reportFocusChange,
                    startupTitle
            );
        }
    }
}
