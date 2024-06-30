package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class DarkEnergyLantern extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(DarkEnergyLantern.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(DarkEnergyLantern.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(DarkEnergyLantern.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=1,NUM2=2;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("DEL_Threshold_v0.12.0", "N1", NUM1, 1,4);
    public static final IntSliderSetting BONUS=new IntSliderSetting("DEL_Bonus_v0.12.0", "N2", NUM2, 1,4);
    public static final IntSliderSetting MH=new IntSliderSetting("DEL_MH_v0.12.0","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("DEL_G_v0.12.0","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public DarkEnergyLantern() {
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
