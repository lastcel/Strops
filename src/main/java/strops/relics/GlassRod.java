//This class is adapted from Ice Cube Tray of the Reliquary mod, credits to Cae!
//everPreUpgrade好像可以省掉，待研究
package strops.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import strops.helpers.ModHelper;
import strops.patch.PatchGlassRod;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.*;

public class GlassRod extends StropsAbstractRelic {
    public static final String ID = ModHelper.makePath(GlassRod.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(GlassRod.class.getSimpleName());
    //private static final String IMG_PATH_O = ModHelper.makeOPath(GlassRod.class.getSimpleName());
    private static final RelicTier RELIC_TIER = RelicTier.RARE;
    private static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public static final String[] UI_TEXT = CardCrawlGame.languagePack.getUIString("CampfireTokeEffect").TEXT;

    public boolean cardsSelected=true;

    public static final int NUM1=3;

    public static final IntSliderSetting THRESHOLD=new IntSliderSetting("GlassRod_Threshold", "N1", NUM1, 1,5);
    public static final IntSliderSetting MH=new IntSliderSetting("GlassRod_MH","MH",0,-20,20);
    public static final IntSliderSetting G=new IntSliderSetting("GlassRod_G","G",0,-100,100);
    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(THRESHOLD);
        settings.add(MH);
        settings.add(G);
        return settings;
    }

    public GlassRod() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        showMHaG(MH,G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy=false;
    }

    @Override
    public void onEquip(){
        onEquipMods(MH,G);
        counter=0;
    }

    @Override
    public void update(){
        super.update();
        if (!isObtained) {
            return;
        }
        for(AbstractCard c: AbstractDungeon.player.masterDeck.group){
            if(!c.upgraded){
                PatchGlassRod.PatchTool1.everPreUpgrade.set(c,true);
                PatchGlassRod.PatchTool2.everCounted.set(c,false);
            }
            if(c.upgraded&&PatchGlassRod.PatchTool1.everPreUpgrade.get(c)&&
            !PatchGlassRod.PatchTool2.everCounted.get(c)){
                counter++;
                PatchGlassRod.PatchTool2.everCounted.set(c,true);
            }
        }
        AbstractRoom.RoomPhase phase = AbstractDungeon.getCurrRoom().phase;
        if (cardsSelected && counter >= THRESHOLD.value && phase != AbstractRoom.RoomPhase.INCOMPLETE && phase != AbstractRoom.RoomPhase.COMBAT && VALID_SCREENS.contains(AbstractDungeon.screen)) {
            counter -= THRESHOLD.value;
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(), 1, UI_TEXT[0], false, false, true, true);
            AbstractDungeon.dynamicBanner.hide();
            beginLongPulse();
            cardsSelected = false;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        }
        if (!cardsSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardsSelected = true;
            stopPulse();
            float displayCount = 0;
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                card.untip();
                card.unhover();
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, Settings.WIDTH / 3.0F + displayCount, Settings.HEIGHT / 2.0F));
                displayCount += Settings.WIDTH / 6.0F;
                AbstractDungeon.player.masterDeck.removeCard(card);
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        } else if (!cardsSelected && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) {
            // Canceled.
            cardsSelected = true;
            stopPulse();
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    private final Set<AbstractDungeon.CurrentScreen> VALID_SCREENS = new HashSet<>(Arrays.asList(
            AbstractDungeon.CurrentScreen.COMBAT_REWARD,
            AbstractDungeon.CurrentScreen.MAP,
            AbstractDungeon.CurrentScreen.NONE,
            AbstractDungeon.CurrentScreen.SHOP,
            AbstractDungeon.CurrentScreen.VICTORY
    ));

    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.floorNum <= 43));
    }

    @Override
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
}
