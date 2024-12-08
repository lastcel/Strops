package strops.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import strops.actions.HermitsPocketsAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class HermitsPockets extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(HermitsPockets.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(HermitsPockets.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(HermitsPockets.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public static final int NUM1=7,NUM2=2,TIER=1;

    public static final IntSliderSetting BONUS=new IntSliderSetting("HermitsPockets_Bonus", "N1", NUM1, 3,12);
    public static final IntSliderSetting CHANCE=new IntSliderSetting("HermitsPockets_Chance", "S1", NUM2, 100);
    public static final IntSliderSetting MH=new IntSliderSetting("HermitsPockets_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("HermitsPockets_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("HermitsPockets_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(CHANCE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public HermitsPockets() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], BONUS.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    @Override
    public void atBattleStartPreDraw(){
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new HermitsPocketsAction(hasTriColor()));
    }
}
