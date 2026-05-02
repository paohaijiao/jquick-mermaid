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
import java.awt.geom.Rectangle2D;

/**
 * packageName com.github.paohaijiao.entity.domain
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickPolygonShape implements JQuickDrawableShape {
    private Polygon polygon;
    private Color color;
    private boolean filled;

    public JQuickPolygonShape(Polygon polygon, Color color, boolean filled) {
        this.polygon = polygon;
        this.color = color;
        this.filled = filled;
    }

    @Override
    public void draw(SVGGraphics2D svgGenerator) {
        if (filled) {
            svgGenerator.setColor(color);
            svgGenerator.fill(polygon);
        } else {
            svgGenerator.setColor(color);
            svgGenerator.draw(polygon);
        }
    }

    @Override
    public void setPosition(double x, double y) {
        polygon.translate((int) (x - polygon.getBounds().getX()),
                (int) (y - polygon.getBounds().getY()));
    }

    @Override
    public Rectangle2D getBounds() {
        return polygon.getBounds2D();
    }
}
