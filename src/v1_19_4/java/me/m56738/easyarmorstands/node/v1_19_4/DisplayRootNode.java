package me.m56738.easyarmorstands.node.v1_19_4;

import me.m56738.easyarmorstands.event.EntityObjectMenuInitializeEvent;
import me.m56738.easyarmorstands.menu.builder.SplitMenuBuilder;
import me.m56738.easyarmorstands.node.ClickContext;
import me.m56738.easyarmorstands.node.ClickType;
import me.m56738.easyarmorstands.node.EditableObjectNode;
import me.m56738.easyarmorstands.session.Session;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.joml.Vector3dc;

public class DisplayRootNode extends DisplayMenuNode implements EditableObjectNode {
    private final Session session;
    private final Component name;
    private final DisplayObject<?> editableObject;

    public DisplayRootNode(Session session, Component name, DisplayObject<?> editableObject) {
        super(session, name, session.properties(editableObject));
        this.session = session;
        this.name = name;
        this.editableObject = editableObject;
    }

    protected void populate(SplitMenuBuilder builder) {
    }

    @Override
    public boolean onClick(Vector3dc eyes, Vector3dc target, ClickContext context) {
        if (context.getType() == ClickType.LEFT_CLICK) {
            SplitMenuBuilder builder = new SplitMenuBuilder();
            populate(builder);
            Bukkit.getPluginManager().callEvent(new EntityObjectMenuInitializeEvent(editableObject, builder));
            session.getPlayer().openInventory(builder.build(name).getInventory());
            return true;
        }
        return super.onClick(eyes, target, context);
    }

    @Override
    public DisplayObject<?> getEditableObject() {
        return editableObject;
    }
}
