package me.m56738.easyarmorstands.event;

import me.m56738.easyarmorstands.editor.SimpleEntityObject;
import org.bukkit.event.HandlerList;

public class EntityObjectInitializeEvent extends EntityObjectEvent {
    private static final HandlerList handlerList = new HandlerList();

    public EntityObjectInitializeEvent(SimpleEntityObject entityObject) {
        super(entityObject);
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
