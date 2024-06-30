package strops.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class DelayedGratification extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(DelayedGratification.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(DelayedGratification.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(DelayedGratification.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.BOSS;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=2;

    public static boolean canBeDelayed=false;
    private boolean firstTurn = true;

    public static final IntSliderSetting BONUS=new IntSliderSetting("DelayedEnough_Bonus", "N1", NUM1, 4);
    public static final IntSliderSetting MH=new IntSliderSetting("DelayedEnough_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("DelayedEnough_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public DelayedGratification() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        AbstractDungeon.player.energy.energyMaster++;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    @Override
    public void atPreBattle() {
        this.firstTurn = true;
        counter=0;
    }

    @Override
    public void atTurnStart(){
        if(this.firstTurn){
            if(BONUS.value>=1){
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                addToBot(new GainEnergyAction(BONUS.value));
            }
            this.firstTurn=false;
        } else {
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new GainEnergyAction(counter));
            counter=0;
        }
    }

    @Override
    public void onVictory(){
        counter=-1;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], BONUS.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
