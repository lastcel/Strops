package strops.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class StringThe extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(StringThe.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(StringThe.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(StringThe.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=65,NUM2=50,NUM3=8,TIER=1;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("NNFN3_Threshold","N1", NUM1,1,100);
    public static final IntSliderSetting GOLD=new IntSliderSetting("NNFN3_Gold","N2", NUM2,300);
    public static final IntSliderSetting HEAL=new IntSliderSetting("NNFN3_Heal","N3", NUM3,30);
    public static final IntSliderSetting MH=new IntSliderSetting("NNFN3_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("NNFN3_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("NNFN3_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(GOLD);
        settings.add(HEAL);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public StringThe() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void onVictory(){
        if(AbstractDungeon.player.gold<THRESHOLD.value){
            flash();
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.player.gainGold(GOLD.value);
            if(AbstractDungeon.player.currentHealth>0){
                AbstractDungeon.player.heal(HEAL.value,true);
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],THRESHOLD.value,GOLD.value,HEAL.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],THRESHOLD.value,GOLD.value,HEAL.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn() {
        return ( Settings.isEndless || (AbstractDungeon.floorNum <= 43) );
    }
}
