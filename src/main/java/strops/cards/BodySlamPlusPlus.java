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

public class BodySlamPlusPlus extends CustomCard {
    public static final String ID = ModHelper.makePath(BodySlamPlusPlus.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/BodySlamPlusPlus.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public BodySlamPlusPlus() {
        super(ID, NAME, IMG_PATH, 0, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        upgrade();
        baseDamage=0;
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgraded=true;
            timesUpgraded++;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.baseDamage = p.currentBlock*2;
        calculateCardDamage(m);
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        this.baseDamage = AbstractDungeon.player.currentBlock*2;
        super.applyPowers();
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.rawDescription += CARD_STRINGS.UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.rawDescription += CARD_STRINGS.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
