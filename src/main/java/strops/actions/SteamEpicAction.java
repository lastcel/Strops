package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SteamEpicAction extends AbstractGameAction {
    public void update() {
        //logger.info("进入蒸汽史诗动作");
        for(AbstractCard c:AbstractDungeon.player.hand.group){
            if(c.exhaust&&!(c.type== AbstractCard.CardType.STATUS)&&
                    !(c.type== AbstractCard.CardType.CURSE)){
                //logger.info("保留："+c.name);
                c.retain = true;
            }
        }
        for(AbstractCard c:AbstractDungeon.player.drawPile.group){
            if(c.exhaust&&!(c.type== AbstractCard.CardType.STATUS)&&
                    !(c.type== AbstractCard.CardType.CURSE)){
                c.retain = true;
            }
        }
        for(AbstractCard c:AbstractDungeon.player.discardPile.group){
            if(c.exhaust&&!(c.type== AbstractCard.CardType.STATUS)&&
                    !(c.type== AbstractCard.CardType.CURSE)){
                c.retain = true;
            }
        }
        for(AbstractCard c:AbstractDungeon.player.exhaustPile.group){
            if(c.exhaust&&!(c.type== AbstractCard.CardType.STATUS)&&
                    !(c.type== AbstractCard.CardType.CURSE)){
                c.retain = true;
            }
        }

        this.isDone=true;
    }
}

