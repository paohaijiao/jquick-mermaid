package com.github.paohaijiao.data;

import org.apache.batik.svggen.SVGGraphics2D;

import java.awt.geom.Rectangle2D;

public interface JQuickDrawableShape {

    void draw(SVGGraphics2D svgGenerator);

    void setPosition(double x, double y);

    Rectangle2D getBounds();

}
