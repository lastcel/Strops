package strops.potions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.powers.CombatwiseDoubleDamagePower;
import strops.relics.SunflowerInASummer;

public class PhantasmalShootingStar extends AbstractPotion {
    public static final String POTION_ID = ModHelper.makePath(PhantasmalShootingStar.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public float normalizedPotency;

    public PhantasmalShootingStar() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.PLACEHOLDER, PotionSize.FAIRY, PotionColor.SWIFT);
        this.isThrown = false;
    }

    public void initializeData() {
        this.potency = getPotency();
        normalizedPotency=(float)potency/10;
        if(normalizedPotency!=MathUtils.floor(normalizedPotency)){
            this.description = String.format(potionStrings.DESCRIPTIONS[0], normalizedPotency);
        } else {
            this.description = String.format(potionStrings.DESCRIPTIONS[1], normalizedPotency);
        }
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void use(AbstractCreature target) {
        AbstractPlayer p = AbstractDungeon.player;
        if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT){
            addToBot(new ApplyPowerAction(p, p, new CombatwiseDoubleDamagePower(p, normalizedPotency)));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return SunflowerInASummer.BONUS.value;
    }

    public AbstractPotion makeCopy() {
        return new PhantasmalShootingStar();
    }
}
