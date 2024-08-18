package strops.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.cards.ToEscape;
import strops.cards.ToFight;
import strops.cards.ToGetHer;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class SunflowerInASummer extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(SunflowerInASummer.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SunflowerInASummer.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=20;

    public static final IntSliderSetting BONUS=new IntSliderSetting("Iriya_Bonus_v0.12.0","10xN1",NUM1,11,30);
    //public static final IntSliderSetting ISELITELIZE=new IntSliderSetting("Iriya_IsElitelize_v0.12.5","B1",0,1);
    public static final IntSliderSetting MH=new IntSliderSetting("Iriya_MH_v0.12.0","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Iriya_G_v0.12.0","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(BONUS);
        //settings.add(ISELITELIZE);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public SunflowerInASummer() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
        canSpawnInBattle =false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);

        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> iriyaChoices = new ArrayList<>();
        iriyaChoices.add(new ToEscape());
        iriyaChoices.add(new ToGetHer());
        iriyaChoices.add(new ToFight());
        if(AbstractDungeon.isScreenUp){
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.cardRewardScreen.chooseOneOpen(iriyaChoices);
    }

    public boolean canSpawn() {
        return  AbstractDungeon.actNum == 1 ;
    }

    /*
    @Override
    public void setCounter(int counter){
        this.counter=counter;
        if(PatchSunflowerInASummer.PatchTool2.everMetSunflower.get()){
            flash();
            beginLongPulse();
        }
    }

     */

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(this.DESCRIPTIONS[0]);
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
