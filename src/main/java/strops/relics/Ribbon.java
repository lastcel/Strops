package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Ribbon extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(Ribbon.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Ribbon.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Ribbon.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=2;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("Ribbon_Threshold", "N1", NUM1, 1,4);
    public static final IntSliderSetting MH=new IntSliderSetting("Ribbon_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Ribbon_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public Ribbon() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=0;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], THRESHOLD.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
