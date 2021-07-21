package org.penough.mp.generator.convertor;

import com.baomidou.mybatisplus.generator.config.INameConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.jetbrains.annotations.NotNull;

public class MyNameConvert implements INameConvert {
    @Override
    public @NotNull String entityNameConvert(@NotNull TableInfo tableInfo) {
        return null;
    }

    @Override
    public @NotNull String propertyNameConvert(@NotNull TableField field) {
        return null;
    }
}
