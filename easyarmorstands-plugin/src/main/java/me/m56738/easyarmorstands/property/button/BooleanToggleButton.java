package me.m56738.easyarmorstands.property.button;

import me.m56738.easyarmorstands.api.property.Property;
import me.m56738.easyarmorstands.api.property.PropertyContainer;
import me.m56738.easyarmorstands.item.SimpleItemTemplate;

public class BooleanToggleButton extends ToggleButton<Boolean> {
    public BooleanToggleButton(Property<Boolean> property, PropertyContainer container, SimpleItemTemplate item) {
        super(property, container, item);
    }

    @Override
    public Boolean getNextValue() {
        return !property.getValue();
    }

    @Override
    public Boolean getPreviousValue() {
        return getNextValue();
    }
}
