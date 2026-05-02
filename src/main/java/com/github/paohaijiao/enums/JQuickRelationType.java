package com.github.paohaijiao.enums;

/**
 * 关系类型
 */
public enum JQuickRelationType {
    ONE_TO_ONE, ONE_TO_MANY, MANY_TO_ONE, MANY_TO_MANY;

    public String getParentCardinality() {
        switch (this) {
            case ONE_TO_ONE:
                return "1";
            case ONE_TO_MANY:
                return "1";
            case MANY_TO_ONE:
                return "N";
            case MANY_TO_MANY:
                return "N";
            default:
                return "1";
        }
    }
    public String getChildCardinality() {
        switch (this) {
            case ONE_TO_ONE:
                return "1";
            case ONE_TO_MANY:
                return "N";
            case MANY_TO_ONE:
                return "1";
            case MANY_TO_MANY:
                return "M";
            default:
                return "N";
        }
    }

}
