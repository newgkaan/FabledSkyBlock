package com.songoda.skyblock.command.commands.island;

import com.songoda.core.compatibility.CompatibleSound;
import com.songoda.core.compatibility.ServerVersion;
import com.songoda.skyblock.biome.BiomeManager;
import com.songoda.skyblock.command.SubCommand;
import com.songoda.skyblock.config.FileManager.Config;
import com.songoda.skyblock.gui.bank.GuiBank;
import com.songoda.skyblock.gui.wip.GuiBiome;
import com.songoda.skyblock.island.Island;
import com.songoda.skyblock.island.IslandManager;
import com.songoda.skyblock.island.IslandRole;
import com.songoda.skyblock.island.IslandWorld;
import com.songoda.skyblock.menus.Biome;
import com.songoda.skyblock.message.MessageManager;
import com.songoda.skyblock.sound.SoundManager;
import com.songoda.skyblock.world.WorldManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class BiomeCommand extends SubCommand {

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        MessageManager messageManager = skyblock.getMessageManager();
        IslandManager islandManager = skyblock.getIslandManager();
        SoundManager soundManager = skyblock.getSoundManager();
        BiomeManager biomeManager = skyblock.getBiomeManager();

        Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        Island island = islandManager.getIsland(player);
    
        if(ServerVersion.isServerVersionAtLeast(ServerVersion.V1_16)){
            messageManager.sendMessage(player, "&bSkyBlock &8| &6Warning&8: &eThis feature is not supported on this Minecraft version yet. Use at your own risk.");
            soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
        }
        if (island == null) {
            messageManager.sendMessage(player, configLoad.getString("Command.Island.Biome.Owner.Message"));
            soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1.0F, 1.0F);
        } else if ((island.hasRole(IslandRole.Operator, player.getUniqueId())
                && skyblock.getPermissionManager().hasPermission(island,"Biome", IslandRole.Operator))
                || island.hasRole(IslandRole.Owner, player.getUniqueId())) {
            if(biomeManager.isUpdating(island)){
                messageManager.sendMessage(player, configLoad.getString("Command.Island.Biome.InProgress.Message"));
                soundManager.playSound(player,  CompatibleSound.ENTITY_VILLAGER_NO.getSound(), 1.0F, 1.0F);
            } else {
                skyblock.getGuiManager().showGUI(player, new GuiBiome(skyblock, player, island, IslandWorld.Normal, null, false)); // TODO Nether and End support
                soundManager.playSound(player, CompatibleSound.BLOCK_CHEST_OPEN.getSound(), 1.0F, 1.0F);
            }
        } else {
            messageManager.sendMessage(player, configLoad.getString("Command.Island.Biome.Permission.Message"));
            soundManager.playSound(player,  CompatibleSound.ENTITY_VILLAGER_NO.getSound(), 1.0F, 1.0F);
        }
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage("SkyBlock | Error: You must be a player to perform that command.");
    }

    @Override
    public String getName() {
        return "biome";
    }

    @Override
    public String getInfoMessagePath() {
        return "Command.Island.Biome.Info.Message";
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
