package strops.relics;

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
    private static final RelicTier RELIC_TIER = RelicTier.COMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=7;

    public static final IntSliderSetting THRESHOLD = new IntSliderSetting("HungrySun_Threshold", "N1", NUM1, 1,12);
    public static final IntSliderSetting MH=new IntSliderSetting("HungrySun_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("HungrySun_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public HungrySun() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
    }

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

    @Override
    public int onLoseHpLast(int damageAmount) {
        if(AbstractDungeon.getCurrRoom() instanceof MonsterRoom){
            return damageAmount;
        }
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
        AbstractDungeon.player.heal(prevent, true);
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
        return (Settings.isEndless || (AbstractDungeon.actNum <= 3));
    }

    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], THRESHOLD.value);
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
