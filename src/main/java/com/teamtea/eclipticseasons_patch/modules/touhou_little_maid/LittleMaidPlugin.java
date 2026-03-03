package com.teamtea.eclipticseasons_patch.modules.touhou_little_maid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.api.entity.ai.IExtraMaidBrain;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ExtraMaidBrainManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.teamtea.eclipticseasons_patch.api.PreloadedConfig;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.List;

@LittleMaidExtension
public class LittleMaidPlugin implements ILittleMaid {
    @Override
    public void addMaidTask(TaskManager manager) {
        if (PreloadedConfig.shouldApply(TLM.MOD_ID)) {
            ILittleMaid.super.addMaidTask(manager);
            manager.add(new CleanSnowTask());
        }
    }

    @Override
    public void addExtraMaidBrain(ExtraMaidBrainManager manager) {
        manager.addExtraMaidBrain(new IExtraMaidBrain() {
            @Override
            public List<MemoryModuleType<?>> getExtraMemoryTypes() {
                return List.of(
                );
            }
        });
    }
}
