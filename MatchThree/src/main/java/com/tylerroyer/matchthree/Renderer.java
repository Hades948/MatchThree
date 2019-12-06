package com.tylerroyer.matchthree;

import java.awt.*;
import java.util.ArrayList;

public class Renderer extends Canvas {
    public Renderer() {
        setBackground(new Color (40, 10, 40));
        setSize(710, 710);

        addMouseListener(Game.getMouseHandler());
        addKeyListener(Game.getKeyboardHandler());
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
        
        Game.getGrid().render(g);
        for (ParticleEmitter emitter : new ArrayList<ParticleEmitter>(Game.getParticleEmitters())) {
            emitter.render(g);
        }

        repaint();
    }
}
