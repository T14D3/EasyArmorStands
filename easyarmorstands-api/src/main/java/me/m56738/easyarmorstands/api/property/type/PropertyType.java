package me.m56738.easyarmorstands.api.property.type;

import io.leangen.geantyref.TypeToken;
import me.m56738.easyarmorstands.api.menu.MenuSlot;
import me.m56738.easyarmorstands.api.property.Property;
import me.m56738.easyarmorstands.api.property.PropertyContainer;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public interface PropertyType<T> extends Keyed {
    static @NotNull TypeToken<PropertyType<?>> type() {
        return PropertyTypeTypeToken.INSTANCE;
    }

    @NotNull Class<T> getValueType();

    @Nullable String getPermission();

    /**
     * Display name of this property.
     *
     * @return Display name.
     */
    @NotNull Component getName();

    /**
     * Format a value for display in a chat message.
     *
     * @param value The value to format.
     * @return The formatted value.
     */
    @NotNull Component getValueComponent(T value);

    default void load(@NotNull CommentedConfigurationNode config) throws SerializationException {
    }

    default T cloneValue(T value) {
        return value;
    }

    default @Nullable MenuSlot createSlot(@NotNull Property<T> property, @NotNull PropertyContainer container) {
        return null;
    }
}
