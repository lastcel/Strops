package strops.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class SoulStitch extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(SoulStitch.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SoulStitch.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public SoulStitch() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    public static final int NUM1=18,NUM2=2,NUM3=10;

    public static final IntSliderSetting PENALTY=new IntSliderSetting("SoulStitch_Penalty", "N1", NUM1, 30);
    public static final IntSliderSetting SUBTRAHEND=new IntSliderSetting("SoulStitch_Subtrahend", "N2", NUM2, -3,5);
    public static final IntSliderSetting MULTIPLIER=new IntSliderSetting("SoulStitch_Multiplier", "N3", NUM3, 1,15);
    public static final IntSliderSetting MH=new IntSliderSetting("SoulStitch_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SoulStitch_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(PENALTY);
        settings.add(SUBTRAHEND);
        settings.add(MULTIPLIER);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    @Override
    public void setCounter(int setCounter) {
        if (setCounter == -2) {
            usedUp();
            this.counter = -2;
        }
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        AbstractDungeon.player.decreaseMaxHealth(PENALTY.value);
    }

    @Override
    public void onTrigger(){
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        int getNum = 0;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.type == AbstractCard.CardType.ATTACK)
            {
                getNum++;
            }
        }
        AbstractDungeon.player.maxHealth= Math.max(MULTIPLIER.value*(getNum- SUBTRAHEND.value),1);

        int healAmt = AbstractDungeon.player.maxHealth;
        if (healAmt < 1)
            healAmt = 1;
        AbstractDungeon.player.heal(healAmt, true);
        setCounter(-2);
    }

    public String getUpdatedDescription() {
        if(SUBTRAHEND.value>=0){
            return String.format(this.DESCRIPTIONS[0],PENALTY.value,SUBTRAHEND.value,MULTIPLIER.value);
        }
        return String.format(this.DESCRIPTIONS[5],PENALTY.value,-SUBTRAHEND.value,MULTIPLIER.value);
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(SUBTRAHEND.value>=0){
            str_out.add(String.format(this.DESCRIPTIONS[0],PENALTY.value,SUBTRAHEND.value,MULTIPLIER.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[5],PENALTY.value,-SUBTRAHEND.value,MULTIPLIER.value));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
