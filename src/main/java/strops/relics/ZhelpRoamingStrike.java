package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ZhelpRoamingStrike extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(ZhelpRoamingStrike.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ZhelpRoamingStrike.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=6,NUM2=8;

    public static final IntSliderSetting MULTIPLIER_BASE=new IntSliderSetting("RoamingStrike_Multiplier_Base", "N1", NUM1, 10);
    public static final IntSliderSetting MULTIPLIER_UPGRADED=new IntSliderSetting("RoamingStrike_Multiplier_Upgraded", "N2", NUM2, 15);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MULTIPLIER_BASE);
        settings.add(MULTIPLIER_UPGRADED);
        return settings;
    }

    public ZhelpRoamingStrike() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], MULTIPLIER_BASE.value, MULTIPLIER_UPGRADED.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], MULTIPLIER_BASE.value, MULTIPLIER_UPGRADED.value));
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return false;
    }
}
