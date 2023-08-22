package me.m56738.easyarmorstands.property.type;

import me.m56738.easyarmorstands.api.menu.MenuSlot;
import me.m56738.easyarmorstands.api.property.Property;
import me.m56738.easyarmorstands.api.property.PropertyContainer;
import me.m56738.easyarmorstands.property.button.EnumToggleButton;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnumTogglePropertyType<T extends Enum<T>> extends EnumPropertyType<T> {
    public EnumTogglePropertyType(@NotNull Key key, Class<T> type) {
        super(key, type);
    }

    @Override
    public @Nullable MenuSlot createSlot(Property<T> property, PropertyContainer container) {
        return new EnumToggleButton<>(property, container, buttonTemplate, values);
    }
}
