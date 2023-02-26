package cc.ruok.tetris.listeners;

import cc.ruok.tetris.Ranking;
import cc.ruok.tetris.Tetris;
import cc.ruok.tetris.TetrisConfig;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.entity.EntityDespawnEvent;
import cn.nukkit.event.level.ChunkLoadEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;

import java.util.HashMap;

public class BaseListener implements Listener {

    @EventHandler
    public void onForm(PlayerFormRespondedEvent event) {
        if (event.getFormID() == 1145142233) {
            FormResponseCustom response = (FormResponseCustom) event.getResponse();
            if (response == null) return;
            HashMap<Integer, Object> map = response.getResponses();
            TetrisConfig config = Tetris.tetris.config;
            switch (String.valueOf(map.get(1))) {
                case "1.5x": config.speed = 3; break;
                case "1.0x": config.speed = 2; break;
                case "0.5x": config.speed = 1; break;
                case "0.2x": config.speed = 0; break;
            }
            switch (String.valueOf(map.get(2))) {
                case "高": config.keen = 2; break;
                case "中": config.keen = 1; break;
                case "低": config.keen = 0; break;
            }
            switch (String.valueOf(map.get(3))) {
                case "羊毛": config.block = 0; break;
                case "混凝土": config.block = 1; break;
                case "染色玻璃": config.block = 2; break;
                case "陶瓦": config.block = 3; break;
            }
            switch (String.valueOf(map.get(4))) {
                case "方块破坏": config.effect = 0; break;
                case "爆炸": config.effect = 1; break;
                case "大爆炸": config.effect = 2; break;
                case "白色烟雾": config.effect = 3; break;
                case "无": config.effect = -1; break;
            }
            config.bgm = (boolean) map.get(5);
            config.save(Tetris.tetris.configFile);
            event.getPlayer().sendMessage("成功保存设置!将在下一次游戏时生效.");
        }
    }

    @EventHandler
    public void onEntityDespawn(EntityDespawnEvent event) {
        TetrisConfig config = Tetris.tetris.config;
        if (config.ranking != null && config.rankingId == event.getEntity().getId()) {
            Ranking.updateFloatingText();
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        TetrisConfig config = Tetris.tetris.config;
        if (config.ranking != null) {
            if (event.getChunk().getX() == config.ranking.chunkX && event.getChunk().getZ() == config.ranking.chunkZ) {
                Ranking.updateFloatingText();
            }
        }
    }

}
