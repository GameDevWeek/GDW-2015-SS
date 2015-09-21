package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.ComponentMapper;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ss15.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ChainLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.ConeLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.DirectionalLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.NormalMapComponent;
import de.hochschuletrier.gdw.ss15.game.components.light.PointLightComponent;

public class ComponentMappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<TriggerComponent> trigger = ComponentMapper.getFor(TriggerComponent.class);
    public static final ComponentMapper<PhysixBodyComponent> physixBody = ComponentMapper.getFor(PhysixBodyComponent.class);
    public static final ComponentMapper<PhysixModifierComponent> physixModifier = ComponentMapper.getFor(PhysixModifierComponent.class);
    public static final ComponentMapper<ImpactSoundComponent> impactSound = ComponentMapper.getFor(ImpactSoundComponent.class);
    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<PointLightComponent>  pointLight = ComponentMapper.getFor(PointLightComponent.class);
    public static final ComponentMapper<DirectionalLightComponent>  directionalLight = ComponentMapper.getFor(DirectionalLightComponent.class);
    public static final ComponentMapper<ChainLightComponent>  chainLight = ComponentMapper.getFor(ChainLightComponent.class);
    public static final ComponentMapper<ConeLightComponent>  coneLight = ComponentMapper.getFor(ConeLightComponent.class);
    public static final ComponentMapper<NormalMapComponent> normalMap = ComponentMapper.getFor(NormalMapComponent.class);
}
