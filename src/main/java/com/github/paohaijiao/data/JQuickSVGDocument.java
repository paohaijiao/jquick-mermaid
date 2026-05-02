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
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName com.github.paohaijiao.combol.mermaind
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class JQuickSVGDocument {
    private List<JQuickDrawableShape> shapes = new ArrayList<>();
    private int width = 800;
    private int height = 600;
    private Color backgroundColor = Color.WHITE;
    private String title = "SVG Image";

    public JQuickSVGDocument() {
    }

    public JQuickSVGDocument(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addShape(JQuickDrawableShape shape) {
        shapes.add(shape);
    }

    public void removeShape(JQuickDrawableShape shape) {
        shapes.remove(shape);
    }

    public void clear() {
        shapes.clear();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String generateSVG() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
            svgGenerator.setSVGCanvasSize(new Dimension(width, height));

            // 绘制背景
            svgGenerator.setColor(backgroundColor);
            svgGenerator.fillRect(0, 0, width, height);

            // 绘制所有图形
            for (JQuickDrawableShape shape : shapes) {
                shape.draw(svgGenerator);
            }
            // 使用 StringWriter 捕获 SVG 输出
            StringWriter stringWriter = new StringWriter();
            boolean useCSS = true;  // 使用 CSS 样式属性
            svgGenerator.stream(stringWriter, useCSS);

            return stringWriter.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + width + "\" height=\"" + height + "\">\n" +
                    "  <text x=\"10\" y=\"20\" fill=\"red\">Error generating SVG: " + e.getMessage() + "</text>\n" +
                    "</svg>";
        }
    }

    public void saveToFile(String filename) {
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(filename),
                    generateSVG().getBytes());
            System.out.println("SVG saved to: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}