package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ZhelpFinalForm extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(ZhelpFinalForm.class.getSimpleName());
    //private static final String IMG_PATH = ModHelper.makeIPath(ZhelpFinalForm.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=1,NUM2=20,NUM3=20,NUM4=10,NUM5=6,NUM6=20;

    public static final IntSliderSetting COST=new IntSliderSetting("FinalForm_Cost", "N1", NUM1, 3);
    public static final IntSliderSetting DMG=new IntSliderSetting("FinalForm_Dmg", "N2", NUM2, 30);
    public static final IntSliderSetting BLK=new IntSliderSetting("FinalForm_Blk", "N3", NUM3, 30);
    public static final IntSliderSetting BONUS_INSTANT=new IntSliderSetting("FinalForm_Bonus_Instant", "N4", NUM4, 30);
    public static final IntSliderSetting SUSPENSE_TURN=new IntSliderSetting("FinalForm_Suspense_Turn", "N5", NUM5, 1,10);
    public static final IntSliderSetting BONUS_DEPENDANT=new IntSliderSetting("FinalForm_Bonus_Dependant", "N6", NUM6, 30);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COST);
        settings.add(DMG);
        settings.add(BLK);
        settings.add(BONUS_INSTANT);
        settings.add(SUSPENSE_TURN);
        settings.add(BONUS_DEPENDANT);
        return settings;
    }

    public ZhelpFinalForm() {
        super(ID, ImageMaster.loadImage(TOOL_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(DESCRIPTIONS[0], COST.value, DMG.value, BLK.value, BONUS_INSTANT.value, BONUS_DEPENDANT.value, SUSPENSE_TURN.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(DESCRIPTIONS[0], COST.value, DMG.value, BLK.value, BONUS_INSTANT.value, BONUS_DEPENDANT.value, SUSPENSE_TURN.value));
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return false;
    }
}
