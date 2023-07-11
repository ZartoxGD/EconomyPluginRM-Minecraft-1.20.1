package zartox.economypluginrm;

import org.bukkit.inventory.ItemStack;
import zartox.economypluginrm.serializableObjects.SerializableOffer;

public class Offer {

    public String Name;
    public int Price;
    public ItemStack Items;
    public PlayerData Owner;
    public int Id;
    public boolean IsAnon;

    public Offer(){

    }

    public Offer(SerializableOffer serializableOffer){
        Name = serializableOffer.Name;
        Price = serializableOffer.Price;
        Items = new ItemStack(serializableOffer.ItemType, serializableOffer.ItemAmount);
        Owner = EconomyPluginRM.GetPlayerDataByName(serializableOffer.OwnerName);
        Id = serializableOffer.Id;
        IsAnon = serializableOffer.IsAnon;
    }

    public Offer(int id, String name, int price, ItemStack items, PlayerData owner, boolean isAnon){
        Id = id;
        Name = name;
        Price = price;
        Items = items;
        Owner = owner;
        IsAnon = isAnon;
    }

    public int GetPricePerUnit(){
        return Price / Items.getAmount();
    }

}
