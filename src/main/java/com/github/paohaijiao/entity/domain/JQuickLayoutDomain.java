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

import java.awt.*;

/**
 * packageName com.github.paohaijiao.entity.domain
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickLayoutDomain {
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
    private double horizontalSpacing = DEFAULT_HORIZONTAL_SPACING;
    private double verticalSpacing = DEFAULT_VERTICAL_SPACING;
    private double centerBias = 0.5;
    private boolean autoAdjustSpacing = true;

    public JQuickLayoutDomain() {
    }

    public double getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public JQuickLayoutDomain setHorizontalSpacing(double spacing) {
        this.horizontalSpacing = spacing;
        return this;
    }

    public double getVerticalSpacing() {
        return verticalSpacing;
    }

    public JQuickLayoutDomain setVerticalSpacing(double spacing) {
        this.verticalSpacing = spacing;
        return this;
    }

    public double getCenterBias() {
        return centerBias;
    }

    public JQuickLayoutDomain setCenterBias(double bias) {
        this.centerBias = Math.max(0, Math.min(1, bias));
        return this;
    }

    public boolean isAutoAdjustSpacing() {
        return autoAdjustSpacing;
    }

    public JQuickLayoutDomain setAutoAdjustSpacing(boolean auto) {
        this.autoAdjustSpacing = auto;
        return this;
    }
}
