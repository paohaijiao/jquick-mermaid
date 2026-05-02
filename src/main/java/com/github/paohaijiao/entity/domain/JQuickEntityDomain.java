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
import com.github.paohaijiao.data.JQuickTextShape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName com.github.paohaijiao.entity.domain
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickEntityDomain {
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
    private String comment;
    private double x, y;
    private double width = ENTITY_MIN_WIDTH;
    private double height = ENTITY_HEADER_HEIGHT;
    private List<JQuickColumn> columns = new ArrayList<>();
    private List<JQuickEntityDomain> parents = new ArrayList<>();
    private List<JQuickEntityDomain> children = new ArrayList<>();
    private int layer = -1;
    private int order = 0;

    private JQuickCommentConfig commentConfig;

    public JQuickEntityDomain(String name) {
        this(name, null);
    }

    public JQuickEntityDomain(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public void setCommentConfig(JQuickCommentConfig config) {
        this.commentConfig = config;
    }

    public void addColumn(JQuickColumn column) {
        columns.add(column);
        updateDimensions();
    }

    public void updateDimensions() {
        // 计算宽度 - 考虑表名+备注的长度
        int maxLen = 0;

        // 表名+备注的长度
        int nameLen = name.length();
        if (commentConfig != null && commentConfig.isShowTableComment() && comment != null && !comment.isEmpty()) {
            nameLen += comment.length() + 4; // " (备注)"
        }
        maxLen = Math.max(maxLen, nameLen);

        // 字段的最大长度
        for (JQuickColumn col : columns) {
            String text = col.getDisplayText();
            maxLen = Math.max(maxLen, text.length());
        }

        this.width = Math.max(ENTITY_MIN_WIDTH, maxLen * 7 + 50);

        // 计算高度
        this.height = ENTITY_HEADER_HEIGHT + columns.size() * ATTR_ROW_HEIGHT + 6;
    }

    public void addParent(JQuickEntityDomain parent) {
        if (!parents.contains(parent)) {
            parents.add(parent);
        }
    }

    public void addChild(JQuickEntityDomain child) {
        if (!children.contains(child)) {
            children.add(child);
        }
    }

    /**
     * 获取带备注的表名显示文本
     */
    public String getDisplayNameWithComment() {
        if (commentConfig != null && commentConfig.isShowTableComment() && comment != null && !comment.isEmpty()) {
            return name + " (" + comment + ")";
        }
        return name;
    }

    public void draw(JQuickSVGDocument doc, JQuickCommentConfig globalConfig) {
        JQuickCommentConfig config = commentConfig != null ? commentConfig : globalConfig;

        // 绘制实体背景
        JQuickRoundedRectShape shape = new JQuickRoundedRectShape(x, y, width, height, 8, 8, ENTITY_BG_COLOR, true);
        shape.setStroke(1.5f, ENTITY_BORDER_COLOR);
        doc.addShape(shape);

        // 绘制头部
        JQuickRoundedRectShape header = new JQuickRoundedRectShape(x, y, width, ENTITY_HEADER_HEIGHT, 8, 8,
                ENTITY_HEADER_COLOR, true);
        header.setStroke(0, null);
        doc.addShape(header);

        JQuickRectShape fixShape = new JQuickRectShape(x, y + 4, width, ENTITY_HEADER_HEIGHT - 4,
                ENTITY_HEADER_COLOR, true);
        fixShape.setStroke(0, null);
        doc.addShape(fixShape);

        // 实体名称 + 备注（同一行）
        Font boldFont = new Font("Microsoft YaHei", Font.BOLD, 13);
        String displayName = getDisplayNameWithComment();
        JQuickTextShape nameText = new JQuickTextShape(
                x + width / 2, y + ENTITY_HEADER_HEIGHT / 2 + 4, displayName, Color.WHITE, 13);
        nameText.setFont(boldFont);
        doc.addShape(nameText);

        // 分割线
        JQuickLineShape divider = new JQuickLineShape(x, y + ENTITY_HEADER_HEIGHT, x + width, y + ENTITY_HEADER_HEIGHT,
                DIVIDER_COLOR, 1.0f);
        doc.addShape(divider);

        // 绘制列
        Font plainFont = new Font("Consolas", Font.PLAIN, 11);
        double startY = y + ENTITY_HEADER_HEIGHT + 12;
        double textY = startY;

        for (JQuickColumn column : columns) {
            JQuickTextShape columnText = new JQuickTextShape(
                    x + 10, textY, column.getDisplayText(), column.getTextColor(), 11);
            columnText.setFont(plainFont);
            doc.addShape(columnText);
            textY += ATTR_ROW_HEIGHT;
        }
    }

    // Getters/Setters
    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getCenterX() {
        return x + width / 2;
    }

    public double getCenterY() {
        return y + height / 2;
    }

    public double getRightX() {
        return x + width;
    }

    public double getLeftX() {
        return x;
    }

    public double getTopY() {
        return y;
    }

    public double getBottomY() {
        return y + height;
    }

    public List<JQuickEntityDomain> getParents() {
        return parents;
    }

    public List<JQuickEntityDomain> getChildren() {
        return children;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
