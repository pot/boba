package dev.mccue.boba;

import java.io.IOException;
import java.io.OutputStream;

public class StandardRenderer implements Renderer{
    private final OutputStream out;

    public StandardRenderer(OutputStream out) {
        this.out = out;
    }

    private void execute(String seq) throws IOException {
        out.write(seq.getBytes());
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void kill() {

    }

    @Override
    public void write(String frame) {

    }

    @Override
    public void repaint() {

    }

    @Override
    public void clearScreen() {
        synchronized (this) {
            // TODO: import ansi
        }
    }

    @Override
    public boolean altScreen() {
        return false;
    }

    @Override
    public void enterAltScreen() {

    }

    @Override
    public void exitAltScreen() {

    }

    @Override
    public void showCursor() {

    }

    @Override
    public void hideCursor() {

    }

    @Override
    public void enableMouseCellMotion() {

    }

    @Override
    public void disableMouseCellMotion() {

    }

    @Override
    public void enableMouseAllMotion() {

    }

    @Override
    public void disableMouseAllMotion() {

    }

    @Override
    public void enableMouseSGRMode() {

    }

    @Override
    public void disableMouseSGRMode() {

    }

    @Override
    public void enableBracketedPaste() {

    }

    @Override
    public void disableBracketedPaste() {

    }

    @Override
    public boolean bracketedPasteActive() {
        return false;
    }

    @Override
    public void setWindowTitle(String title) {

    }

    @Override
    public boolean reportFocus() {
        return false;
    }

    @Override
    public void enableReportFocus() {

    }

    @Override
    public void disableReportFocus() {

    }
}
