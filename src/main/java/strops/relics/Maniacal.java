//本遗物的部分patch写在了PatchFTLEngines里面
package strops.relics;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.patch.PatchGrassNowAndFlowersThen;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.utilities.StaticHelpers;

import java.util.ArrayList;

public class Maniacal extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(Maniacal.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Maniacal.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Maniacal.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public static final int NUM1=30,NUM2=10,NUM3=13,TIER=2;

    public static final IntSliderSetting BLOCK_CO=new IntSliderSetting("Maniacal_Multiplier_Block", "N1/10", NUM1, 10,50);
    public static final IntSliderSetting COST=new IntSliderSetting("Maniacal_Cost", "N2/10", NUM2, 1,10);
    public static final IntSliderSetting DAMAGE_CO=new IntSliderSetting("Maniacal_Multiplier_Damage", "N3/10", NUM3, 5,30);
    public static final IntSliderSetting MH=new IntSliderSetting("Maniacal_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Maniacal_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("Maniacal_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BLOCK_CO);
        settings.add(COST);
        settings.add(DAMAGE_CO);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Maniacal() {
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
    public void onRightClick() {
        AbstractRoom currRoom= AbstractDungeon.getCurrRoom();
        if((currRoom!=null)&&(currRoom.phase == AbstractRoom.RoomPhase.COMBAT)){
            if(!StaticHelpers.canClickRelic(this)|| PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>1){
                return;
            }
        }

        if(grayscale){
            counter=-1;
            grayscale=false;
        } else {
            counter=-2;
            grayscale=true;
        }
    }

    public void onThisAttack(int damageAmount, AbstractCreature target) {
        if (target.isPlayer)
            return;
        int overkill = damageAmount - target.currentHealth;
        if (overkill <= 0)
            return;
        overkill= MathUtils.floor(overkill*Maniacal.BLOCK_CO.value/10.0f);
        addToTop(new GainBlockAction(AbstractDungeon.player,AbstractDungeon.player,overkill));
    }

    @Override
    public void setCounter(int counter){
        this.counter=counter;
        grayscale= (this.counter != -1);
    }

    @Override
    public boolean canSpawn(){
        return Settings.isEndless||AbstractDungeon.floorNum<=54;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],BLOCK_CO.value*10,COST.value*10,DAMAGE_CO.value*10);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],BLOCK_CO.value*10,COST.value*10,DAMAGE_CO.value*10));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
