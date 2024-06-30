package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ZhelpFireTalents extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(ZhelpFireTalents.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ZhelpFireTalents.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=1;
    public static final int NUM2=10;
    public static final int NUM3=15;
    public static final int NUM4=4;
    public static final int NUM5=8;

    public static final IntSliderSetting COST=new IntSliderSetting("Fir_COST", "N1", NUM1, 3);
    public static final IntSliderSetting MGC_BASE=new IntSliderSetting("Fir_MGC_BASE", "N2", NUM2, 22);
    public static final IntSliderSetting MGC_UPGRADED=new IntSliderSetting("Fir_MGC_UPGRADED", "N3", NUM3, 30);
    public static final IntSliderSetting THIRST_TURN=new IntSliderSetting("Fir_THIRST_TURN", "N4", NUM4, 1,10);
    public static final IntSliderSetting THIRST_BOOST=new IntSliderSetting("Fir_THIRST_BOOST", "N5", NUM5, 1,50);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COST);
        settings.add(MGC_BASE);
        settings.add(MGC_UPGRADED);
        settings.add(THIRST_TURN);
        settings.add(THIRST_BOOST);
        return settings;
    }

    public ZhelpFireTalents() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], COST.value, MGC_BASE.value,MGC_UPGRADED.value,
                THIRST_TURN.value,THIRST_BOOST.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], COST.value, MGC_BASE.value,MGC_UPGRADED.value,
                THIRST_TURN.value,THIRST_BOOST.value));
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return false;
    }

    public AbstractRelic makeCopy() {return new ZhelpFireTalents();}
}
