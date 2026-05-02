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
package com.github.paohaijiao.entity;

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

import com.github.paohaijiao.data.JQuickDrawableShape;
import com.github.paohaijiao.data.JQuickSVGDocument;
import com.github.paohaijiao.data.JQuickTextShape;
import org.apache.batik.svggen.SVGGraphics2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * PowerDesigner风格ER图生成器
 * 特点：
 * 1. 实体使用矩形+水平分割线（表结构风格）
 * 2. 属性显示在实体内部
 * 3. 主键使用PK标记或下划线
 * 4. 关系使用带基数标记的连线
 * 5. 支持一对多、多对多等关系的可视化
 *
 * @author Martin
 * @version 2.0.0
 * @since 2026/5/2
 */
public class ERDiagramGeneratorRender {

    // PowerDesigner 风格颜色
    private static final Color ENTITY_BG_COLOR = new Color(248, 248, 248);      // 实体内背景

    private static final Color ENTITY_HEADER_COLOR = new Color(66, 140, 200);    // 实体头部背景色（PowerDesigner蓝色）

    private static final Color ENTITY_BORDER_COLOR = new Color(100, 100, 100);   // 边框颜色

    private static final Color PRIMARY_KEY_COLOR = new Color(255, 0, 0);          // 主键文字颜色

    private static final Color FOREIGN_KEY_COLOR = new Color(0, 128, 0);          // 外键文字颜色

    private static final Color RELATIONSHIP_LINE_COLOR = new Color(0, 0, 255);    // 关系线颜色（蓝色）

    private static final Color CARDINALITY_COLOR = new Color(128, 0, 128);        // 基数颜色（紫色）

    private static final Color TEXT_COLOR = Color.BLACK;

    private static final Color DIVIDER_COLOR = new Color(180, 180, 180);          // 分割线颜色

    // 布局常量
    private static final double ENTITY_MIN_WIDTH = 180;

    private static final double ENTITY_HEADER_HEIGHT = 28;

    private static final double ATTR_ROW_HEIGHT = 22;

    private static final double HORIZONTAL_SPACING = 280;

    private static final double VERTICAL_SPACING = 200;

    /**
     * 创建用户管理ER图（PowerDesigner风格）
     */
    public static void createUserManagementERDiagram() {
        ERDiagramBuilder builder = new ERDiagramBuilder(1100, 700);
        // 创建实体
        Entity user = builder.createEntity("t_user", "用户表", 100, 200);
        user.addColumn(builder.createColumn("user_id", "BIGINT", true));
        user.addColumn(builder.createColumn("username", "VARCHAR(50)", false));
        user.addColumn(builder.createColumn("password", "VARCHAR(255)", false));
        user.addColumn(builder.createColumn("email", "VARCHAR(100)", false));
        user.addColumn(builder.createColumn("phone", "VARCHAR(20)", false));
        user.addColumn(builder.createColumn("status", "TINYINT", false));
        user.addColumn(builder.createColumn("create_time", "DATETIME", false));
        user.addColumn(builder.createColumn("update_time", "DATETIME", false));
        Entity role = builder.createEntity("t_role", "角色表", 100, 450);
        role.addColumn(builder.createColumn("role_id", "BIGINT", true));
        role.addColumn(builder.createColumn("role_code", "VARCHAR(50)", false));
        role.addColumn(builder.createColumn("role_name", "VARCHAR(100)", false));
        role.addColumn(builder.createColumn("description", "VARCHAR(255)", false));

        Entity permission = builder.createEntity("t_permission", "权限表", 700, 200);
        permission.addColumn(builder.createColumn("perm_id", "BIGINT", true));
        permission.addColumn(builder.createColumn("perm_code", "VARCHAR(100)", false));
        permission.addColumn(builder.createColumn("perm_name", "VARCHAR(100)", false));
        permission.addColumn(builder.createColumn("resource_type", "VARCHAR(20)", false));
        permission.addColumn(builder.createColumn("resource_url", "VARCHAR(255)", false));

        Entity userRole = builder.createEntity("t_user_role", "用户角色关联表", 450, 350);
        userRole.addColumn(builder.createColumn("id", "BIGINT", true));
        userRole.addColumn(builder.createColumn("user_id", "BIGINT", false, true));
        userRole.addColumn(builder.createColumn("role_id", "BIGINT", false, true));

        Entity rolePerm = builder.createEntity("t_role_permission", "角色权限关联表", 700, 450);
        rolePerm.addColumn(builder.createColumn("id", "BIGINT", true));
        rolePerm.addColumn(builder.createColumn("role_id", "BIGINT", false, true));
        rolePerm.addColumn(builder.createColumn("perm_id", "BIGINT", false, true));

        // 创建关系
        Relationship userToUserRole = builder.createRelationship("用户分配角色", user, userRole, RelationType.ONE_TO_MANY);
        Relationship roleToUserRole = builder.createRelationship("角色关联用户", role, userRole, RelationType.ONE_TO_MANY);
        Relationship roleToRolePerm = builder.createRelationship("角色分配权限", role, rolePerm, RelationType.ONE_TO_MANY);
        Relationship permToRolePerm = builder.createRelationship("权限关联角色", permission, rolePerm, RelationType.ONE_TO_MANY);

        builder.generate();
        builder.save("d://test//er_diagram_powerdesigner_user.svg");
        System.out.println("PowerDesigner风格ER图已保存到: d://test//er_diagram_powerdesigner_user.svg");
    }

    /**
     * 创建订单管理系统ER图（PowerDesigner风格）
     */
    public static void createOrderManagementERDiagram() {
        System.out.println("创建订单管理系统ER图（PowerDesigner风格）...");

        ERDiagramBuilder builder = new ERDiagramBuilder(1200, 800);

        // 客户实体
        Entity customer = builder.createEntity("t_customer", "客户表", 80, 300);
        customer.addColumn(builder.createColumn("cust_id", "BIGINT", true));
        customer.addColumn(builder.createColumn("cust_name", "VARCHAR(50)", false));
        customer.addColumn(builder.createColumn("cust_level", "VARCHAR(20)", false));
        customer.addColumn(builder.createColumn("contact_phone", "VARCHAR(20)", false));
        customer.addColumn(builder.createColumn("contact_address", "VARCHAR(255)", false));
        customer.addColumn(builder.createColumn("credit_limit", "DECIMAL(10,2)", false));

        // 订单实体
        Entity order = builder.createEntity("t_order", "订单表", 480, 100);
        order.addColumn(builder.createColumn("order_id", "BIGINT", true));
        order.addColumn(builder.createColumn("cust_id", "BIGINT", false, true));
        order.addColumn(builder.createColumn("order_no", "VARCHAR(50)", false));
        order.addColumn(builder.createColumn("order_date", "DATETIME", false));
        order.addColumn(builder.createColumn("order_status", "VARCHAR(20)", false));
        order.addColumn(builder.createColumn("total_amount", "DECIMAL(12,2)", false));
        order.addColumn(builder.createColumn("shipping_address", "VARCHAR(255)", false));

        // 订单明细实体
        Entity orderItem = builder.createEntity("t_order_item", "订单明细表", 480, 450);
        orderItem.addColumn(builder.createColumn("item_id", "BIGINT", true));
        orderItem.addColumn(builder.createColumn("order_id", "BIGINT", false, true));
        orderItem.addColumn(builder.createColumn("product_id", "BIGINT", false, true));
        orderItem.addColumn(builder.createColumn("quantity", "INT", false));
        orderItem.addColumn(builder.createColumn("unit_price", "DECIMAL(10,2)", false));
        orderItem.addColumn(builder.createColumn("subtotal", "DECIMAL(12,2)", false));

        // 商品实体
        Entity product = builder.createEntity("t_product", "商品表", 850, 300);
        product.addColumn(builder.createColumn("product_id", "BIGINT", true));
        product.addColumn(builder.createColumn("product_code", "VARCHAR(50)", false));
        product.addColumn(builder.createColumn("product_name", "VARCHAR(200)", false));
        product.addColumn(builder.createColumn("category_id", "INT", false));
        product.addColumn(builder.createColumn("price", "DECIMAL(10,2)", false));
        product.addColumn(builder.createColumn("stock_qty", "INT", false));

        // 支付实体
        Entity payment = builder.createEntity("t_payment", "支付表", 850, 550);
        payment.addColumn(builder.createColumn("payment_id", "BIGINT", true));
        payment.addColumn(builder.createColumn("order_id", "BIGINT", false, true));
        payment.addColumn(builder.createColumn("payment_method", "VARCHAR(20)", false));
        payment.addColumn(builder.createColumn("payment_amount", "DECIMAL(12,2)", false));
        payment.addColumn(builder.createColumn("payment_time", "DATETIME", false));
        payment.addColumn(builder.createColumn("payment_status", "VARCHAR(20)", false));

        // 创建关系
        Relationship custToOrder = builder.createRelationship("客户下单", customer, order, RelationType.ONE_TO_MANY);
        Relationship orderToItem = builder.createRelationship("订单包含明细", order, orderItem, RelationType.ONE_TO_MANY);
        Relationship productToItem = builder.createRelationship("商品关联明细", product, orderItem, RelationType.ONE_TO_MANY);
        Relationship orderToPayment = builder.createRelationship("订单支付", order, payment, RelationType.ONE_TO_ONE);

        builder.generate();
        builder.save("d://test//er_diagram_powerdesigner_order.svg");
        System.out.println("PowerDesigner风格订单ER图已保存到: d://test//er_diagram_powerdesigner_order.svg");
    }

    /**
     * 创建博客系统ER图（PowerDesigner风格）
     */
    public static void createBlogSystemERDiagram() {
        ERDiagramBuilder builder = new ERDiagramBuilder(1100, 750);
        Entity article = builder.createEntity("t_article", "文章表", 100, 200);
        article.addColumn(builder.createColumn("article_id", "BIGINT", true));
        article.addColumn(builder.createColumn("title", "VARCHAR(200)", false));
        article.addColumn(builder.createColumn("content", "TEXT", false));
        article.addColumn(builder.createColumn("author_id", "BIGINT", false, true));
        article.addColumn(builder.createColumn("category_id", "INT", false, true));
        article.addColumn(builder.createColumn("view_count", "INT", false));
        article.addColumn(builder.createColumn("like_count", "INT", false));
        article.addColumn(builder.createColumn("create_time", "DATETIME", false));

        // 用户实体
        Entity userBlog = builder.createEntity("t_blog_user", "博客用户表", 100, 500);
        userBlog.addColumn(builder.createColumn("user_id", "BIGINT", true));
        userBlog.addColumn(builder.createColumn("nickname", "VARCHAR(50)", false));
        userBlog.addColumn(builder.createColumn("avatar", "VARCHAR(255)", false));
        userBlog.addColumn(builder.createColumn("signature", "VARCHAR(255)", false));
        userBlog.addColumn(builder.createColumn("register_time", "DATETIME", false));

        // 分类实体
        Entity category = builder.createEntity("t_category", "分类表", 500, 500);
        category.addColumn(builder.createColumn("category_id", "INT", true));
        category.addColumn(builder.createColumn("category_name", "VARCHAR(50)", false));
        category.addColumn(builder.createColumn("parent_id", "INT", false));
        category.addColumn(builder.createColumn("sort_order", "INT", false));

        // 评论实体
        Entity comment = builder.createEntity("t_comment", "评论表", 500, 200);
        comment.addColumn(builder.createColumn("comment_id", "BIGINT", true));
        comment.addColumn(builder.createColumn("article_id", "BIGINT", false, true));
        comment.addColumn(builder.createColumn("user_id", "BIGINT", false, true));
        comment.addColumn(builder.createColumn("parent_id", "BIGINT", false));
        comment.addColumn(builder.createColumn("content", "VARCHAR(500)", false));
        comment.addColumn(builder.createColumn("create_time", "DATETIME", false));

        // 标签实体
        Entity tag = builder.createEntity("t_tag", "标签表", 850, 500);
        tag.addColumn(builder.createColumn("tag_id", "INT", true));
        tag.addColumn(builder.createColumn("tag_name", "VARCHAR(30)", false));
        tag.addColumn(builder.createColumn("use_count", "INT", false));

        // 文章标签关联实体
        Entity articleTag = builder.createEntity("t_article_tag", "文章标签关联表", 850, 200);
        articleTag.addColumn(builder.createColumn("id", "BIGINT", true));
        articleTag.addColumn(builder.createColumn("article_id", "BIGINT", false, true));
        articleTag.addColumn(builder.createColumn("tag_id", "INT", false, true));

        // 创建关系
        Relationship userToArticle = builder.createRelationship("用户创作", userBlog, article, RelationType.ONE_TO_MANY);
        Relationship categoryToArticle = builder.createRelationship("分类包含", category, article, RelationType.ONE_TO_MANY);
        Relationship articleToComment = builder.createRelationship("文章评论", article, comment, RelationType.ONE_TO_MANY);
        Relationship userToComment = builder.createRelationship("用户评论", userBlog, comment, RelationType.ONE_TO_MANY);
        Relationship articleToTag = builder.createRelationship("标签关联", article, articleTag, RelationType.ONE_TO_MANY);
        Relationship tagToArticle = builder.createRelationship("文章关联", tag, articleTag, RelationType.ONE_TO_MANY);

        builder.generate();
        builder.save("d://test//er_diagram_powerdesigner_blog.svg");
        System.out.println("PowerDesigner风格博客ER图已保存到: d://test//er_diagram_powerdesigner_blog.svg");
    }

    public static void main(String[] args) {
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("d://test"));
        } catch (Exception e) {
            System.out.println("创建目录失败: " + e.getMessage());
        }

        System.out.println("=== PowerDesigner风格ER图生成器 ===");
        System.out.println();

        createUserManagementERDiagram();
        System.out.println();

        createOrderManagementERDiagram();
        System.out.println();

        createBlogSystemERDiagram();
        System.out.println();

        System.out.println("所有ER图生成完成！");
        System.out.println("输出目录: d://test/");
        System.out.println("生成的文件:");
        System.out.println("  - er_diagram_powerdesigner_user.svg (用户权限管理)");
        System.out.println("  - er_diagram_powerdesigner_order.svg (订单管理系统)");
        System.out.println("  - er_diagram_powerdesigner_blog.svg (博客系统)");
    }

    /**
     * 关系类型
     */
    public enum RelationType {
        ONE_TO_ONE("1:1"),
        ONE_TO_MANY("1:N"),
        MANY_TO_ONE("N:1"),
        MANY_TO_MANY("N:M");

        private String symbol;

        RelationType(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    /**
     * 列/属性定义（PowerDesigner风格）
     */
    public static class Column {
        private String name;
        private String type;
        private boolean isPrimaryKey;
        private boolean isForeignKey;
        private boolean isNullable;
        private String defaultValue;

        public Column(String name, String type, boolean isPrimaryKey) {
            this(name, type, isPrimaryKey, false);
        }

        public Column(String name, String type, boolean isPrimaryKey, boolean isForeignKey) {
            this.name = name;
            this.type = type;
            this.isPrimaryKey = isPrimaryKey;
            this.isForeignKey = isForeignKey;
            this.isNullable = !isPrimaryKey;
        }

        public void setNullable(boolean nullable) {
            isNullable = nullable;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isPrimaryKey() {
            return isPrimaryKey;
        }

        public boolean isForeignKey() {
            return isForeignKey;
        }

        public String getDisplayText() {
            StringBuilder sb = new StringBuilder();
            if (isPrimaryKey) sb.append("🔑 ");
            if (isForeignKey && !isPrimaryKey) sb.append("🔗 ");
            sb.append(name).append(" : ").append(type);
            if (isNullable) sb.append(" (可空)");
            return sb.toString();
        }

        public Color getTextColor() {
            if (isPrimaryKey) return PRIMARY_KEY_COLOR;
            if (isForeignKey) return FOREIGN_KEY_COLOR;
            return TEXT_COLOR;
        }
    }

    /**
     * 实体类 - PowerDesigner风格，包含属性和方法
     */
    public static class Entity {
        private String name;
        private String comment;
        private double x, y;
        private double width = ENTITY_MIN_WIDTH;
        private double height = ENTITY_HEADER_HEIGHT;
        private List<Column> columns = new ArrayList<>();
        private List<Relationship> outgoingRelations = new ArrayList<>();
        private List<Relationship> incomingRelations = new ArrayList<>();

        public Entity(String name, double x, double y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public Entity(String name, String comment, double x, double y) {
            this(name, x, y);
            this.comment = comment;
        }

        public void addColumn(Column column) {
            columns.add(column);
            updateHeight();
        }

        public void addColumn(int index, Column column) {
            columns.add(index, column);
            updateHeight();
        }

        private void updateHeight() {
            this.height = ENTITY_HEADER_HEIGHT + columns.size() * ATTR_ROW_HEIGHT + 2;
        }

        public void addOutgoingRelation(Relationship rel) {
            outgoingRelations.add(rel);
        }

        public void addIncomingRelation(Relationship rel) {
            incomingRelations.add(rel);
        }

        public void draw(JQuickSVGDocument doc) {
            RoundRectangle2D roundedRect = new RoundRectangle2D.Double(x, y, width, height, 8, 8);
            drawRoundedRect(doc, roundedRect);
            Rectangle2D headerRect = new Rectangle2D.Double(x, y, width, ENTITY_HEADER_HEIGHT);
            drawHeaderBackground(doc, headerRect);

            drawEntityName(doc);

            drawDivider(doc, y + ENTITY_HEADER_HEIGHT);

            drawColumns(doc);

            if (comment != null && !comment.isEmpty()) {
                drawComment(doc);
            }
        }

        private void drawRoundedRect(JQuickSVGDocument doc, RoundRectangle2D roundedRect) {
            RoundedRectShape background = new RoundedRectShape(x, y, width, height, 8, 8, ENTITY_BG_COLOR, true);
            background.setStroke(1.5f, ENTITY_BORDER_COLOR);
            doc.addShape(background);
        }

        private void drawHeaderBackground(JQuickSVGDocument doc, Rectangle2D headerRect) {
            RoundedRectShape header = new RoundedRectShape(x, y, width, ENTITY_HEADER_HEIGHT, 8, 8, ENTITY_HEADER_COLOR, true);
            header.setStroke(0, null);
            doc.addShape(header);
            Rectangle2D headerFix = new Rectangle2D.Double(x, y + 4, width, ENTITY_HEADER_HEIGHT - 4);
            RectShape fixShape = new RectShape(x, y + 4, width, ENTITY_HEADER_HEIGHT - 4, ENTITY_HEADER_COLOR, true);
            fixShape.setStroke(0, null);
            doc.addShape(fixShape);
        }

        private void drawEntityName(JQuickSVGDocument doc) {
            Font boldFont = new Font("Microsoft YaHei", Font.BOLD, 13);
            JQuickTextShape nameText = new JQuickTextShape(x + width / 2, y + ENTITY_HEADER_HEIGHT / 2 + 4, name, Color.WHITE, 13);
            nameText.setFont(boldFont);
            doc.addShape(nameText);
        }

        private void drawDivider(JQuickSVGDocument doc, double yPos) {
            Line2D line = new Line2D.Double(x, yPos, x + width, yPos);
            LineShape divider = new LineShape(x, yPos, x + width, yPos, DIVIDER_COLOR, 1.0f);
            doc.addShape(divider);
        }

        private void drawColumns(JQuickSVGDocument doc) {
            Font plainFont = new Font("Consolas", Font.PLAIN, 11);
            double textY = y + ENTITY_HEADER_HEIGHT + 15;

            for (Column column : columns) {
                JQuickTextShape columnText = new JQuickTextShape(x + 10, textY, column.getDisplayText(), column.getTextColor(), 11);
                columnText.setFont(plainFont);
                doc.addShape(columnText);
                textY += ATTR_ROW_HEIGHT;
            }
        }

        private void drawComment(JQuickSVGDocument doc) {
            Font italicFont = new Font("Microsoft YaHei", Font.ITALIC, 9);
            JQuickTextShape commentText = new JQuickTextShape(x + 5, y + height + 12, "// " + comment, new Color(128, 128, 128), 9);
            commentText.setFont(italicFont);
            doc.addShape(commentText);
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

        public List<Column> getColumns() {
            return columns;
        }

        public Column getPrimaryKey() {
            return columns.stream().filter(Column::isPrimaryKey).findFirst().orElse(null);
        }
    }

    /**
     * 关系类 - PowerDesigner风格
     */
    public static class Relationship {

        private String name;
        private Entity parentEntity;      // 主实体（一的一侧）
        private Entity childEntity;       // 子实体（多的一侧）
        private RelationType type;
        private String parentCardinality;  // 父端基数（如 0..1, 1, 1..N）
        private String childCardinality;   // 子端基数
        private List<Column> foreignKeys;  // 外键列

        public Relationship(String name, Entity parent, Entity child, RelationType type) {
            this.name = name;
            this.parentEntity = parent;
            this.childEntity = child;
            this.type = type;
            this.foreignKeys = new ArrayList<>();

            // 设置默认基数
            switch (type) {
                case ONE_TO_ONE:
                    parentCardinality = "1";
                    childCardinality = "1";
                    break;
                case ONE_TO_MANY:
                    parentCardinality = "1";
                    childCardinality = "N";
                    break;
                case MANY_TO_ONE:
                    parentCardinality = "N";
                    childCardinality = "1";
                    break;
                case MANY_TO_MANY:
                    parentCardinality = "N";
                    childCardinality = "M";
                    break;
            }

            parent.addOutgoingRelation(this);
            child.addIncomingRelation(this);
        }

        public void setCardinality(String parentCard, String childCard) {
            this.parentCardinality = parentCard;
            this.childCardinality = childCard;
        }

        public void addForeignKey(Column column) {
            column.isForeignKey = true;
            foreignKeys.add(column);
        }

        public void draw(JQuickSVGDocument doc) {
            Point2D.Double startPoint = getConnectionPoint(parentEntity, childEntity.getCenterX(), childEntity.getCenterY());
            Point2D.Double endPoint = getConnectionPoint(childEntity, startPoint.x, startPoint.y);

            // 绘制关系线
            RelationshipLine line = new RelationshipLine(startPoint.x, startPoint.y,
                    endPoint.x, endPoint.y, RELATIONSHIP_LINE_COLOR, 1.5f, type);
            doc.addShape(line);

            // 绘制基数标记
            drawCardinality(doc, startPoint, endPoint);

            // 绘制关系名称（可选，放在连线中间）
            if (name != null && !name.isEmpty()) {
                drawRelationName(doc, startPoint, endPoint);
            }
        }

        private Point2D.Double getConnectionPoint(Entity entity, double targetX, double targetY) {
            double dx = targetX - entity.getCenterX();
            double dy = targetY - entity.getCenterY();

            if (Math.abs(dx) < 0.001) dx = 0.001;
            double slope = dy / dx;

            double intersectX, intersectY;
            double halfW = entity.getWidth() / 2;
            double halfH = entity.getHeight() / 2;

            // 计算与矩形边界的交点
            if (Math.abs(slope) <= halfH / halfW) {
                if (dx > 0) {
                    intersectX = entity.getRightX();
                } else {
                    intersectX = entity.getLeftX();
                }
                intersectY = entity.getCenterY() + slope * (intersectX - entity.getCenterX());
            } else {
                if (dy > 0) {
                    intersectY = entity.getBottomY();
                } else {
                    intersectY = entity.getTopY();
                }
                intersectX = entity.getCenterX() + (intersectY - entity.getCenterY()) / slope;
            }

            return new Point2D.Double(intersectX, intersectY);
        }

        private void drawCardinality(JQuickSVGDocument doc, Point2D.Double start, Point2D.Double end) {
            double totalDist = Math.hypot(end.x - start.x, end.y - start.y);
            if (totalDist < 0.1) return;

            // 父端基数（靠近父实体，距离20px）
            double startRatio = 25.0 / totalDist;
            double parentCardX = start.x + (end.x - start.x) * startRatio;
            double parentCardY = start.y + (end.y - start.y) * startRatio;

            // 子端基数（靠近子实体）
            double endRatio = 1.0 - 25.0 / totalDist;
            double childCardX = start.x + (end.x - start.x) * endRatio;
            double childCardY = start.y + (end.y - start.y) * endRatio;

            // 绘制基数背景
            drawCardinalityMarker(doc, parentCardX, parentCardY, parentCardinality, true);
            drawCardinalityMarker(doc, childCardX, childCardY, childCardinality, false);
        }

        private void drawCardinalityMarker(JQuickSVGDocument doc, double x, double y, String cardinality, boolean isParent) {
            // 偏移避免与线重叠
            double angle = Math.atan2(y, x);
            double offsetX = Math.cos(angle + Math.PI / 2) * 8;
            double offsetY = Math.sin(angle + Math.PI / 2) * 8;

            double markerX = x + offsetX;
            double markerY = y + offsetY;

            // 白色背景框
            RectShape bg = new RectShape(markerX - 12, markerY - 8, 24, 16, Color.WHITE, true);
            bg.setStroke(0.5f, new Color(200, 200, 200));
            doc.addShape(bg);

            Font boldFont = new Font("Microsoft YaHei", Font.BOLD, 11);
            JQuickTextShape cardText = new JQuickTextShape(markerX, markerY + 4, cardinality, CARDINALITY_COLOR, 11);
            cardText.setFont(boldFont);
            doc.addShape(cardText);
        }

        private void drawRelationName(JQuickSVGDocument doc, Point2D.Double start, Point2D.Double end) {
            double midX = (start.x + end.x) / 2;
            double midY = (start.y + end.y) / 2;

            // 偏移避免与线重叠
            double angle = Math.atan2(end.y - start.y, end.x - start.x);
            double perpX = Math.cos(angle + Math.PI / 2) * 12;
            double perpY = Math.sin(angle + Math.PI / 2) * 12;

            RectShape bg = new RectShape(midX + perpX - 30, midY + perpY - 10, 60, 20,
                    new Color(255, 255, 200, 220), true);
            bg.setStroke(0.5f, new Color(180, 180, 180));
            doc.addShape(bg);

            JQuickTextShape relName = new JQuickTextShape(midX + perpX, midY + perpY + 4, name, TEXT_COLOR, 10);
            doc.addShape(relName);
        }

        public Entity getParentEntity() {
            return parentEntity;
        }

        public Entity getChildEntity() {
            return childEntity;
        }

        public RelationType getType() {
            return type;
        }
    }

    /**
     * 圆角矩形形状类
     */
    public static class RoundedRectShape implements JQuickDrawableShape {
        private double x, y, width, height, arcW, arcH;
        private Color color;
        private boolean filled;
        private float strokeWidth = 1.0f;
        private Color strokeColor = Color.BLACK;

        public RoundedRectShape(double x, double y, double width, double height, double arcW, double arcH,
                                Color color, boolean filled) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.arcW = arcW;
            this.arcH = arcH;
            this.color = color;
            this.filled = filled;
        }

        public void setStroke(float width, Color color) {
            this.strokeWidth = width;
            this.strokeColor = color;
        }

        @Override
        public void draw(SVGGraphics2D svgGenerator) {
            RoundRectangle2D rect = new RoundRectangle2D.Double(x, y, width, height, arcW, arcH);
            if (filled) {
                svgGenerator.setColor(color);
                svgGenerator.fill(rect);
            }
            if (strokeColor != null && strokeWidth > 0) {
                svgGenerator.setColor(strokeColor);
                svgGenerator.setStroke(new BasicStroke(strokeWidth));
                svgGenerator.draw(rect);
            }
        }

        @Override
        public void setPosition(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Rectangle2D getBounds() {
            return new Rectangle2D.Double(x, y, width, height);
        }
    }

    /**
     * 矩形形状类
     */
    public static class RectShape implements JQuickDrawableShape {
        private double x, y, width, height;
        private Color color;
        private boolean filled;
        private float strokeWidth = 1.0f;
        private Color strokeColor = Color.BLACK;

        public RectShape(double x, double y, double width, double height, Color color, boolean filled) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
            this.filled = filled;
        }

        public void setStroke(float width, Color color) {
            this.strokeWidth = width;
            this.strokeColor = color;
        }

        @Override
        public void draw(SVGGraphics2D svgGenerator) {
            Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
            if (filled) {
                svgGenerator.setColor(color);
                svgGenerator.fill(rect);
            }
            if (strokeColor != null && strokeWidth > 0) {
                svgGenerator.setColor(strokeColor);
                svgGenerator.setStroke(new BasicStroke(strokeWidth));
                svgGenerator.draw(rect);
            }
        }

        @Override
        public void setPosition(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Rectangle2D getBounds() {
            return new Rectangle2D.Double(x, y, width, height);
        }
    }

    /**
     * 线形状类
     */
    public static class LineShape implements JQuickDrawableShape {
        private double x1, y1, x2, y2;
        private Color color;
        private float strokeWidth;

        public LineShape(double x1, double y1, double x2, double y2, Color color, float strokeWidth) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.strokeWidth = strokeWidth;
        }

        @Override
        public void draw(SVGGraphics2D svgGenerator) {
            svgGenerator.setColor(color);
            svgGenerator.setStroke(new BasicStroke(strokeWidth));
            svgGenerator.draw(new Line2D.Double(x1, y1, x2, y2));
        }

        @Override
        public void setPosition(double x, double y) {
            double dx = x - x1;
            double dy = y - y1;
            x1 += dx;
            y1 += dy;
            x2 += dx;
            y2 += dy;
        }

        @Override
        public Rectangle2D getBounds() {
            return new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x2 - x1), Math.abs(y2 - y1));
        }
    }

    /**
     * 关系线形状 - 支持箭头和标记
     */
    public static class RelationshipLine implements JQuickDrawableShape {
        private double x1, y1, x2, y2;
        private Color color;
        private float strokeWidth;
        private RelationType type;

        public RelationshipLine(double x1, double y1, double x2, double y2,
                                Color color, float strokeWidth, RelationType type) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.strokeWidth = strokeWidth;
            this.type = type;
        }

        @Override
        public void draw(SVGGraphics2D svgGenerator) {
            // 绘制主线
            svgGenerator.setColor(color);
            svgGenerator.setStroke(new BasicStroke(strokeWidth));
            svgGenerator.draw(new Line2D.Double(x1, y1, x2, y2));

            // 根据关系类型添加末端标记
            drawEndMarker(svgGenerator);

            // 对于多对多，添加双向标记
            if (type == RelationType.MANY_TO_MANY) {
                drawStartMarker(svgGenerator);
            }
        }

        private void drawEndMarker(SVGGraphics2D svgGenerator) {
            double angle = Math.atan2(y2 - y1, x2 - x1);
            double arrowSize = 10;

            double arrowX = x2;
            double arrowY = y2;

            double leftX = arrowX - arrowSize * Math.cos(angle - Math.PI / 6);
            double leftY = arrowY - arrowSize * Math.sin(angle - Math.PI / 6);
            double rightX = arrowX - arrowSize * Math.cos(angle + Math.PI / 6);
            double rightY = arrowY - arrowSize * Math.sin(angle + Math.PI / 6);

            if (type == RelationType.ONE_TO_MANY || type == RelationType.MANY_TO_MANY) {
                // 多端使用鱼尾纹标记或箭头
                int[] xPoints = {(int) arrowX, (int) leftX, (int) rightX};
                int[] yPoints = {(int) arrowY, (int) leftY, (int) rightY};
                Polygon arrow = new Polygon(xPoints, yPoints, 3);
                svgGenerator.setColor(color);
                svgGenerator.fill(arrow);
            } else if (type == RelationType.ONE_TO_ONE) {
                // 如果画的是1:1，使用实心圆点标记
                svgGenerator.fillOval((int) (arrowX - 4), (int) (arrowY - 4), 8, 8);
            }
        }

        private void drawStartMarker(SVGGraphics2D svgGenerator) {
            double angle = Math.atan2(y1 - y2, x1 - x2);
            double arrowSize = 10;

            double arrowX = x1;
            double arrowY = y1;

            double leftX = arrowX - arrowSize * Math.cos(angle - Math.PI / 6);
            double leftY = arrowY - arrowSize * Math.sin(angle - Math.PI / 6);
            double rightX = arrowX - arrowSize * Math.cos(angle + Math.PI / 6);
            double rightY = arrowY - arrowSize * Math.sin(angle + Math.PI / 6);

            int[] xPoints = {(int) arrowX, (int) leftX, (int) rightX};
            int[] yPoints = {(int) arrowY, (int) leftY, (int) rightY};
            Polygon arrow = new Polygon(xPoints, yPoints, 3);
            svgGenerator.setColor(color);
            svgGenerator.fill(arrow);
        }

        @Override
        public void setPosition(double x, double y) {
            double dx = x - x1;
            double dy = y - y1;
            x1 += dx;
            y1 += dy;
            x2 += dx;
            y2 += dy;
        }

        @Override
        public Rectangle2D getBounds() {
            return new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2),
                    Math.abs(x2 - x1), Math.abs(y2 - y1));
        }
    }

    /**
     * ER图构建器 - PowerDesigner风格
     */
    public static class ERDiagramBuilder {
        private JQuickSVGDocument document;
        private Map<String, Entity> entities = new LinkedHashMap<>();
        private List<Relationship> relationships = new ArrayList<>();
        private int width;
        private int height;

        public ERDiagramBuilder(int width, int height) {
            this.width = width;
            this.height = height;
            this.document = new JQuickSVGDocument(width, height);
            this.document.setBackgroundColor(new Color(250, 250, 250));
            this.document.setTitle("PowerDesigner Style ER Diagram");
        }

        public Entity createEntity(String name, double x, double y) {
            return createEntity(name, null, x, y);
        }

        public Entity createEntity(String name, String comment, double x, double y) {
            Entity entity = new Entity(name, comment, x, y);
            entities.put(name, entity);
            return entity;
        }

        public Column createColumn(String name, String type, boolean isPrimaryKey) {
            return new Column(name, type, isPrimaryKey);
        }

        public Column createColumn(String name, String type, boolean isPrimaryKey, boolean isForeignKey) {
            return new Column(name, type, isPrimaryKey, isForeignKey);
        }

        public Relationship createRelationship(String name, Entity parent, Entity child, RelationType type) {
            Relationship rel = new Relationship(name, parent, child, type);
            relationships.add(rel);
            return rel;
        }

        public void generate() {
            // 绘制所有实体
            for (Entity entity : entities.values()) {
                entity.draw(document);
            }

            // 绘制所有关系（先画线，确保在实体下方）
            for (Relationship rel : relationships) {
                rel.draw(document);
            }

            // 添加标题
            addTitle();

            // 添加图例
            addLegend();
        }

        private void addTitle() {
            Font boldFont = new Font("Microsoft YaHei", Font.BOLD, 18);
            JQuickTextShape title = new JQuickTextShape(width / 2, 35, "Physical Data Model",
                    new Color(66, 140, 200), 18);
            title.setFont(boldFont);
            document.addShape(title);

            LineShape underline = new LineShape(width / 2 - 150, 45, width / 2 + 150, 45,
                    new Color(66, 140, 200), 2.0f);
            document.addShape(underline);
        }

        private void addLegend() {
            int legendX = width - 220;
            int legendY = 20;

            // 背景
            RectShape bg = new RectShape(legendX - 5, legendY - 5, 210, 95,
                    new Color(255, 255, 255, 230), true);
            bg.setStroke(1.0f, new Color(200, 200, 200));
            document.addShape(bg);

            Font legendFont = new Font("Microsoft YaHei", Font.BOLD, 11);
            JQuickTextShape legendTitle = new JQuickTextShape(legendX + 60, legendY + 15,
                    "图例", TEXT_COLOR, 11);
            legendTitle.setFont(legendFont);
            document.addShape(legendTitle);

            int y = legendY + 30;

            // 主键
            JQuickTextShape pkLegend = new JQuickTextShape(legendX + 20, y,
                    "🔑 主键 (Primary Key)", PRIMARY_KEY_COLOR, 10);
            document.addShape(pkLegend);
            y += 18;

            // 外键
            JQuickTextShape fkLegend = new JQuickTextShape(legendX + 20, y,
                    "🔗 外键 (Foreign Key)", FOREIGN_KEY_COLOR, 10);
            document.addShape(fkLegend);
            y += 18;

            // 关系线
            JQuickTextShape lineLegend = new JQuickTextShape(legendX + 20, y,
                    "── 关系线 (Relationship)", RELATIONSHIP_LINE_COLOR, 10);
            document.addShape(lineLegend);
            y += 18;
        }

        public void save(String filename) {
            document.saveToFile(filename);
        }
    }
}
