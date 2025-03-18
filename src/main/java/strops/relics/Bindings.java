package strops.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.actions.SetBlockAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Bindings extends StropsAbstractRelic implements OnAfterUseCardRelic {
    public static final String ID = ModHelper.makePath(Bindings.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Bindings.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Bindings.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public static final int NUM1=1,NUM2=3,NUM3=1,TIER=1;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("Bindings_Threshold", "N1", NUM1, 1,3);
    public static final IntSliderSetting BONUS=new IntSliderSetting("Bindings_Bonus", "N2", NUM2, 1,9);
    public static final IntSliderSetting DRAWBACK=new IntSliderSetting("Bindings_Drawback", "N3", NUM3, 1,9);
    public static final IntSliderSetting MH=new IntSliderSetting("Bindings_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Bindings_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Bindings_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(BONUS);
        settings.add(DRAWBACK);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Bindings() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atTurnStart(){
        counter=0;
        beginLongPulse();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if(card.type!=AbstractCard.CardType.ATTACK||
                !(card.target==AbstractCard.CardTarget.ENEMY||
                        card.target==AbstractCard.CardTarget.SELF_AND_ENEMY)){
            return;
        }
        counter++;
        if(counter==THRESHOLD.value){
            stopPulse();
        }
        if(counter>THRESHOLD.value){
            return;
        }

        flash();
        AbstractCreature cr=action.target;
        addToBot(new RelicAboveCreatureAction(cr, this));
        addToBot(new SetBlockAction(cr,DRAWBACK.value));
    }

    @Override
    public void onVictory(){
        counter=-1;
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], THRESHOLD.value, BONUS.value, DRAWBACK.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value, BONUS.value, DRAWBACK.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
