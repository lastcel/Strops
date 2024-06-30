package strops.relics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class WhiteDClips extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(WhiteDClips.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(WhiteDClips.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(WhiteDClips.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=20;

    public static final IntSliderSetting BONUS=new IntSliderSetting("WhiteDClips_Bonus", "10xN1", NUM1, 11,40);
    public static final IntSliderSetting MH=new IntSliderSetting("WhiteDClips_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("WhiteDClips_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public WhiteDClips() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public boolean canSpawn(){
        return Settings.isEndless|| AbstractDungeon.floorNum<=54;
    }

    @Override
    public String getUpdatedDescription() {
        float multiplier=(float) BONUS.value/10;
        if(multiplier!=MathUtils.floor(multiplier)){
            return String.format(this.DESCRIPTIONS[0], multiplier);
        }
        return String.format(this.DESCRIPTIONS[5], multiplier);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        float multiplier=(float) BONUS.value/10;
        if(multiplier!=MathUtils.floor(multiplier)){
            str_out.add(String.format(this.DESCRIPTIONS[0], multiplier));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[5], multiplier));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
