package strops.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import strops.actions.LoseBlockAction;
import strops.helpers.ModHelper;
import strops.relics.ZhelpPartScrapper;

public class PartScrapper extends CustomCard {
    public static final String ID = ModHelper.makePath(PartScrapper.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/PartScrapper.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public PartScrapper() {
        super(ID, NAME, IMG_PATH, ZhelpPartScrapper.COST.value, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber=baseMagicNumber=ZhelpPartScrapper.BLOCK_CONSUMPTION.value;
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(AbstractDungeon.player.currentBlock>=magicNumber){
            addToBot(new LoseBlockAction(magicNumber));
            if(upgraded){
                addToBot(new GainEnergyAction(3));
            } else {
                addToBot(new GainEnergyAction(2));
            }
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F,String.format(CARD_STRINGS.EXTENDED_DESCRIPTION[0],magicNumber), true));
        }
    }
}
