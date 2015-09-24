﻿package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.ComponentMapper;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ss15.game.components.BulletComponent;
import de.hochschuletrier.gdw.ss15.game.components.DamageComponent;
import de.hochschuletrier.gdw.ss15.game.components.DeathComponent;
import de.hochschuletrier.gdw.ss15.game.components.HealthComponent;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.InterpolatePositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.InventoryComponent;
import de.hochschuletrier.gdw.ss15.game.components.LineOfSightComponent;
import de.hochschuletrier.gdw.ss15.game.components.MetalShardSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.MineableComponent;
import de.hochschuletrier.gdw.ss15.game.components.MoveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PickableComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SoundEmitterComponent;
import de.hochschuletrier.gdw.ss15.game.components.SpawnSatelliteComponent;
import de.hochschuletrier.gdw.ss15.game.components.TimerComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.components.WeaponComponent;
import de.hochschuletrier.gdw.ss15.game.components.animation.AnimatorComponent;
import de.hochschuletrier.gdw.ss15.game.components.effects.ParticleEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.input.InputComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ChainLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ConeLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.DirectionalLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.NormalMapComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.PointLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.client.NetworkIDComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.ClientComponent;
import de.hochschuletrier.gdw.ss15.game.components.network.server.PositionSynchComponent;
import de.hochschuletrier.gdw.ss15.game.components.texture.TextureComponent;

public class ComponentMappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<TriggerComponent> trigger = ComponentMapper.getFor(TriggerComponent.class);
    public static final ComponentMapper<PhysixBodyComponent> physixBody = ComponentMapper.getFor(PhysixBodyComponent.class);
    public static final ComponentMapper<PhysixModifierComponent> physixModifier = ComponentMapper.getFor(PhysixModifierComponent.class);
    public static final ComponentMapper<ImpactSoundComponent> impactSound = ComponentMapper.getFor(ImpactSoundComponent.class);
    public static final ComponentMapper<AnimatorComponent> animator = ComponentMapper.getFor(AnimatorComponent.class);
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<PointLightComponent>  pointLight = ComponentMapper.getFor(PointLightComponent.class);
    public static final ComponentMapper<DirectionalLightComponent>  directionalLight = ComponentMapper.getFor(DirectionalLightComponent.class);
    public static final ComponentMapper<ChainLightComponent>  chainLight = ComponentMapper.getFor(ChainLightComponent.class);
    public static final ComponentMapper<ConeLightComponent>  coneLight = ComponentMapper.getFor(ConeLightComponent.class);
    public static final ComponentMapper<NormalMapComponent> normalMap = ComponentMapper.getFor(NormalMapComponent.class);
    public static final ComponentMapper<HealthComponent> health = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<DamageComponent> damage = ComponentMapper.getFor(DamageComponent.class);
    public static final ComponentMapper<DeathComponent> death = ComponentMapper.getFor(DeathComponent.class);
    public static final ComponentMapper<BulletComponent> bullet = ComponentMapper.getFor(BulletComponent.class);
    public static final ComponentMapper<PickableComponent> pickable = ComponentMapper.getFor(PickableComponent.class);
    public static final ComponentMapper<MoveComponent> move = ComponentMapper.getFor(MoveComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<TimerComponent> timer = ComponentMapper.getFor(TimerComponent.class);
    public static final ComponentMapper<WeaponComponent> weapon = ComponentMapper.getFor(WeaponComponent.class);
    public static final ComponentMapper<ClientComponent> client = ComponentMapper.getFor(ClientComponent.class);
    public static final ComponentMapper<PositionSynchComponent> positionSynch = ComponentMapper.getFor(PositionSynchComponent.class);
    public static final ComponentMapper<SoundEmitterComponent> soundEmitter = ComponentMapper.getFor(SoundEmitterComponent.class);
    public static final ComponentMapper<InventoryComponent> inventory = ComponentMapper.getFor(InventoryComponent.class);
    public static final ComponentMapper<NetworkIDComponent> networkID = ComponentMapper.getFor(NetworkIDComponent.class);
    public static final ComponentMapper<LineOfSightComponent> lineOfSight = ComponentMapper.getFor(LineOfSightComponent.class);
    public static final ComponentMapper<InputComponent> input = ComponentMapper.getFor(InputComponent.class);
    public static final ComponentMapper<ParticleEffectComponent> particleEffect = ComponentMapper.getFor(ParticleEffectComponent.class);
    public static final ComponentMapper<MineableComponent> mineable = ComponentMapper.getFor(MineableComponent.class);
    public static final ComponentMapper<InterpolatePositionComponent> interpolate = ComponentMapper.getFor(InterpolatePositionComponent.class);
    public static final ComponentMapper<SpawnSatelliteComponent> spawnSatellite = ComponentMapper.getFor(SpawnSatelliteComponent.class);
    public static final ComponentMapper<MetalShardSpawnComponent> metalShardSpawn = ComponentMapper.getFor(MetalShardSpawnComponent.class);
}
