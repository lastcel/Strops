//Partially adapted from Thinking Cap in Reliquary mod, credits to Cae!
package strops.relics;

import basemod.abstracts.CustomBottleRelic;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import strops.helpers.ModHelper;
import strops.patch.PatchLuckyStar;
import strops.utilities.IntSliderSetting;
import strops.utilities.RelicSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class LuckyStar extends StropsAbstractRelic implements CustomBottleRelic {
    public static final String ID = ModHelper.makePath(LuckyStar.class.getSimpleName());
    private static final String IMG_PATH = ModHelper.makeIPath(LuckyStar.class.getSimpleName());
    private static final String IMG_PATH_O = ModHelper.makeOPath(LuckyStar.class.getSimpleName());
    private static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    private final List<UUID> UUID_QUEUE = new ArrayList<>();

    public UUID uuidStart = null;

    public static final int NUM1 = 2, TIER = 2;

    public static final IntSliderSetting LOCATION = new IntSliderSetting("LuckyStar_Location", "N1", NUM1, 1, 4);
    public static final IntSliderSetting MH = new IntSliderSetting("LuckyStar_MH", "MH", 0, -20, 20);
    public static final IntSliderSetting G = new IntSliderSetting("LuckyStar_G", "G", 0, -100, 100);
    public static final IntSliderSetting R = new IntSliderSetting("LuckyStar_R", "R", TIER, 0, 5);

    public ArrayList<RelicSetting> BuildRelicSettings() {
        ArrayList<RelicSetting> settings = new ArrayList<>();
        settings.add(LOCATION);
        settings.add(MH);
        settings.add(G);
        settings.add(R);
        return settings;
    }

    public LuckyStar() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(IMG_PATH_O), num2Tier(R.value), LANDING_SOUND);
        showMHaG(MH, G);
        this.tips.add(new PowerTip(this.DESCRIPTIONS[1], this.DESCRIPTIONS[2]));
        canCopy = false;
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return PatchLuckyStar.PatchTool1.inLuckyStar::get;
    }

    @Override
    public void onEquip() {
        onEquipMods(MH, G);
    }

    @Override
    public void atBattleStart() {
        UUID_QUEUE.clear();
        uuidStart = null;
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return String.format(this.DESCRIPTIONS[0], LOCATION.value, LOCATION.value);
    }

    @Override
    public ArrayList<String> getUpdatedDescription2() {
        ArrayList<String> str_out = new ArrayList<>();
        str_out.add(String.format(this.DESCRIPTIONS[0], LOCATION.value, LOCATION.value));
        str_out.add("");
        str_out.add(getMHaG(MH, G));
        str_out.add(this.DESCRIPTIONS[1]);
        str_out.add(this.DESCRIPTIONS[2]);
        return str_out;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        UUID_QUEUE.add(card.uuid);
        counter++;

        if (uuidStart == null && UUID_QUEUE.size() == LOCATION.value) {
            uuidStart = card.uuid;
            PatchLuckyStar.PatchTool1.inLuckyStar.set(card, true);

            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c.uuid == uuidStart) {
                    PatchLuckyStar.PatchTool1.inLuckyStar.set(c, true);
                }
            }
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c.uuid == uuidStart) {
                    PatchLuckyStar.PatchTool1.inLuckyStar.set(c, true);
                }
            }
            for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                if (c.uuid == uuidStart) {
                    PatchLuckyStar.PatchTool1.inLuckyStar.set(c, true);
                }
            }
            for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
                if (c.uuid == uuidStart) {
                    PatchLuckyStar.PatchTool1.inLuckyStar.set(c, true);
                }
            }
        }

        if (card.uuid == uuidStart) {
            flash();
            counter = 1;
        }

        if (UUID_QUEUE.size() > LOCATION.value) {
            UUID_QUEUE.remove(0);
        }
    }

    @Override
    public void onVictory() {
        counter = -1;

        if (UUID_QUEUE.size() == LOCATION.value && uuidStart == UUID_QUEUE.get(0)) {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.uuid.equals(uuidStart)) {
                    c.upgrade();
                    AbstractDungeon.player.bottledCardUpgradeCheck(c);
                    AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                }
            }
        }
    }

    @Override
    public boolean canSpawn() {
        return (Settings.isEndless || (AbstractDungeon.floorNum <= 43));
    }
}
