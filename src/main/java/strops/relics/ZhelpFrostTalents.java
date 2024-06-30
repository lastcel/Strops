package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ZhelpFrostTalents extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(ZhelpFrostTalents.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ZhelpFrostTalents.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=1;
    public static final int NUM2=5;
    public static final int NUM3=9;
    public static final int NUM4=3;
    public static final int NUM5=7;

    public static final IntSliderSetting COST=new IntSliderSetting("Fro_COST", "N1", NUM1, 3);
    public static final IntSliderSetting BLK_BASE=new IntSliderSetting("Fro_BLK_BASE", "N2", NUM2, 18);
    public static final IntSliderSetting BLK_UPGRADED=new IntSliderSetting("Fro_BLK_UPGRADED", "N3", NUM3, 24);
    public static final IntSliderSetting THIRST_TURN=new IntSliderSetting("Fro_THIRST_TURN", "N4", NUM4, 1,7);
    public static final IntSliderSetting THIRST_BOOST=new IntSliderSetting("Fro_THIRST_BOOST", "N5", NUM5, 1,25);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COST);
        settings.add(BLK_BASE);
        settings.add(BLK_UPGRADED);
        settings.add(THIRST_TURN);
        settings.add(THIRST_BOOST);
        return settings;
    }

    public ZhelpFrostTalents() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], COST.value, BLK_BASE.value,BLK_UPGRADED.value,
                THIRST_TURN.value,THIRST_BOOST.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], COST.value, BLK_BASE.value,BLK_UPGRADED.value,
                THIRST_TURN.value,THIRST_BOOST.value));
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return false;
    }

    public AbstractRelic makeCopy() {return new ZhelpArcaneTalents();}
}
