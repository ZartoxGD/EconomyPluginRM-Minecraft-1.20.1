package zartox.economypluginrm.serializableObjects;

import org.bukkit.Material;
import zartox.economypluginrm.Offer;

public class SerializableOffer {

    public String Name;
    public int Price;
    public int Id;
    public boolean IsAnon;
    public String OwnerName;
    public Material ItemType;
    public int ItemAmount;

    public SerializableOffer(Offer offer){
        Id = offer.Id;
        Name = offer.Name;
        Price = offer.Price;
        IsAnon = offer.IsAnon;
        OwnerName = offer.Owner.Name;
        ItemType = offer.Items.getType();
        ItemAmount = offer.Items.getAmount();
    }

}
