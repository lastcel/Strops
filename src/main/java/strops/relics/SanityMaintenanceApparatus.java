package strops.relics;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.*;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class SanityMaintenanceApparatus extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(SanityMaintenanceApparatus.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SanityMaintenanceApparatus.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(SanityMaintenanceApparatus.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=1,TIER=2;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("SMA_Threshold_v0.12.0", "N1", NUM1, 1,30);
    public static final IntSliderSetting MH=new IntSliderSetting("SMA_MH_v0.12.0","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SMA_G_v0.12.0","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("SMA_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public SanityMaintenanceApparatus() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        //tier=num2Tier(R.value);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    public int currhp;

    @Override
    public int onLoseHpLast(int damageAmount){
        currhp=AbstractDungeon.player.currentHealth;

        if(!(AbstractDungeon.getCurrRoom() instanceof MonsterRoom) ||
                (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) ||
                (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)){
            return  damageAmount;
        }
        if(currhp<THRESHOLD.value){
            return damageAmount;
        }
        if((damageAmount==0)||(damageAmount<=currhp-THRESHOLD.value)){
            return damageAmount;
        }
        flash();
        return (currhp - THRESHOLD.value);
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

    @Override
    public boolean canSpawn(){
        return (Settings.isEndless || (AbstractDungeon.floorNum <= 47));
    }
}
