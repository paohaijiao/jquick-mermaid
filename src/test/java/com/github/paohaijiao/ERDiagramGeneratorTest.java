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
package com.github.paohaijiao;

import com.github.paohaijiao.data.*;
import com.github.paohaijiao.factory.JQuickShapeFactory;
import org.apache.batik.svggen.SVGGraphics2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * ER图生成器Demo
 * 展示如何使用SVG图形库绘制实体关系图
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */
public class ERDiagramGeneratorTest {

    // ER图组件样式常量
    private static final Color ENTITY_COLOR = new Color(173, 216, 230);      // 实体：浅蓝色
    private static final Color ATTRIBUTE_COLOR = new Color(144, 238, 144);    // 属性：浅绿色
    private static final Color RELATIONSHIP_COLOR = new Color(255, 182, 193); // 关系：浅粉色
    private static final Color LINE_COLOR = Color.BLACK;
    private static final Color TEXT_COLOR = Color.BLACK;

    /**
     * Demo示例1：简单的用户-订单ER图
     */
    public static void createSimpleERDiagram() {
        System.out.println("创建简单ER图示例...");

        ERDiagramBuilder builder = new ERDiagramBuilder(800, 600);

        // 添加实体
        Entity user = builder.addEntity("User", 100, 250, 120, 60);
        Entity order = builder.addEntity("Order", 500, 250, 120, 60);

        // 添加关系
        Relationship places = builder.addRelationship("Places", 310, 250, "one-to-many");

        // 连接实体和关系
        builder.connect(user, places, "1");
        builder.connect(order, places, "N");

        // 为用户添加属性
        Attribute userId = builder.addAttribute("User ID", true);
        Attribute userName = builder.addAttribute("Name", false);
        Attribute userEmail = builder.addAttribute("Email", false);

        builder.linkAttribute(user, userId, -80, -40);
        builder.linkAttribute(user, userName, -80, 0);
        builder.linkAttribute(user, userEmail, -80, 40);

        // 为订单添加属性
        Attribute orderId = builder.addAttribute("Order ID", true);
        Attribute orderDate = builder.addAttribute("Date", false);
        Attribute orderTotal = builder.addAttribute("Total", false);

        builder.linkAttribute(order, orderId, 80, -40);
        builder.linkAttribute(order, orderDate, 80, 0);
        builder.linkAttribute(order, orderTotal, 80, 40);

        builder.generate();
        builder.save("d://test//er_diagram_simple.svg");
        System.out.println("简单ER图已保存到: d://test//er_diagram_simple.svg");
    }

    /**
     * Demo示例2：复杂ER图 - 学校管理系统
     */
    public static void createSchoolERDiagram() {
        System.out.println("创建学校管理系统ER图...");

        ERDiagramBuilder builder = new ERDiagramBuilder(1000, 700);
        builder.document.setBackgroundColor(new Color(245, 245, 250));

        // 创建实体
        Entity student = builder.addEntity("Student", 150, 400, 140, 70);
        Entity course = builder.addEntity("Course", 500, 150, 140, 70);
        Entity teacher = builder.addEntity("Teacher", 800, 400, 140, 70);
        Entity department = builder.addEntity("Department", 500, 550, 140, 70);

        // 创建关系
        Relationship enrolls = builder.addRelationship("Enrolls", 340, 300, "many-to-many");
        Relationship teaches = builder.addRelationship("Teaches", 660, 280, "one-to-many");
        Relationship belongs = builder.addRelationship("Belongs", 340, 530, "many-to-one");
        Relationship worksFor = builder.addRelationship("Works For", 660, 530, "many-to-one");

        // 建立连接
        builder.connect(student, enrolls, "N");
        builder.connect(course, enrolls, "N");
        builder.connect(teacher, teaches, "1");
        builder.connect(course, teaches, "N");
        builder.connect(student, belongs, "N");
        builder.connect(department, belongs, "1");
        builder.connect(teacher, worksFor, "N");
        builder.connect(department, worksFor, "1");

        // 学生属性
        Attribute stuId = builder.addAttribute("Student ID", true);
        Attribute stuName = builder.addAttribute("Name", false);
        Attribute stuMajor = builder.addAttribute("Major", false);
        Attribute stuGrade = builder.addAttribute("Grade", false);

        builder.linkAttribute(student, stuId, -90, -50);
        builder.linkAttribute(student, stuName, -90, 0);
        builder.linkAttribute(student, stuMajor, -90, 50);
        builder.linkAttribute(student, stuGrade, -90, 100);

        // 课程属性
        Attribute courseId = builder.addAttribute("Course ID", true);
        Attribute courseName = builder.addAttribute("Name", false);
        Attribute credits = builder.addAttribute("Credits", false);

        builder.linkAttribute(course, courseId, 0, -60);
        builder.linkAttribute(course, courseName, 90, -50);
        builder.linkAttribute(course, credits, 90, 0);

        // 教师属性
        Attribute teaId = builder.addAttribute("Teacher ID", true);
        Attribute teaName = builder.addAttribute("Name", false);
        Attribute teaTitle = builder.addAttribute("Title", false);

        builder.linkAttribute(teacher, teaId, 90, -50);
        builder.linkAttribute(teacher, teaName, 90, 0);
        builder.linkAttribute(teacher, teaTitle, 90, 50);

        // 院系属性
        Attribute deptId = builder.addAttribute("Dept ID", true);
        Attribute deptName = builder.addAttribute("Name", false);

        builder.linkAttribute(department, deptId, 0, -50);
        builder.linkAttribute(department, deptName, 0, 50);

        // 添加图例
        addLegend(builder.document);

        // 添加标题
        JQuickTextShape title = JQuickShapeFactory.createText(500, 50, "School Management System ER Diagram",
                new Color(0, 51, 102), 20);
        builder.document.addShape(title);

        builder.generate();
        builder.save("d://test//er_diagram_school.svg");
        System.out.println("学校管理系统ER图已保存到: d://test//er_diagram_school.svg");
    }

    /**
     * Demo示例3：电商系统ER图
     */
    public static void createEcommerceERDiagram() {
        System.out.println("创建电商系统ER图...");

        ERDiagramBuilder builder = new ERDiagramBuilder(1100, 700);
        builder.document.setBackgroundColor(new Color(255, 250, 240));

        // 实体
        Entity customer = builder.addEntity("Customer", 150, 350, 140, 70);
        Entity product = builder.addEntity("Product", 550, 150, 140, 70);
        Entity orderEcom = builder.addEntity("Order", 550, 550, 140, 70);
        Entity cart = builder.addEntity("Shopping Cart", 350, 400, 160, 70);
        Entity category = builder.addEntity("Category", 850, 300, 140, 70);

        // 关系
        Relationship owns = builder.addRelationship("Owns", 260, 480, "one-to-one");
        Relationship contains = builder.addRelationship("Contains", 460, 480, "one-to-many");
        Relationship belongsTo = builder.addRelationship("Belongs To", 710, 350, "many-to-one");
        Relationship hasItems = builder.addRelationship("Has Items", 460, 300, "many-to-many");

        // 连接
        builder.connect(customer, owns, "1");
        builder.connect(cart, owns, "1");
        builder.connect(cart, contains, "1");
        builder.connect(orderEcom, contains, "N");
        builder.connect(product, belongsTo, "N");
        builder.connect(category, belongsTo, "1");
        builder.connect(cart, hasItems, "N");
        builder.connect(product, hasItems, "N");

        // 添加属性
        Attribute custId = builder.addAttribute("Customer ID", true);
        Attribute custName = builder.addAttribute("Name", false);
        Attribute custEmail = builder.addAttribute("Email", false);
        builder.linkAttribute(customer, custId, -90, -50);
        builder.linkAttribute(customer, custName, -90, 0);
        builder.linkAttribute(customer, custEmail, -90, 50);

        Attribute prodId = builder.addAttribute("Product ID", true);
        Attribute prodName = builder.addAttribute("Name", false);
        Attribute price = builder.addAttribute("Price", false);
        builder.linkAttribute(product, prodId, 0, -60);
        builder.linkAttribute(product, prodName, 90, -50);
        builder.linkAttribute(product, price, 90, 0);

        Attribute orderId = builder.addAttribute("Order ID", true);
        Attribute orderDate = builder.addAttribute("Date", false);
        Attribute total = builder.addAttribute("Total", false);
        builder.linkAttribute(orderEcom, orderId, 90, -50);
        builder.linkAttribute(orderEcom, orderDate, 90, 0);
        builder.linkAttribute(orderEcom, total, 90, 50);

        // 添加标题和说明
        JQuickTextShape title = JQuickShapeFactory.createText(550, 40, "E-Commerce System ER Diagram",
                new Color(0, 51, 102), 22);
        builder.document.addShape(title);

        builder.generate();
        builder.save("d://test//er_diagram_ecommerce.svg");
        System.out.println("电商系统ER图已保存到: d://test//er_diagram_ecommerce.svg");
    }

    /**
     * 添加图例
     */
    private static void addLegend(JQuickSVGDocument doc) {
        int legendX = 20;
        int legendY = 20;

        // 实体图例
        JQuickRectangleShape entityLegend = JQuickShapeFactory.createRectangle(legendX, legendY, 30, 20, ENTITY_COLOR, true);
        entityLegend.setStroke(1.0f, Color.BLACK);
        doc.addShape(entityLegend);
        JQuickTextShape entityText = JQuickShapeFactory.createText(legendX + 35, legendY + 15, "Entity", TEXT_COLOR, 10);
        doc.addShape(entityText);

        // 关系图例
        PolygonShape relLegend = createLegendDiamond(legendX + 90, legendY + 10, 20, 14);
        relLegend.setStroke(1.0f, Color.BLACK);
        doc.addShape(relLegend);
        JQuickTextShape relText = JQuickShapeFactory.createText(legendX + 125, legendY + 15, "Relationship", TEXT_COLOR, 10);
        doc.addShape(relText);

        // 属性图例
        JQuickEllipseShape attrLegend = JQuickShapeFactory.createEllipse(legendX + 210, legendY, 30, 20, ATTRIBUTE_COLOR, true);
        attrLegend.setStroke(1.0f, Color.BLACK);
        doc.addShape(attrLegend);
        JQuickTextShape attrText = JQuickShapeFactory.createText(legendX + 245, legendY + 15, "Attribute", TEXT_COLOR, 10);
        doc.addShape(attrText);
    }

    private static PolygonShape createLegendDiamond(double centerX, double centerY, double width, double height) {
        Polygon p = new Polygon();
        p.addPoint((int) (centerX), (int) (centerY - height / 2));
        p.addPoint((int) (centerX + width / 2), (int) (centerY));
        p.addPoint((int) (centerX), (int) (centerY + height / 2));
        p.addPoint((int) (centerX - width / 2), (int) (centerY));
        return new PolygonShape(p, RELATIONSHIP_COLOR, true);
    }

    public static void main(String[] args) {
        // 确保输出目录存在
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("d://test"));
        } catch (Exception e) {
            System.out.println("创建目录失败: " + e.getMessage());
        }

        System.out.println("=== ER图生成器Demo ===");
        System.out.println();

        // 生成多个ER图示例
        createSimpleERDiagram();
        System.out.println();

        createSchoolERDiagram();
        System.out.println();

        createEcommerceERDiagram();
        System.out.println();

        System.out.println("所有ER图生成完成！");
        System.out.println("输出目录: d://test/");
        System.out.println("生成的文件:");
        System.out.println("  - er_diagram_simple.svg (简单用户订单ER图)");
        System.out.println("  - er_diagram_school.svg (学校管理系统ER图)");
        System.out.println("  - er_diagram_ecommerce.svg (电商系统ER图)");
    }

    /**
     * 实体类 - 表示ER图中的实体
     */
    public static class Entity {
        private String name;
        private double x, y;
        private double width = 120;
        private double height = 60;
        private List<Attribute> attributes = new ArrayList<>();
        private List<Relationship> relationships = new ArrayList<>();

        public Entity(String name, double x, double y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public Entity(String name, double x, double y, double width, double height) {
            this(name, x, y);
            this.width = width;
            this.height = height;
        }

        public void addAttribute(Attribute attr) {
            attributes.add(attr);
        }

        public void addRelationship(Relationship rel) {
            relationships.add(rel);
        }

        public JQuickRectangleShape draw(JQuickSVGDocument doc) {
            JQuickRectangleShape rect = JQuickShapeFactory.createRectangle(
                    x, y, width, height, ENTITY_COLOR, true);
            rect.setStroke(2.0f, Color.BLACK);
            doc.addShape(rect);
            JQuickTextShape nameText = JQuickShapeFactory.createText(
                    x + width / 2, y + height / 2 + 5, name, TEXT_COLOR, 14);
            doc.addShape(nameText);

            return rect;
        }

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

        public List<Attribute> getAttributes() {
            return attributes;
        }
    }

    /**
     * 属性类 - 表示实体的属性
     */
    public static class Attribute {
        private String name;
        private double x, y;
        private boolean isPrimaryKey;  // 是否主键

        public Attribute(String name, boolean isPrimaryKey) {
            this.name = name;
            this.isPrimaryKey = isPrimaryKey;
        }

        public void setPosition(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void draw(JQuickSVGDocument doc) {
            // 绘制属性椭圆
            JQuickEllipseShape ellipse = JQuickShapeFactory.createEllipse(
                    x, y, 100, 40, ATTRIBUTE_COLOR, true);
            ellipse.setStroke(1.5f, LINE_COLOR);
            doc.addShape(ellipse);

            // 绘制属性名称（主键加下划线或前缀）
            String displayName = isPrimaryKey ? "🔑 " + name : name;
            JQuickTextShape attrText = JQuickShapeFactory.createText(
                    x + 50, y + 25, displayName, TEXT_COLOR, 12);
            doc.addShape(attrText);
        }

        public String getName() {
            return name;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getCenterX() {
            return x + 50;
        }

        public double getCenterY() {
            return y + 20;
        }
    }

    /**
     * 关系类 - 表示实体间的关系
     */
    public static class Relationship {
        private String name;
        private double x, y;
        private String type; // "one-to-one", "one-to-many", "many-to-many"

        public Relationship(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public void setPosition(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void draw(JQuickSVGDocument doc) {
            // 绘制关系菱形
            PolygonShape diamond = createDiamond(x, y, 80, 50);
            diamond.setStroke(2.0f, LINE_COLOR);
            doc.addShape(diamond);

            // 绘制关系名称
            JQuickTextShape relText = JQuickShapeFactory.createText(
                    x, y + 5, name, TEXT_COLOR, 12);
            doc.addShape(relText);
        }

        private PolygonShape createDiamond(double centerX, double centerY, double width, double height) {
            Polygon p = new Polygon();
            p.addPoint((int) (centerX), (int) (centerY - height / 2));
            p.addPoint((int) (centerX + width / 2), (int) (centerY));
            p.addPoint((int) (centerX), (int) (centerY + height / 2));
            p.addPoint((int) (centerX - width / 2), (int) (centerY));

            return new PolygonShape(p, RELATIONSHIP_COLOR, true);
        }

        public String getType() {
            return type;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    /**
     * 连线类 - 表示实体与关系之间的连线
     */
    public static class Connection {
        private Entity entity;
        private Relationship relationship;
        private String cardinality; // "1", "N", "0..1", "1..N"

        public Connection(Entity entity, Relationship relationship, String cardinality) {
            this.entity = entity;
            this.relationship = relationship;
            this.cardinality = cardinality;
        }

        public void draw(JQuickSVGDocument doc) {
            // 绘制连接线
            JQuickLineShape line = JQuickShapeFactory.createLine(
                    entity.getCenterX(), entity.getCenterY(),
                    relationship.getX(), relationship.getY(), LINE_COLOR);
            line.setStroke(1.5f, LINE_COLOR);
            doc.addShape(line);

            // 绘制基数标记
            double midX = (entity.getCenterX() + relationship.getX()) / 2;
            double midY = (entity.getCenterY() + relationship.getY()) / 2;

            JQuickTextShape cardText = JQuickShapeFactory.createText(
                    midX, midY - 5, cardinality, Color.RED, 11);
            doc.addShape(cardText);
        }
    }

    /**
     * 自定义多边形形状类
     */
    public static class PolygonShape extends JQuickBaseShape {
        private Polygon polygon;

        public PolygonShape(Polygon polygon, Color color, boolean filled) {
            super(0, 0, color, filled);
            this.polygon = polygon;
        }

        @Override
        protected Shape getShape() {
            return polygon;
        }

        @Override
        public Rectangle2D getBounds() {
            return polygon.getBounds2D();
        }

        @Override
        public void draw(SVGGraphics2D svgGenerator) {
            setupGraphics(svgGenerator);
        }
    }

    /**
     * ER图构建器
     */
    public static class ERDiagramBuilder {
        private JQuickSVGDocument document;
        private List<Entity> entities = new ArrayList<>();
        private List<Relationship> relationships = new ArrayList<>();
        private List<Connection> connections = new ArrayList<>();
        private List<Attribute> attributes = new ArrayList<>();

        public ERDiagramBuilder(int width, int height) {
            this.document = new JQuickSVGDocument(width, height);
            this.document.setBackgroundColor(Color.WHITE);
            this.document.setTitle("ER Diagram");
        }

        public Entity addEntity(String name, double x, double y) {
            Entity entity = new Entity(name, x, y);
            entities.add(entity);
            return entity;
        }

        public Entity addEntity(String name, double x, double y, double width, double height) {
            Entity entity = new Entity(name, x, y, width, height);
            entities.add(entity);
            return entity;
        }

        public Attribute addAttribute(String name, boolean isPrimaryKey) {
            Attribute attr = new Attribute(name, isPrimaryKey);
            attributes.add(attr);
            return attr;
        }

        public Relationship addRelationship(String name, double x, double y, String type) {
            Relationship rel = new Relationship(name, type);
            rel.setPosition(x, y);
            relationships.add(rel);
            return rel;
        }

        public void connect(Entity entity, Relationship relationship, String cardinality) {
            Connection conn = new Connection(entity, relationship, cardinality);
            connections.add(conn);
        }

        public void linkAttribute(Entity entity, Attribute attr, double offsetX, double offsetY) {
            attr.setPosition(entity.getCenterX() + offsetX, entity.getCenterY() + offsetY);

            // 绘制属性连线
            JQuickLineShape line = JQuickShapeFactory.createLine(
                    entity.getCenterX(), entity.getCenterY(),
                    attr.getCenterX(), attr.getCenterY(), LINE_COLOR);
            line.setStroke(1.0f, Color.GRAY);
            document.addShape(line);
        }

        public void generate() {
            // 绘制所有实体
            for (Entity entity : entities) {
                entity.draw(document);
            }

            // 绘制所有关系
            for (Relationship rel : relationships) {
                rel.draw(document);
            }

            // 绘制所有连接
            for (Connection conn : connections) {
                conn.draw(document);
            }

            // 绘制所有属性
            for (Attribute attr : attributes) {
                attr.draw(document);
            }
        }

        public String getSVG() {
            return document.generateSVG();
        }

        public void save(String filename) {
            document.saveToFile(filename);
        }
    }
}
