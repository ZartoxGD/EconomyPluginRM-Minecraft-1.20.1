package zartox.economypluginrm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {

    public int MoneyAmount;
    public String Name;
    public UUID Id;

    public PlayerData(Player player){
        Name = player.getName();
        Id = player.getUniqueId();
        MoneyAmount = 0;
    }

    public Player GetPlayer()
    {
        return Bukkit.getPlayerExact(Name);
    }

    public void SendMoney(PlayerData target, int amount){
        Player thisPlayer = GetPlayer();
        if(MoneyAmount >= amount){
            MoneyAmount -= amount;
            target.ReceiveMoney(target, amount);
            thisPlayer.sendMessage(ChatColor.GREEN + "You sent " + amount + "$ to " + target.Name);
            CheckMoney();
        }
        else{
            thisPlayer.sendMessage(ChatColor.RED + "You don't have " + amount + "$...");
            CheckMoney();
        }
    }

    public void BuyOffer(Offer offer){
        if(MoneyAmount >= offer.Price){
            MoneyAmount -= offer.Price;
            offer.Owner.SellOffer(offer, this);
            GetPlayer().getInventory().addItem(offer.Items);
            GetPlayer().sendMessage(ChatColor.GREEN + "You just bought: " + offer.Name + " for " + offer.Price+ "$");
            Shop.RemoveOffer(offer);
        }
        else{
            GetPlayer().sendMessage(ChatColor.YELLOW + "You don't have enough money to buy this offer");
        }
    }

    public void SellOffer(Offer offer, PlayerData buyerPlayerData){
        MoneyAmount += offer.Price;
        EconomyPluginRM.SendMessageToOnlinePlayer(GetPlayer(), ChatColor.GREEN + buyerPlayerData.Name + " just bought your " + offer.Name + "offer for " + offer.Price + "$");
        //GetPlayer().sendMessage(ChatColor.GREEN + buyerPlayerData.Name + " just bought your " + offer.Name + "offer for " + offer.Price + "$");
    }

    public void SendMoneyAnonymous(PlayerData target, int amount){
        Player thisPlayer = GetPlayer();
        if(MoneyAmount >= amount){
            MoneyAmount -= amount;
            target.ReceiveMoneyAnonymous(amount);
            thisPlayer.sendMessage(ChatColor.GREEN + "You sent " + amount + "$ to " + target.Name);
            CheckMoney();
        }
        else{
            thisPlayer.sendMessage(ChatColor.RED + "You don't have " + amount + "$...");
            CheckMoney();
        }
    }

    public void SetMoneyGreaterZero(){
        if(MoneyAmount < 0)
            MoneyAmount = 0;
    }

    public void CheckMoney(){
        Player thisPlayer = GetPlayer();
        thisPlayer.sendMessage(ChatColor.GREEN + "You have " + MoneyAmount + "$");
    }

    public void ReceiveMoney(PlayerData sender, int amount){
        MoneyAmount += amount;
        Player thisPlayer = GetPlayer();
        EconomyPluginRM.SendMessageToOnlinePlayer(thisPlayer, ChatColor.GREEN + "You received " + amount + "$ from " + sender.Name);
        //thisPlayer.sendMessage(ChatColor.GREEN + "You received " + amount + "$ from " + sender.Name);
        CheckMoney();
    }

    public void ReceiveMoneyAnonymous(int amount){
        MoneyAmount += amount;
        Player thisPlayer = GetPlayer();
        EconomyPluginRM.SendMessageToOnlinePlayer(thisPlayer, ChatColor.GREEN + "You received " + amount + "$ from someone");
        //thisPlayer.sendMessage(ChatColor.GREEN + "You received " + amount + "$ from someone");
        CheckMoney();
    }

    public void SellDiams(int amount){
        int diamsPrice = EconomyPluginRM.settings.diamondPrice;
        int price = amount * diamsPrice;
        MoneyAmount += price;
        Player thisPlayer = GetPlayer();
        thisPlayer.sendMessage(ChatColor.GREEN + "You sold " + amount + " Diamonds for " + price + "$!");
        CheckMoney();
    }
}
