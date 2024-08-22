package strops.relics;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class Zan extends StropsAbstractRelic implements ClickableRelic, CustomSavable<Integer> {
    public static final String ID = ModHelper.makePath(Zan.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Zan.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Zan.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    private int keptSecondCounter;

    public static final int NUM1=10,NUM2=20,NUM3=30,NUM4=20,TIER=2;
    public static final int BLIZZARD_BONUS=70;
    public static final int SHOOTINGSTAR_BONUS=50;
    public static final int FRUGALPOTION_BONUS=50;
    public static final int GREEDYPOTION_BONUS=50;

    public static final int LIMIT=3;

    public static final IntSliderSetting COMMON_BONUS=new IntSliderSetting("Zan_Common_Bonus", "N1", NUM1, 6,15);
    public static final IntSliderSetting UNCOMMON_BONUS=new IntSliderSetting("Zan_Uncommon_Bonus", "N2", NUM2, 6,30);
    public static final IntSliderSetting RARE_BONUS=new IntSliderSetting("Zan_Rare_Bonus", "N3", NUM3, 6,45);
    public static final IntSliderSetting STORAGE=new IntSliderSetting("Zan_Storage", "N4", NUM4, 0,45);
    public static final IntSliderSetting MH=new IntSliderSetting("Zan_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Zan_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Zan_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(COMMON_BONUS);
        settings.add(UNCOMMON_BONUS);
        settings.add(RARE_BONUS);
        settings.add(STORAGE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Zan() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public Integer onSave(){
        return secondCounter;
    }

    @Override
    public void onLoad(Integer savedSecondCounter){
        secondCounter=savedSecondCounter;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=(STORAGE.value==0)?-1:0;
        if(AbstractDungeon.getCurrRoom().phase!=AbstractRoom.RoomPhase.COMBAT){
            secondCounter=0;
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room){
        secondCounter=0;
    }

    @Override
    public void atPreBattle(){
        keptSecondCounter=secondCounter;
        secondCounter=-1;
    }

    @Override
    public void onVictory(){
        secondCounter=keptSecondCounter;
    }

    @Override
    public void onRightClick(){
        if(counter>0&&AbstractDungeon.getCurrRoom().phase!=AbstractRoom.RoomPhase.COMBAT){
            AbstractDungeon.player.heal(counter,true);
            counter=0;
        }
    }

    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.actNum <= 3));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], COMMON_BONUS.value,
                UNCOMMON_BONUS.value, RARE_BONUS.value, STORAGE.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], COMMON_BONUS.value,
                UNCOMMON_BONUS.value, RARE_BONUS.value, STORAGE.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
