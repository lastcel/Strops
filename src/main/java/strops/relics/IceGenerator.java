package strops.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.cards.GetBlizzard;
import strops.cards.HealToFull;
import strops.helpers.ModHelper;
import strops.patch.PatchStrongestPotion;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class IceGenerator extends StropsAbstractRelic implements CustomSavable<Boolean> {
    public static final String ID = ModHelper.makePath(IceGenerator.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(IceGenerator.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(IceGenerator.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public boolean isHealToFull=false;
    //public static AbstractRoom.RoomPhase previousPhase;

    public static final int NUM1=12,NUM2=8,NUM3=1,TIER=3;

    public static final IntSliderSetting MAXHP_PENALTY=new IntSliderSetting("IceGenerator_MaxHp_Penalty", "N1", NUM1, 20);
    public static final IntSliderSetting HP_PENALTY=new IntSliderSetting("IceGenerator_Hp_Penalty", "N2", NUM2, 20);
    public static final IntSliderSetting BOTTLE=new IntSliderSetting("IceGenerator_Bottle", "N3", NUM3, 1,3);
    public static final IntSliderSetting MH=new IntSliderSetting("IceGenerator_MH_v0.12.0","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("IceGenerator_G_v0.12.0","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("IceGenerator_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(MAXHP_PENALTY);
        settings.add(HP_PENALTY);
        settings.add(BOTTLE);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public IceGenerator() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canSpawnInBattle =false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        //logger.info("当前幕数="+AbstractDungeon.actNum+"，层数="+AbstractDungeon.floorNum);
        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> iceGenChoices = new ArrayList<>();
        iceGenChoices.add(new HealToFull());
        iceGenChoices.add(new GetBlizzard());
        if(AbstractDungeon.isScreenUp){
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        //previousPhase=AbstractDungeon.getCurrRoom().phase;
        AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.INCOMPLETE;
        PatchStrongestPotion.PatchTool1.whichCallThis.set(AbstractDungeon.cardRewardScreen,this);
        AbstractDungeon.cardRewardScreen.chooseOneOpen(iceGenChoices);
    }

    @Override
    public Boolean onSave(){
        return isHealToFull;
    }

    @Override
    public void onLoad(Boolean savedIsHealToFull){
        isHealToFull=savedIsHealToFull;
        if(isHealToFull){
            flash();
            beginLongPulse();
        }
    }

    @Override
    public boolean canSpawn() {
        return ((Settings.isEndless || (AbstractDungeon.actNum <= 3))&&(AbstractDungeon.floorNum>=1));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], MAXHP_PENALTY.value, HP_PENALTY.value, BOTTLE.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], MAXHP_PENALTY.value, HP_PENALTY.value, BOTTLE.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
