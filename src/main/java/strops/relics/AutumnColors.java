package strops.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class AutumnColors extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(AutumnColors.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(AutumnColors.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(SquirrelsRelief.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public static final int NUM1=1,NUM2=1,TIER=tier2Num(RELIC_TIER);

    public static final IntSliderSetting IN=new IntSliderSetting("AutumnColors_In","N1",NUM1,1,5);
    public static final IntSliderSetting OUT=new IntSliderSetting("AutumnColors_Out","N2",NUM2,1,5);
    public static final IntSliderSetting MH=new IntSliderSetting("AutumnColors_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("AutumnColors_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("AutumnColors_R","R", TIER,1,3);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(IN);
        settings.add(OUT);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public AutumnColors() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        tier=num2Tier(R.value);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=0;
    }

    @Override
    public void justEnteredRoom(AbstractRoom room){
        if(AbstractDungeon.actNum==2&&room instanceof RestRoom){
            flash();
            counter+=IN.value;
        }
    }

    @Override
    public void atBattleStart(){
        if(AbstractDungeon.actNum==3&&counter>=OUT.value){
            flash();
            AbstractRelic.RelicTier tier = AbstractDungeon.returnRandomRelicTier();
            AbstractDungeon.getCurrRoom().addRelicToRewards(tier);
            counter-=OUT.value;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],IN.value,OUT.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],IN.value,OUT.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public boolean canSpawn(){
        return AbstractDungeon.actNum==1;
    }
}
