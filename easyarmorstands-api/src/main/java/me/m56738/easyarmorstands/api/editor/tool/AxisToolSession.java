package me.m56738.easyarmorstands.api.editor.tool;

import me.m56738.easyarmorstands.api.editor.Snapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface AxisToolSession extends ToolSession {
    void setChange(double change);

    @Contract(pure = true)
    double snapChange(double change, @NotNull Snapper context);

    default void setValue(double value) {
        setChange(value);
    }
}
