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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * packageName com.github.paohaijiao.entity.domain
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickCircleShape implements JQuickDrawableShape {
    private double cx, cy, radius;
    private Color color;
    private boolean filled;

    public JQuickCircleShape(double cx, double cy, double radius, Color color, boolean filled) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.color = color;
        this.filled = filled;
    }

    @Override
    public void draw(SVGGraphics2D svgGenerator) {
        Ellipse2D.Double circle = new Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2);
        if (filled) {
            svgGenerator.setColor(color);
            svgGenerator.fill(circle);
        } else {
            svgGenerator.setColor(color);
            svgGenerator.draw(circle);
        }
    }

    @Override
    public void setPosition(double x, double y) {
        this.cx = x;
        this.cy = y;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(cx - radius, cy - radius, radius * 2, radius * 2);
    }
}