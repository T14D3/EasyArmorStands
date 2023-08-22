package me.m56738.easyarmorstands.display.property.display;

import me.m56738.easyarmorstands.api.property.Property;
import me.m56738.easyarmorstands.api.property.type.PropertyType;
import me.m56738.easyarmorstands.display.api.property.type.DisplayPropertyTypes;
import org.bukkit.entity.Display;
import org.bukkit.entity.Display.Billboard;
import org.jetbrains.annotations.NotNull;

public class DisplayBillboardProperty implements Property<Billboard> {
    private final Display entity;

    public DisplayBillboardProperty(Display entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull PropertyType<Billboard> getType() {
        return DisplayPropertyTypes.BILLBOARD;
    }

    @Override
    public Billboard getValue() {
        return entity.getBillboard();
    }

    @Override
    public boolean setValue(Billboard value) {
        entity.setBillboard(value);
        return true;
    }
}