package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ZhelpPartScrapper extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(ZhelpPartScrapper.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ZhelpPartScrapper.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=0,NUM2=6;

    public static final IntSliderSetting COST=new IntSliderSetting("PartScrapper_Cost", "N1", NUM1, 1);
    public static final IntSliderSetting BLOCK_CONSUMPTION=new IntSliderSetting("PartScrapper_Block_Consumption", "N2", NUM2, 2,10);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COST);
        settings.add(BLOCK_CONSUMPTION);
        return settings;
    }

    public ZhelpPartScrapper() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], COST.value, BLOCK_CONSUMPTION.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], COST.value, BLOCK_CONSUMPTION.value));
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return false;
    }
}
