package me.m56738.easyarmorstands.display.element;

import me.m56738.easyarmorstands.api.element.Element;
import me.m56738.easyarmorstands.api.element.EntityElementProvider;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class DisplayElementProvider<T extends Display> implements EntityElementProvider {
    private final DisplayElementType<T> type;

    public DisplayElementProvider(DisplayElementType<T> type) {
        this.type = type;
    }

    @Override
    public @Nullable Element getElement(Entity entity) {
        if (type.getEntityType().isInstance(entity)) {
            return type.getElement(type.getEntityType().cast(entity));
        }
        return null;
    }

    public DisplayElementType<T> getType() {
        return type;
    }
}
