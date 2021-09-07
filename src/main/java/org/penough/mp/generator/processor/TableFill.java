package org.penough.mp.generator.processor;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.IFill;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class TableFill implements IFill {

    private String fieldName;
    private FieldFill fiedFill;
    @Override
    public @NotNull String getName() {
        return this.fieldName;
    }

    @Override
    public @NotNull FieldFill getFieldFill() {
        return this.fiedFill;
    }
}
