package strops.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.actions.GainMaxHpAction;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ToxicLump extends StropsAbstractRelic implements CustomSavable<Integer> {
    public static final String ID = ModHelper.makePath(ToxicLump.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ToxicLump.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(ToxicLump.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=9,NUM2=3,LIMIT=50;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("ToxicLump_Threshold", "N1", NUM1, 3,30);
    public static final IntSliderSetting BONUS=new IntSliderSetting("ToxicLump_Bonus", "N2", NUM2, 1,10);
    public static final IntSliderSetting MH=new IntSliderSetting("ToxicLump_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("ToxicLump_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(BONUS);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public ToxicLump() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
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
        counter=0;

        if(AbstractDungeon.getCurrRoom().phase== AbstractRoom.RoomPhase.COMBAT){
            secondCounter=0;
        }
    }

    @Override
    public void atPreBattle(){
        secondCounter=0;
    }

    @Override
    public void onVictory(){
        secondCounter=-1;
    }

    @Override
    public void wasHPLost(int damageAmount) {
        if (AbstractDungeon.player.stance.ID.equals("Wrath")&&secondCounter<=LIMIT) {
            counter+=damageAmount;
            while(counter>=THRESHOLD.value){
                flash();
                counter-=THRESHOLD.value;
                addToTop(new GainMaxHpAction(BONUS.value));
                addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            }
        }
    }

    @Override
    public int onPlayerHeal(int healAmount){
        if(AbstractDungeon.getCurrRoom().phase== AbstractRoom.RoomPhase.COMBAT){
            secondCounter+=healAmount;
        }

        return healAmount;
    }

    @Override
    public boolean canSpawn(){
        return (Settings.isEndless || (AbstractDungeon.actNum <= 3));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], THRESHOLD.value, BONUS.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], THRESHOLD.value, BONUS.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
