package me.m56738.easyarmorstands.node;

import me.m56738.easyarmorstands.EasyArmorStands;
import me.m56738.easyarmorstands.bone.ArmorStandPartPoseBone;
import me.m56738.easyarmorstands.bone.ArmorStandPartPositionBone;
import me.m56738.easyarmorstands.bone.ArmorStandPositionBone;
import me.m56738.easyarmorstands.capability.component.ComponentCapability;
import me.m56738.easyarmorstands.capability.equipment.EquipmentCapability;
import me.m56738.easyarmorstands.capability.glow.GlowCapability;
import me.m56738.easyarmorstands.capability.invulnerability.InvulnerabilityCapability;
import me.m56738.easyarmorstands.capability.lock.LockCapability;
import me.m56738.easyarmorstands.capability.particle.ParticleCapability;
import me.m56738.easyarmorstands.capability.persistence.PersistenceCapability;
import me.m56738.easyarmorstands.capability.spawn.SpawnCapability;
import me.m56738.easyarmorstands.capability.tick.TickCapability;
import me.m56738.easyarmorstands.capability.visibility.VisibilityCapability;
import me.m56738.easyarmorstands.event.SessionEntityMenuBuildEvent;
import me.m56738.easyarmorstands.menu.LegacyArmorStandMenu;
import me.m56738.easyarmorstands.menu.builder.SplitMenuBuilder;
import me.m56738.easyarmorstands.menu.slot.ButtonPropertySlot;
import me.m56738.easyarmorstands.particle.ParticleColor;
import me.m56738.easyarmorstands.property.PropertyContainer;
import me.m56738.easyarmorstands.property.PropertyRegistry;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandArmsProperty;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandBasePlateProperty;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandCanTickProperty;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandGravityProperty;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandInvulnerabilityProperty;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandLockProperty;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandMarkerProperty;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandPoseProperty;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandSizeProperty;
import me.m56738.easyarmorstands.property.armorstand.ArmorStandVisibilityProperty;
import me.m56738.easyarmorstands.property.entity.EntityCustomNameProperty;
import me.m56738.easyarmorstands.property.entity.EntityCustomNameVisibleProperty;
import me.m56738.easyarmorstands.property.entity.EntityEquipmentProperty;
import me.m56738.easyarmorstands.property.entity.EntityGlowingProperty;
import me.m56738.easyarmorstands.property.entity.EntityLocationProperty;
import me.m56738.easyarmorstands.property.key.Key;
import me.m56738.easyarmorstands.session.Session;
import me.m56738.easyarmorstands.util.ArmorStandPart;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.joml.Vector3dc;

import java.util.EnumMap;
import java.util.Locale;

public class ArmorStandRootNode extends EntityMenuNode {
    private final Session session;
    private final ArmorStand entity;
    private final PositionBoneButton positionButton;
    private final EnumMap<ArmorStandPart, ArmorStandPartButton> partButtons = new EnumMap<>(ArmorStandPart.class);
    private final PropertyRegistry properties = new PropertyRegistry();
    private ArmorStand skeleton;

    public ArmorStandRootNode(Session session, ArmorStand entity) {
        super(session, Component.text("Select a bone"), entity);
        this.session = session;
        this.entity = entity;

        ComponentCapability componentCapability = EasyArmorStands.getInstance().getCapability(ComponentCapability.class);
        EquipmentCapability equipmentCapability = EasyArmorStands.getInstance().getCapability(EquipmentCapability.class);
        GlowCapability glowCapability = EasyArmorStands.getInstance().getCapability(GlowCapability.class);
        if (glowCapability != null) {
            properties.register(EntityGlowingProperty.KEY, new EntityGlowingProperty(entity, glowCapability));
        }
        properties.register(EntityLocationProperty.KEY, new EntityLocationProperty(entity));
        properties.register(EntityCustomNameProperty.KEY, new EntityCustomNameProperty(entity, componentCapability));
        properties.register(EntityCustomNameVisibleProperty.KEY, new EntityCustomNameVisibleProperty(entity));
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            String upperName = slot.name().replace('_', ' ');
            String lowerName = upperName.toLowerCase(Locale.ROOT);
            properties.register(EntityEquipmentProperty.key(slot), new EntityEquipmentProperty(entity, slot, Component.text(lowerName), equipmentCapability, componentCapability));
        }
        properties.register(ArmorStandArmsProperty.KEY, new ArmorStandArmsProperty(entity));
        properties.register(ArmorStandBasePlateProperty.KEY, new ArmorStandBasePlateProperty(entity));
        properties.register(ArmorStandMarkerProperty.KEY, new ArmorStandMarkerProperty(entity));
        properties.register(ArmorStandSizeProperty.KEY, new ArmorStandSizeProperty(entity));
        properties.register(ArmorStandVisibilityProperty.KEY, new ArmorStandVisibilityProperty(entity));
        TickCapability tickCapability = EasyArmorStands.getInstance().getCapability(TickCapability.class);
        if (tickCapability != null) {
            properties.register(ArmorStandCanTickProperty.KEY, new ArmorStandCanTickProperty(entity, tickCapability));
        }
        properties.register(ArmorStandGravityProperty.KEY, new ArmorStandGravityProperty(entity, tickCapability));
        InvulnerabilityCapability invulnerabilityCapability = EasyArmorStands.getInstance().getCapability(InvulnerabilityCapability.class);
        if (invulnerabilityCapability != null) {
            properties.register(ArmorStandInvulnerabilityProperty.KEY, new ArmorStandInvulnerabilityProperty(entity, invulnerabilityCapability));
        }
        LockCapability lockCapability = EasyArmorStands.getInstance().getCapability(LockCapability.class);
        if (lockCapability != null) {
            properties.register(ArmorStandLockProperty.KEY, new ArmorStandLockProperty(entity, lockCapability));
        }
        for (ArmorStandPart part : ArmorStandPart.values()) {
            ArmorStandPoseProperty property = new ArmorStandPoseProperty(entity, part);
            properties.register(Key.of(ArmorStandPoseProperty.KEY, part), property);
        }

        setRoot(true);

        for (ArmorStandPart part : ArmorStandPart.values()) {
            ArmorStandPartPositionBone positionBone = new ArmorStandPartPositionBone(session, entity, part);
            ArmorStandPartPoseBone poseBone = new ArmorStandPartPoseBone(session, entity, part);

            MenuNode localNode = new EntityMenuNode(session, part.getDisplayName().append(Component.text(" (local)")), entity);
            localNode.addMoveButtons(session, positionBone, poseBone, 3);
            localNode.addRotationButtons(session, poseBone, 1, poseBone);

            MenuNode globalNode = new EntityMenuNode(session, part.getDisplayName().append(Component.text(" (global)")), entity);
            globalNode.addPositionButtons(session, positionBone, 3);
            globalNode.addRotationButtons(session, poseBone, 1, null);

            localNode.setNextNode(globalNode);
            globalNode.setNextNode(localNode);

            ArmorStandPartButton partButton = new ArmorStandPartButton(session, entity, part, localNode);
            addButton(partButton);
            partButtons.put(part, partButton);
        }

        ArmorStandPositionBone positionBone = new ArmorStandPositionBone(session, entity);

        MenuNode positionNode = new EntityMenuNode(session, Component.text("Position"), entity);
        positionNode.addYawButton(session, positionBone, 1);
        positionNode.addPositionButtons(session, positionBone, 3);
        positionNode.addCarryButtonWithYaw(session, positionBone);

        this.positionButton = new PositionBoneButton(session, positionBone, positionNode, Component.text("Position"), ParticleColor.YELLOW);
        this.positionButton.setPriority(1);
        addButton(this.positionButton);
    }

    @Override
    public void onUpdate(Vector3dc eyes, Vector3dc target) {
        super.onUpdate(eyes, target);
        if (skeleton != null) {
            updateSkeleton(skeleton);
        }
    }

    @Override
    public void onInactiveUpdate() {
        super.onInactiveUpdate();
        if (skeleton != null) {
            updateSkeleton(skeleton);
        }
    }

    public PositionBoneButton getPositionButton() {
        return positionButton;
    }

    public ArmorStandPartButton getPartButton(ArmorStandPart part) {
        return partButtons.get(part);
    }

    @Override
    public void onAdd() {
        if (skeleton != null) {
            skeleton.remove();
        }

        EasyArmorStands plugin = EasyArmorStands.getInstance();
        GlowCapability glowCapability = plugin.getCapability(GlowCapability.class);
        ParticleCapability particleCapability = plugin.getCapability(ParticleCapability.class);
        if (glowCapability != null && !particleCapability.isVisibleThroughWalls()) {
            // Entity glowing is supported and particles are not visible through walls
            // Spawn a glowing skeleton instead
            SpawnCapability spawnCapability = plugin.getCapability(SpawnCapability.class);
            skeleton = spawnCapability.spawnEntity(entity.getLocation(), ArmorStand.class, e -> {
                e.setVisible(false);
                e.setBasePlate(false);
                e.setArms(true);
                e.setGravity(false);
                PersistenceCapability persistenceCapability = plugin.getCapability(PersistenceCapability.class);
                if (persistenceCapability != null) {
                    persistenceCapability.setPersistent(e, false);
                }
                TickCapability tickCapability = plugin.getCapability(TickCapability.class);
                if (tickCapability != null) {
                    tickCapability.setCanTick(e, false);
                }
                updateSkeleton(e);
                VisibilityCapability visibilityCapability = plugin.getCapability(VisibilityCapability.class);
                if (visibilityCapability != null) {
                    Player player = session.getPlayer();
                    for (Player other : Bukkit.getOnlinePlayers()) {
                        if (player != other) {
                            visibilityCapability.hideEntity(other, plugin, e);
                        }
                    }
                }
                glowCapability.setGlowing(e, true);
            });
        } else {
            skeleton = null;
        }
    }

    private void updateSkeleton(ArmorStand skeleton) {
        skeleton.teleport(entity.getLocation());
        skeleton.setSmall(entity.isSmall());
        for (ArmorStandPart part : ArmorStandPart.values()) {
            part.setPose(skeleton, part.getPose(entity));
        }
    }

    public void hideSkeleton(Player player) {
        EasyArmorStands plugin = EasyArmorStands.getInstance();
        VisibilityCapability visibilityCapability = plugin.getCapability(VisibilityCapability.class);
        if (skeleton != null && visibilityCapability != null) {
            visibilityCapability.hideEntity(player, plugin, skeleton);
        }
    }

    @Override
    public void onRemove() {
        if (skeleton != null) {
            skeleton.remove();
        }
    }

    @Override
    public boolean onClick(Vector3dc eyes, Vector3dc target, ClickContext context) {
        Player player = session.getPlayer();
        if (context.getType() == ClickType.LEFT_CLICK && player.hasPermission("easyarmorstands.open")) {
            if (player.isSneaking()) {
                player.openInventory(new LegacyArmorStandMenu(session, entity).getInventory());
            } else {
                SplitMenuBuilder builder = new SplitMenuBuilder();
                EntityEquipmentProperty.populate(builder, session);
                ArmorStandBasePlateProperty property = session.findProperty(ArmorStandBasePlateProperty.KEY);
                builder.addButton(new ButtonPropertySlot(property, session));
                Bukkit.getPluginManager().callEvent(new SessionEntityMenuBuildEvent(session, builder, entity));
                player.openInventory(builder.build(Component.text("EasyArmorStands")).getInventory());
            }
            return true;
        }
        return super.onClick(eyes, target, context);
    }

    @Override
    public ArmorStand getEntity() {
        return entity;
    }

    @Override
    public PropertyContainer properties() {
        return properties;
    }
}
