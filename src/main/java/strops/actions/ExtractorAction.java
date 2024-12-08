package strops.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import strops.relics.Extractor;

public class ExtractorAction extends AbstractGameAction {
    public Extractor extractor;

    public ExtractorAction(Extractor extractor, AbstractCreature target, int amount) {
        this.extractor=extractor;
        this.target=target;
        this.amount=amount;
        this.source=AbstractDungeon.player;
    }

    public void update() {
        //AbstractDungeon.player.gainGold(this.amount);
        extractor.counter+=amount;
        for(int i=0;i<this.amount;i++){
            AbstractDungeon.effectList.add(new GainPennyEffect(this.source, this.target.hb.cX, this.target.hb.cY, this.source.hb.cX, this.source.hb.cY, true));
        }
        this.isDone = true;
    }
}
