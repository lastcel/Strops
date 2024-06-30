package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ZhelpLightningStorm extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(ZhelpLightningStorm.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ZhelpLightningStorm.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=2;
    public static final int NUM2=3;
    public static final int NUM3=1;

    public static final IntSliderSetting COST=new IntSliderSetting("LightningStorm_Cost", "N1", NUM1, 5);
    public static final IntSliderSetting MULTIPLIER=new IntSliderSetting("LightningStorm_Multiplier", "N2", NUM2, 1,5);
    public static final IntSliderSetting FIXED_PART=new IntSliderSetting("LightningStorm_Fixed_Part", "N3", NUM3, 2);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COST);
        settings.add(MULTIPLIER);
        settings.add(FIXED_PART);
        return settings;
    }

    public ZhelpLightningStorm() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], COST.value, MULTIPLIER.value, FIXED_PART.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], COST.value, MULTIPLIER.value, FIXED_PART.value));
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return false;
    }

    public AbstractRelic makeCopy() {return new ZhelpLightningStorm();}
}
