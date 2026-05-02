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
package com.github.paohaijiao.factory;


import com.github.paohaijiao.data.*;

import java.awt.*;

/**
 * packageName com.github.paohaijiao.combol
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickShapeFactory {

    public static JQuickRectangleShape createRectangle(double x, double y, double width, double height, Color color, boolean filled) {
        return new JQuickRectangleShape(x, y, width, height, color, filled);
    }

    public static JQuickCircleShape createCircle(double x, double y, double radius, Color color, boolean filled) {
        return new JQuickCircleShape(x, y, radius, color, filled);
    }

    public static JQuickEllipseShape createEllipse(double x, double y, double width, double height, Color color, boolean filled) {
        return new JQuickEllipseShape(x, y, width, height, color, filled);
    }

    public static JQuickLineShape createLine(double startX, double startY, double endX, double endY, Color color) {
        return new JQuickLineShape(startX, startY, endX, endY, color);
    }

    public static JQuickTextShape createText(double x, double y, String text, Color color, int fontSize) {
        return new JQuickTextShape(x, y, text, color, fontSize);
    }
}
