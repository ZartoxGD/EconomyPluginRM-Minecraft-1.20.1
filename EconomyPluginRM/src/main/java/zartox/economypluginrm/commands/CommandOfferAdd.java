package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.Offer;
import zartox.economypluginrm.PlayerData;
import zartox.economypluginrm.Shop;

public class CommandOfferAdd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player playerSender = (Player) sender;
        PlayerData playerData = EconomyPluginRM.GetPlayerData(playerSender);
        ItemStack itemStack = playerSender.getInventory().getItemInMainHand();
        int offerId = Shop.offers.size();

        if(!EconomyPluginRM.settings.enableMarket){
            playerSender.sendMessage(ChatColor.RED + "You need need to enable the market to use this command...");
            playerSender.sendMessage(ChatColor.YELLOW + "Try: /enablemarket true");
            return false;
        }

        if(itemStack == null || itemStack.getType() == Material.AIR){
            playerSender.sendMessage(ChatColor.RED + "There is no item in your hand...");
            return false;
        }

        if(!(sender instanceof Player)){
            System.out.println("Command reserved to players !");
            return false;
        }

        if(args.length < 3){
            playerSender.sendMessage(ChatColor.RED + "Invalid args ! Try: /offeradd Name Price IsAnon");
            return false;
        }

        Offer offer = new Offer(offerId, args[0], Integer.parseInt(args[1]), itemStack, playerData, Boolean.parseBoolean(args[2]));
        playerSender.getInventory().setItemInMainHand(null);
        Shop.AddOffer(offer);
        playerSender.sendMessage(ChatColor.GREEN + "Offer created !");

        return true;
    }
}
