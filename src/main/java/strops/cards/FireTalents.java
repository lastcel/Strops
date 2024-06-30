package strops.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.helpers.ModHelper;
import strops.powers.FireTalentsPower;
import strops.relics.ZhelpFireTalents;

public class FireTalents extends AbstractStropsCard{
    public static final String ID = ModHelper.makePath(FireTalents.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/FireTalents.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public FireTalents() {
        super(ID, NAME, IMG_PATH, ZhelpFireTalents.COST.value, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber=this.baseMagicNumber=ZhelpFireTalents.MGC_BASE.value;
        this.keyNumber1=this.baseKeyNumber1=ZhelpFireTalents.THIRST_TURN.value;
        this.keyNumber2=this.baseKeyNumber2=ZhelpFireTalents.THIRST_BOOST.value;
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            upgradeMagicNumber(ZhelpFireTalents.MGC_UPGRADED.value-ZhelpFireTalents.MGC_BASE.value);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //BaseMod.logger.info("元时间="+ Gdx.graphics.getDeltaTime());
        addToBot(new ApplyPowerAction(p,p,new FireTalentsPower(p,baseMagicNumber),baseMagicNumber));
    }
}
