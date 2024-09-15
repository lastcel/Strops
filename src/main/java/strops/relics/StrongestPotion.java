package strops.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.cards.EatMango;
import strops.cards.EatSuperMango;
import strops.helpers.ModHelper;
import strops.patch.PatchStrongestPotion;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class StrongestPotion extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(StrongestPotion.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(StrongestPotion.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(StrongestPotion.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=13,NUM2=19,NUM3=2,NUM4=1;

    public static final IntSliderSetting BONUS=new IntSliderSetting("StrongestPotion_Bonus", "N1", NUM1, 11,13);
    public static final IntSliderSetting SUPER_BONUS=new IntSliderSetting("StrongestPotion_Super_Bonus", "N2", NUM2, 15,30);
    public static final IntSliderSetting DURATION=new IntSliderSetting("StrongestPotion_Duration", "N3", NUM3, 1,5);
    public static final IntSliderSetting INTENSE=new IntSliderSetting("StrongestPotion_Intense", "N4", NUM4, 1,3);
    public static final IntSliderSetting MH=new IntSliderSetting("StrongestPotion_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("StrongestPotion_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        settings.add(SUPER_BONUS);
        settings.add(DURATION);
        settings.add(INTENSE);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public StrongestPotion() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        canSpawnInBattle=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> strongestPotionChoices = new ArrayList<>();
        strongestPotionChoices.add(new EatMango());
        strongestPotionChoices.add(new EatSuperMango());
        if(AbstractDungeon.isScreenUp){
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.INCOMPLETE;
        PatchStrongestPotion.PatchTool1.whichCallThis.set(AbstractDungeon.cardRewardScreen,this);
        AbstractDungeon.cardRewardScreen.chooseOneOpen(strongestPotionChoices);
    }

    @Override
    public void atBattleStart() {
        if(counter>=1){
            flash();
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, -INTENSE.value), -INTENSE.value));
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            counter--;
            if(counter==0){
                stopPulse();
            }
        }
    }

    @Override
    public void setCounter(int setCounter){
        counter=setCounter;
        if (counter >= 1) {
            flash();
            beginLongPulse();
        }
    }

    @Override
    public String getUpdatedDescription() {
        if(DESCRIPTIONS[5].equals("1")){
            return String.format(this.DESCRIPTIONS[0], BONUS.value, SUPER_BONUS.value,
                    INTENSE.value,DURATION.value);
        }
        return String.format(this.DESCRIPTIONS[0], BONUS.value, SUPER_BONUS.value,
                DURATION.value,INTENSE.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        if(DESCRIPTIONS[5].equals("1")){
            str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value, SUPER_BONUS.value,
                    INTENSE.value,DURATION.value));
        } else {
            str_out.add(String.format(this.DESCRIPTIONS[0], BONUS.value, SUPER_BONUS.value,
                    DURATION.value,INTENSE.value));
        }
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }
}
