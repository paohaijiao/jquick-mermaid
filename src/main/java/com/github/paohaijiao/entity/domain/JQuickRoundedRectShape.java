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
import java.awt.geom.RoundRectangle2D;

/**
 * packageName com.github.paohaijiao.entity.domain
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */

public class JQuickRoundedRectShape implements JQuickDrawableShape {
    private double x, y, width, height, arcW, arcH;
    private Color color;
    private boolean filled;
    private float strokeWidth = 1.0f;
    private Color strokeColor = Color.BLACK;

    public JQuickRoundedRectShape(double x, double y, double width, double height, double arcW, double arcH,
                                  Color color, boolean filled) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.arcW = arcW;
        this.arcH = arcH;
        this.color = color;
        this.filled = filled;
    }

    public void setStroke(float width, Color color) {
        this.strokeWidth = width;
        this.strokeColor = color;
    }

    @Override
    public void draw(SVGGraphics2D svgGenerator) {
        RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, arcW, arcH);
        if (filled) {
            svgGenerator.setColor(color);
            svgGenerator.fill(rect);
        }
        if (strokeColor != null && strokeWidth > 0) {
            svgGenerator.setColor(strokeColor);
            svgGenerator.setStroke(new BasicStroke(strokeWidth));
            svgGenerator.draw(rect);
        }
    }

    @Override
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }
}

