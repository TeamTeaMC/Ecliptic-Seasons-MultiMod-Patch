package com.teamtea.eclipticseasons_patch.modules.touhou_little_maid;

import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.client.util.ClientCon;
import com.teamtea.eclipticseasons.common.core.snow.SnowyMapChecker;
import com.teamtea.eclipticseasons.common.registry.ItemRegistry;
import com.teamtea.eclipticseasons_patch.EclipticSeasonsPatch;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class CleanSnowTask implements IFarmTask {

    @Override
    public @NotNull ResourceLocation getUid() {
        return EclipticSeasonsPatch.rl("clean_snow");
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return ItemRegistry.broom.get().getDefaultInstance();
    }

    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_REMOVE_SNOW.get(), 0.5f);
    }


    @Override
    public @NotNull List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(@NotNull EntityMaid entityMaid) {
        MaidSnowyBlockMoveBehavior snowyBlockMoveBehavior = new MaidSnowyBlockMoveBehavior(this, 0.6F);
        MaidCleanSnowBehavior maidFarmPlantTask = new MaidCleanSnowBehavior(this);
        return Lists.newArrayList(new Pair[]{Pair.of(5, snowyBlockMoveBehavior), Pair.of(6, maidFarmPlantTask)});
        // MaidFarmMoveTask maidFarmMoveTask = new MaidFarmMoveTask(this, 0.6F);
        // MaidFarmPlantTask maidFarmPlantTask = new MaidFarmPlantTask(this);
        // return Lists.newArrayList(new Pair[]{Pair.of(5, maidFarmMoveTask), Pair.of(6, maidFarmPlantTask)});
    }

    @Override
    public boolean isSeed(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        if (!hasBroom(maid)) return false;

        Level level = maid.level();
        BlockState blockState;
        BlockPos below;
        blockState = cropState;
        below = cropPos;
        boolean snowyBlock =
                cropState.is(Blocks.SNOW) || EclipticSeasonsApi.getInstance().isSnowyBlock(level, blockState, below);
        if (snowyBlock) {
            // ChatBubbleManger.addInnerChatText(maid, cropPos.toString()+"****"+cropPos.distToCenterSqr(maid.blockPosition().getCenter()));
        }
        return snowyBlock;
    }

    @Override
    public void harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState) {
        Level level = maid.level();
        BlockPos below;
        BlockState blockState;
        blockState = cropState;
        below = cropPos;

        boolean shouldSet = EclipticSeasonsApi.getInstance().isSnowyBlock(level, blockState, below);
        if (shouldSet && EclipticUtil.canSnowyBlockInteract()) {
            if (level instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel) level;
                SnowyMapChecker.removeSnowyStatus(serverLevel, below);
                HumanoidArm humanoidarm = maid.getUsedItemHand() == InteractionHand.MAIN_HAND ? maid.getMainArm() : maid.getMainArm().getOpposite();
                this.spawnDustParticles(level, new BlockHitResult(below.getCenter().add(0, 0.5, 0), Direction.UP, below, false), Blocks.SNOW.defaultBlockState(),
                        maid.getViewVector(0.0F), humanoidarm);
            } else {
                ClientCon.agent.setChunkDirty(SectionPos.of(below));
            }
        } else if (cropState.is(Blocks.SNOW)) {
            level.destroyBlock(cropPos, true);
        }
    }

    @Override
    public boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return false;
    }

    @Override
    public ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed) {
        return seed;
    }

    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return List.of(Pair.of("has_broom", this::hasBroom),
                Pair.of("broom_work", this::broomWork));
    }

    private boolean hasBroom(EntityMaid maid) {
        return maid.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.broom.get());
    }

    private boolean broomWork(EntityMaid maid) {
        return EclipticUtil.canSnowyBlockInteract();
    }

    private void spawnDustParticles(Level pLevel, BlockHitResult pHitResult, BlockState pState, Vec3 pPos, HumanoidArm pArm) {
        double speed = 3.0;
        int right = pArm == HumanoidArm.RIGHT ? 1 : -1;
        int count = pLevel.getRandom().nextInt(7, 12);
        BlockParticleOption blockparticleoption = new BlockParticleOption(ParticleTypes.BLOCK, pState);
        Direction direction = pHitResult.getDirection();
        DustParticlesDelta brushitem$dustparticlesdelta = DustParticlesDelta.fromDirection(pPos, direction);
        Vec3 vec3 = pHitResult.getLocation();

        for (int k = 0; k < count; ++k) {
            pLevel.addParticle(blockparticleoption, vec3.x - (double) (direction == Direction.WEST ? 1.0E-6F : 0.0F), vec3.y, vec3.z - (double) (direction == Direction.NORTH ? 1.0E-6F : 0.0F), brushitem$dustparticlesdelta.xd() * (double) right * speed * pLevel.getRandom().nextDouble(), 0.0, brushitem$dustparticlesdelta.zd() * (double) right * speed * pLevel.getRandom().nextDouble());
        }

    }

    static record DustParticlesDelta(double xd, double yd, double zd) {
        private static final double ALONG_SIDE_DELTA = 1.0;
        private static final double OUT_FROM_SIDE_DELTA = 0.1;

        DustParticlesDelta(double xd, double yd, double zd) {
            this.xd = xd;
            this.yd = yd;
            this.zd = zd;
        }

        public static DustParticlesDelta fromDirection(Vec3 pPos, Direction pDirection) {
            double yd = 0.0;
            DustParticlesDelta var10000;
            switch (pDirection) {
                case DOWN:
                case UP:
                    var10000 = new DustParticlesDelta(pPos.z(), yd, -pPos.x());
                    break;
                case NORTH:
                    var10000 = new DustParticlesDelta(1.0, yd, -0.1);
                    break;
                case SOUTH:
                    var10000 = new DustParticlesDelta(-1.0, yd, 0.1);
                    break;
                case WEST:
                    var10000 = new DustParticlesDelta(-0.1, yd, -1.0);
                    break;
                case EAST:
                    var10000 = new DustParticlesDelta(0.1, yd, 1.0);
                    break;
                default:
                    throw new IncompatibleClassChangeError();
            }

            return var10000;
        }

        public double xd() {
            return this.xd;
        }

        public double yd() {
            return this.yd;
        }

        public double zd() {
            return this.zd;
        }
    }

}
