package strops.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.random.Random;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class GenerosityCharm extends StropsAbstractRelic implements CustomSavable<Integer> {
    public static final String ID = ModHelper.makePath(GenerosityCharm.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(GenerosityCharm.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(GenerosityCharm.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public static Random generosityCharmRng;

    public static final int NUM1=3,NUM2=4,TIER=1;

    public static final IntSliderSetting CHOICES=new IntSliderSetting("GenerosityCharm_Choices", "N1", NUM1, 2,5);
    public static final IntSliderSetting BOSS_CHOICES=new IntSliderSetting("GenerosityCharm_Boss_Choices", "N1", NUM2, 3,5);
    public static final IntSliderSetting MH=new IntSliderSetting("GenerosityCharm_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("GenerosityCharm_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("GenerosityCharm_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(CHOICES);
        settings.add(BOSS_CHOICES);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public GenerosityCharm() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public Integer onSave(){
        /*
        if(PatchGenerosityCharm.isHolding){
            PatchGenerosityCharm.isHolding=false;
            BaseMod.logger.info("存="+heldCounter);
            return heldCounter;
        }

         */
        //BaseMod.logger.info("存="+generosityCharmRng.counter);
        return generosityCharmRng.counter;
    }

    @Override
    public void onLoad(Integer savedCounter){
        generosityCharmRng = new Random(Settings.seed, savedCounter);
        //BaseMod.logger.info("读="+generosityCharmRng.counter);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        generosityCharmRng=new Random(Settings.seed);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], CHOICES.value, BOSS_CHOICES.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], CHOICES.value, BOSS_CHOICES.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.floorNum <= 54));
    }
}
