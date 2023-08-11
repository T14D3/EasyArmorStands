package me.m56738.easyarmorstands.editor;

import me.m56738.easyarmorstands.node.Button;
import me.m56738.easyarmorstands.node.Node;
import org.bukkit.entity.Entity;

public class SimpleEntityObject extends AbstractEditableObject {
    private final Entity entity;
    private final Button button;

    public SimpleEntityObject(Entity entity, Button button) {
        this.entity = entity;
        this.button = button;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public EditableObjectReference asReference() {
        return new EntityObjectReference(entity.getUniqueId());
    }

    @Override
    public Button createButton() {
        return button;
    }

    @Override
    public Node createNode() {
        return button.createNode();
    }
}
