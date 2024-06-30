//有待同时支持我将富有和我无伤痛
package strops.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.ImageEventPhase;
import basemod.abstracts.events.phases.TextPhase;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StropsMindBloom extends PhasedEvent {
    public static final String ID = "Strops:MindBloom";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String NAME = eventStrings.NAME;

    public StropsMindBloom() {
        super(ID, NAME, "StropsResources/img/events/StropsMindBloom.jpg");

        ArrayList<String> list = new ArrayList<>();
        list.add("The Guardian");
        list.add("Hexaghost");
        list.add("Slime Boss");
        Collections.shuffle(list, new Random(AbstractDungeon.miscRng.randomLong()));

        boolean isEnrich=(AbstractDungeon.floorNum%50<=40);

        registerPhase("Start", new TextPhase(DESCRIPTIONS[0])
                .addOption(OPTIONS[0], (i) -> transitionKey("Option 1 Do"))
                .addOption(OPTIONS[3], (i) -> transitionKey("Option 2 Done"))
                .addOption(isEnrich?OPTIONS[1]:OPTIONS[2] , (i) -> transitionKey("Option 3 Done"))
                .addOption(OPTIONS[5], (i) -> transitionKey("Exit Straight")));

        registerPhase("Exit Straight", new TextPhase(DESCRIPTIONS[4])
                .addOption(OPTIONS[4], (i) -> openMap()));

        registerPhase("Exit After 1", new TextPhase(DESCRIPTIONS[3])
                .addOption(OPTIONS[4], (i) -> openMap()));

        registerPhase("Exit After 2", new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[4], (i) -> openMap()));

        registerPhase("Exit After 3", new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[4], (i) -> openMap()));

        registerPhase("Exit With ALL Done", new TextPhase(DESCRIPTIONS[5])
                .addOption(OPTIONS[4], (i) -> openMap()));

        registerPhase("Option 1 Do", new CombatPhase(list.get(0))
                .addRewards(true, room-> {room.addRelicToRewards(AbstractRelic.RelicTier.RARE);
                    room.addGoldToRewards(AbstractDungeon.ascensionLevel >= 13 ? 25 : 50);})
                .setNextKey("Option 1 Done"));

        registerPhase("Option 1 Done", new TextPhase(DESCRIPTIONS[3])
                .addOption(OPTIONS[3], (i) -> transitionKey("Option 1,2 Done"))
                .addOption(isEnrich?OPTIONS[1]:OPTIONS[2], (i) -> transitionKey("Option 1,3 Done"))
                .addOption(OPTIONS[4], (i) -> transitionKey("Exit After 1")));

        registerPhase("Option 1,2 Done", new TextPhase(DESCRIPTIONS[1])
                .addOption(isEnrich?OPTIONS[1]:OPTIONS[2], (i) -> transitionKey("Exit With ALL Done"))
                .addOption(OPTIONS[4], (i) -> transitionKey("Exit After 2")));

        registerPhase("Option 1,3 Done", new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[3], (i) -> transitionKey("Exit With ALL Done"))
                .addOption(OPTIONS[4], (i) -> transitionKey("Exit After 3")));

        registerPhase("Option 2 Done", new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[0], (i) -> transitionKey("Option 1 Do,2 Done"))
                .addOption(isEnrich?OPTIONS[1]:OPTIONS[2], (i) -> transitionKey("Option 2,3 Done"))
                .addOption(OPTIONS[4], (i) -> transitionKey("Exit After 2")));

        registerPhase("Option 1 Do,2 Done", new CombatPhase(list.get(0))
                .addRewards(true, room-> {room.addRelicToRewards(AbstractRelic.RelicTier.RARE);
                    room.addGoldToRewards(AbstractDungeon.ascensionLevel >= 13 ? 25 : 50);})
                .setNextKey("Option 2,1 Done"));

        registerPhase("Option 2,1 Done", new TextPhase(DESCRIPTIONS[3])
                .addOption(isEnrich?OPTIONS[1]:OPTIONS[2], (i) -> transitionKey("Exit With ALL Done"))
                .addOption(OPTIONS[4], (i) -> transitionKey("Exit After 1")));

        registerPhase("Option 2,3 Done", new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[0], (i) -> transitionKey("Option 1 Do,2,3 Done"))
                .addOption(OPTIONS[4], (i) -> transitionKey("Exit After 3")));

        registerPhase("Option 1 Do,2,3 Done", new CombatPhase(list.get(0))
                .addRewards(true, room-> {room.addRelicToRewards(AbstractRelic.RelicTier.RARE);
                    room.addGoldToRewards(AbstractDungeon.ascensionLevel >= 13 ? 25 : 50);})
                .setNextKey("Exit With ALL Done"));

        registerPhase("Option 3 Done", new TextPhase(DESCRIPTIONS[2])
                .addOption(OPTIONS[0], (i) -> transitionKey("Option 1 Do,3 Done"))
                .addOption(OPTIONS[3], (i) -> transitionKey("Option 3,2 Done"))
                .addOption(OPTIONS[4], (i) -> transitionKey("Exit After 2")));

        registerPhase("Option 1 Do,3 Done", new CombatPhase(list.get(0))
                .addRewards(true, room-> {room.addRelicToRewards(AbstractRelic.RelicTier.RARE);
                    room.addGoldToRewards(AbstractDungeon.ascensionLevel >= 13 ? 25 : 50);})
                .setNextKey("Option 3,1 Done"));

        registerPhase("Option 3,1 Done", new TextPhase(DESCRIPTIONS[3])
                .addOption(OPTIONS[3], (i) -> transitionKey("Exit With ALL Done"))
                .addOption(OPTIONS[4], (i) -> transitionKey("Exit After 1")));

        registerPhase("Option 3,2 Done", new TextPhase(DESCRIPTIONS[1])
                .addOption(OPTIONS[0], (i) -> transitionKey("Option 1 Do,3,2 Done"))
                .addOption(OPTIONS[4], (i) -> transitionKey("Exit After 2")));

        registerPhase("Option 1 Do,3,2 Done", new CombatPhase(list.get(0))
                .addRewards(true, room-> {room.addRelicToRewards(AbstractRelic.RelicTier.RARE);
                    room.addGoldToRewards(AbstractDungeon.ascensionLevel >= 13 ? 25 : 50);})
                .setNextKey("Exit With ALL Done"));

        transitionKey("Start");


    }

    @Override
    protected void buttonEffect(int buttonPressed){


        if (currentPhase.equals(getPhase("Start"))){
            dynamicSwitch(buttonPressed,0,1,2);
        } else if(currentPhase.equals(getPhase("Option 1 Done"))){
            dynamicSwitch(buttonPressed,-1,0,1);
        } else if(currentPhase.equals(getPhase("Option 2 Done"))){
            dynamicSwitch(buttonPressed,0,-1,1);
        } else if(currentPhase.equals(getPhase("Option 3 Done"))){
            dynamicSwitch(buttonPressed,0,1,-1);
        } else if(currentPhase.equals(getPhase("Option 1,2 Done"))){
            dynamicSwitch(buttonPressed,-1,-1,0);
        } else if(currentPhase.equals(getPhase("Option 2,1 Done"))){
            dynamicSwitch(buttonPressed,-1,-1,0);
        } else if(currentPhase.equals(getPhase("Option 1,3 Done"))){
            dynamicSwitch(buttonPressed,-1,0,-1);
        } else if(currentPhase.equals(getPhase("Option 3,1 Done"))){
            dynamicSwitch(buttonPressed,-1,0,-1);
        } else if(currentPhase.equals(getPhase("Option 2,3 Done"))){
            dynamicSwitch(buttonPressed,0,-1,-1);
        } else if(currentPhase.equals(getPhase("Option 3,2 Done"))) {
            dynamicSwitch(buttonPressed, 0, -1, -1);
        }

        if (currentPhase instanceof ImageEventPhase) {
            ((ImageEventPhase) currentPhase).optionChosen(this, buttonPressed);
        }
    }

private static void dynamicSwitch(int buttonPressed,int case1,int case2,int case3){
    int effectCount;
    List<String> upgradedCards, obtainedRelic;
    Doubt doubt;
    if(buttonPressed==case1){
        logMetric("MindBloom", "Fight");
        CardCrawlGame.music.playTempBgmInstantly("MINDBLOOM", true);
        return;
    }
    if(buttonPressed==case2){
        effectCount = 0;
        upgradedCards = new ArrayList<>();
        obtainedRelic = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                effectCount++;
                if (effectCount <= 20) {
                    float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
                    float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;
                    AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c
                            .makeStatEquivalentCopy(), x, y));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(x, y));
                }
                upgradedCards.add(c.cardID);
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
            }
        }
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F,

                RelicLibrary.getRelic("Mark of the Bloom").makeCopy());
        obtainedRelic.add("Mark of the Bloom");
        logMetric("MindBloom", "Upgrade", null, null, null, upgradedCards, obtainedRelic, null, null, 0, 0, 0, 0, 0, 0);
        return;
    }
    if(buttonPressed==case3){
        if (AbstractDungeon.floorNum % 50 <= 40) {
            List<String> cardsAdded = new ArrayList<>();
            cardsAdded.add("Normality");
            cardsAdded.add("Normality");
            logMetric("MindBloom", "Gold", cardsAdded, null, null, null, null, null, null, 0, 0, 0, 0, 999, 0);
            AbstractDungeon.effectList.add(new RainingGoldEffect(999));
            AbstractDungeon.player.gainGold(999);
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Normality(), Settings.WIDTH * 0.6F, Settings.HEIGHT / 2.0F));
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Normality(), Settings.WIDTH * 0.3F, Settings.HEIGHT / 2.0F));
            return;
        }
        doubt = new Doubt();
        logMetricObtainCardAndHeal("MindBloom", "Heal", doubt, AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth);
        AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth);
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(doubt, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    }
}

}
