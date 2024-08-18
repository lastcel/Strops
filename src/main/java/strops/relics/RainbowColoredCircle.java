package strops.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class RainbowColoredCircle extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(RainbowColoredCircle.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(RainbowColoredCircle.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(RainbowColoredCircle.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=3,TIER=2;

    public static final IntSliderSetting MATCH=new IntSliderSetting("RCC_Match", "N1", NUM1, 2,5);
    public static final IntSliderSetting MH=new IntSliderSetting("RCC_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("RCC_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("RCC_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MATCH);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public RainbowColoredCircle() {
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
    public void atTurnStart(){
        counter=0;
    }

    /*
    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS &&
                info.type != DamageInfo.DamageType.HP_LOSS) {
            counter++;
            if(counter==MATCH.value){
                flash();
                grayscale=true;
                return 0;
            }
        }

        return damageAmount;
    }

     */

    @Override
    public void onVictory(){
        counter=-1;
    }

    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.floorNum <= 54));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], MATCH.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], MATCH.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
