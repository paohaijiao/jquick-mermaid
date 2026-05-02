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
package com.github.paohaijiao.entity.layout;

/**
 * packageName com.github.paohaijiao.entity.layout
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */

import com.github.paohaijiao.entity.JQuickErDiagramProvider;
import com.github.paohaijiao.entity.domain.JQuickEntityDomain;
import com.github.paohaijiao.entity.domain.JQuickLayoutDomain;
import com.github.paohaijiao.entity.domain.JQuickRelationship;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Queue;


/**
 * 动态层次布局引擎
 */
public class JQuickDynamicHierarchicalLayout {


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

    private List<JQuickEntityDomain> entities;
    private List<JQuickRelationship> relationships;
    private Map<Integer, List<JQuickEntityDomain>> layerGroups = new LinkedHashMap<>();
    private JQuickLayoutDomain layoutConfig;

    public JQuickDynamicHierarchicalLayout(List<JQuickEntityDomain> entities, List<JQuickRelationship> relationships, JQuickLayoutDomain config) {
        this.entities = entities;
        this.relationships = relationships;
        this.layoutConfig = config != null ? config : new JQuickLayoutDomain();
    }

    public void layout() {
        if (entities.isEmpty()) return;

        calculateLayers();
        orderWithinLayers();
        calculateDynamicSpacing();
        calculatePositions();
        adjustBounds();
        resolveOverlaps();
    }

    private void calculateLayers() {
        Map<JQuickEntityDomain, Integer> inDegree = new HashMap<>();
        for (JQuickEntityDomain e : entities) {
            inDegree.put(e, e.getParents().size());
        }

        Queue<JQuickEntityDomain> queue = new LinkedList<>();
        for (JQuickEntityDomain e : entities) {
            if (inDegree.get(e) == 0) {
                queue.offer(e);
                e.setLayer(0);
            }
        }

        if (queue.isEmpty() && !entities.isEmpty()) {
            for (JQuickEntityDomain e : entities) {
                if (e.getParents().isEmpty()) {
                    queue.offer(e);
                    e.setLayer(0);
                }
            }
            if (queue.isEmpty()) {
                queue.offer(entities.get(0));
                entities.get(0).setLayer(0);
            }
        }

        while (!queue.isEmpty()) {
            JQuickEntityDomain current = queue.poll();
            int currentLayer = current.getLayer();

            for (JQuickEntityDomain child : current.getChildren()) {
                int newLayer = currentLayer + 1;
                if (child.getLayer() < newLayer) {
                    child.setLayer(newLayer);
                }
                int newIndegree = inDegree.get(child) - 1;
                inDegree.put(child, newIndegree);
                if (newIndegree == 0) {
                    queue.offer(child);
                }
            }
        }

        for (JQuickEntityDomain e : entities) {
            if (e.getLayer() == -1) {
                e.setLayer(0);
            }
        }

        for (JQuickEntityDomain e : entities) {
            int layer = e.getLayer();
            layerGroups.computeIfAbsent(layer, k -> new ArrayList<>()).add(e);
        }
    }

    private void orderWithinLayers() {
        int maxLayer = layerGroups.keySet().stream().max(Integer::compareTo).orElse(0);

        for (int layer = 1; layer <= maxLayer; layer++) {
            List<JQuickEntityDomain> currentLayer = layerGroups.get(layer);
            if (currentLayer == null) continue;

            currentLayer.sort((a, b) -> {
                double avgPosA = getAverageParentOrder(a);
                double avgPosB = getAverageParentOrder(b);
                return Double.compare(avgPosA, avgPosB);
            });

            for (int i = 0; i < currentLayer.size(); i++) {
                currentLayer.get(i).setOrder(i);
            }
        }

        for (int layer = maxLayer - 1; layer >= 0; layer--) {
            List<JQuickEntityDomain> currentLayer = layerGroups.get(layer);
            if (currentLayer == null) continue;

            currentLayer.sort((a, b) -> {
                double avgPosA = getAverageChildOrder(a);
                double avgPosB = getAverageChildOrder(b);
                return Double.compare(avgPosA, avgPosB);
            });

            for (int i = 0; i < currentLayer.size(); i++) {
                currentLayer.get(i).setOrder(i);
            }
        }
    }

    private double getAverageParentOrder(JQuickEntityDomain entity) {
        if (entity.getParents().isEmpty()) return entity.getOrder();
        double sum = 0;
        for (JQuickEntityDomain parent : entity.getParents()) {
            sum += parent.getOrder();
        }
        return sum / entity.getParents().size();
    }

    private double getAverageChildOrder(JQuickEntityDomain entity) {
        if (entity.getChildren().isEmpty()) return entity.getOrder();
        double sum = 0;
        for (JQuickEntityDomain child : entity.getChildren()) {
            sum += child.getOrder();
        }
        return sum / entity.getChildren().size();
    }

    private void calculateDynamicSpacing() {
        if (!layoutConfig.isAutoAdjustSpacing()) return;

        int maxLayerSize = 0;
        double maxWidth = 0;
        double totalHeight = 0;

        for (List<JQuickEntityDomain> layer : layerGroups.values()) {
            maxLayerSize = Math.max(maxLayerSize, layer.size());
            for (JQuickEntityDomain e : layer) {
                maxWidth = Math.max(maxWidth, e.getWidth());
                totalHeight += e.getHeight();
            }
        }

        double avgHeight = totalHeight / entities.size();

        if (maxLayerSize > 5) {
            layoutConfig.setHorizontalSpacing(Math.max(DEFAULT_HORIZONTAL_SPACING, maxWidth * 1.5));
        } else if (maxLayerSize > 3) {
            layoutConfig.setHorizontalSpacing(Math.max(DEFAULT_HORIZONTAL_SPACING * 0.9, maxWidth * 1.3));
        } else {
            layoutConfig.setHorizontalSpacing(DEFAULT_HORIZONTAL_SPACING);
        }

        layoutConfig.setVerticalSpacing(Math.max(DEFAULT_VERTICAL_SPACING, avgHeight * 2.5));
    }

    private void calculatePositions() {
        Map<Integer, Double> layerTotalWidth = new HashMap<>();

        for (Map.Entry<Integer, List<JQuickEntityDomain>> entry : layerGroups.entrySet()) {
            int layer = entry.getKey();
            List<JQuickEntityDomain> layerEntities = entry.getValue();
            double totalWidth = 0;
            for (JQuickEntityDomain e : layerEntities) {
                totalWidth += e.getWidth();
            }
            totalWidth += (layerEntities.size() - 1) * layoutConfig.getHorizontalSpacing();
            layerTotalWidth.put(layer, totalWidth);
        }

        double maxTotalWidth = DEFAULT_CANVAS_WIDTH * 0.7;
        for (double w : layerTotalWidth.values()) {
            maxTotalWidth = Math.max(maxTotalWidth, w);
        }

        int maxLayer = layerGroups.keySet().stream().max(Integer::compareTo).orElse(0);
        double totalLayersHeight = (maxLayer + 1) * layoutConfig.getVerticalSpacing();
        double verticalOffset = Math.max(PADDING, (DEFAULT_CANVAS_HEIGHT - totalLayersHeight) / 2);

        for (Map.Entry<Integer, List<JQuickEntityDomain>> entry : layerGroups.entrySet()) {
            int layer = entry.getKey();
            List<JQuickEntityDomain> layerEntities = entry.getValue();

            Double totalWidthObj = layerTotalWidth.get(layer);
            double layerTotalWidthValue = (totalWidthObj != null) ? totalWidthObj : 0;

            double canvasCenter = DEFAULT_CANVAS_WIDTH / 2;
            double layerStartX = canvasCenter - layerTotalWidthValue / 2;
            double biasOffset = (1 - layoutConfig.getCenterBias()) * (canvasCenter - layerStartX);
            layerStartX = canvasCenter - layerTotalWidthValue / 2 + biasOffset;
            layerStartX = Math.max(PADDING, Math.min(DEFAULT_CANVAS_WIDTH - layerTotalWidthValue - PADDING, layerStartX));

            double currentX = layerStartX;
            double yPos = verticalOffset + layer * layoutConfig.getVerticalSpacing();

            for (JQuickEntityDomain e : layerEntities) {
                e.setPosition(currentX, yPos);
                currentX += e.getWidth() + layoutConfig.getHorizontalSpacing();
            }
        }
    }

    private void adjustBounds() {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;

        for (JQuickEntityDomain e : entities) {
            minX = Math.min(minX, e.getX());
            minY = Math.min(minY, e.getY());
            maxX = Math.max(maxX, e.getX() + e.getWidth());
            maxY = Math.max(maxY, e.getY() + e.getHeight());
        }

        if (minX == Double.MAX_VALUE) {
            minX = 0;
            minY = 0;
            maxX = 800;
            maxY = 600;
        }

        double offsetX = Math.max(PADDING - minX, 0);
        double offsetY = Math.max(PADDING - minY, 0);

        for (JQuickEntityDomain e : entities) {
            e.setPosition(e.getX() + offsetX, e.getY() + offsetY);
        }
    }

    private void resolveOverlaps() {
        boolean hasOverlap = true;
        int maxIterations = 50;
        int iteration = 0;

        while (hasOverlap && iteration < maxIterations) {
            hasOverlap = false;
            iteration++;

            for (List<JQuickEntityDomain> layer : layerGroups.values()) {
                for (int i = 0; i < layer.size() - 1; i++) {
                    JQuickEntityDomain e1 = layer.get(i);
                    JQuickEntityDomain e2 = layer.get(i + 1);

                    double overlap = (e1.getX() + e1.getWidth() + layoutConfig.getHorizontalSpacing() / 3) - e2.getX();
                    if (overlap > 0) {
                        hasOverlap = true;
                        e2.setPosition(e2.getX() + overlap + 10, e2.getY());
                    }
                }
            }

            List<Integer> layers = new ArrayList<>(layerGroups.keySet());
            for (int i = 0; i < layers.size() - 1; i++) {
                List<JQuickEntityDomain> upperLayer = layerGroups.get(layers.get(i));
                List<JQuickEntityDomain> lowerLayer = layerGroups.get(layers.get(i + 1));

                for (JQuickEntityDomain upper : upperLayer) {
                    for (JQuickEntityDomain lower : lowerLayer) {
                        double upperBottom = upper.getY() + upper.getHeight();
                        double lowerTop = lower.getY();

                        if (upperBottom > lowerTop &&
                                Math.abs(upper.getCenterX() - lower.getCenterX()) < (upper.getWidth() + lower.getWidth()) / 2) {
                            hasOverlap = true;
                            double newY = upperBottom + layoutConfig.getVerticalSpacing() / 2;
                            double deltaY = newY - lower.getY();
                            lower.setPosition(lower.getX(), lower.getY() + deltaY);
                        }
                    }
                }
            }
        }
    }
}
