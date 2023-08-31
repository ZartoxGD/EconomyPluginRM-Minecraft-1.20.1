package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.PhysicalShop;
import zartox.economypluginrm.PrivateChest;

import java.util.Objects;

public class CommandListChests implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            if(!player.isOp()){
                player.sendMessage(ChatColor.RED + "Admin reserved command !");
                return false;
            }

            player.sendMessage(ChatColor.GREEN + "Chest List:");
            player.sendMessage(ChatColor.BLUE + "-----------------------------------------------------");

            for(PrivateChest chest : EconomyPluginRM.privateChests){
                player.sendMessage(ChatColor.GREEN + "Id: " + EconomyPluginRM.physicalShops.indexOf(chest));
                player.sendMessage(ChatColor.GREEN + "Owner: " + chest.OwnerName);

                if(!Objects.equals(chest.AuthorizedPlayersName[0], "") && !Objects.equals(chest.AuthorizedPlayersName[1], "") && !Objects.equals(chest.AuthorizedPlayersName[2], "")){
                    player.sendMessage(ChatColor.GREEN + "Authorized players: ");
                    for(String playerName : chest.AuthorizedPlayersName){
                        player.sendMessage(ChatColor.GREEN + playerName);
                    }
                }

                player.sendMessage(ChatColor.GREEN + "Position: X:" + chest.ChestPosition[0] + " Y:" + chest.ChestPosition[1] + " Z:" + chest.ChestPosition[2]);
                player.sendMessage(ChatColor.BLUE + "-----------------------------------------------------");
            }
            return true;
        }
        else{
            System.out.println("Chest List:");
            System.out.println("-----------------------------------------------------");

            for(PrivateChest chest : EconomyPluginRM.privateChests){
                System.out.println("Id: " + EconomyPluginRM.physicalShops.indexOf(chest));
                System.out.println("Owner: " + chest.OwnerName);

                if(!Objects.equals(chest.AuthorizedPlayersName[0], "") && !Objects.equals(chest.AuthorizedPlayersName[1], "") && !Objects.equals(chest.AuthorizedPlayersName[2], "")) {
                    System.out.println("Authorized players: ");
                    for(String playerName : chest.AuthorizedPlayersName){
                        System.out.println(playerName);
                    }
                }

                System.out.println("Position: X:" + chest.ChestPosition[0] + " Y:" + chest.ChestPosition[1] + " Z:" + chest.ChestPosition[2]);
                System.out.println("-----------------------------------------------------");
            }
            return true;
        }
    }
}
