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
    private static final String IMG_PATH_O = ModHelper.makeOPath(SoulStitch.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public SoulStitch() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    public static final int MAXHP=-18,NUM1=2,NUM2=10,TIER=5;

    //public static final IntSliderSetting PENALTY=new IntSliderSetting("SoulStitch_Penalty", "N1", NUM1, 30);
    public static final IntSliderSetting SUBTRAHEND=new IntSliderSetting("SoulStitch_Subtrahend_v0.13.4", "N1", NUM1, -3,5);
    public static final IntSliderSetting MULTIPLIER=new IntSliderSetting("SoulStitch_Multiplier_v0.13.4", "N2", NUM2, 1,15);
    public static final IntSliderSetting MH=new IntSliderSetting("SoulStitch_MH_v0.13.4","MH", MAXHP,-40,20);
    public static final IntSliderSetting G=new IntSliderSetting("SoulStitch_G_v0.13.4","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("SoulStitch_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(SUBTRAHEND);
        settings.add(MULTIPLIER);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
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
        //AbstractDungeon.player.decreaseMaxHealth(PENALTY.value);
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
        AbstractDungeon.player.heal(healAmt, true);
        setCounter(-2);
    }

    public String getUpdatedDescription() {
        if(SUBTRAHEND.value>=0){
            return String.format(this.DESCRIPTIONS[0],SUBTRAHEND.value,MULTIPLIER.value);
        }
        return String.format(this.DESCRIPTIONS[5],-SUBTRAHEND.value,MULTIPLIER.value);
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(SUBTRAHEND.value>=0){
            str_out.add(String.format(this.DESCRIPTIONS[0],SUBTRAHEND.value,MULTIPLIER.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[5],-SUBTRAHEND.value,MULTIPLIER.value));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
