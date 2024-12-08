package strops.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import strops.helpers.ModHelper;
import strops.utilities.FeedOption;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Gluttony extends StropsAbstractRelic implements CustomSavable<Integer> {
    public static final String ID = ModHelper.makePath(Gluttony.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Gluttony.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Gluttony.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public int level;

    public static final int NUM1=80,NUM2=40,NUM3=3,TIER=3;

    public static final IntSliderSetting INITIAL=new IntSliderSetting("Gluttony_Initial","N1", NUM1,100);
    public static final IntSliderSetting STEP=new IntSliderSetting("Gluttony_Step","N2", NUM2,100);
    public static final IntSliderSetting BONUS=new IntSliderSetting("Gluttony_Bonus","N3", NUM3,6);
    public static final IntSliderSetting MH=new IntSliderSetting("Gluttony_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Gluttony_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Gluttony_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(INITIAL);
        settings.add(STEP);
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Gluttony() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        //canCopy=false;
    }

    @Override
    public Integer onSave() {
        return level;
    }

    @Override
    public void onLoad(Integer savedLevel){
        level=savedLevel;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        level=0;
        counter=0;
    }

    @Override
    public void update() {
        super.update();
        if(isObtained&&counter!=-1){
            counter=AbstractDungeon.player.maxHealth*(INITIAL.value-level*STEP.value)/100;
            if(counter<=1){
                counter=-1;
                updateDesc();
            }
            grayscale=(AbstractDungeon.player.currentHealth<counter);
        }
    }

    @Override
    public void atPreBattle(){
        if(!grayscale){
            flash();
            AbstractPlayer p=AbstractDungeon.player;
            addToTop(new ApplyPowerAction(p,p,new DexterityPower(p,BONUS.value),BONUS.value));
            addToTop(new RelicAboveCreatureAction(p,this));
        }
    }

    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options){
        if(counter!=-1){
            options.add(new FeedOption(true,this));
        }
    }

    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.actNum <= 3));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],INITIAL.value,BONUS.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(counter!=-1){
            str_out.add(String.format(this.DESCRIPTIONS[0],INITIAL.value,BONUS.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[7],INITIAL.value,BONUS.value));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public void setCounter(int setCounter){
        counter=setCounter;
        updateDesc();
    }
}
