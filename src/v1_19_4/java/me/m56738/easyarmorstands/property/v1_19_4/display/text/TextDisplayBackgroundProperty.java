package me.m56738.easyarmorstands.property.v1_19_4.display.text;

import me.m56738.easyarmorstands.property.Property;
import me.m56738.easyarmorstands.property.type.PropertyType;
import me.m56738.easyarmorstands.property.v1_19_4.display.DisplayPropertyTypes;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation") // presence checked in isSupported
public class TextDisplayBackgroundProperty implements Property<@Nullable Color> {
    private final TextDisplay entity;

    public TextDisplayBackgroundProperty(TextDisplay entity) {
        this.entity = entity;
    }

    public static boolean isSupported() {
        try {
            TextDisplay.class.getMethod("getBackgroundColor");
            TextDisplay.class.getMethod("setBackgroundColor", Color.class);
            TextDisplay.class.getMethod("isDefaultBackground");
            TextDisplay.class.getMethod("setDefaultBackground", boolean.class);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public PropertyType<@Nullable Color> getType() {
        return DisplayPropertyTypes.TEXT_DISPLAY_BACKGROUND;
    }

    @Override
    public @Nullable Color getValue() {
        if (entity.isDefaultBackground()) {
            return null;
        }
        Color color = entity.getBackgroundColor();
        if (color == null) {
            color = Color.WHITE;
        }
        return color;
    }

    @Override
    public boolean setValue(@Nullable Color value) {
        if (value != null) {
            entity.setDefaultBackground(false);
            entity.setBackgroundColor(value);
        } else {
            entity.setDefaultBackground(true);
            entity.setBackgroundColor(null);
        }
        return true;
    }
}
