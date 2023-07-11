package zartox.economypluginrm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zartox.economypluginrm.EconomyPluginRM;
import zartox.economypluginrm.Offer;
import zartox.economypluginrm.PlayerData;
import zartox.economypluginrm.Shop;

public class CommandOfferBuy implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player playerSender = (Player) sender;
            PlayerData buyerPlayerData = EconomyPluginRM.GetPlayerData(playerSender);

            if(!EconomyPluginRM.settings.enableMarket){
                playerSender.sendMessage(ChatColor.RED + "You need need to enable the market to use this command...");
                playerSender.sendMessage(ChatColor.YELLOW + "Try: /enablemarket true");
                return false;
            }

            Offer offer;

            try {
                offer = Shop.offers.get(Integer.parseInt(args[0]));
            }
            catch (Exception e){
                playerSender.sendMessage(ChatColor.YELLOW + "There is no offer with this id");
                return false;
            }

            buyerPlayerData.BuyOffer(offer);
        }
        else{
            System.out.println("Command reserved to players !");
        }

        return true;
    }
}
