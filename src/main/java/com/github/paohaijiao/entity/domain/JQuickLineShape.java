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
package com.github.paohaijiao.entity.domain;

import com.github.paohaijiao.data.JQuickDrawableShape;
import org.apache.batik.svggen.SVGGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * packageName com.github.paohaijiao.entity.domain
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickLineShape implements JQuickDrawableShape {
    private double x1, y1, x2, y2;
    private Color color;
    private float strokeWidth;

    public JQuickLineShape(double x1, double y1, double x2, double y2, Color color, float strokeWidth) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(SVGGraphics2D svgGenerator) {
        svgGenerator.setColor(color);
        svgGenerator.setStroke(new BasicStroke(strokeWidth));
        svgGenerator.draw(new Line2D.Double(x1, y1, x2, y2));
    }

    @Override
    public void setPosition(double x, double y) {
        double dx = x - x1;
        double dy = y - y1;
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2),
                Math.abs(x2 - x1), Math.abs(y2 - y1));
    }
}
