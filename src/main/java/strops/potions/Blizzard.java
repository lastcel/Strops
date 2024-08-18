package strops.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.powers.FrozenPower;

public class Blizzard extends AbstractStropsPotion {
    public static final String POTION_ID = ModHelper.makePath(Blizzard.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public Blizzard() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.PLACEHOLDER, PotionSize.H, PotionColor.BLUE);
        this.isThrown = true;
    }

    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void use(AbstractCreature target) {

    }

    @Override
    public void onDiscarded(){
        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                if (!m.isDeadOrEscaped()){
                    addToBot(new ApplyPowerAction(m,AbstractDungeon.player,new FrozenPower(m)));
                }
            }
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 0;
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new Blizzard();
    }
}
