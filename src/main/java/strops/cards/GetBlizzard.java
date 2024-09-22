package strops.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import strops.helpers.ModHelper;
import strops.potions.Blizzard;
import strops.relics.Decanter;
import strops.relics.IceGenerator;

public class GetBlizzard extends AbstractStropsCard {
    public static final String ID = ModHelper.makePath(GetBlizzard.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "StropsResources/img/cards/GetBlizzard.png";
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.STATUS;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public GetBlizzard(){
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber=this.baseMagicNumber=IceGenerator.MAXHP_PENALTY.value;
        this.keyNumber1=this.baseKeyNumber1=IceGenerator.HP_PENALTY.value;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {}

    public void onChoseThisOption() {
        if(IceGenerator.MAXHP_PENALTY.value>0){
            AbstractDungeon.player.decreaseMaxHealth(IceGenerator.MAXHP_PENALTY.value);
        }

        if(IceGenerator.HP_PENALTY.value>0){
            AbstractDungeon.player.damage(new DamageInfo(null, IceGenerator.HP_PENALTY.value));
        }

        boolean canGet=true;
        if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
            AbstractRelic r2;
            if(AbstractDungeon.player.hasRelic(Decanter.ID)&&
                    ((Decanter)(r2 = AbstractDungeon.player.getRelic(Decanter.ID)))
                            .relicToDisenchant.equals(Sozu.ID)){
                r2.flash();
                ((Decanter) r2).decay();
            } else {
                AbstractDungeon.player.getRelic(Sozu.ID).flash();
                canGet=false;
            }
        }
        if(canGet){
            for(int i=0;i<IceGenerator.BOTTLE.value;i++){
                AbstractDungeon.player.obtainPotion(PotionHelper.getPotion(Blizzard.POTION_ID));
            }
        }

        if(AbstractDungeon.getCurrRoom().phase==AbstractRoom.RoomPhase.INCOMPLETE){
            AbstractDungeon.getCurrRoom().phase=AbstractRoom.RoomPhase.COMPLETE;
        }
    }

    public void upgrade() {}

    public AbstractCard makeCopy() {
        return new GetBlizzard();
    }
}
