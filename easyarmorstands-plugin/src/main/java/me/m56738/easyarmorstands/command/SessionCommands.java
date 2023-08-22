package me.m56738.easyarmorstands.command;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import cloud.commandframework.annotations.specifier.Greedy;
import cloud.commandframework.annotations.specifier.Range;
import cloud.commandframework.bukkit.arguments.selector.SingleEntitySelector;
import me.m56738.easyarmorstands.EasyArmorStands;
import me.m56738.easyarmorstands.api.editor.Session;
import me.m56738.easyarmorstands.api.element.DestroyableElement;
import me.m56738.easyarmorstands.api.element.Element;
import me.m56738.easyarmorstands.api.element.ElementType;
import me.m56738.easyarmorstands.api.element.MenuElement;
import me.m56738.easyarmorstands.api.property.Property;
import me.m56738.easyarmorstands.api.property.PropertyContainer;
import me.m56738.easyarmorstands.api.property.type.ArmorStandPropertyTypes;
import me.m56738.easyarmorstands.api.property.type.EntityPropertyTypes;
import me.m56738.easyarmorstands.command.sender.EasCommandSender;
import me.m56738.easyarmorstands.command.sender.EasPlayer;
import me.m56738.easyarmorstands.history.action.ElementCreateAction;
import me.m56738.easyarmorstands.history.action.ElementDestroyAction;
import me.m56738.easyarmorstands.message.Message;
import me.m56738.easyarmorstands.node.ValueNode;
import me.m56738.easyarmorstands.property.TrackedPropertyContainer;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandCanTickProperty;
import me.m56738.easyarmorstands.session.SessionImpl;
import me.m56738.easyarmorstands.util.AlignAxis;
import me.m56738.easyarmorstands.util.Util;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.joml.Vector3d;
import org.joml.Vector3dc;

@CommandMethod("eas")
public class SessionCommands {
    public static void showText(Audience audience, Component type, Component text, String command) {
        String serialized = MiniMessage.miniMessage().serializeOr(text, "");
        audience.sendMessage(Component.text()
                .append(Message.title(type))
                .append(Component.space())
                .append(Message.chatButton("easyarmorstands.button.text.edit")
                        .hoverEvent(Message.hover("easyarmorstands.click-to-edit"))
                        .clickEvent(ClickEvent.suggestCommand(command + " " + serialized)))
                .append(Component.space())
                .append(Message.chatButton("easyarmorstands.button.text.copy")
                        .hoverEvent(Message.hover("easyarmorstands.click-to-copy"))
                        .clickEvent(ClickEvent.copyToClipboard(serialized)))
                .append(Component.space())
                .append(Message.chatButton("easyarmorstands.button.text.syntax-help")
                        .hoverEvent(Message.hover("easyarmorstands.click-to-open-minimessage"))
                        .clickEvent(ClickEvent.openUrl("https://docs.advntr.dev/minimessage/format.html"))));
        if (text == null) {
            audience.sendMessage(Message.hint("easyarmorstands.hint.not-set"));
        } else {
            audience.sendMessage(text);
        }
    }

    public static void sendNoSessionError(EasCommandSender sender) {
        sender.sendMessage(Message.error("easyarmorstands.error.not-using-editor"));
        if (sender.get().hasPermission("easyarmorstands.give")) {
            sender.sendMessage(Message.hint("easyarmorstands.hint.give-tool", Message.command("/eas give")));
        }
    }

    public static void sendNoSessionElementError(EasCommandSender sender) {
        sender.sendMessage(Message.error("easyarmorstands.error.nothing-selected"));
        sender.sendMessage(Message.hint("easyarmorstands.hint.select-entity"));
    }

    public static void sendUnsupportedEntityError(EasCommandSender sender) {
        sender.sendMessage(Message.error("easyarmorstands.error.unsupported-entity"));
    }

    public static void sendNoEntityError(EasCommandSender sender) {
        sender.sendMessage(Message.error("easyarmorstands.error.entity-not-found"));
    }

    public static SessionImpl getSessionOrError(EasPlayer sender) {
        SessionImpl session = sender.session();
        if (session == null) {
            sendNoSessionError(sender);
        }
        return session;
    }

    public static Element getElementOrError(EasPlayer sender, Session session) {
        if (session == null) {
            return null;
        }
        Element element = session.getElement();
        if (element == null) {
            sendNoSessionElementError(sender);
        }
        return element;
    }

    public static Element getElementOrError(EasPlayer sender) {
        return getElementOrError(sender, getSessionOrError(sender));
    }

    public static Element getElementOrError(EasPlayer sender, Entity entity) {
        if (entity == null) {
            sendNoEntityError(sender);
            return null;
        }
        Element element = EasyArmorStands.getInstance().getEntityElementProviderRegistry().getElement(entity);
        if (element == null) {
            sendUnsupportedEntityError(sender);
        }
        return element;
    }

    @CommandMethod("open")
    @CommandPermission("easyarmorstands.open")
    @CommandDescription("Open the menu")
    public void open(EasPlayer sender) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        if (!(element instanceof MenuElement)) {
            sender.sendMessage(Message.error("easyarmorstands.error.menu-unsupported"));
            return;
        }
        MenuElement menuElement = (MenuElement) element;
        menuElement.openMenu(sender.get());
    }

    @CommandMethod("open <entity>")
    @CommandPermission("easyarmorstands.open")
    @CommandDescription("Open the menu of an entity")
    public void open(EasPlayer sender, @Argument("entity") SingleEntitySelector selector) {
        Entity entity = selector.getEntity();
        Element element = getElementOrError(sender, entity);
        if (element == null) {
            return;
        }
        if (!(element instanceof MenuElement)) {
            sender.sendMessage(Message.error("easyarmorstands.error.menu-unsupported"));
            return;
        }
        MenuElement menuElement = (MenuElement) element;
        menuElement.openMenu(sender.get());
    }

    @CommandMethod("clone")
    @CommandPermission("easyarmorstands.clone")
    @CommandDescription("Spawn a copy of the selected entity")
    public void clone(EasPlayer sender) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        ElementType type = element.getType();
        PropertyContainer properties = PropertyContainer.immutable(element.getProperties());
        if (!sender.canCreateElement(type, properties)) {
            return;
        }

        Element clone = type.createElement(properties);
        sender.history().push(new ElementCreateAction(clone));

        sender.sendMessage(Message.success("easyarmorstands.success.entity-cloned"));
    }

    @CommandMethod("spawn")
    @CommandPermission("easyarmorstands.spawn")
    @CommandDescription("Open the spawn menu")
    public void spawn(EasPlayer sender) {
        SessionImpl.openSpawnMenu(sender.get());
    }

    @CommandMethod("destroy")
    @CommandPermission("easyarmorstands.destroy")
    @CommandDescription("Destroy the selected entity")
    public void destroy(EasPlayer sender) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        if (!(element instanceof DestroyableElement)) {
            sender.sendMessage(Message.error("easyarmorstands.error.destroy-unsupported"));
            return;
        }

        DestroyableElement destroyableElement = (DestroyableElement) element;
        if (!sender.canDestroyElement(destroyableElement)) {
            return;
        }

        sender.history().push(new ElementDestroyAction(element));
        destroyableElement.destroy();
        sender.sendMessage(Message.success("easyarmorstands.success.entity-destroyed"));
    }

    @CommandMethod("snap angle [value]")
    @CommandPermission("easyarmorstands.snap")
    @CommandDescription("Change the angle snapping increment")
    public void setAngleSnapIncrement(
            EasPlayer sender,
            @Argument(value = "value") @Range(min = "0", max = "90") Double value) {
        SessionImpl session = getSessionOrError(sender);
        if (session == null) {
            return;
        }
        if (value == null) {
            value = SessionImpl.DEFAULT_ANGLE_SNAP_INCREMENT;
            if (value == session.getAngleSnapIncrement()) {
                value = 0.0;
            }
        }
        session.setAngleSnapIncrement(value);
        sender.sendMessage(Message.success("easyarmorstands.success.snap-changed.angle", Component.text(value)));
    }

    @CommandMethod("snap move [value]")
    @CommandPermission("easyarmorstands.snap")
    @CommandDescription("Change the position snapping increment")
    public void setSnapIncrement(
            EasPlayer sender,
            @Argument(value = "value") @Range(min = "0", max = "10") Double value) {
        SessionImpl session = getSessionOrError(sender);
        if (session == null) {
            return;
        }
        if (value == null) {
            value = SessionImpl.DEFAULT_SNAP_INCREMENT;
            if (value == session.getSnapIncrement()) {
                value = 0.0;
            }
        }
        session.setSnapIncrement(value);
        sender.sendMessage(Message.success("easyarmorstands.success.snap-changed.position", Component.text(value)));
    }

    @CommandMethod("align [axis] [value] [offset]")
    @CommandPermission("easyarmorstands.align")
    @CommandDescription("Move the selected entity to the middle of the block")
    public void align(
            EasPlayer sender,
            @Argument(value = "axis", defaultValue = "all") AlignAxis axis,
            @Argument(value = "value") @Range(min = "0.001", max = "1") Double value,
            @Argument(value = "offset") @Range(min = "-1", max = "1") Double offset
    ) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        PropertyContainer properties = new TrackedPropertyContainer(element, sender);
        Property<Location> property = properties.get(EntityPropertyTypes.LOCATION);
        Vector3d offsetVector = new Vector3d();
        if (value == null) {
            // None specified: Snap to the middle of the bottom of a block
            value = 1.0;
            offsetVector.set(0.5, 0.0, 0.5);
        } else if (offset != null) {
            offsetVector.set(offset, offset, offset);
        }
        Location location = property.getValue();
        Vector3dc position = axis.snap(Util.toVector3d(location), value, offsetVector, new Vector3d());
        location.setX(position.x());
        location.setY(position.y());
        location.setZ(position.z());
        if (!property.setValue(location)) {
            sender.sendMessage(Message.error("easyarmorstands.error.cannot-move"));
            return;
        }
        properties.commit();
        sender.sendMessage(Message.success("easyarmorstands.success.moved", Util.formatPosition(position)));
    }

    @CommandMethod("position <position>")
    @CommandPermission("easyarmorstands.property.location")
    @CommandDescription("Teleport the selected entity")
    public void position(EasPlayer sender, @Argument("position") Location location) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        PropertyContainer properties = new TrackedPropertyContainer(element, sender);
        Property<Location> property = properties.get(EntityPropertyTypes.LOCATION);
        Location oldLocation = property.getValue();
        location.setYaw(oldLocation.getYaw());
        location.setPitch(oldLocation.getPitch());
        if (!property.setValue(location)) {
            sender.sendMessage(Message.error("easyarmorstands.error.cannot-move"));
            return;
        }
        properties.commit();
        sender.sendMessage(Message.success("easyarmorstands.success.moved", Util.formatLocation(location)));
    }

    @CommandMethod("yaw <yaw>")
    @CommandPermission("easyarmorstands.property.location")
    @CommandDescription("Set the yaw of the selected entity")
    public void setYaw(EasPlayer sender, @Argument("yaw") float yaw) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        PropertyContainer properties = new TrackedPropertyContainer(element, sender);
        Property<Location> property = properties.get(EntityPropertyTypes.LOCATION);
        Location location = property.getValue();
        location.setYaw(yaw);
        if (!property.setValue(location)) {
            sender.sendMessage(Message.error("easyarmorstands.error.cannot-move"));
            return;
        }
        properties.commit();
        sender.sendMessage(Message.success("easyarmorstands.success.changed-yaw", Util.formatAngle(yaw)));
    }

    @CommandMethod("pitch <pitch>")
    @CommandPermission("easyarmorstands.property.location")
    @CommandDescription("Set the pitch of the selected entity")
    public void setPitch(EasPlayer sender, @Argument("pitch") float pitch) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        PropertyContainer properties = new TrackedPropertyContainer(element, sender);
        Property<Location> property = properties.get(EntityPropertyTypes.LOCATION);
        Location location = property.getValue();
        location.setPitch(pitch);
        if (!property.setValue(location)) {
            sender.sendMessage(Message.error("easyarmorstands.error.cannot-move"));
            return;
        }
        properties.commit();
        sender.sendMessage(Message.success("easyarmorstands.success.changed-pitch", Util.formatAngle(pitch)));
    }

    @CommandMethod("name")
    @CommandPermission("easyarmorstands.property.name")
    @CommandDescription("Show the custom name of the selected entity")
    public void showName(EasPlayer sender) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        Property<Component> property = element.getProperties().getOrNull(EntityPropertyTypes.CUSTOM_NAME);
        if (property == null) {
            sender.sendMessage(Message.error("easyarmorstands.error.name-unsupported"));
            return;
        }
        Component text = property.getValue();
        showText(sender, EntityPropertyTypes.CUSTOM_NAME.getName(), text, "/eas name set");
    }

    @CommandMethod("name set <value>")
    @CommandPermission("easyarmorstands.property.name")
    @CommandDescription("Set the custom name of the selected entity")
    public void setName(EasPlayer sender, @Argument("value") @Greedy String input) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        PropertyContainer properties = new TrackedPropertyContainer(element, sender);
        Property<Component> nameProperty = properties.getOrNull(EntityPropertyTypes.CUSTOM_NAME);
        if (nameProperty == null) {
            sender.sendMessage(Message.error("easyarmorstands.error.name-unsupported"));
            return;
        }
        Property<Boolean> nameVisibleProperty = properties.getOrNull(EntityPropertyTypes.CUSTOM_NAME_VISIBLE);
        Component name = MiniMessage.miniMessage().deserialize(input);
        boolean hadName = nameProperty.getValue() != null;
        if (!nameProperty.setValue(name)) {
            sender.sendMessage(Message.error("easyarmorstands.error.cannot-change"));
            return;
        }
        if (!hadName && nameVisibleProperty != null) {
            nameVisibleProperty.setValue(true);
        }
        properties.commit();
        sender.sendMessage(Message.success("easyarmorstands.success.changed-name", name.colorIfAbsent(NamedTextColor.WHITE)));
    }

    @CommandMethod("name clear")
    @CommandPermission("easyarmorstands.property.name")
    @CommandDescription("Remove the custom name of the selected entity")
    public void clearName(EasPlayer sender) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        PropertyContainer properties = new TrackedPropertyContainer(element, sender);
        Property<Component> nameProperty = properties.getOrNull(EntityPropertyTypes.CUSTOM_NAME);
        if (nameProperty == null) {
            sender.sendMessage(Message.error("easyarmorstands.error.name-unsupported"));
            return;
        }
        Property<Boolean> nameVisibleProperty = properties.getOrNull(EntityPropertyTypes.CUSTOM_NAME_VISIBLE);
        if (!nameProperty.setValue(null)) {
            sender.sendMessage(Message.error("easyarmorstands.error.cannot-change"));
            return;
        }
        if (nameVisibleProperty != null) {
            nameVisibleProperty.setValue(false);
        }
        properties.commit();
        sender.sendMessage(Message.success("easyarmorstands.success.removed-name"));
    }

    @CommandMethod("name visible <value>")
    @CommandPermission("easyarmorstands.property.name.visible")
    @CommandDescription("Change the custom name visibility of the selected entity")
    public void setNameVisible(EasPlayer sender, @Argument("value") boolean visible) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        PropertyContainer properties = new TrackedPropertyContainer(element, sender);
        Property<Boolean> property = properties.getOrNull(EntityPropertyTypes.CUSTOM_NAME_VISIBLE);
        if (property == null) {
            sender.sendMessage(Message.error("easyarmorstands.error.name-unsupported"));
            return;
        }
        if (!property.setValue(visible)) {
            sender.sendMessage(Message.error("easyarmorstands.error.cannot-change"));
            return;
        }
        properties.commit();
        sender.sendMessage(Message.success("easyarmorstands.success.changed-name-visibility", property.getType().getValueComponent(visible)));
    }

    @CommandMethod("cantick <value>")
    @CommandPermission("easyarmorstands.property.armorstand.cantick")
    @CommandDescription("Toggle whether the selected armor stand should be ticked")
    public void setCanTick(EasPlayer sender, @Argument("value") boolean canTick) {
        Element element = getElementOrError(sender);
        if (element == null) {
            return;
        }
        PropertyContainer properties = new TrackedPropertyContainer(element, sender);
        Property<Boolean> property = properties.getOrNull(ArmorStandPropertyTypes.CAN_TICK);
        if (property == null) {
            if (ArmorStandCanTickProperty.isSupported()) {
                sender.sendMessage(Message.error("easyarmorstands.error.can-tick-unsupported-entity"));
            } else {
                sender.sendMessage(Message.error("easyarmorstands.error.can-tick-unsupported"));
            }
            return;
        }
        if (!property.setValue(canTick)) {
            sender.sendMessage(Message.error("easyarmorstands.error.cannot-change"));
            return;
        }
        properties.commit();
        sender.sendMessage(Message.success("easyarmorstands.success.changed-can-tick",
                property.getType().getValueComponent(canTick)));
    }

    // TODO Restore /eas reset?
//    @SuppressWarnings({"rawtypes", "unchecked"})
//    @CommandMethod("reset <property>")
//    @CommandPermission("easyarmorstands.edit")
//    @CommandDescription("Reset a property of the selected entity")
//    @RequireSession
//    @RequireEntity
//    public void resetProperty(EasCommandSender sender, Session session, Entity entity, @Argument("property") ResettableEntityProperty property) {
//        Object value = property.getResetValue();
//        if (!session.tryChange(property.bind(entity), value)) {
//            sender.sendMessage(Component.text("Unable to change the property", NamedTextColor.RED));
//            return;
//        }
//        session.commit();
//        sender.sendMessage(Component.text("Reset ", NamedTextColor.GREEN)
//                .append(property.getDisplayName()));
//    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @CommandMethod("set <value>")
    @CommandPermission("easyarmorstands.edit")
    @CommandDescription("Set the value of the selected tool")
    public void set(
            EasCommandSender sender,
            ValueNode node,
            @Argument(value = "value", parserName = "node_value") Object value
    ) {
        node.setValue(value);
        sender.sendMessage(Message.success("easyarmorstands.success.changed-value",
                node.getName(),
                node.getValueComponent(value)));
    }
}