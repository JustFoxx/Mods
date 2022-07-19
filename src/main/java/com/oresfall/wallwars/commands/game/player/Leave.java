package com.oresfall.wallwars.commands.game.player;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oresfall.wallwars.gameclass.Game;
import com.oresfall.wallwars.IEntityDataSaver;
import com.oresfall.wallwars.db.Database;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import static com.oresfall.wallwars.utls.Utils.defaultMsg;
import static com.oresfall.wallwars.utls.Utils.errorMsg;

/**
 * Command for leaving from game
 * Usage: `/game player leave`
 */
public class Leave {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("leave")
                .executes(Leave::run);
    }
    public static int run(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity target = context.getSource().getPlayer();
        IEntityDataSaver targetData = (IEntityDataSaver)target;
        Game game = null;
        for(Game gameThing : Database.getGames()) {
            if(gameThing.getPlayers().contains(target)) {
                game = gameThing;
            }
        }


        if(!targetData.getPersistentData().getBoolean("JoinedGame") || game == null || game.leavePlayer(target)) {
            target.sendMessage(errorMsg("You are not in game!"));
            return -1;
        }

        target.sendMessage(defaultMsg("Left the game!"));

        targetData.getPersistentData().putBoolean("JoinedGame",false);
        return 0;
    }
}
