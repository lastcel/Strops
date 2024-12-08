package strops.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.SpotlightPlayerEffect;
import strops.helpers.ModHelper;
import strops.patch.PatchGrassNowAndFlowersThen;
import strops.powers.SkipPlayerTurnPower;
import strops.powers.SuspendedGoldPower;
import strops.relics.ZhelpFinalForm;
import strops.relics.ZhelpFinalFormPlus;

public class FinalForm extends AbstractStropsCard {
    public static final String ID = ModHelper.makePath(FinalForm.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/FinalForm.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public FinalForm() {
        super(ID, NAME, IMG_PATH, ZhelpFinalForm.COST.value, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage=this.baseDamage=ZhelpFinalForm.DMG.value;
        this.block=this.baseBlock=ZhelpFinalForm.BLK.value;
        this.magicNumber=this.baseMagicNumber=ZhelpFinalForm.BONUS_INSTANT.value;
        this.keyNumber1=this.baseKeyNumber1=ZhelpFinalForm.SUSPENSE_TURN.value;
        this.keyNumber2=this.baseKeyNumber2=ZhelpFinalForm.BONUS_DEPENDANT.value;
        this.exhaust=true;
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            upgradeDamage(ZhelpFinalFormPlus.DMG.value-ZhelpFinalForm.DMG.value);
            upgradeBlock(ZhelpFinalFormPlus.BLK.value-ZhelpFinalForm.BLK.value);
            upgradeMagicNumber(ZhelpFinalFormPlus.BONUS_INSTANT.value-ZhelpFinalForm.BONUS_INSTANT.value);
            upgradeKeyNumber2(ZhelpFinalFormPlus.BONUS_DEPENDANT.value-ZhelpFinalForm.BONUS_DEPENDANT.value);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p,
                damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        addToBot(new GainBlockAction(p, p, block));
        AbstractDungeon.effectList.add(new RainingGoldEffect(magicNumber*2, true));
        AbstractDungeon.effectsQueue.add(new SpotlightPlayerEffect());
        addToBot(new GainGoldAction(magicNumber));
        if(PatchGrassNowAndFlowersThen.PatchTool1.earliestTurnCount.get(AbstractDungeon.player)<=ZhelpFinalForm.SUSPENSE_TURN.value){
            addToBot(new ApplyPowerAction(p,p,new SuspendedGoldPower(p,keyNumber2),keyNumber2));
        }
        addToBot(new ApplyPowerAction(p,p,new SkipPlayerTurnPower(p,1),1));
    }
}
