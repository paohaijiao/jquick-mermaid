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

import com.github.paohaijiao.entity.domain.JQuickCommentConfig;
import com.github.paohaijiao.entity.domain.JQuickEntityDomain;
import com.github.paohaijiao.entity.domain.JQuickErDomainBuilder;
import com.github.paohaijiao.entity.domain.JQuickLayoutDomain;
import com.github.paohaijiao.enums.JQuickRelationType;

import java.awt.*;

/**
 * PowerDesigner风格ER图生成器 - 动态布局版
 * <p>
 * 特点：
 * 1. 支持离散度参数控制布局紧凑度
 * 2. 表备注与表名在同一行显示（PowerDesigner风格）
 * 3. 字段备注使用括号显示在同一行
 * 4. 动态计算最佳位置避免重叠
 * 5. 无标题，无横线，界面简洁
 *
 * @author Martin
 * @version 9.0.0
 * @since 2026/5/2
 */
public class JQuickErDiagramProvider {

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

    /**
     * 示例1：用户权限管理系统
     */
    public static void createUserManagementERDiagram() {
        JQuickErDomainBuilder builder = new JQuickErDomainBuilder();
        JQuickLayoutDomain layoutConfig = new JQuickLayoutDomain()
                .setHorizontalSpacing(380)
                .setVerticalSpacing(300)
                .setCenterBias(0.5)
                .setAutoAdjustSpacing(true);
        JQuickCommentConfig commentConfig = new JQuickCommentConfig()
                .setShowTableComment(true)
                .setShowFieldComment(true);
        builder.setLayoutConfig(layoutConfig);
        builder.setCommentConfig(commentConfig);
        // 用户表
        JQuickEntityDomain user = builder.createEntity("t_user", "用户信息表");
        user.addColumn(builder.createColumn("user_id", "BIGINT", "用户唯一标识", true, false));
        user.addColumn(builder.createColumn("username", "VARCHAR(50)", "登录用户名", false, false));
        user.addColumn(builder.createColumn("password", "VARCHAR(255)", "BCrypt加密密码", false, false));
        user.addColumn(builder.createColumn("email", "VARCHAR(100)", "电子邮箱", false, false));
        user.addColumn(builder.createColumn("phone", "VARCHAR(20)", "手机号码", false, false));
        user.addColumn(builder.createColumn("status", "TINYINT", "状态:0禁用,1启用", false, false));
        user.addColumn(builder.createColumn("create_time", "DATETIME", "创建时间", false, false));

        // 角色表
        JQuickEntityDomain role = builder.createEntity("t_role", "角色信息表");
        role.addColumn(builder.createColumn("role_id", "BIGINT", "角色唯一标识", true, false));
        role.addColumn(builder.createColumn("role_code", "VARCHAR(50)", "角色编码", false, false));
        role.addColumn(builder.createColumn("role_name", "VARCHAR(100)", "角色名称", false, false));
        role.addColumn(builder.createColumn("description", "VARCHAR(255)", "角色描述", false, false));

        // 权限表
        JQuickEntityDomain permission = builder.createEntity("t_permission", "权限信息表");
        permission.addColumn(builder.createColumn("perm_id", "BIGINT", "权限唯一标识", true, false));
        permission.addColumn(builder.createColumn("perm_code", "VARCHAR(100)", "权限编码", false, false));
        permission.addColumn(builder.createColumn("perm_name", "VARCHAR(100)", "权限名称", false, false));
        permission.addColumn(builder.createColumn("resource_url", "VARCHAR(255)", "资源路径", false, false));

        // 用户角色关联表
        JQuickEntityDomain userRole = builder.createEntity("t_user_role", "用户角色关联表");
        userRole.addColumn(builder.createColumn("id", "BIGINT", "关联唯一标识", true, false));
        userRole.addColumn(builder.createColumn("user_id", "BIGINT", "用户ID", false, true));
        userRole.addColumn(builder.createColumn("role_id", "BIGINT", "角色ID", false, true));

        // 角色权限关联表
        JQuickEntityDomain rolePerm = builder.createEntity("t_role_permission", "角色权限关联表");
        rolePerm.addColumn(builder.createColumn("id", "BIGINT", "关联唯一标识", true, false));
        rolePerm.addColumn(builder.createColumn("role_id", "BIGINT", "角色ID", false, true));
        rolePerm.addColumn(builder.createColumn("perm_id", "BIGINT", "权限ID", false, true));

        // 操作日志表
        JQuickEntityDomain opLog = builder.createEntity("t_operation_log", "操作日志表");
        opLog.addColumn(builder.createColumn("log_id", "BIGINT", "日志唯一标识", true, false));
        opLog.addColumn(builder.createColumn("user_id", "BIGINT", "操作用户ID", false, true));
        opLog.addColumn(builder.createColumn("operation", "VARCHAR(100)", "操作类型", false, false));
        opLog.addColumn(builder.createColumn("create_time", "DATETIME", "操作时间", false, false));

        // 创建关系
        builder.createRelationship("拥有", user, userRole, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("属于", role, userRole, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("分配", role, rolePerm, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("包含", permission, rolePerm, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("记录", user, opLog, JQuickRelationType.ONE_TO_MANY);

        builder.generateAndSave("d://test//er_diagram_user_permission.svg");
    }

    /**
     * 示例2：电商系统
     */
    public static void createEcommerceERDiagram() {
        System.out.println("\n创建电商系统ER图...");

        JQuickErDomainBuilder builder = new JQuickErDomainBuilder();

        JQuickLayoutDomain layoutConfig = new JQuickLayoutDomain()
                .setHorizontalSpacing(450)
                .setVerticalSpacing(350)
                .setCenterBias(0.4)
                .setAutoAdjustSpacing(true);

        JQuickCommentConfig commentConfig = new JQuickCommentConfig()
                .setShowTableComment(true)
                .setShowFieldComment(true);

        builder.setLayoutConfig(layoutConfig);
        builder.setCommentConfig(commentConfig);

        // 商品表
        JQuickEntityDomain product = builder.createEntity("t_product", "商品信息表");
        product.addColumn(builder.createColumn("product_id", "BIGINT", "商品唯一标识", true, false));
        product.addColumn(builder.createColumn("product_name", "VARCHAR(200)", "商品名称", false, false));
        product.addColumn(builder.createColumn("price", "DECIMAL(10,2)", "销售价格", false, false));
        product.addColumn(builder.createColumn("stock_qty", "INT", "库存数量", false, false));
        product.addColumn(builder.createColumn("sold_qty", "INT", "已售数量", false, false));

        // 客户表
        JQuickEntityDomain customer = builder.createEntity("t_customer", "客户信息表");
        customer.addColumn(builder.createColumn("cust_id", "BIGINT", "客户唯一标识", true, false));
        customer.addColumn(builder.createColumn("cust_name", "VARCHAR(50)", "客户姓名", false, false));
        customer.addColumn(builder.createColumn("phone", "VARCHAR(20)", "联系电话", false, false));
        customer.addColumn(builder.createColumn("points", "INT", "积分余额", false, false));

        // 订单表
        JQuickEntityDomain order = builder.createEntity("t_order", "订单主表");
        order.addColumn(builder.createColumn("order_id", "BIGINT", "订单唯一标识", true, false));
        order.addColumn(builder.createColumn("order_no", "VARCHAR(50)", "订单号", false, false));
        order.addColumn(builder.createColumn("cust_id", "BIGINT", "客户ID", false, true));
        order.addColumn(builder.createColumn("order_amount", "DECIMAL(12,2)", "订单总金额", false, false));
        order.addColumn(builder.createColumn("order_status", "VARCHAR(20)", "订单状态", false, false));

        // 订单明细表
        JQuickEntityDomain orderItem = builder.createEntity("t_order_item", "订单明细表");
        orderItem.addColumn(builder.createColumn("item_id", "BIGINT", "明细唯一标识", true, false));
        orderItem.addColumn(builder.createColumn("order_id", "BIGINT", "订单ID", false, true));
        orderItem.addColumn(builder.createColumn("product_id", "BIGINT", "商品ID", false, true));
        orderItem.addColumn(builder.createColumn("quantity", "INT", "购买数量", false, false));
        orderItem.addColumn(builder.createColumn("unit_price", "DECIMAL(10,2)", "单价快照", false, false));

        // 购物车表
        JQuickEntityDomain cart = builder.createEntity("t_shopping_cart", "购物车表");
        cart.addColumn(builder.createColumn("cart_id", "BIGINT", "购物车项唯一标识", true, false));
        cart.addColumn(builder.createColumn("cust_id", "BIGINT", "客户ID", false, true));
        cart.addColumn(builder.createColumn("product_id", "BIGINT", "商品ID", false, true));
        cart.addColumn(builder.createColumn("quantity", "INT", "数量", false, false));

        // 支付记录表
        JQuickEntityDomain payment = builder.createEntity("t_payment", "支付记录表");
        payment.addColumn(builder.createColumn("payment_id", "BIGINT", "支付记录唯一标识", true, false));
        payment.addColumn(builder.createColumn("order_id", "BIGINT", "订单ID", false, true));
        payment.addColumn(builder.createColumn("payment_amount", "DECIMAL(12,2)", "支付金额", false, false));
        payment.addColumn(builder.createColumn("payment_time", "DATETIME", "支付时间", false, false));

        // 创建关系
        builder.createRelationship("下单", customer, order, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("包含", order, orderItem, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("关联", product, orderItem, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("添加", customer, cart, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("存放", product, cart, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("支付", order, payment, JQuickRelationType.ONE_TO_ONE);

        builder.generateAndSave("d://test//er_diagram_ecommerce.svg");
    }

    /**
     * 示例3：博客系统
     */
    public static void createBlogERDiagram() {
        System.out.println("\n创建博客系统ER图...");
        JQuickErDomainBuilder builder = new JQuickErDomainBuilder();
        JQuickLayoutDomain layoutConfig = new JQuickLayoutDomain()
                .setHorizontalSpacing(400)
                .setVerticalSpacing(320)
                .setCenterBias(0.6)
                .setAutoAdjustSpacing(true);

        JQuickCommentConfig commentConfig = new JQuickCommentConfig()
                .setShowTableComment(true)
                .setShowFieldComment(true);

        builder.setLayoutConfig(layoutConfig);
        builder.setCommentConfig(commentConfig);

        // 文章表
        JQuickEntityDomain article = builder.createEntity("t_article", "文章信息表");
        article.addColumn(builder.createColumn("article_id", "BIGINT", "文章唯一标识", true, false));
        article.addColumn(builder.createColumn("title", "VARCHAR(200)", "文章标题", false, false));
        article.addColumn(builder.createColumn("content", "LONGTEXT", "文章内容", false, false));
        article.addColumn(builder.createColumn("author_id", "BIGINT", "作者ID", false, true));
        article.addColumn(builder.createColumn("view_count", "INT", "浏览次数", false, false));
        article.addColumn(builder.createColumn("like_count", "INT", "点赞次数", false, false));
        article.addColumn(builder.createColumn("create_time", "DATETIME", "创建时间", false, false));

        // 用户表
        JQuickEntityDomain user = builder.createEntity("t_blog_user", "博客用户表");
        user.addColumn(builder.createColumn("user_id", "BIGINT", "用户唯一标识", true, false));
        user.addColumn(builder.createColumn("nickname", "VARCHAR(50)", "用户昵称", false, false));
        user.addColumn(builder.createColumn("email", "VARCHAR(100)", "电子邮箱", false, false));
        user.addColumn(builder.createColumn("avatar", "VARCHAR(255)", "头像URL", false, false));
        user.addColumn(builder.createColumn("register_time", "DATETIME", "注册时间", false, false));

        // 分类表
        JQuickEntityDomain category = builder.createEntity("t_category", "文章分类表");
        category.addColumn(builder.createColumn("category_id", "INT", "分类唯一标识", true, false));
        category.addColumn(builder.createColumn("category_name", "VARCHAR(50)", "分类名称", false, false));
        category.addColumn(builder.createColumn("parent_id", "INT", "父分类ID", false, false));

        // 评论表
        JQuickEntityDomain comment = builder.createEntity("t_comment", "评论表");
        comment.addColumn(builder.createColumn("comment_id", "BIGINT", "评论唯一标识", true, false));
        comment.addColumn(builder.createColumn("article_id", "BIGINT", "文章ID", false, true));
        comment.addColumn(builder.createColumn("user_id", "BIGINT", "评论用户ID", false, true));
        comment.addColumn(builder.createColumn("content", "VARCHAR(500)", "评论内容", false, false));
        comment.addColumn(builder.createColumn("create_time", "DATETIME", "评论时间", false, false));

        // 标签表
        JQuickEntityDomain tag = builder.createEntity("t_tag", "标签表");
        tag.addColumn(builder.createColumn("tag_id", "INT", "标签唯一标识", true, false));
        tag.addColumn(builder.createColumn("tag_name", "VARCHAR(30)", "标签名称", false, false));

        // 文章标签关联表
        JQuickEntityDomain articleTag = builder.createEntity("t_article_tag", "文章标签关联表");
        articleTag.addColumn(builder.createColumn("id", "BIGINT", "关联唯一标识", true, false));
        articleTag.addColumn(builder.createColumn("article_id", "BIGINT", "文章ID", false, true));
        articleTag.addColumn(builder.createColumn("tag_id", "INT", "标签ID", false, true));

        // 创建关系
        builder.createRelationship("创作", user, article, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("属于", category, article, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("评论", article, comment, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("发表", user, comment, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("关联", article, articleTag, JQuickRelationType.ONE_TO_MANY);
        builder.createRelationship("使用", tag, articleTag, JQuickRelationType.ONE_TO_MANY);

        builder.generateAndSave("d://test//er_diagram_blog.svg");
    }

    public static void main(String[] args) {
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("d://test"));
        } catch (Exception e) {
            System.out.println("创建目录失败: " + e.getMessage());
        }

        System.out.println("=== PowerDesigner风格ER图生成器 ===");
        System.out.println("表备注格式: t_user (用户信息表)");

        createUserManagementERDiagram();
        createEcommerceERDiagram();
        createBlogERDiagram();

        System.out.println("\n所有ER图生成完成！");
        System.out.println("输出目录: d://test/");
        System.out.println("\n生成的文件:");
        System.out.println("  - er_diagram_user_permission.svg (用户权限系统)");
        System.out.println("  - er_diagram_ecommerce.svg (电商平台)");
        System.out.println("  - er_diagram_blog.svg (博客系统)");
    }



}