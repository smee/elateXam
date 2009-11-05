package drawing;

import java.applet.Applet;

public class DrawingApplet extends Applet {
    private CompleteDrawingPanel panel;

    public String getForegroundPictureWithUndoData(final int numUndoSteps) {
        final String s = panel.getForegroundPictureWithUndoData(numUndoSteps);
        return s;
    }

    public boolean hasChanged() {
        return panel.hasChanged();
    }

    @Override
    public void init() {
        panel = new CompleteDrawingPanel(getWidth(), getHeight(), Boolean.valueOf(getParameter("tutorMode")));
        final String mutableForeground = getParameter("mutableForeground");
        panel.setDefaultForeGround(mutableForeground);
        final String foreground = getParameter("foreground");
        final String undoData = getParameter("undoData");
        if (undoData != null && undoData.length() > 0) {
            panel.setForegroundWithUndoData(foreground + "%%%" + undoData);
        } else {
            panel.setForegroundImage(foreground);
        }
        // stupid, panel resets bg when setting undodata :(
        final String bgString = getParameter("background");
        panel.setBackGroundImage(bgString);
        final boolean wasResetted = Boolean.valueOf(getParameter("resetted")).booleanValue();
        panel.setResetted(wasResetted);
        final boolean editable = !Boolean.valueOf(getParameter("viewonly")).booleanValue();
        panel.setEditable(editable);
        add(panel);
    }

    public boolean isResetted() {
        return panel.isResetted();
    }

    @Override
    public void start() {
        panel.setHasChanged(false);
    }

    @Override
    public void stop() {
        panel.setHasChanged(false);
    }
}
