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

/**
 * packageName com.github.paohaijiao.entity.domain
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */

import com.github.paohaijiao.data.JQuickSVGDocument;
import com.github.paohaijiao.data.JQuickTextShape;
import com.github.paohaijiao.enums.JQuickRelationType;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * 关系类
 */
public class JQuickRelationship {

    private static final Color ENTITY_BG_COLOR = new Color(248, 248, 248);

    private static final Color ENTITY_HEADER_COLOR = new Color(66, 140, 200);

    private static final Color ENTITY_BORDER_COLOR = new Color(100, 100, 100);

    private static final Color PRIMARY_KEY_COLOR = new Color(200, 0, 0);

    private static final Color FOREIGN_KEY_COLOR = new Color(0, 128, 0);

    private static final Color RELATIONSHIP_LINE_COLOR = new Color(0, 0, 255);

    private static final Color CARDINALITY_COLOR = new Color(128, 0, 128);

    private static final Color DIVIDER_COLOR = new Color(180, 180, 180);

    // 布局常量
    private static final double ENTITY_MIN_WIDTH = 320;

    private static final double ENTITY_HEADER_HEIGHT = 34;

    private static final double ATTR_ROW_HEIGHT = 23;

    private static final double PADDING = 50;

    private static final int DEFAULT_CANVAS_WIDTH = 2000;

    private static final int DEFAULT_CANVAS_HEIGHT = 1600;

    // 默认离散度参数
    private static final double DEFAULT_HORIZONTAL_SPACING = 380;

    private static final double DEFAULT_VERTICAL_SPACING = 280;

    private String name;
    private JQuickEntityDomain parent;
    private JQuickEntityDomain child;
    private JQuickRelationType type;

    public JQuickRelationship(String name, JQuickEntityDomain parent, JQuickEntityDomain child, JQuickRelationType type) {
        this.name = name;
        this.parent = parent;
        this.child = child;
        this.type = type;
        parent.addChild(child);
        child.addParent(parent);
    }

    public void draw(JQuickSVGDocument doc) {
        Point2D.Double start = getConnectionPoint(parent, child.getCenterX(), child.getCenterY());
        Point2D.Double end = getConnectionPoint(child, parent.getCenterX(), parent.getCenterY());

        drawOrthogonalLine(doc, start, end);
        drawCardinality(doc, start, end);

        if (name != null && !name.isEmpty()) {
            drawRelationName(doc, start, end);
        }
    }

    private Point2D.Double getConnectionPoint(JQuickEntityDomain entity, double targetX, double targetY) {
        double dx = targetX - entity.getCenterX();
        double dy = targetY - entity.getCenterY();

        if (Math.abs(dx) < 0.001) dx = 0.001;
        double slope = dy / dx;

        double halfW = entity.getWidth() / 2;
        double halfH = entity.getHeight() / 2;

        double intersectX, intersectY;

        if (Math.abs(slope) <= halfH / halfW) {
            intersectX = dx > 0 ? entity.getRightX() : entity.getLeftX();
            intersectY = entity.getCenterY() + slope * (intersectX - entity.getCenterX());
        } else {
            intersectY = dy > 0 ? entity.getBottomY() : entity.getTopY();
            intersectX = entity.getCenterX() + (intersectY - entity.getCenterY()) / slope;
        }

        return new Point2D.Double(intersectX, intersectY);
    }

    private void drawOrthogonalLine(JQuickSVGDocument doc, Point2D.Double start, Point2D.Double end) {
        double midX = (start.x + end.x) / 2;

        JQuickPolylineShape polyline = new JQuickPolylineShape();
        polyline.addPoint(start.x, start.y);

        if (Math.abs(start.y - end.y) < 60) {
            polyline.addPoint(end.x, end.y);
        } else {
            polyline.addPoint(midX, start.y);
            polyline.addPoint(midX, end.y);
            polyline.addPoint(end.x, end.y);
        }

        polyline.setColor(RELATIONSHIP_LINE_COLOR);
        polyline.setStrokeWidth(1.5f);
        doc.addShape(polyline);

        drawArrowMarker(doc, end, type);
    }

    private void drawArrowMarker(JQuickSVGDocument doc, Point2D.Double end, JQuickRelationType type) {
        if (type == JQuickRelationType.ONE_TO_MANY || type == JQuickRelationType.MANY_TO_MANY) {
            Polygon p = new Polygon();
            p.addPoint((int) end.x, (int) end.y);
            p.addPoint((int) (end.x - 10), (int) (end.y - 5));
            p.addPoint((int) (end.x - 10), (int) (end.y + 5));
            JQuickPolygonShape arrow = new JQuickPolygonShape(p, RELATIONSHIP_LINE_COLOR, true);
            doc.addShape(arrow);
        } else if (type == JQuickRelationType.ONE_TO_ONE) {
            JQuickCircleShape dot = new JQuickCircleShape(end.x, end.y, 4, RELATIONSHIP_LINE_COLOR, true);
            doc.addShape(dot);
        }
    }

    private void drawCardinality(JQuickSVGDocument doc, Point2D.Double start, Point2D.Double end) {
        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double dist = Math.hypot(dx, dy);

        if (dist < 0.1) return;

        double startRatio = 35.0 / dist;
        double px = start.x + dx * startRatio;
        double py = start.y + dy * startRatio;

        double endRatio = 1.0 - 35.0 / dist;
        double cx = start.x + dx * endRatio;
        double cy = start.y + dy * endRatio;

        drawCardinalityBox(doc, px, py, type.getParentCardinality());
        drawCardinalityBox(doc, cx, cy, type.getChildCardinality());
    }

    private void drawCardinalityBox(JQuickSVGDocument doc, double x, double y, String text) {
        double offsetX = 12;
        double offsetY = 8;

        JQuickRectShape bg = new JQuickRectShape(x - offsetX, y - offsetY, 24, 16, Color.WHITE, true);
        bg.setStroke(0.5f, new Color(200, 200, 200));
        doc.addShape(bg);

        Font boldFont = new Font("Microsoft YaHei", Font.BOLD, 11);
        JQuickTextShape cardText = new JQuickTextShape(x, y + 4, text, CARDINALITY_COLOR, 11);
        cardText.setFont(boldFont);
        doc.addShape(cardText);
    }

    private void drawRelationName(JQuickSVGDocument doc, Point2D.Double start, Point2D.Double end) {
        double midX = (start.x + end.x) / 2;
        double midY = (start.y + end.y) / 2;

        JQuickRectShape bg = new JQuickRectShape(midX - 35, midY - 10, 70, 20,
                new Color(255, 255, 200, 220), true);
        bg.setStroke(0.5f, new Color(180, 180, 180));
        doc.addShape(bg);

        JQuickTextShape relName = new JQuickTextShape(midX, midY + 4, name, Color.BLACK, 10);
        doc.addShape(relName);
    }

}