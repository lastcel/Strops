package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ZhelpGreedyPotion extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(ZhelpGreedyPotion.class.getSimpleName());
    //private static final String IMG_PATH = ModHelper.makeIPath(ZhelpGreedyPotion.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=112,NUM2=1,NUM3=1,NUM4=1,NUM5=150;

    public static final IntSliderSetting FLOWING=new IntSliderSetting("GreedyPotion_Flowing", "N1", NUM1, 101,150);
    public static final IntSliderSetting WEAK=new IntSliderSetting("GreedyPotion_Weak", "N2", NUM2, 3);
    public static final IntSliderSetting FRAIL=new IntSliderSetting("GreedyPotion_Frail", "N3", NUM3, 3);
    public static final IntSliderSetting VULNERABLE=new IntSliderSetting("GreedyPotion_Vulnerable", "N4", NUM4, 3);
    public static final IntSliderSetting DISCARDING=new IntSliderSetting("GreedyPotion_Discarding", "N5", NUM5, 101,200);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(FLOWING);
        settings.add(WEAK);
        settings.add(FRAIL);
        settings.add(VULNERABLE);
        settings.add(DISCARDING);
        return settings;
    }

    public ZhelpGreedyPotion() {
        super(ID, ImageMaster.loadImage(TOOL_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], FLOWING.value, WEAK.value, FRAIL.value, VULNERABLE.value, DISCARDING.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], FLOWING.value, WEAK.value, FRAIL.value, VULNERABLE.value, DISCARDING.value));
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return false;
    }

    public AbstractRelic makeCopy() {return new ZhelpGreedyPotion();}
}
