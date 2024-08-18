package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ZhelpArcaneTalents extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(ZhelpArcaneTalents.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ZhelpArcaneTalents.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=1;
    public static final int NUM2=4;
    public static final int NUM3=7;
    public static final int NUM4=2;
    public static final int NUM5=6;

    public static final IntSliderSetting COST=new IntSliderSetting("Arc_Cost", "N1", NUM1, 3);
    public static final IntSliderSetting DMG_BASE=new IntSliderSetting("Arc_Dmg_Base", "N2", NUM2, 15);
    public static final IntSliderSetting DMG_UPGRADED=new IntSliderSetting("Arc_Dmg_Upgraded", "N3", NUM3, 20);
    public static final IntSliderSetting THIRST_TURN=new IntSliderSetting("Arc_Thirst_Turn", "N4", NUM4, 1,5);
    public static final IntSliderSetting THIRST_BOOST=new IntSliderSetting("Arc_Thirst_Boost", "N5", NUM5, 1,20);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COST);
        settings.add(DMG_BASE);
        settings.add(DMG_UPGRADED);
        settings.add(THIRST_TURN);
        settings.add(THIRST_BOOST);
        return settings;
    }

    public ZhelpArcaneTalents() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], COST.value, DMG_BASE.value,DMG_UPGRADED.value,
                THIRST_TURN.value,THIRST_BOOST.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], COST.value, DMG_BASE.value,DMG_UPGRADED.value,
                THIRST_TURN.value,THIRST_BOOST.value));
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return false;
    }

    public AbstractRelic makeCopy() {return new ZhelpArcaneTalents();}
}

