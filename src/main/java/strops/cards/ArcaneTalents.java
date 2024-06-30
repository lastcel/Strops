package strops.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import strops.helpers.ModHelper;
import strops.patch.PatchGrassNowAndFlowersThen;
import strops.relics.ZhelpArcaneTalents;

public class ArcaneTalents extends AbstractStropsCard {
    public static final String ID = ModHelper.makePath(ArcaneTalents.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/ArcaneTalents.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ArcaneTalents() {
        super(ID, NAME, IMG_PATH, ZhelpArcaneTalents.COST.value, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage=this.baseDamage=ZhelpArcaneTalents.DMG_BASE.value;
        this.magicNumber=this.baseMagicNumber=ZhelpArcaneTalents.THIRST_TURN.value;
        this.keyNumber1=this.baseKeyNumber1=ZhelpArcaneTalents.THIRST_BOOST.value;
        this.exhaust = true;
        this.cardsToPreview=new FrostTalents();
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            upgradeDamage(ZhelpArcaneTalents.DMG_UPGRADED.value-ZhelpArcaneTalents.DMG_BASE.value);
            this.cardsToPreview.upgrade();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        FrostTalents c;
        addToBot(new DamageAction(m, new DamageInfo(p,
                this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        addToBot(new MakeTempCardInDrawPileAction(c=new FrostTalents(), 1, false, true));
        if(this.upgraded)
        {
            c.upgrade();
        }
        if(PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player)>=c.magicNumber){
            c.baseBlock+=c.keyNumber1;
        }
    }
}
