package com.tylerroyer.matchthree;

import java.awt.*;

public class Renderer extends Canvas {
    private Grid grid;

    public Renderer(Grid grid) {
        this.grid = grid;

        setBackground(Color.BLACK);
        setSize(710, 710);

        addMouseListener(Game.getMouseHandler());
    }

    @Override
    public void update(Graphics g) {
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

        
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        grid.render(g);
        repaint();
    }
}
