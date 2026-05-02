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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName com.github.paohaijiao.entity.domain
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickPolylineShape implements JQuickDrawableShape {
    private List<Point2D.Double> points = new ArrayList<>();
    private Color color = Color.BLACK;
    private float strokeWidth = 1.0f;

    public void addPoint(double x, double y) {
        points.add(new Point2D.Double(x, y));
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
    }

    @Override
    public void draw(SVGGraphics2D svgGenerator) {
        if (points.size() < 2) return;
        svgGenerator.setColor(color);
        svgGenerator.setStroke(new BasicStroke(strokeWidth));
        for (int i = 0; i < points.size() - 1; i++) {
            Point2D.Double p1 = points.get(i);
            Point2D.Double p2 = points.get(i + 1);
            svgGenerator.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
        }
    }

    @Override
    public void setPosition(double x, double y) {
        double dx = x - points.get(0).x;
        double dy = y - points.get(0).y;
        for (Point2D.Double p : points) {
            p.x += dx;
            p.y += dy;
        }
    }

    @Override
    public Rectangle2D getBounds() {
        if (points.isEmpty()) return new Rectangle2D.Double();
        double minX = points.stream().mapToDouble(p -> p.x).min().getAsDouble();
        double minY = points.stream().mapToDouble(p -> p.y).min().getAsDouble();
        double maxX = points.stream().mapToDouble(p -> p.x).max().getAsDouble();
        double maxY = points.stream().mapToDouble(p -> p.y).max().getAsDouble();
        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }
}
