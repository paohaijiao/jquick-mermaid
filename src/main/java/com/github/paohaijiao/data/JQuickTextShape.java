/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright (c) [2025-2099] Martin (goudingcheng@gmail.com)
 */
package com.github.paohaijiao.data;

import org.apache.batik.svggen.SVGGraphics2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * packageName com.github.paohaijiao.combol.mermaind
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickTextShape implements JQuickDrawableShape {
    private double x, y;
    private String text;
    private Color color;
    private int fontSize;
    private Font font;

    public JQuickTextShape(double x, double y, String text, Color color, int fontSize) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.fontSize = fontSize;
        this.font = new Font("Arial", Font.PLAIN, fontSize);
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public void draw(SVGGraphics2D svgGenerator) {
        svgGenerator.setColor(color);
        svgGenerator.setFont(font);
        svgGenerator.drawString(text, (float) x, (float) y);
    }

    @Override
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Rectangle2D getBounds() {
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        return new Rectangle2D.Double(x, y - height, width, height);
    }
}

