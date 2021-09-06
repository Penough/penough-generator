package org.penough.mp.generator.processor;

import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;

/**
 * 表字段信息扩展
 */
public class MySqlQueryExt extends MySqlQuery {
    /**
     * 多查询一个Null，判断该Field是否可以为空
     * 该部分信息会被保存在field.customMap中
     * @return
     */
    @Override
    public String[] fieldCustom() {
        return new String[]{"Null"};
    }
}
