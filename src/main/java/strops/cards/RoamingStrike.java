package strops.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.helpers.ModHelper;
import strops.relics.ZhelpRoamingStrike;

public class RoamingStrike extends CustomCard {
    public static final String ID = ModHelper.makePath(RoamingStrike.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/RoamingStrike.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public RoamingStrike() {
        super(ID, NAME, IMG_PATH, 1, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage=0;
        magicNumber=baseMagicNumber=ZhelpRoamingStrike.MULTIPLIER_BASE.value;
        exhaust=true;
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            upgradeMagicNumber(ZhelpRoamingStrike.MULTIPLIER_UPGRADED.value-ZhelpRoamingStrike.MULTIPLIER_BASE.value);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.baseDamage = AbstractDungeon.actNum * magicNumber;
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
        rawDescription = CARD_STRINGS.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        baseDamage = AbstractDungeon.actNum * magicNumber;
        super.applyPowers();
        rawDescription = CARD_STRINGS.DESCRIPTION;
        rawDescription += CARD_STRINGS.UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = CARD_STRINGS.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        rawDescription = CARD_STRINGS.DESCRIPTION;
        rawDescription += CARD_STRINGS.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
