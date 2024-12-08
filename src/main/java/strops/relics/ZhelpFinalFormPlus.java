package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ZhelpFinalFormPlus extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(ZhelpFinalFormPlus.class.getSimpleName());
    //private static final String IMG_PATH = ModHelper.makeIPath(ZhelpFinalFormPlus.class.getSimpleName());
    private static final AbstractRelic.RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final AbstractRelic.LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=1,NUM2=30,NUM3=30,NUM4=15,NUM6=30;

    public static final IntSliderSetting COST=new IntSliderSetting("FinalFormPlus_Cost", "N1", NUM1, 3);
    public static final IntSliderSetting DMG=new IntSliderSetting("FinalFormPlus_Dmg", "N2", NUM2, 50);
    public static final IntSliderSetting BLK=new IntSliderSetting("FinalFormPlus_Blk", "N3", NUM3, 50);
    public static final IntSliderSetting BONUS_INSTANT=new IntSliderSetting("FinalFormPlus_Bonus_Instant", "N4", NUM4, 50);
    public static final IntSliderSetting UNUSED_SUSPENSE_TURN=new IntSliderSetting("Unused_FinalFormPlus_Suspense_Turn", "N5?", -1, -1,-1);
    public static final IntSliderSetting BONUS_DEPENDANT=new IntSliderSetting("FinalFormPlus_Bonus_Dependant", "N6", NUM6, 50);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COST);
        settings.add(DMG);
        settings.add(BLK);
        settings.add(BONUS_INSTANT);
        settings.add(UNUSED_SUSPENSE_TURN);
        settings.add(BONUS_DEPENDANT);
        return settings;
    }

    public ZhelpFinalFormPlus() {
        super(ID, ImageMaster.loadImage(TOOL_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], COST.value, DMG.value, BLK.value, BONUS_INSTANT.value, BONUS_DEPENDANT.value, ZhelpFinalForm.SUSPENSE_TURN.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(DESCRIPTIONS[0], COST.value, DMG.value, BLK.value, BONUS_INSTANT.value, BONUS_DEPENDANT.value, ZhelpFinalForm.SUSPENSE_TURN.value));
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return false;
    }
}
