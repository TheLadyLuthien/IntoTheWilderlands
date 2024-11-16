// package com.skadoosh.wilderlands.entities;

// import net.minecraft.entity.EntityType;
// import net.minecraft.entity.attribute.DefaultAttributeContainer;
// import net.minecraft.entity.attribute.EntityAttributes;
// import net.minecraft.entity.mob.MobEntity;
// import net.minecraft.entity.mob.PathAwareEntity;
// import net.minecraft.world.World;

// public class MimicEntity extends PathAwareEntity
// {
//     public MimicEntity(EntityType<? extends PathAwareEntity> entityType, World world)
//     {
//         super(entityType, world);
//     }

//     public static DefaultAttributeContainer.Builder createAttributes()
//     {
//         return MobEntity.createAttributes()
//             .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32f)
//             .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1F)
//             .add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0);
//     }
// }
