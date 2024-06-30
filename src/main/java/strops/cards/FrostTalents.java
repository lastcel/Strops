package strops.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.helpers.ModHelper;
import strops.patch.PatchGrassNowAndFlowersThen;
import strops.relics.ZhelpFrostTalents;

public class FrostTalents extends AbstractStropsCard{
    public static final String ID = ModHelper.makePath(FrostTalents.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/FrostTalents.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public FrostTalents() {
        super(ID, NAME, IMG_PATH, ZhelpFrostTalents.COST.value, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block=this.baseBlock= ZhelpFrostTalents.BLK_BASE.value;
        this.magicNumber=this.baseMagicNumber=ZhelpFrostTalents.THIRST_TURN.value;
        this.keyNumber1=this.baseKeyNumber1=ZhelpFrostTalents.THIRST_BOOST.value;
        this.exhaust = true;
        this.cardsToPreview=new FireTalents();
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            upgradeBlock(ZhelpFrostTalents.BLK_UPGRADED.value-ZhelpFrostTalents.BLK_BASE.value);
            this.cardsToPreview.upgrade();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        FireTalents c;
        addToBot(new GainBlockAction(p,p, this.block));
        addToBot(new MakeTempCardInDrawPileAction(c=new FireTalents(), 1, false, true));
        if(this.upgraded)
        {
            c.upgrade();
        }
        if(PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>=c.keyNumber1){
            c.baseMagicNumber+=c.keyNumber2;
        }
    }
}
