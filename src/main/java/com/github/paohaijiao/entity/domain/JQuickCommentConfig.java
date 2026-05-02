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
 * packageName com.github.paohaijiao.entity
 *
 * @author Martin
 * @version 1.0.0
 * @since 2026/5/2
 */

/**
 * 备注配置类
 */
public class JQuickCommentConfig {
    private boolean showTableComment = true;
    private boolean showFieldComment = true;

    public JQuickCommentConfig() {
    }

    public boolean isShowTableComment() {
        return showTableComment;
    }

    public JQuickCommentConfig setShowTableComment(boolean showTableComment) {
        this.showTableComment = showTableComment;
        return this;
    }

    public boolean isShowFieldComment() {
        return showFieldComment;
    }

    public JQuickCommentConfig setShowFieldComment(boolean showFieldComment) {
        this.showFieldComment = showFieldComment;
        return this;
    }
}