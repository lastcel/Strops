package strops.utilities;

public class ExtendedCardSave {
    public int upgrades;

    public int misc;

    public String id;

    public boolean savedEverPreUpgrade;

    public boolean savedEverCounted;

    public boolean isSelfRetain;

    public ExtendedCardSave(String cardID, int timesUpgraded, int misc,
                            boolean savedEverPreUpgrade, boolean savedEverCounted, boolean selfRetain) {
        this.id = cardID;
        this.upgrades = timesUpgraded;
        this.misc = misc;
        this.savedEverPreUpgrade=savedEverPreUpgrade;
        this.savedEverCounted=savedEverCounted;
        this.isSelfRetain=selfRetain;
    }
}
