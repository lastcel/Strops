package strops.relics;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class HungrySun extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(HungrySun.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(HungrySun.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(HungrySun.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=6,NUM2=10,TIER=1;

    public static final IntSliderSetting THRESHOLD = new IntSliderSetting("HungrySun_Threshold", "N1", NUM1, 1,20);
    public static final IntSliderSetting MULTIPLIER = new IntSliderSetting("HungrySun_Multiplier", "N2/10", NUM2, 1,50);
    public static final IntSliderSetting MH=new IntSliderSetting("HungrySun_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("HungrySun_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("HungrySun_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MULTIPLIER);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public HungrySun() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if(!(room instanceof MonsterRoom)){
            counter=THRESHOLD.value;
            beginLongPulse();
        }
        else {
            counter=-1;
            pulse=false;
        }
    }

    /*
    @Override
    public void justEnteredRoom(AbstractRoom room) {
        if(!(room instanceof MonsterRoom)){
            counter=THRESHOLD.value;
            beginLongPulse();
        }
        else {
            counter=-1;
            pulse=false;
        }
    }

     */

    @Override
    public int onLoseHpLast(int damageAmount) {
        /*
        if(AbstractDungeon.getCurrRoom() instanceof MonsterRoom){
            return damageAmount;
        }

         */
        if(counter<=0){
            return damageAmount;
        }
        if(damageAmount==0){
            return damageAmount;
        }
        int prevent = Math.min(counter, damageAmount);
        damageAmount -= prevent;
        counter -= prevent;
        flash();
        AbstractDungeon.player.heal(MathUtils.floor(prevent*MULTIPLIER.value/10.0f), true);
        if(counter==0){
            pulse=false;
        }
        return damageAmount;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);

        if(!(AbstractDungeon.getCurrRoom() instanceof MonsterRoom)){
            counter=THRESHOLD.value;
            beginLongPulse();
        }
    }

    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.floorNum <= 43));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], THRESHOLD.value, MULTIPLIER.value*10);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value, MULTIPLIER.value*10));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
