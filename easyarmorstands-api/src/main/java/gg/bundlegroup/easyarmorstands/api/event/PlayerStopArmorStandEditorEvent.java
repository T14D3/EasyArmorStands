package gg.bundlegroup.easyarmorstands.api.event;

import gg.bundlegroup.easyarmorstands.api.Session;
import org.bukkit.event.HandlerList;

public class PlayerStopArmorStandEditorEvent extends ArmorStandSessionEvent {

    public PlayerStopArmorStandEditorEvent(Session session) {
        super(session);
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}