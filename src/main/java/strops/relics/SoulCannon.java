package strops.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.cards.*;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class SoulCannon extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(SoulCannon.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(SoulCannon.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(SoulCannon.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public static final int NUM1=3,TIER=2;

    public static final IntSliderSetting WARMUP=new IntSliderSetting("SoulCannon_Warmup","N1", NUM1,1,10);
    public static final IntSliderSetting MH=new IntSliderSetting("SoulCannon_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("SoulCannon_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("SoulCannon_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(WARMUP);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public SoulCannon() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
    }

    @Override
    public void onRightClick() {
        AbstractRoom currRoom = AbstractDungeon.getCurrRoom();
        if ((currRoom != null) && (currRoom.phase == AbstractRoom.RoomPhase.COMBAT)) {
            return;
        }

        if(AbstractDungeon.screen==AbstractDungeon.CurrentScreen.GRID||AbstractDungeon.screen==AbstractDungeon.CurrentScreen.CARD_REWARD){
            return;
        }

        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> typeChoices = new ArrayList<>();
        typeChoices.add(new OneTiny());
        typeChoices.add(new OneHuge());
        typeChoices.add(new TwoTiny());
        typeChoices.add(new TwoHuge());
        typeChoices.add(new ThreeTiny());
        typeChoices.add(new ThreeHuge());

        if(AbstractDungeon.isScreenUp){
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.cardRewardScreen.chooseOneOpen(typeChoices);
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],WARMUP.value);
    }


    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],WARMUP.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        return str_out;
    }

    @Override
    public boolean canSpawn() {
        return !Settings.isEndless;
    }

    @Override
    public AbstractRelic makeCopy(){
        return cannonMakeCopy();
    }
}
