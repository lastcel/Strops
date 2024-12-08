package strops.relics;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ZhelpFrugalPotion extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(ZhelpFrugalPotion.class.getSimpleName());
    //private static final String IMG_PATH = ModHelper.makeIPath(ZhelpFrugalPotion.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=90,NUM2=3,NUM3=5;

    public static final IntSliderSetting MULTIPLIER=new IntSliderSetting("FrugalPotion_Multiplier", "N1", NUM1, 99);
    public static final IntSliderSetting ARTIFACT=new IntSliderSetting("FrugalPotion_Artifact", "N2", NUM2, 1,10);
    public static final IntSliderSetting HEAL=new IntSliderSetting("FrugalPotion_Heal", "N3", NUM3, 1,20);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MULTIPLIER);
        settings.add(ARTIFACT);
        settings.add(HEAL);
        return settings;
    }

    public ZhelpFrugalPotion() {
        super(ID, ImageMaster.loadImage(TOOL_PATH), RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], MULTIPLIER.value, ARTIFACT.value, HEAL.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], MULTIPLIER.value, ARTIFACT.value, HEAL.value));
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return false;
    }

    public AbstractRelic makeCopy() {return new ZhelpFrugalPotion();}
}
