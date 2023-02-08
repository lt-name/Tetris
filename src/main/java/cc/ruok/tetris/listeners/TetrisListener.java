package cc.ruok.tetris.listeners;

import cc.ruok.tetris.Tetris;
import cc.ruok.tetris.TetrisGame;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.level.Position;

public class TetrisListener implements Listener {

    int ori = 1;
    boolean press = false;

    private Player player;
    private TetrisGame game;
    private Position position;

    public TetrisListener(Player player, TetrisGame game) {
        this.player = player;
        this.game = game;
        position = new Position(
                    Tetris.tetris.config.playX,
                    Tetris.tetris.config.playY,
                    Tetris.tetris.config.playZ,
                    TetrisGame.getLevel()
                );
    }

    @EventHandler
    public void onQUit(PlayerQuitEvent event) {
        if (event.getPlayer() == player) {
            game.stop();
        }
    }

    @EventHandler
    public void onDead(PlayerDeathEvent event) {
        if (event.getEntity() == player) {
            game.stop();
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer() == null) {
            event.setDrops(null);
        }
    }

    @EventHandler
    public void onJump(PlayerJumpEvent event) {
//        event.setCancelled();
        if (event.getPlayer() == player) {
            game.rotate();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer() == game.getPlayer()) {
            game.full();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer() != player) return;
        if (press) return;
        press = true;
        Server.getInstance().getScheduler().scheduleDelayedTask(this::onPress, 8);

        double x = event.getTo().x - event.getFrom().x;
        double z = event.getTo().z - event.getFrom().z;
        double _x = (x >= 0)? x : -x;
        double _z = (z >= 0)? z : -z;
        if (x == 0 && z == 0) return;
        event.getPlayer().teleport(position);
        if (_x > _z) {
            onLocationMove(event.getPlayer(), (x >= 0)?4:3);
            if (x >= 0) {
                game.move(2);
            } else {
                game.move(1);
            }
        } else {
            onLocationMove(event.getPlayer(), (z >= 0)?2:1);
            if (z >= 0) {
                game.rotate();
            } else {
                game.rotate();
            }
        }
    }

    public void onLocationMove(Player player, int dire) {
        String s = (dire == 1)?"W":((dire == 2)?"S":((dire == 3)?"A":"D"));
        player.sendTip(s);
    }

    public void onPress() {
        press = false;
    }

}
