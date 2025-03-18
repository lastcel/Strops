package strops.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.cards.ChooseAchievedVersion;
import strops.cards.ChooseAttemptedVersion;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;

public class ProfiteeringMerchant extends StropsAbstractRelic{
    public static final String ID = ModHelper.makePath(ProfiteeringMerchant.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(ProfiteeringMerchant.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(ProfiteeringMerchant.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.UNCOMMON;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final int NUM1=35,NUM2=40,TIER=2;

    public static final IntSliderSetting DISCOUNT_ATTEMPTED=new IntSliderSetting("PM_Discount_Attempted_2", "N1",NUM1, 21,60);
    public static final IntSliderSetting DISCOUNT_ACHIEVED=new IntSliderSetting("PM_Discount_Achieved_2", "N2", NUM2, 70);
    public static final IntSliderSetting MH=new IntSliderSetting("PM_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("PM_G","G",0,-100,100);
    public static final IntSliderSetting R=new IntSliderSetting("PM_R","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(DISCOUNT_ATTEMPTED);
        settings.add(DISCOUNT_ACHIEVED);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public ProfiteeringMerchant() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canSpawnInBattle=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        InputHelper.moveCursorToNeutralPosition();
        ArrayList<AbstractCard> relicChoices = new ArrayList<>();
        relicChoices.add(new ChooseAttemptedVersion());
        relicChoices.add(new ChooseAchievedVersion());
        if(AbstractDungeon.isScreenUp){
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase= AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.cardRewardScreen.chooseOneOpen(relicChoices);
    }

    @Override
    public boolean canSpawn() {
        return ((Settings.isEndless || AbstractDungeon.actNum <= 3) &&
                !(AbstractDungeon.getCurrRoom() instanceof com.megacrit.cardcrawl.rooms.ShopRoom));
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], DISCOUNT_ATTEMPTED.value, DISCOUNT_ACHIEVED.value);
    }

    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], DISCOUNT_ATTEMPTED.value, DISCOUNT_ACHIEVED.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }
}
