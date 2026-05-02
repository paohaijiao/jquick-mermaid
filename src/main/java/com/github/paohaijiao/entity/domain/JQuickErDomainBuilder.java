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

import com.github.paohaijiao.data.JQuickSVGDocument;
import com.github.paohaijiao.entity.layout.JQuickDynamicHierarchicalLayout;
import com.github.paohaijiao.enums.JQuickRelationType;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * packageName com.github.paohaijiao.entity.domain
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickErDomainBuilder {
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

    private Map<String, JQuickEntityDomain> entities = new LinkedHashMap<>();
    private List<JQuickRelationship> relationships = new ArrayList<>();
    private JQuickCommentConfig commentConfig = new JQuickCommentConfig();
    private JQuickLayoutDomain layoutConfig = new JQuickLayoutDomain();
    private int width = DEFAULT_CANVAS_WIDTH;
    private int height = DEFAULT_CANVAS_HEIGHT;

    public JQuickErDomainBuilder() {
    }

    public JQuickErDomainBuilder setCommentConfig(JQuickCommentConfig config) {
        this.commentConfig = config;
        return this;
    }

    public JQuickErDomainBuilder setLayoutConfig(JQuickLayoutDomain config) {
        this.layoutConfig = config;
        return this;
    }

    public JQuickEntityDomain createEntity(String name) {
        return createEntity(name, null);
    }

    public JQuickEntityDomain createEntity(String name, String comment) {
        JQuickEntityDomain entity = new JQuickEntityDomain(name, comment);
        entities.put(name, entity);
        return entity;
    }

    public JQuickColumn createColumn(String name, String type, boolean isPrimaryKey) {
        return new JQuickColumn(name, type, isPrimaryKey);
    }

    public JQuickColumn createColumn(String name, String type, boolean isPrimaryKey, boolean isForeignKey) {
        return new JQuickColumn(name, type, isPrimaryKey, isForeignKey);
    }

    public JQuickColumn createColumn(String name, String type, String comment, boolean isPrimaryKey, boolean isForeignKey) {
        return new JQuickColumn(name, type, comment, isPrimaryKey, isForeignKey);
    }

    public JQuickRelationship createRelationship(String name, JQuickEntityDomain parent, JQuickEntityDomain child, JQuickRelationType type) {
        JQuickRelationship rel = new JQuickRelationship(name, parent, child, type);
        relationships.add(rel);
        return rel;
    }

    public void autoLayout() {
        for (JQuickEntityDomain entity : entities.values()) {
            entity.setCommentConfig(commentConfig);
            entity.updateDimensions();
        }

        List<JQuickEntityDomain> entityList = new ArrayList<>(entities.values());
        JQuickDynamicHierarchicalLayout layout = new JQuickDynamicHierarchicalLayout(entityList, relationships, layoutConfig);
        layout.layout();
    }

    public void generateAndSave(String filename) {
        autoLayout();

        Rectangle bounds = getBounds();
        width = (int) (bounds.getWidth() + PADDING * 3);
        height = (int) (bounds.getHeight() + PADDING * 3);
        width = Math.max(width, DEFAULT_CANVAS_WIDTH);
        height = Math.max(height, DEFAULT_CANVAS_HEIGHT);

        JQuickSVGDocument doc = new JQuickSVGDocument(width, height);
        doc.setBackgroundColor(new Color(250, 250, 250));
        doc.setTitle("ER Diagram");

        // 绘制关系线
        for (JQuickRelationship rel : relationships) {
            rel.draw(doc);
        }

        // 绘制实体
        for (JQuickEntityDomain entity : entities.values()) {
            entity.draw(doc, commentConfig);
        }

        doc.saveToFile(filename);
        System.out.println("ER图已保存到: " + filename);
        System.out.println("  - 布局参数: 水平间距=" + String.format("%.0f", layoutConfig.getHorizontalSpacing()) +
                ", 垂直间距=" + String.format("%.0f", layoutConfig.getVerticalSpacing()) +
                ", 中心偏向=" + layoutConfig.getCenterBias());
    }

    private Rectangle getBounds() {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (JQuickEntityDomain e : entities.values()) {
            minX = Math.min(minX, e.getX());
            minY = Math.min(minY, e.getY());
            maxX = Math.max(maxX, e.getX() + e.getWidth());
            maxY = Math.max(maxY, e.getY() + e.getHeight());
        }

        if (minX == Double.MAX_VALUE) {
            return new Rectangle(0, 0, DEFAULT_CANVAS_WIDTH, DEFAULT_CANVAS_HEIGHT);
        }

        return new Rectangle((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));
    }
}
