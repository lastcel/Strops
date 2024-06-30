package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;


public class Atri extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(Atri.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Atri.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Atri.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SHOP;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=3,NUM2=1;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("Atri_Threshold", "N1", NUM1, 6);
    public static final IntSliderSetting BONUS=new IntSliderSetting("Atri_Bonus", "N2", NUM2, 5);
    public static final IntSliderSetting MH=new IntSliderSetting("Atri_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Atri_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public Atri() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        canCopy=false;
    }

    public void onVictory() {
        counter = -1;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    /*
    @Override
    public boolean canSpawn(){
        return !AbstractDungeon.player.hasRelic(ModHelper.makePath(Lemonade.class.getSimpleName()));
    }

     */

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], THRESHOLD.value, BONUS.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value, BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
