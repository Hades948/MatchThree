package com.tylerroyer.engine;

import java.awt.*;

/**
 * Handles all looping for the game including the updating and drawing cycles.
 */
public class Looper extends Canvas {
    private static final long serialVersionUID = 4453541421570323839L;
    
    private float lastFrameTime, timeLeftInFrame, currentFPS;
    private long frameStartTime;
    private final float TARGET_FPS = 60.0f;

    Looper(int width, int height) {
        setBackground(new Color (40, 10, 40));
        setSize(width, height);

        addMouseListener(Game.getMouseHandler());
        addKeyListener(Game.getKeyboardHandler());

        frameStartTime = System.currentTimeMillis();
    }

    @Override
    public void update(Graphics g) {
        frameStartTime = System.currentTimeMillis();

        // Double buffer code //
		Graphics offgc;
		Image offscreen = null;
		Dimension d = getSize();

		// create the offscreen buffer and associated Graphics
		offscreen = createImage(d.width, d.height);
		offgc = offscreen.getGraphics();

		// clear the exposed area
		offgc.setColor(getBackground());
		offgc.fillRect(0, 0, d.width, d.height);
		offgc.setColor(getForeground());

		// do normal redraw
		paint(offgc);
		// scale and transfer offscreen to window
        g.drawImage(offscreen, 0, 0, this);
        // End double buffer code //

        Game.getCurrentScreen().update();

        // FPS Calculation
        lastFrameTime = System.currentTimeMillis() - frameStartTime;
        timeLeftInFrame = (1000 / TARGET_FPS) - lastFrameTime;
        if (timeLeftInFrame >= 0) {
            // On time.  Sleep remainder of frame.
            try { Thread.sleep((long) timeLeftInFrame); } catch (InterruptedException e) { e.printStackTrace(); }
            currentFPS = TARGET_FPS;
        } else {
            // Running behind
            currentFPS = 1000 / lastFrameTime;
        }
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        Game.getCurrentScreen().render(g);
        repaint();
    }

    /**
     * @return The game's current speed in frames per second.
     */
    public double getCurrentFPS() {
        return currentFPS;
    }
}
