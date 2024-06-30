package strops.cards;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import strops.helpers.ModHelper;
import strops.relics.ZhelpLightningStorm;

public class LightningStorm extends AbstractStropsCard{
    public static final String ID = ModHelper.makePath(LightningStorm.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/LightningStorm.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public static int LightningCountLastTurn=0;

    public LightningStorm() {
        super(ID, NAME, IMG_PATH, ZhelpLightningStorm.COST.value, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber=this.baseMagicNumber=ZhelpLightningStorm.MULTIPLIER.value;
        this.keyNumber1=this.baseKeyNumber1=ZhelpLightningStorm.FIXED_PART.value;
        this.keyNumber2=this.baseKeyNumber2=0;
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.baseKeyNumber2 = LightningCountLastTurn;
        this.baseKeyNumber2*=this.baseMagicNumber;
        if(upgraded){
            this.baseKeyNumber2+=this.baseKeyNumber1;
        }
        this.keyNumber2 = this.baseKeyNumber2;
        for (int i = 0; i < this.keyNumber2; i++){
            addToBot(new ChannelAction(new Lightning()));
        }
    }

    public void applyPowers() {
        super.applyPowers();
        this.keyNumber2=0;
        this.baseKeyNumber2=LightningCountLastTurn;
        this.baseKeyNumber2*=this.baseMagicNumber;
        if(upgraded){
            this.baseKeyNumber2+=this.baseKeyNumber1;
        }
        if (this.baseKeyNumber2 > 0) {
            this.rawDescription = (upgraded?CARD_STRINGS.UPGRADE_DESCRIPTION:DESCRIPTION) +
                    CARD_STRINGS.EXTENDED_DESCRIPTION[0];
            initializeDescription();
        }
    }

    public void onMoveToDiscard() {
        this.rawDescription = (upgraded?CARD_STRINGS.UPGRADE_DESCRIPTION:DESCRIPTION);
        initializeDescription();
    }

    public AbstractCard makeCopy() {
        return new LightningStorm();
    }
}
