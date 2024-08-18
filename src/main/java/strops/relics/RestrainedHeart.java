package strops.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.utilities.StaticHelpers;

import java.util.ArrayList;

public class RestrainedHeart extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(RestrainedHeart.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(RestrainedHeart.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(RestrainedHeart.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    //public boolean isEffective=true;

    public static final int TIER=3;

    public static final IntSliderSetting MH=new IntSliderSetting("RestrainedHeart_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("RestrainedHeart_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("RestrainedHeart_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public RestrainedHeart() {
        super(ID, ImageMaster.loadImage(IMG_PATH), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void onRightClick() {
        if(StaticHelpers.canClickRelic2(this)){
            if(grayscale){
                counter=-1;
                grayscale=false;
            } else {
                counter=-2;
                grayscale=true;
            }
            //isEffective=!isEffective;
        }
    }

    @Override
    public void setCounter(int counter){
        this.counter=counter;
        grayscale= (this.counter != -1);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(this.DESCRIPTIONS[0]);
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
