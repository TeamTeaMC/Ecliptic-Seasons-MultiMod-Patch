package com.teamtea.eclipticseasons_patch.modules.touhou_little_maid;

import com.github.tartaricacid.touhoulittlemaid.api.task.IFarmTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidFarmPlantTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.state.BlockState;

public class MaidCleanSnowBehavior extends MaidFarmPlantTask {
    private final IFarmTask task;

    public MaidCleanSnowBehavior(IFarmTask task) {
        super(task);
        this.task = task;
    }

    @Override
    protected void start(ServerLevel world, EntityMaid maid, long gameTimeIn) {
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockPos basePos = posWrapper.currentBlockPosition();
            BlockState cropState = world.getBlockState(basePos);
            if (maid.canDestroyBlock(basePos) && this.task.canHarvest(maid, basePos, cropState)) {
                this.task.harvest(maid, basePos, cropState);
                maid.swing(InteractionHand.MAIN_HAND);
                maid.getBrain().eraseMemory((MemoryModuleType)InitEntities.TARGET_POS.get());
                maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                LivingEntity patt0$tempx = maid.getOwner();
                if (patt0$tempx instanceof ServerPlayer) {
                    ServerPlayer serverPlayerx = (ServerPlayer)patt0$tempx;
                    InitTrigger.MAID_EVENT.get().trigger(serverPlayerx, "maid_farm");
                }
            }
        });
    }
}
