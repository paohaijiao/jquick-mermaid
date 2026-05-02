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

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName com.github.paohaijiao.combol.mermaind
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickGroupShape implements JQuickDrawableShape {

    private List<JQuickDrawableShape> shapes = new ArrayList<>();

    private double x = 0, y = 0;

    public void addShape(JQuickDrawableShape shape) {
        shapes.add(shape);
    }

    public void removeShape(JQuickDrawableShape shape) {
        shapes.remove(shape);
    }

    public void clear() {
        shapes.clear();
    }

    @Override
    public void draw(SVGGraphics2D svgGenerator) {
        for (JQuickDrawableShape shape : shapes) {
            shape.draw(svgGenerator);
        }
    }

    @Override
    public void setPosition(double x, double y) {
        double deltaX = x - this.x;
        double deltaY = y - this.y;
        for (JQuickDrawableShape shape : shapes) {
            Rectangle2D bounds = shape.getBounds();
            shape.setPosition(bounds.getX() + deltaX, bounds.getY() + deltaY);
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public Rectangle2D getBounds() {
        if (shapes.isEmpty()) return new Rectangle2D.Double(0, 0, 0, 0);

        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (JQuickDrawableShape shape : shapes) {
            Rectangle2D bounds = shape.getBounds();
            minX = Math.min(minX, bounds.getMinX());
            minY = Math.min(minY, bounds.getMinY());
            maxX = Math.max(maxX, bounds.getMaxX());
            maxY = Math.max(maxY, bounds.getMaxY());
        }

        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }

    public List<JQuickDrawableShape> getShapes() {
        return shapes;
    }
}
