package com.songoda.skyblock.command.commands.island;

import com.songoda.core.compatibility.CompatibleSound;
import com.songoda.skyblock.command.SubCommand;
import com.songoda.skyblock.config.FileManager.Config;
import com.songoda.skyblock.island.Island;
import com.songoda.skyblock.island.IslandManager;
import com.songoda.skyblock.island.IslandRole;
import com.songoda.skyblock.message.MessageManager;
import com.songoda.skyblock.permission.PermissionManager;
import com.songoda.skyblock.playerdata.PlayerData;
import com.songoda.skyblock.playerdata.PlayerDataManager;
import com.songoda.skyblock.scoreboard.ScoreboardManager;
import com.songoda.skyblock.sound.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ScoreboardCommand extends SubCommand {

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        MessageManager messageManager = skyblock.getMessageManager();
        PlayerDataManager playerDataManager = skyblock.getPlayerDataManager();
        SoundManager soundManager = skyblock.getSoundManager();
        ScoreboardManager scoreboardManager = skyblock.getScoreboardManager();
    
        Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();
    
        PlayerData playerData = playerDataManager.getPlayerData(player);
    
        if (playerData == null) {
            messageManager.sendMessage(player, configLoad.getString("Command.Scoreboard.Error.Message"));
            soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
        } else if(!skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"))
                .getFileConfiguration().getBoolean("Island.Scoreboard.Enable", false)) {
            messageManager.sendMessage(player, configLoad.getString("Command.Scoreboard.GlobalDisable.Message"));
            soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
        }else if (playerData.isScoreboard()) {
            playerData.setScoreboard(false);
            scoreboardManager.removePlayer(player);
            
            messageManager.sendMessage(player, configLoad.getString("Command.Scoreboard.Disabled.Message"));
            soundManager.playSound(player, CompatibleSound.BLOCK_WOODEN_DOOR_CLOSE.getSound(), 1.0F, 1.0F);
        } else {
            playerData.setScoreboard(true);
            scoreboardManager.addPlayer(player);

            messageManager.sendMessage(player, configLoad.getString("Command.Scoreboard.Enabled.Message"));
            soundManager.playSound(player, CompatibleSound.BLOCK_WOODEN_DOOR_OPEN.getSound(), 1.0F, 1.0F);
        }
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage("SkyBlock | Error: You must be a player to perform that command.");
    }

    @Override
    public String getName() {
        return "scoreboard";
    }

    @Override
    public String getInfoMessagePath() {
        return "Command.Scoreboard.Info.Message";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }
}