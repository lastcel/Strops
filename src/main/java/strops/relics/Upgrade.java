package strops.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import strops.helpers.ModHelper;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;
import strops.vfx.ObtainRelicLater;
import strops.patch.PatchUpgrade;

import java.util.ArrayList;

public class Upgrade extends StropsAbstractRelic implements ClickableRelic {
    public static final String ID = ModHelper.makePath(Upgrade.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(Upgrade.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(Upgrade.class.getSimpleName());
    //private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public static final int NUM1=3,NUM2=100,TIER=3,GOLD=100;

    public static final IntSliderSetting USABLE=new IntSliderSetting("Upgrade_Usable_v0.13.4", "N1", NUM1, 1,6);
    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("Upgrade_Threshold_v0.13.4", "N2", NUM2, 50,150);
    public static final IntSliderSetting MH=new IntSliderSetting("Upgrade_MH_v0.13.4","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("Upgrade_G_v0.13.4","G", GOLD,-100,500);
    public static final IntSliderSetting R=new IntSliderSetting("Upgrade_R_v0.13.4","R", TIER,0,5);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(USABLE);
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public Upgrade() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], String.format(this.DESCRIPTIONS[2], THRESHOLD.value)));
        this.tips.add(new PowerTip(this.DESCRIPTIONS[3], this.DESCRIPTIONS[4]));
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=USABLE.value;
        pulse=true;
    }

    @Override
    public void onRightClick(){
        if(counter<=0) {
            return;
        }
        AbstractRoom currRoom= AbstractDungeon.getCurrRoom();
        if((currRoom!=null)&&(currRoom.phase == AbstractRoom.RoomPhase.COMBAT)){
            return;
        }

        int rng;
        AbstractPlayer p=AbstractDungeon.player;
        String relicName;

        for(int i=0;i<30;i++) {
            p.loseGold(THRESHOLD.value);
            //System.out.println("负截断："+PatchUpgrade.PatchTool1.isNegativelyCutoff.get(p));
            if (!PatchUpgrade.PatchTool1.isNegativelyCutoff.get(p))
            {
                rng = AbstractDungeon.treasureRng.random(1, 4);
                switch (rng) {
                    case 1:
                        relicName = "Vajra";
                        break;
                    case 2:
                        relicName = "Oddly Smooth Stone";
                        break;
                    case 3:
                        relicName = "Golden Idol";
                        break;
                    case 4:
                        relicName = "Strawberry";
                        break;
                    default:
                        relicName = "Circlet";
                        break;
                }
                AbstractDungeon.effectsQueue.add(0, new ObtainRelicLater(RelicLibrary.getRelic(relicName).makeCopy()));
            }
            if (p.gold == 0) {
                break;
            }
        }

        counter--;
        if (counter == 0) {
            counter = -2;
            pulse = false;
            usedUp();
        }
    }

    @Override
    public void setCounter(int setCounter){
        this.counter=setCounter;
        if (setCounter == -2) {
            usedUp();
            this.counter = -2;
        } else {
            flash();
            pulse=true;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0],USABLE.value,THRESHOLD.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out=new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0],USABLE.value,THRESHOLD.value));
        str_out.add("");
        str_out.add(getMHaG(MH,G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(String.format(this.DESCRIPTIONS[2], THRESHOLD.value));
        str_out.add(this.DESCRIPTIONS[3]);
        str_out.add(this.DESCRIPTIONS[4]);
        return str_out;
    }

    @Override
    public boolean canSpawn() {
        return ((Settings.isEndless || AbstractDungeon.actNum <= 3) &&
                !(AbstractDungeon.getCurrRoom() instanceof ShopRoom));
    }
}
