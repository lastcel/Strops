//This file (and more related ones) is mainly copied from the Blackbeard mod,
// credits to JohnnyBazooka89!
package strops.cards;

import basemod.abstracts.CustomCard;
import strops.helpers.ModHelper;
import strops.interfaces.IKeyNumber1;
import strops.interfaces.IKeyNumber2;
import strops.relics.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractStropsCard extends CustomCard implements IKeyNumber1, IKeyNumber2 {
    public AbstractStropsCard(String id, String name, String img, int cost, String description, CardType type, CardColor color, CardRarity rarity, CardTarget target){
        super(id,name,img,cost,description,type,color,rarity,target);
    }

    protected int baseKeyNumber1,baseKeyNumber2 = -1;
    public int keyNumber1,keyNumber2 = -1;
    protected boolean upgradedKeyNumber1,upgradedKeyNumber2 = false;
    private boolean isKeyNumber1Modified,isKeyNumber2Modified = false;

    public static final Set<String> CANNON_RELICS = new HashSet<>(Arrays.asList(
            ModHelper.makePath(SoulCannon.class.getSimpleName()),
            ModHelper.makePath(SoulCannonOneTiny.class.getSimpleName()),
            ModHelper.makePath(SoulCannonOneHuge.class.getSimpleName()),
            ModHelper.makePath(SoulCannonTwoTiny.class.getSimpleName()),
            ModHelper.makePath(SoulCannonTwoHuge.class.getSimpleName()),
            ModHelper.makePath(SoulCannonThreeTiny.class.getSimpleName()),
            ModHelper.makePath(SoulCannonThreeHuge.class.getSimpleName()),
            SoulCannonFour.ID
            ));

    @Override
    public int keyNumber1() {
        return this.keyNumber1;
    }

    @Override
    public int baseKeyNumber1() {
        return this.baseKeyNumber1;
    }

    @Override
    public boolean upgradedKeyNumber1() {
        return upgradedKeyNumber1;
    }

    @Override
    public boolean isKeyNumber1Modified() {
        return this.isKeyNumber1Modified;
    }

    @Override
    public int keyNumber2() {return this.keyNumber2;}

    @Override
    public int baseKeyNumber2() {
        return this.baseKeyNumber2;
    }

    @Override
    public boolean upgradedKeyNumber2() {
        return upgradedKeyNumber2;
    }

    @Override
    public boolean isKeyNumber2Modified() {
        return this.isKeyNumber2Modified;
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        this.keyNumber1 = this.baseKeyNumber1;
        this.keyNumber2 = this.baseKeyNumber2;
        this.isKeyNumber1Modified = false;
        this.isKeyNumber2Modified = false;
    }

    @Override
    public void displayUpgrades() {
        super.displayUpgrades();
        if (this.upgradedKeyNumber1) {
            this.keyNumber1 = this.baseKeyNumber1;
            this.isKeyNumber1Modified = true;
        }
        if (this.upgradedKeyNumber2) {
            this.keyNumber2 = this.baseKeyNumber2;
            this.isKeyNumber2Modified = true;
        }
    }

    protected void upgradeKeyNumber1(int amount) {
        this.baseKeyNumber1 += amount;
        this.keyNumber1 = baseKeyNumber1;
        this.upgradedKeyNumber1 = true;
    }

    protected void upgradeKeyNumber2(int amount) {
        this.baseKeyNumber2 += amount;
        this.keyNumber2 = baseKeyNumber2;
        this.upgradedKeyNumber2 = true;
    }
}
