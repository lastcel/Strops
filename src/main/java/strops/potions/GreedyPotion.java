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
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import strops.helpers.ModHelper;
import strops.relics.ZhelpGreedyPotion;

import static strops.relics.ZhelpGreedyPotion.*;

public class GreedyPotion extends AbstractStropsPotion{
    public static final String POTION_ID = ModHelper.makePath(GreedyPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public GreedyPotion() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.PLACEHOLDER, PotionSize.EYE, PotionColor.ENERGY);
        this.isThrown = false;
    }

    public void initializeData() {
        this.potency = getPotency();
        this.description = String.format(potionStrings.DESCRIPTIONS[0],FLOWING.value, WEAK.value, FRAIL.value, VULNERABLE.value, potency);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void use(AbstractCreature target) {

    }

    @Override
    public void atPreBattleFlowing(){
        flash();
        AbstractPlayer p = AbstractDungeon.player;
        p.gainGold(MathUtils.floor(p.gold*(ZhelpGreedyPotion.FLOWING.value/100.0f-1)));
    }

    @Override
    public void atBattleStartFlowing(){
        AbstractPlayer p = AbstractDungeon.player;
        for(int i=0;i<ZhelpGreedyPotion.WEAK.value;i++){
            addToTop(new ApplyPowerAction(p,p,new WeakPower(p,2,false),2));
        }
        for(int i=0;i<ZhelpGreedyPotion.FRAIL.value;i++){
            addToTop(new ApplyPowerAction(p,p,new FrailPower(p,2,false),2));
        }
        for(int i=0;i<ZhelpGreedyPotion.VULNERABLE.value;i++){
            addToTop(new ApplyPowerAction(p,p,new VulnerablePower(p,2,false),2));
        }
    }

    @Override
    public void onDiscarded(){
        AbstractDungeon.player.gainGold(MathUtils.floor(AbstractDungeon.player.gold*(potency/100.0f-1)));
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return ZhelpGreedyPotion.DISCARDING.value;
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new GreedyPotion();
    }
}
