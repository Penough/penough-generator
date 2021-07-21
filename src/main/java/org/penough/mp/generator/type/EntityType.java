package org.penough.mp.generator.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 父类实体类型
 */
@Getter
@AllArgsConstructor
public enum EntityType {
    /**
     * 只有id
     */
    SUPER_ENTITY("org.penough.boot.core.database.entity.SuperEntity", new String[]{"id", "create_time", "create_user","modify_time", "modify_user"}),

    /**
     * 不继承任何实体
     */
    NONE("", new String[]{""}),
    ;

    private String val;
    private String[] columns;


    public boolean eq(String val) {
        if (this.name().equals(val)) {
            return true;
        }
        return false;
    }

    public boolean eq(EntityType val) {
        if (val == null) {
            return false;
        }
        return eq(val.name());
    }

}
