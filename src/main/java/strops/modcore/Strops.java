package strops.modcore;

import basemod.*;
import basemod.abstracts.CustomSavable;
import basemod.eventUtil.AddEventParams;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import basemod.interfaces.*;
import basemod.helpers.*;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.GeneticAlgorithm;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import strops.cards.*;
import strops.events.StropsMindBloom;
import strops.helpers.ModHelper;
import strops.patch.PatchGrassNowAndFlowersThen;
import strops.potions.Blizzard;
import strops.potions.FrugalPotion;
import strops.potions.GreedyPotion;
import strops.potions.PhantasmalShootingStar;
import strops.relics.*;
import strops.textkeynumbers.*;
import strops.utilities.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;

@SpireInitializer
public class Strops implements EditCardsSubscriber, EditRelicsSubscriber,
        EditStringsSubscriber, PostInitializeSubscriber, EditKeywordsSubscriber,
        CustomSavable<StropsSaveInfo>, MaxHPChangeSubscriber, PostDungeonInitializeSubscriber {

    public Strops() {
        BaseMod.subscribe(this);
        BaseMod.addSaveField("Strops",this);
    }

    public static void initialize() {
        new Strops();
    }

    public static HashMap<String, RelicSetting> ConfigSettings = new HashMap<>();
    public static TreeMap<StropsAbstractRelic, ArrayList<RelicSetting>> RelicSettings = new TreeMap<>((o1, o2) -> {
        int tierDiff=StropsAbstractRelic.tier2Num(o1.tier)-StropsAbstractRelic.tier2Num(o2.tier);
        if(tierDiff!=0){
            return tierDiff;
        }

        if(Settings.language!= Settings.GameLanguage.ZHS){
            return o1.compareTo(o2);
        }

        Collator collator = Collator.getInstance(Locale.SIMPLIFIED_CHINESE);
        return collator.compare(o1.name,o2.name);
    });
    public static ModPanel settingsPanel;
    static SpireConfig config;
    public static Pagination pager;

    public static AbstractRelic FourthBossRelicStorage = null;
    public static AbstractRelic FifthBossRelicStorage = null;

    public static final Logger logger = LogManager.getLogger(Strops.class.getName());

    public static String lastPotion="";
    public static int continuousRest=0;
    public static int continuousNonElite=0;

    @Override
    public StropsSaveInfo onSave() {
        return new StropsSaveInfo(lastPotion,continuousRest,continuousNonElite);
    }

    @Override
    public void onLoad(StropsSaveInfo savedStropsSaveInfo) {
        lastPotion=savedStropsSaveInfo.s_lastPotion;
        if(!lastPotion.equals("")){
            for(AbstractRelic r:AbstractDungeon.player.relics){
                if(r.relicId.equals(Echo.ID)&&r.counter>0){
                    ((Echo) r).setDescriptionAfterObtainingPotion();
                }
            }
        }

        continuousRest=savedStropsSaveInfo.s_continuousRest;
        continuousNonElite=savedStropsSaveInfo.s_continuousNonElite;
    }

    public static void BuildSettings(StropsAbstractRelic relic) {
        RelicSettings.put(relic, relic.BuildRelicSettings());
        for (RelicSetting setting : RelicSettings.get(relic))
            ConfigSettings.put(setting.settingsId, setting);
    }

    public static RelicSettingsButton BuildSettingsButton(StropsAbstractRelic relic) {
        ArrayList<IUIElement> settingElements = new ArrayList<>();
        ArrayList<RelicSetting> settings = new ArrayList<>();
        float x = 350.0F;
        float y = 550.0F;
        for (String s : relic.GetSettingStrings()) {
            settingElements.add(new ModLabel(s, x + ((y == 550.0F) ? 150.0F : 0.0F), y, settingsPanel, me -> {

            }));
            y -= 40.0F;
        }
        for (RelicSetting setting : RelicSettings.get(relic)) {
            settings.add(setting);
            settingElements.addAll(setting.GenerateElements(x, y));
            y -= setting.elementHeight;
        }
        return new RelicSettingsButton(relic, settingElements, settings);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new TextKeyNumber1());
        BaseMod.addDynamicVariable(new TextKeyNumber2());

        BaseMod.addCard(new ArcaneTalents());
        BaseMod.addCard(new FrostTalents());
        BaseMod.addCard(new FireTalents());
        BaseMod.addCard(new LightningStorm());
        BaseMod.addCard(new PartScrapper());
        BaseMod.addCard(new BodySlamPlusPlus());
        BaseMod.addCard(new Aurora());
        BaseMod.addCard(new SoulCraftedCard());
        BaseMod.addCard(new RoamingStrike());
        BaseMod.addCard(new FinalForm());
    }

    @Override
    public void receiveEditStrings() {
        String lang;
        switch (Settings.language) {
            case ZHS: lang="zhs"; break;// 如果语言设置为简体中文，则加载ZHS文件夹的资源
            case ZHT: lang="zht"; break;
            default: lang="eng"; break;// 如果没有相应语言的版本，默认加载英语
        }
        BaseMod.loadCustomStringsFile(RelicStrings.class, "StropsResources/localization/" + lang + "/RelicStrings.json"); // 加载相应语言的卡牌本地化内容。
        BaseMod.loadCustomStringsFile(PowerStrings.class, "StropsResources/localization/" + lang + "/PowerStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, "StropsResources/localization/" + lang + "/CardStrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class,"StropsResources/localization/" + lang + "/EventStrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class,"StropsResources/localization/" + lang + "/PotionStrings.json");
        // 如果是中文，加载的就是"ExampleResources/localization/ZHS/cards.json"
    }

    @Override
    public void receiveEditRelics() {

        BaseMod.addRelic(new ButchersBlade(), RelicType.SHARED);// RelicType表示是所有角色都能拿到的遗物，还是一个角色的独有遗物
        BaseMod.addRelic(new InsanityStone(), RelicType.SHARED);
        BaseMod.addRelic(new HermitsTiara(), RelicType.SHARED);
        BaseMod.addRelic(new Bolster(), RelicType.SHARED);
        BaseMod.addRelic(new Polearm(), RelicType.SHARED);
        BaseMod.addRelic(new Fractal(), RelicType.SHARED);
        BaseMod.addRelic(new Transcendence(), RelicType.SHARED);
        BaseMod.addRelic(new HungrySun(), RelicType.SHARED);
        BaseMod.addRelic(new IcyTouch(), RelicType.BLUE);
        BaseMod.addRelic(new SanityMaintenanceApparatus(), RelicType.SHARED);
        BaseMod.addRelic(new SunflowerInASummer(), RelicType.SHARED);
        BaseMod.addPotion(PhantasmalShootingStar.class, Color.valueOf("0d429dff"), null, Color.CYAN.cpy(), PhantasmalShootingStar.POTION_ID);
        BaseMod.addRelic(new Atri(), RelicType.SHARED);
        BaseMod.addRelic(new GrassNowAndFlowersThen(), RelicType.SHARED);
        BaseMod.addRelic(new FilmStarryCurtain(), RelicType.RED);
        BaseMod.addRelic(new DarkEnergyLantern(), RelicType.BLUE);
        BaseMod.addRelic(new DepartmentStore(), RelicType.SHARED);
        BaseMod.addRelic(new TheCrow(), RelicType.SHARED);
        BaseMod.addRelic(new SiphonGem(), RelicType.SHARED);
        BaseMod.addRelic(new Upgrade(), RelicType.SHARED);
        BaseMod.addRelic(new Key(), RelicType.SHARED);
        BaseMod.addRelic(new SoulStitch(), RelicType.SHARED);
        BaseMod.addRelic(new Ribbon(), RelicType.SHARED);
        BaseMod.addRelic(new GlassRod(), RelicType.SHARED);
        BaseMod.addRelic(new DelayedGratification(), RelicType.SHARED);
        BaseMod.addRelic(new WeatherControlDevice(), RelicType.BLUE);
        BaseMod.addRelic(new Lemonade(), RelicType.SHARED);
        BaseMod.addRelic(new BambooDragonflyOfHanyuHorner(), RelicType.SHARED);
        BaseMod.addRelic(new ProfiteeringMerchant(), RelicType.SHARED);
        BaseMod.addRelic(new ProfiteeringMerchantAttempted(), RelicType.SHARED);
        BaseMod.addRelic(new ProfiteeringMerchantAchieved(), RelicType.SHARED);
        BaseMod.addRelic(new ToxicLump(), RelicType.PURPLE);
        BaseMod.addRelic(new PlagueStopwatch(), RelicType.GREEN);
        //BaseMod.addRelic(new Proto(), RelicType.SHARED);
        BaseMod.addRelic(new SoulCannon(), RelicType.SHARED);
        BaseMod.addRelic(new SoulCannonOneTiny(), RelicType.SHARED);
        BaseMod.addRelic(new SoulCannonOneHuge(), RelicType.SHARED);
        BaseMod.addRelic(new SoulCannonTwoTiny(), RelicType.SHARED);
        BaseMod.addRelic(new SoulCannonTwoHuge(), RelicType.SHARED);
        BaseMod.addRelic(new SoulCannonThreeTiny(), RelicType.SHARED);
        BaseMod.addRelic(new SoulCannonThreeHuge(), RelicType.SHARED);
        BaseMod.addRelic(new SoulCannonFour(), RelicType.SHARED);
        BaseMod.addRelic(new Bindings(), RelicType.SHARED);
        BaseMod.addRelic(new WhiteDClips(), RelicType.SHARED);
        BaseMod.addRelic(new RestrainedHeart(), RelicType.GREEN);
        BaseMod.addRelic(new SteamEpic(), RelicType.SHARED);
        BaseMod.addRelic(new FTLEngines(), RelicType.SHARED);
        BaseMod.addRelic(new Decanter(), RelicType.SHARED);
        BaseMod.addRelic(new ClockWithNoHands(), RelicType.SHARED);
        BaseMod.addRelic(new RainingRuins(), RelicType.SHARED);
        BaseMod.addRelic(new PhoenixGift(), RelicType.SHARED);
        BaseMod.addRelic(new Extractor(), RelicType.SHARED);
        BaseMod.addRelic(new Echo(), RelicType.SHARED);
        BaseMod.addRelic(new ChuggingMask(), RelicType.SHARED);
        BaseMod.addRelic(new ExitPhase(), RelicType.SHARED);
        BaseMod.addRelic(new Maniacal(), RelicType.SHARED);
        BaseMod.addRelic(new RainbowColoredCircle(), RelicType.SHARED);
        BaseMod.addRelic(new BigBangBell(), RelicType.SHARED);
        BaseMod.addRelic(new IceGenerator(), RelicType.SHARED);
        BaseMod.addPotion(Blizzard.class, CardHelper.getColor(89,174,209),CardHelper.getColor(89,174,209),CardHelper.getColor(89,174,209),Blizzard.POTION_ID);
        BaseMod.addRelic(new GrabbyHands(), RelicType.SHARED);
        BaseMod.addRelic(new SeaOfMoon(), RelicType.SHARED);
        BaseMod.addRelic(new Zan(), RelicType.SHARED);
        BaseMod.addRelic(new StrongestPotion(), RelicType.SHARED);
        BaseMod.addRelic(new GlowFeather(), RelicType.SHARED);
        BaseMod.addRelic(new Evasive(), RelicType.SHARED);
        BaseMod.addRelic(new BigBow(), RelicType.PURPLE);
        BaseMod.addRelic(new Calculator(), RelicType.SHARED);
        BaseMod.addRelic(new StackedPlate(), RelicType.SHARED);
        BaseMod.addRelic(new HearthsMores(), RelicType.SHARED);
        BaseMod.addRelic(new SquirrelsRelief(), RelicType.RED);
        BaseMod.addRelic(new AutumnColors(), RelicType.SHARED);
        BaseMod.addRelic(new GenerosityCharm(), RelicType.SHARED);
        BaseMod.addRelic(new Leviboard(), RelicType.SHARED);
        BaseMod.addRelic(new CuttingMachine(), RelicType.SHARED);
        BaseMod.addRelic(new MessyPuppy(), RelicType.SHARED);
        BaseMod.addRelic(new MinoshiroModoki(), RelicType.SHARED);
        BaseMod.addRelic(new HermitsPockets(), RelicType.SHARED);
        BaseMod.addRelic(new AmorphousMass(), RelicType.SHARED);
        BaseMod.addRelic(new SwordOfFeastAndFamine(), RelicType.SHARED);
        BaseMod.addPotion(FrugalPotion.class, Color.ORANGE.cpy(), null, null, FrugalPotion.POTION_ID);
        BaseMod.addPotion(GreedyPotion.class, Color.GOLD.cpy(), null, null, GreedyPotion.POTION_ID);
        BaseMod.addRelic(new VolcanicCryster(), RelicType.SHARED);
        BaseMod.addRelic(new Turbolens(), RelicType.SHARED);
        BaseMod.addRelic(new BlackRabbit(), RelicType.SHARED);
        BaseMod.addRelic(new StroopsTester(), RelicType.SHARED);
        BaseMod.addRelic(new LuckyStar(), RelicType.SHARED);
        BaseMod.addRelic(new Wedgue(), RelicType.SHARED);
        BaseMod.addRelic(new StringThe(), RelicType.SHARED);
        BaseMod.addRelic(new Register(), RelicType.SHARED);
        BaseMod.addRelic(new SoulCraft(), RelicType.SHARED);
        BaseMod.addRelic(new StrikersVeil(), RelicType.SHARED);
        BaseMod.addRelic(new FishingNet(), RelicType.SHARED);
        BaseMod.addRelic(new MosquitoCoil(), RelicType.SHARED);
        BaseMod.addRelic(new PainsReward(), RelicType.SHARED);
        BaseMod.addRelic(new NoNameForNow5(), RelicType.SHARED);
        BaseMod.addRelic(new CirculatingLightAndColorShifter(), RelicType.SHARED);
        BaseMod.addRelic(new BanishingMace(), RelicType.SHARED);
        BaseMod.addRelic(new TheOwl(), RelicType.SHARED);
        BaseMod.addRelic(new ShammyPeach(), RelicType.SHARED);
        BaseMod.addRelic(new Catalyst(), RelicType.SHARED);
        BaseMod.addRelic(new Gluttony(), RelicType.SHARED);
        BaseMod.addRelic(new LoveChocolate(), RelicType.SHARED);

        BaseMod.addRelic(new ZhelpArcaneTalents(), RelicType.SHARED);
        BaseMod.addRelic(new ZhelpFrostTalents(), RelicType.SHARED);
        BaseMod.addRelic(new ZhelpFireTalents(), RelicType.SHARED);
        BaseMod.addRelic(new ZhelpLightningStorm(), RelicType.SHARED);
        BaseMod.addRelic(new ZhelpDecanter(), RelicType.SHARED);
        BaseMod.addRelic(new ZhelpPartScrapper(), RelicType.SHARED);
        BaseMod.addRelic(new ZhelpFrugalPotion(), RelicType.SHARED);
        BaseMod.addRelic(new ZhelpGreedyPotion(), RelicType.SHARED);
        BaseMod.addRelic(new ZhelpRoamingStrike(), RelicType.SHARED);
        BaseMod.addRelic(new ZhelpFinalForm(), RelicType.SHARED);
        BaseMod.addRelic(new ZhelpFinalFormPlus(), RelicType.SHARED);

        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Atri.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Bolster.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ButchersBlade.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(DarkEnergyLantern.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(DepartmentStore.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(FilmStarryCurtain.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Fractal.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(GrassNowAndFlowersThen.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(HermitsTiara.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(HungrySun.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(IcyTouch.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(InsanityStone.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Key.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Polearm.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SanityMaintenanceApparatus.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SiphonGem.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SoulStitch.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SunflowerInASummer.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(TheCrow.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Transcendence.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Upgrade.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Ribbon.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(GlassRod.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(DelayedGratification.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(WeatherControlDevice.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Lemonade.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(BambooDragonflyOfHanyuHorner.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ProfiteeringMerchant.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ProfiteeringMerchantAttempted.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ProfiteeringMerchantAchieved.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ToxicLump.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(PlagueStopwatch.class.getSimpleName()));
        //UnlockTracker.markRelicAsSeen(ModHelper.makePath(Proto.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SoulCannon.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SoulCannonOneTiny.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SoulCannonOneHuge.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SoulCannonTwoTiny.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SoulCannonTwoHuge.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SoulCannonThreeTiny.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SoulCannonThreeHuge.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(SoulCannonFour.ID);
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Bindings.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(WhiteDClips.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(RestrainedHeart.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(SteamEpic.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(FTLEngines.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Decanter.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ClockWithNoHands.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(RainingRuins.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(PhoenixGift.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Extractor.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(Echo.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ChuggingMask.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ExitPhase.ID);
        UnlockTracker.markRelicAsSeen(Maniacal.ID);
        UnlockTracker.markRelicAsSeen(RainbowColoredCircle.ID);
        UnlockTracker.markRelicAsSeen(BigBangBell.ID);
        UnlockTracker.markRelicAsSeen(IceGenerator.ID);
        UnlockTracker.markRelicAsSeen(GrabbyHands.ID);
        UnlockTracker.markRelicAsSeen(SeaOfMoon.ID);
        UnlockTracker.markRelicAsSeen(Zan.ID);
        UnlockTracker.markRelicAsSeen(StrongestPotion.ID);
        UnlockTracker.markRelicAsSeen(GlowFeather.ID);
        UnlockTracker.markRelicAsSeen(Evasive.ID);
        UnlockTracker.markRelicAsSeen(BigBow.ID);
        UnlockTracker.markRelicAsSeen(Calculator.ID);
        UnlockTracker.markRelicAsSeen(StackedPlate.ID);
        UnlockTracker.markRelicAsSeen(HearthsMores.ID);
        UnlockTracker.markRelicAsSeen(SquirrelsRelief.ID);
        UnlockTracker.markRelicAsSeen(AutumnColors.ID);
        UnlockTracker.markRelicAsSeen(GenerosityCharm.ID);
        UnlockTracker.markRelicAsSeen(Leviboard.ID);
        UnlockTracker.markRelicAsSeen(CuttingMachine.ID);
        UnlockTracker.markRelicAsSeen(MessyPuppy.ID);
        UnlockTracker.markRelicAsSeen(MinoshiroModoki.ID);
        UnlockTracker.markRelicAsSeen(HermitsPockets.ID);
        UnlockTracker.markRelicAsSeen(AmorphousMass.ID);
        UnlockTracker.markRelicAsSeen(SwordOfFeastAndFamine.ID);
        UnlockTracker.markRelicAsSeen(VolcanicCryster.ID);
        UnlockTracker.markRelicAsSeen(Turbolens.ID);
        UnlockTracker.markRelicAsSeen(BlackRabbit.ID);
        UnlockTracker.markRelicAsSeen(StroopsTester.ID);
        UnlockTracker.markRelicAsSeen(LuckyStar.ID);
        UnlockTracker.markRelicAsSeen(Wedgue.ID);
        UnlockTracker.markRelicAsSeen(StringThe.ID);
        UnlockTracker.markRelicAsSeen(Register.ID);
        UnlockTracker.markRelicAsSeen(SoulCraft.ID);
        UnlockTracker.markRelicAsSeen(StrikersVeil.ID);
        UnlockTracker.markRelicAsSeen(FishingNet.ID);
        UnlockTracker.markRelicAsSeen(MosquitoCoil.ID);
        UnlockTracker.markRelicAsSeen(PainsReward.ID);
        UnlockTracker.markRelicAsSeen(NoNameForNow5.ID);
        UnlockTracker.markRelicAsSeen(CirculatingLightAndColorShifter.ID);
        UnlockTracker.markRelicAsSeen(BanishingMace.ID);
        UnlockTracker.markRelicAsSeen(TheOwl.ID);
        UnlockTracker.markRelicAsSeen(ShammyPeach.ID);
        UnlockTracker.markRelicAsSeen(Catalyst.ID);
        UnlockTracker.markRelicAsSeen(Gluttony.ID);
        UnlockTracker.markRelicAsSeen(LoveChocolate.ID);

        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ZhelpArcaneTalents.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ZhelpFrostTalents.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ZhelpFireTalents.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ModHelper.makePath(ZhelpLightningStorm.class.getSimpleName()));
        UnlockTracker.markRelicAsSeen(ZhelpDecanter.ID);
        UnlockTracker.markRelicAsSeen(ZhelpPartScrapper.ID);
        UnlockTracker.markRelicAsSeen(ZhelpFrugalPotion.ID);
        UnlockTracker.markRelicAsSeen(ZhelpGreedyPotion.ID);
        UnlockTracker.markRelicAsSeen(ZhelpRoamingStrike.ID);
        UnlockTracker.markRelicAsSeen(ZhelpFinalForm.ID);
        UnlockTracker.markRelicAsSeen(ZhelpFinalFormPlus.ID);

        UnlockTracker.markCardAsSeen(ModHelper.makePath(ArcaneTalents.class.getSimpleName()));
        UnlockTracker.markCardAsSeen(ModHelper.makePath(FrostTalents.class.getSimpleName()));
        UnlockTracker.markCardAsSeen(ModHelper.makePath(FireTalents.class.getSimpleName()));
        UnlockTracker.markCardAsSeen(ModHelper.makePath(LightningStorm.class.getSimpleName()));
        UnlockTracker.markCardAsSeen(PartScrapper.ID);
        UnlockTracker.markCardAsSeen(BodySlamPlusPlus.ID);
        UnlockTracker.markCardAsSeen(Aurora.ID);
        UnlockTracker.markCardAsSeen(SoulCraftedCard.ID);
        UnlockTracker.markCardAsSeen(RoamingStrike.ID);
        UnlockTracker.markCardAsSeen(FinalForm.ID);
    }

    @Override
    public void receiveEditKeywords(){
        Gson gson = new Gson();
        String lang;
        switch (Settings.language) {
            case ZHS: lang="zhs"; break;// 如果语言设置为简体中文，则加载ZHS文件夹的资源
            case ZHT: lang="zht"; break;
            default: lang="eng"; break;// 如果没有相应语言的版本，默认加载英语
        }

        String json = Gdx.files.internal("StropsResources/localization/" + lang + "/KeywordStrings.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("strops", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    public void receivePostInitialize(){

        BaseMod.addEvent(new AddEventParams.Builder("Strops:MindBloom", StropsMindBloom.class).
                dungeonID(TheBeyond.ID).bonusCondition(()->AbstractDungeon.player.hasRelic(ModHelper.makePath(Key.class.getSimpleName()))
                ).overrideEvent("MindBloom").create());

        try {
            config = new SpireConfig("strops", "stropsSettingsData");
        } catch (IOException e) {
            logger.info("An exception happened while creating stropsSettingsData!");
        }

        Texture badgeTexture = new Texture("StropsResources/img/misc/ModBadge.png");
        settingsPanel = new ModPanel();
        List<RelicSettingsButton> settingsButtons = new ArrayList<>();

        BuildSettings(new Atri());
        BuildSettings(new BambooDragonflyOfHanyuHorner());
        BuildSettings(new Bolster());
        BuildSettings(new ButchersBlade());
        BuildSettings(new DarkEnergyLantern());
        BuildSettings(new DelayedGratification());
        BuildSettings(new DepartmentStore());
        BuildSettings(new FilmStarryCurtain());
        BuildSettings(new Fractal());
        BuildSettings(new GlassRod());
        BuildSettings(new GrassNowAndFlowersThen());
        BuildSettings(new HermitsTiara());
        BuildSettings(new HungrySun());
        BuildSettings(new IcyTouch());
        BuildSettings(new InsanityStone());
        BuildSettings(new Key());
        BuildSettings(new Lemonade());
        BuildSettings(new PlagueStopwatch());
        BuildSettings(new Polearm());
        BuildSettings(new Ribbon());
        BuildSettings(new SanityMaintenanceApparatus());
        BuildSettings(new SiphonGem());
        BuildSettings(new SoulStitch());
        BuildSettings(new SunflowerInASummer());
        BuildSettings(new TheCrow());
        BuildSettings(new ToxicLump());
        BuildSettings(new Transcendence());
        BuildSettings(new Upgrade());
        BuildSettings(new WeatherControlDevice());
        BuildSettings(new ProfiteeringMerchant());
        BuildSettings(new SoulCannon());
        BuildSettings(new SoulCannonOneTiny());
        BuildSettings(new SoulCannonOneHuge());
        BuildSettings(new SoulCannonTwoTiny());
        BuildSettings(new SoulCannonTwoHuge());
        BuildSettings(new SoulCannonThreeTiny());
        BuildSettings(new SoulCannonThreeHuge());
        BuildSettings(new SoulCannonFour());
        BuildSettings(new Bindings());
        BuildSettings(new WhiteDClips());
        BuildSettings(new RestrainedHeart());
        BuildSettings(new SteamEpic());
        BuildSettings(new FTLEngines());
        BuildSettings(new Decanter());
        BuildSettings(new ClockWithNoHands());
        BuildSettings(new RainingRuins());
        BuildSettings(new PhoenixGift());
        BuildSettings(new Extractor());
        BuildSettings(new Echo());
        BuildSettings(new ChuggingMask());
        BuildSettings(new ExitPhase());
        BuildSettings(new Maniacal());
        BuildSettings(new RainbowColoredCircle());
        BuildSettings(new BigBangBell());
        BuildSettings(new IceGenerator());
        BuildSettings(new GrabbyHands());
        BuildSettings(new SeaOfMoon());
        BuildSettings(new Zan());
        BuildSettings(new StrongestPotion());
        BuildSettings(new GlowFeather());
        BuildSettings(new Evasive());
        BuildSettings(new BigBow());
        BuildSettings(new Calculator());
        BuildSettings(new StackedPlate());
        BuildSettings(new HearthsMores());
        BuildSettings(new SquirrelsRelief());
        BuildSettings(new AutumnColors());
        BuildSettings(new GenerosityCharm());
        BuildSettings(new Leviboard());
        BuildSettings(new CuttingMachine());
        BuildSettings(new MessyPuppy());
        BuildSettings(new MinoshiroModoki());
        BuildSettings(new HermitsPockets());
        BuildSettings(new AmorphousMass());
        BuildSettings(new SwordOfFeastAndFamine());
        BuildSettings(new VolcanicCryster());
        BuildSettings(new Turbolens());
        BuildSettings(new BlackRabbit());
        BuildSettings(new StroopsTester());
        BuildSettings(new LuckyStar());
        BuildSettings(new Wedgue());
        BuildSettings(new StringThe());
        BuildSettings(new Register());
        BuildSettings(new SoulCraft());
        BuildSettings(new StrikersVeil());
        BuildSettings(new FishingNet());
        BuildSettings(new MosquitoCoil());
        BuildSettings(new PainsReward());
        BuildSettings(new NoNameForNow5());
        BuildSettings(new CirculatingLightAndColorShifter());
        BuildSettings(new BanishingMace());
        BuildSettings(new TheOwl());
        BuildSettings(new ShammyPeach());
        BuildSettings(new Catalyst());
        BuildSettings(new Gluttony());
        BuildSettings(new LoveChocolate());

        BuildSettings(new ZhelpArcaneTalents());
        BuildSettings(new ZhelpFrostTalents());
        BuildSettings(new ZhelpFireTalents());
        BuildSettings(new ZhelpLightningStorm());
        BuildSettings(new ZhelpPartScrapper());
        BuildSettings(new ZhelpFrugalPotion());
        BuildSettings(new ZhelpGreedyPotion());
        BuildSettings(new ZhelpRoamingStrike());
        BuildSettings(new ZhelpFinalForm());
        BuildSettings(new ZhelpFinalFormPlus());

        loadSettingsData();
        for (StropsAbstractRelic relic : RelicSettings.keySet()) {
            settingsButtons.add(BuildSettingsButton(relic));
        }

        pager = new Pagination(new ImageButton("StropsResources/img/misc/tinyRightArrow.png", 615, 550, 100, 100, b -> {

        }), new ImageButton("StropsResources/img/misc/tinyLeftArrow.png", 350, 550, 100, 100, b -> {

        }), 2, 3, 50, 50, settingsButtons);
        settingsPanel.addUIElement(pager);
        BaseMod.registerModBadge(badgeTexture, "strops", "旅渚Lastcel", "0230", settingsPanel);

        if(!Loader.isModLoaded("RelicFilter")&&
                !(Loader.isModLoaded("IsaacMod")&&Loader.isModLoaded("MoreIsaacExpansion"))){
            RelicLibrary.resetForReload();
            RelicLibrary.initialize();
        }

        CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo(){
            @Override
            public boolean test(AbstractCard c) {
                if(c.cardID.equals(ArcaneTalents.ID)&&
                        PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount
                                .get(AbstractDungeon.player)>=c.magicNumber) {
                    return true;
                }

                if(c.cardID.equals(FrostTalents.ID)&&
                        PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount
                                .get(AbstractDungeon.player)>=c.magicNumber) {
                    return true;
                }

                if(c.cardID.equals(FireTalents.ID)&&
                        PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount
                                .get(AbstractDungeon.player)>=((FireTalents)c).keyNumber1) {
                    return true;
                }

                return false;
            }
            @Override
            public Color getColor(AbstractCard card) {
                return Color.YELLOW.cpy();
            }
            @Override
            public String glowID() {
                return GrassNowAndFlowersThen.ID+"@Glow";
            }
        });

        CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo(){
            @Override
            public boolean test(AbstractCard c) {
                if(AbstractDungeon.player==null){
                    return false;
                }
                for(AbstractRelic r:AbstractDungeon.player.relics) {
                    if(r.relicId.equals(Wedgue.ID)&&c==((Wedgue)r).drawnCard) {
                        return true;
                    }
                }
                return false;
            }
            @Override
            public Color getColor(AbstractCard card) {
                return Color.PURPLE.cpy();
            }
            @Override
            public String glowID() {
                return Wedgue.ID+"@Glow";
            }
        });

        CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo(){
            @Override
            public boolean test(AbstractCard c) {
                if(!(c instanceof SoulCraftedCard)){
                    return false;
                }
                for(AbstractCard c2:AbstractDungeon.player.masterDeck.group) {
                    if(c2.uuid.equals(c.uuid)) {
                        return true;
                    }
                }
                return false;
            }
            @Override
            public Color getColor(AbstractCard card) {
                return Color.YELLOW.cpy();
            }
            @Override
            public String glowID() {
                return SoulCraft.ID+"@Glow";
            }
        });

        CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo(){
            @Override
            public boolean test(AbstractCard c) {
                if(AbstractDungeon.player==null){
                    return false;
                }
                if(!AbstractDungeon.player.hasRelic(LuckyStar.ID)){
                    return false;
                }
                if(((LuckyStar)AbstractDungeon.player.getRelic(LuckyStar.ID)).uuidStart!=null){
                    return false;
                }
                for(AbstractCard c2:AbstractDungeon.player.masterDeck.group) {
                    if(c2.uuid.equals(c.uuid)) {
                        return true;
                    }
                }
                return false;
            }
            @Override
            public Color getColor(AbstractCard card) {
                return Color.ORANGE.cpy();
            }
            @Override
            public String glowID() {
                return LuckyStar.ID+"@Glow";
            }
        });

        CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo(){
            @Override
            public boolean test(AbstractCard c) {
                if(!c.cardID.equals(RitualDagger.ID)&&!c.cardID.equals(GeneticAlgorithm.ID)){
                    return false;
                }
                for(AbstractCard c2:AbstractDungeon.player.masterDeck.group) {
                    if(c2.uuid.equals(c.uuid)) {
                        return true;
                    }
                }
                return false;
            }
            @Override
            public Color getColor(AbstractCard card) {
                return Color.YELLOW.cpy();
            }
            @Override
            public String glowID() {
                return "Strops:VanillaDeckCheck@Glow";
            }
        });
    }

    public static void saveSettingsData() {
        try {
            SpireConfig config = new SpireConfig("strops", "stropsSettingsData");
            for (String key : ConfigSettings.keySet()) {
                RelicSetting setting = ConfigSettings.get(key);
                //logger.info("strops保存"+key+"为"+((IntSliderSetting)setting).value);
                setting.SaveToData(config);
            }
            config.save();
        } catch (IOException e) {
            logger.info("An exception happened during strops' saveSettingsData!");
        }
    }

    public static void loadSettingsData() {
        //logger.info("Strops | Loading Data...");
        try {
            config.load();
            for (String key : ConfigSettings.keySet()) {
                //logger.info("key="+key);
                RelicSetting setting = ConfigSettings.get(key);
                if (!config.has(setting.settingsId)) {
                    config.setString(setting.settingsId, setting.defaultProperty);
                    config.setBool(setting.settingsId + "_IS_SET_TO_DEFAULT", true);
                } else if (!config.has(setting.settingsId + "_IS_SET_TO_DEFAULT")) {
                    config.setBool(setting.settingsId + "_IS_SET_TO_DEFAULT", false);
                }
                //logger.info("strops读取");
                setting.LoadFromData(config);
            }
        } catch (IOException e) {
            //logger.error("Failed to load Strops settings data!");
            logger.info("An exception happened during strops' loadSettingsData!");
        }
        for (StropsAbstractRelic _r : RelicSettings.keySet()) {
            AbstractRelic r = RelicLibrary.getRelic(_r.relicId);
            if ((r != null) && (!r.relicId.equals("Circlet")))
                ((StropsAbstractRelic)r).updateDesc();
        }
    }

    @Override
    public int receiveMaxHPChange(int amount){
        for(AbstractRelic r:AbstractDungeon.player.relics){
            if(r.relicId.equals(ChuggingMask.ID)){
                r.flash();
                if(amount>0){
                    AbstractDungeon.player.heal(amount);
                } else if(amount<0){
                    AbstractDungeon.player.heal(-amount);
                }
                break;
            }
        }

        /*
        if(AbstractDungeon.getCurrMapNode()!=null&&AbstractDungeon.getCurrRoom()!=null&&AbstractDungeon.getCurrRoom().phase!=AbstractRoom.RoomPhase.COMBAT){

        }

         */

        return amount;
    }

    @Override
    public void receivePostDungeonInitialize() {
        if(AbstractDungeon.floorNum>1){
            return;
        }

        if(LoveChocolate.getBelovedDates().contains(new SimpleDateFormat("MM/dd").format(new Date(System.currentTimeMillis())))){
            LoveChocolate loveChocolate=new LoveChocolate();
            if (!AbstractDungeon.player.hasRelic(LoveChocolate.ID)) {
                loveChocolate.instantObtain();
            }
            return;
        }

        for(String s:LoveChocolate.getBelovedUserNames()){
            if(CardCrawlGame.playerName.toLowerCase().contains(s)){
                LoveChocolate loveChocolate=new LoveChocolate();
                if (!AbstractDungeon.player.hasRelic(LoveChocolate.ID)) {
                    loveChocolate.instantObtain();
                }
                return;
            }
        }
    }
}
