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

/**
 * packageName com.github.paohaijiao.combol.mermaind
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public abstract class JQuickBaseShape implements JQuickDrawableShape {

    protected double x, y;

    protected Color color;

    protected boolean filled;

    protected float strokeWidth = 1.0f;

    protected Color strokeColor = Color.BLACK;

    public JQuickBaseShape(double x, double y, Color color, boolean filled) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.filled = filled;
    }

    public void setStroke(float width, Color color) {
        this.strokeWidth = width;
        this.strokeColor = color;
    }

    protected void setupGraphics(SVGGraphics2D svgGenerator) {
        svgGenerator.setColor(color);
        if (filled) {
            svgGenerator.fill(getShape());
        }
        svgGenerator.setColor(strokeColor);
        svgGenerator.setStroke(new BasicStroke(strokeWidth));
        svgGenerator.draw(getShape());
    }

    protected abstract Shape getShape();

    @Override
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
