package strops.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import java.util.ArrayList;

public class RelicSelectScreen implements ScrollBarListener {
    private static final float SPACE = 80.0F * Settings.scale;

    private static final float START_X = 600.0F * Settings.scale;

    private static final float START_Y = Settings.HEIGHT - 300.0F * Settings.scale;

    private float scrollY = START_Y;

    private float targetY = this.scrollY;

    private float scrollLowerBound = Settings.HEIGHT - 200.0F * Settings.scale;

    private float scrollUpperBound = this.scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT;

    private int row = 0;

    private int col = 0;

    private static final Color RED_OUTLINE_COLOR = new Color(-10132568);

    private static final Color GREEN_OUTLINE_COLOR = new Color(2147418280);

    private static final Color BLUE_OUTLINE_COLOR = new Color(-2016482392);

    private static final Color BLACK_OUTLINE_COLOR = new Color(168);

    private AbstractRelic hoveredRelic = null;

    private AbstractRelic clickStartedRelic = null;

    private boolean grabbedScreen = false;

    private float grabStartY = 0.0F;

    private ScrollBar scrollBar;

    private Hitbox controllerRelicHb = null;

    private ArrayList<AbstractRelic> relics;

    private boolean show = false;

    private int selectCount = 1;

    private ArrayList<AbstractRelic> selectedRelics = new ArrayList<>();

    public boolean doneSelecting() {
        return (this.selectedRelics.size() >= this.selectCount);
    }

    public ArrayList<AbstractRelic> getSelectedRelics() {
        ArrayList<AbstractRelic> ret = new ArrayList<>(this.selectedRelics);
        this.selectedRelics.clear();
        return ret;
    }

    public RelicSelectScreen() {
        this.scrollBar = new ScrollBar(this);
    }

    public void open(ArrayList<AbstractRelic> relics) {
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.showBlackScreen(0.5F);
        AbstractDungeon.overlayMenu.proceedButton.hide();
        this.show = true;
        this.controllerRelicHb = null;
        this.relics = relics;
        this.targetY = this.scrollLowerBound;
        this.scrollY = Settings.HEIGHT - 400.0F * Settings.scale;
        calculateScrollBounds();
        this.selectedRelics.clear();
    }

    public void close() {
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.GRID;
        AbstractDungeon.closeCurrentScreen();
        this.show = false;
    }

    public boolean isOpen() {
        return this.show;
    }

    public void update() {
        if (!isOpen())
            return;
        updateControllerInput();
        if (Settings.isControllerMode && this.controllerRelicHb != null)
            if (Gdx.input.getY() > Settings.HEIGHT * 0.7F) {
                this.targetY += Settings.SCROLL_SPEED;
                if (this.targetY > this.scrollUpperBound)
                    this.targetY = this.scrollUpperBound;
            } else if (Gdx.input.getY() < Settings.HEIGHT * 0.3F) {
                this.targetY -= Settings.SCROLL_SPEED;
                if (this.targetY < this.scrollLowerBound)
                    this.targetY = this.scrollLowerBound;
            }
        if (this.hoveredRelic != null) {
            if (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed())
                this.clickStartedRelic = this.hoveredRelic;
            if (InputHelper.justReleasedClickLeft || CInputActionSet.select.isJustPressed()) {
                CInputActionSet.select.unpress();
                if (this.hoveredRelic == this.clickStartedRelic) {
                    this.selectedRelics.add(this.hoveredRelic);
                    this.clickStartedRelic = null;
                    if (doneSelecting())
                        close();
                }
            }
            if (InputHelper.justClickedRight || CInputActionSet.select.isJustPressed())
                this.clickStartedRelic = this.hoveredRelic;
            if (InputHelper.justReleasedClickRight || CInputActionSet.select.isJustPressed()) {
                CInputActionSet.select.unpress();
                if (this.hoveredRelic == this.clickStartedRelic) {
                    CardCrawlGame.relicPopup.open(this.hoveredRelic, this.relics);
                    this.clickStartedRelic = null;
                }
            }
        } else {
            this.clickStartedRelic = null;
        }
        boolean isScrollingScrollBar = this.scrollBar.update();
        if (!isScrollingScrollBar)
            updateScrolling();
        InputHelper.justClickedLeft = false;
        InputHelper.justClickedRight = false;
        this.hoveredRelic = null;
        updateList(this.relics);
        if (Settings.isControllerMode && this.controllerRelicHb != null)
            Gdx.input.setCursorPosition((int)this.controllerRelicHb.cX, (int)(Settings.HEIGHT - this.controllerRelicHb.cY));
    }

    private void updateControllerInput() {}

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (!this.grabbedScreen) {
            if (InputHelper.scrolledDown) {
                this.targetY += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                this.targetY -= Settings.SCROLL_SPEED;
            }
            if (InputHelper.justClickedLeft) {
                this.grabbedScreen = true;
                this.grabStartY = y - this.targetY;
            }
        } else if (InputHelper.isMouseDown) {
            this.targetY = y - this.grabStartY;
        } else {
            this.grabbedScreen = false;
        }
        this.scrollY = MathHelper.scrollSnapLerpSpeed(this.scrollY, this.targetY);
        resetScrolling();
        updateBarPosition();
    }

    private void calculateScrollBounds() {
        int size = this.relics.size();
        int scrollTmp = 0;
        if (size > 10) {
            scrollTmp = size / 10 - 2;
            if (size % 10 != 0)
                scrollTmp++;
            this.scrollUpperBound = this.scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT + scrollTmp * 50.0F * Settings.scale;
        } else {
            this.scrollUpperBound = this.scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT;
        }
    }

    private void resetScrolling() {
        if (this.targetY < this.scrollLowerBound) {
            this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollLowerBound);
        } else if (this.targetY > this.scrollUpperBound) {
            this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollUpperBound);
        }
    }

    private void updateList(ArrayList<AbstractRelic> list) {
        for (AbstractRelic r : list) {
            r.hb.move(r.currentX, r.currentY);
            r.update();
            if (r.hb.hovered)
                this.hoveredRelic = r;
        }
    }

    public void render(SpriteBatch sb) {
        if (!isOpen())
            return;
        this.row = -1;
        this.col = 0;
        //logger.info("预备进入渲染遗物列表方法");
        renderList(sb, this.relics);
        this.scrollBar.render(sb);
    }

    private void renderList(SpriteBatch sb, ArrayList<AbstractRelic> list) {
        //logger.info("进入渲染列表方法");
        this.row++;
        this.col = 0;
        for (AbstractRelic r : list) {
            //logger.info("遍历列表，遗物="+r.relicId);
            if (this.col == 10) {
                this.col = 0;
                this.row++;
            }
            r.currentX = START_X + SPACE * this.col;
            r.currentY = this.scrollY - SPACE * this.row;
            if (RelicLibrary.redList.contains(r)) {
                r.render(sb, false, RED_OUTLINE_COLOR);
            } else if (RelicLibrary.greenList.contains(r)) {
                r.render(sb, false, GREEN_OUTLINE_COLOR);
            } else if (RelicLibrary.blueList.contains(r)) {
                r.render(sb, false, BLUE_OUTLINE_COLOR);
            } else {
                //logger.info("预备进入通用遗物渲染方法，遗物="+r.relicId);
                r.render(sb, false, BLACK_OUTLINE_COLOR);
            }
            this.col++;
        }
    }

    public void scrolledUsingBar(float newPercent) {
        float newPosition = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.scrollY = newPosition;
        this.targetY = newPosition;
        updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.scrollY);
        this.scrollBar.parentScrolledToPercent(percent);
    }
}

