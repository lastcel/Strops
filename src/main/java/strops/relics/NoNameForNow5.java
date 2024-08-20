//待研究&支持互动项：捣蛋的小狗，出口相位，荆棘，倏忽魔等

package strops.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.Abacus;
import strops.actions.EndBattleAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class NoNameForNow5 extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(NoNameForNow5.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(NoNameForNow5.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(NoNameForNow5.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public boolean activated=false,finished=false/*,everActivated=false*/;

    public static final int NUM1=4,NUM2=6,NUM3=1,TIER=2;

    public static final IntSliderSetting LOWER=new IntSliderSetting("NNFN5_Lower","N1", NUM1,1,10);
    public static final IntSliderSetting UPPER=new IntSliderSetting("NNFN5_Upper","N2", NUM2,1,10);
    public static final IntSliderSetting MAX_SHUFFLE=new IntSliderSetting("NNFN5_Max_Shuffle","N3", NUM3,3);
    public static final IntSliderSetting MH=new IntSliderSetting("NNFN5_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("NNFN5_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("NNFN5_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(LOWER);
        settings.add(UPPER);
        settings.add(MAX_SHUFFLE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public NoNameForNow5() {
        super(ID, ImageMaster.loadImage(IMG_PATH), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atPreBattle(){
        counter=0;
        secondCounter=0;
    }

    @Override
    public void atTurnStart(){
        counter++;

        if(counter==LOWER.value||counter==UPPER.value&&activated){
            beginLongPulse();
        } else {
            stopPulse();
        }

        /*
        if(counter>=LOWER.value&&counter<=UPPER.value&&!grayscale){
            //flash();
            beginLongPulse();
        }

         */
    }

    @Override
    public void onPlayerEndTurn(){
        if(activated&&counter==UPPER.value){
            //AbstractDungeon.getCurrRoom().endBattle();
            //activated=false;
            finished=true;
            addToBot(new EndBattleAction());
        }
    }

    @Override
    public void onShuffle(){
        secondCounter++;

        if(secondCounter>MAX_SHUFFLE.value){
            grayscale=true;
        }
    }

    @Override
    public void onVictory(){
        if(finished&&!grayscale){
            flash();
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.getCurrRoom().addRelicToRewards(RelicLibrary.getRelic(Abacus.ID).makeCopy());
        }

        counter=-1;
        secondCounter=-1;
        activated=false;
        finished=false;
        grayscale=false;
        stopPulse();
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],LOWER.value,UPPER.value,MAX_SHUFFLE.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],LOWER.value,UPPER.value,MAX_SHUFFLE.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn() {
        return ( Settings.isEndless || (AbstractDungeon.actNum <= 2) );
    }
}
