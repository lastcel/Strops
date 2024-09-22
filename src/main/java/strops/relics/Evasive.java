//patch写在了虹色之环的patch里面
package strops.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Evasive extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(Evasive.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Evasive.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Evasive.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public static final int NUM1=4,NUM2=8,NUM3=0,TIER=3;

    public static final IntSliderSetting MATCH_1=new IntSliderSetting("Evasive_Match_1", "N1", NUM1, 1,32);
    public static final IntSliderSetting MATCH_2=new IntSliderSetting("Evasive_Match_2", "N2", NUM2, 1,32);
    public static final IntSliderSetting MATCH_3=new IntSliderSetting("Evasive_Match_3", "S1", NUM3, 32);
    public static final IntSliderSetting MH=new IntSliderSetting("Evasive_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Evasive_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Evasive_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MATCH_1);
        settings.add(MATCH_2);
        settings.add(MATCH_3);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Evasive() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void atBattleStart(){
        counter=0;
    }

    @Override
    public void onVictory(){
        counter=-1;
    }

    @Override
    public boolean canSpawn() {
        if(MATCH_1.value!=1&&MATCH_1.value!=16&&MATCH_1.value!=17&&MATCH_1.value!=32&&
                MATCH_2.value!=1&&MATCH_2.value!=16&&MATCH_2.value!=17&&MATCH_2.value!=32&&
                MATCH_3.value!=1&&MATCH_3.value!=16&&MATCH_3.value!=17&&MATCH_3.value!=32){
            return (Settings.isEndless || (AbstractDungeon.floorNum <= 54));
        }
        return true;
    }

    @Override
    public String getUpdatedDescription() {
        if(MATCH_3.value==0){
            return String.format(this.DESCRIPTIONS[0], MATCH_1.value, MATCH_2.value);
        }
        return String.format(this.DESCRIPTIONS[5], MATCH_1.value, MATCH_2.value, MATCH_3.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(MATCH_3.value==0){
            str_out.add(String.format(this.DESCRIPTIONS[0], MATCH_1.value, MATCH_2.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[5], MATCH_1.value, MATCH_2.value, MATCH_3.value));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
