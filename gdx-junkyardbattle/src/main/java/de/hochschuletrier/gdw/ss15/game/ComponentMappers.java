package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.ComponentMapper;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ss15.game.components.*;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.*;
import de.hochschuletrier.gdw.ss15.game.components.network.client.NetworkIDComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.ClientComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;

import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.ParticleEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ChainLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ConeLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.DirectionalLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.NormalMapComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.PointLightComponent;

import static com.badlogic.ashley.core.ComponentMapper.*;

public class ComponentMappers {
    public static final ComponentMapper<PositionComponent> position = getFor(PositionComponent.class);
    public static final ComponentMapper<TriggerComponent> trigger = getFor(TriggerComponent.class);
    public static final ComponentMapper<PhysixBodyComponent> physixBody = getFor(PhysixBodyComponent.class);
    public static final ComponentMapper<PhysixModifierComponent> physixModifier = getFor(PhysixModifierComponent.class);
    public static final ComponentMapper<ImpactSoundComponent> impactSound = getFor(ImpactSoundComponent.class);
    public static final ComponentMapper<AnimatorComponent> animator = getFor(AnimatorComponent.class);
    public static final ComponentMapper<TextureComponent> texture = getFor(TextureComponent.class);
    public static final ComponentMapper<PointLightComponent>  pointLight = getFor(PointLightComponent.class);
    public static final ComponentMapper<DirectionalLightComponent>  directionalLight = getFor(DirectionalLightComponent.class);
    public static final ComponentMapper<ChainLightComponent>  chainLight = getFor(ChainLightComponent.class);
    public static final ComponentMapper<ConeLightComponent>  coneLight = getFor(ConeLightComponent.class);
    public static final ComponentMapper<NormalMapComponent> normalMap = getFor(NormalMapComponent.class);
    public static final ComponentMapper<HealthComponent> health = getFor(HealthComponent.class);
    public static final ComponentMapper<DamageComponent> damage = getFor(DamageComponent.class);
    public static final ComponentMapper<DeathComponent> death = getFor(DeathComponent.class);
    public static final ComponentMapper<BulletComponent> bullet = getFor(BulletComponent.class);
    public static final ComponentMapper<PickableComponent> pickable = getFor(PickableComponent.class);
    public static final ComponentMapper<MoveComponent> move = getFor(MoveComponent.class);
    public static final ComponentMapper<PlayerComponent> player = getFor(PlayerComponent.class);
    public static final ComponentMapper<WeaponComponent> weapon = getFor(WeaponComponent.class);
    public static final ComponentMapper<ClientComponent> client = getFor(ClientComponent.class);
    public static final ComponentMapper<PositionSynchComponent> positionSynch = getFor(PositionSynchComponent.class);
    public static final ComponentMapper<SoundEmitterComponent> soundEmitter = getFor(SoundEmitterComponent.class);
    public static final ComponentMapper<InventoryComponent> inventory = getFor(InventoryComponent.class);
    public static final ComponentMapper<NetworkIDComponent> networkID = getFor(NetworkIDComponent.class);
    public static final ComponentMapper<LineOfSightComponent> lineOfSight = getFor(LineOfSightComponent.class);
    public static final ComponentMapper<InputComponent> input = getFor(InputComponent.class);
    public static final ComponentMapper<ParticleEffectComponent> particleEffect = getFor(ParticleEffectComponent.class);
}
