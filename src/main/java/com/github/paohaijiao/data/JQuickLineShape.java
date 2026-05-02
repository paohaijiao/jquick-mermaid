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
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * packageName com.github.paohaijiao.combol.mermaind
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickLineShape extends JQuickBaseShape {
    private double endX, endY;

    public JQuickLineShape(double startX, double startY, double endX, double endY, Color color) {
        super(startX, startY, color, false);
        this.endX = endX;
        this.endY = endY;
    }

    @Override
    protected Shape getShape() {
        return new Line2D.Double(x, y, endX, endY);
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(Math.min(x, endX), Math.min(y, endY),
                Math.abs(endX - x), Math.abs(endY - y));
    }

    @Override
    public void draw(SVGGraphics2D svgGenerator) {
        svgGenerator.setColor(color);
        svgGenerator.setStroke(new BasicStroke(strokeWidth));
        svgGenerator.draw(getShape());
    }
}