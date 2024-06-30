package strops.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import strops.helpers.ModHelper;
import strops.powers.FrozenPower;

public class Blizzard extends AbstractPotion {
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
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (!m.isDeadOrEscaped()){
                addToBot(new ApplyPowerAction(m,AbstractDungeon.player,new FrozenPower(m)));
            }
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 0;
    }

    public AbstractPotion makeCopy() {
        return new Blizzard();
    }
}
