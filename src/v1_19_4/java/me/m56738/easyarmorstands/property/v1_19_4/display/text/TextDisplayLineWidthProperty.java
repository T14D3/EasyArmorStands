package me.m56738.easyarmorstands.property.v1_19_4.display.text;

import me.m56738.easyarmorstands.property.Property;
import me.m56738.easyarmorstands.property.type.PropertyType;
import me.m56738.easyarmorstands.property.v1_19_4.display.DisplayPropertyTypes;
import org.bukkit.entity.TextDisplay;

public class TextDisplayLineWidthProperty implements Property<Integer> {
    private final TextDisplay entity;

    public TextDisplayLineWidthProperty(TextDisplay entity) {
        this.entity = entity;
    }

    @Override
    public PropertyType<Integer> getType() {
        return DisplayPropertyTypes.TEXT_DISPLAY_LINE_WIDTH;
    }

    @Override
    public Integer getValue() {
        return entity.getLineWidth();
    }

    @Override
    public boolean setValue(Integer value) {
        entity.setLineWidth(value);
        return true;
    }
}
