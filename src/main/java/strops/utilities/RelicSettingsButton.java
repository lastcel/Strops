package strops.utilities;

import basemod.IUIElement;
import basemod.ModButton;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import java.util.ArrayList;
import java.util.List;
import strops.modcore.Strops;

public class RelicSettingsButton implements IUIElement {
    public static final float DEFAULT_X = 450.0F;

    public static final float DEFAULT_Y = 625.0F;

    public static final float DEFAULT_W = 100.0F;

    public static final float DEFAULT_H = 100.0F;

    public static RelicSettingsButton activeButton;

    public Texture image;

    public Texture outline;

    public float x;

    public float y;

    public float w;

    public float h;

    private Hitbox hitbox;

    private IUIElement resetButton;

    public AbstractRelic relic;

    public List<IUIElement> elements;

    public List<RelicSetting> settings;

    public boolean isSelected;

    private Color rendColor;

    public RelicSettingsButton(AbstractRelic relic, List<IUIElement> elements) {
        this(relic, 450.0F, 625.0F, 100.0F, 100.0F, elements);
    }

    public RelicSettingsButton(AbstractRelic relic, List<IUIElement> elements, List<RelicSetting> settings) {
        this(relic, elements);
        this.settings = settings;
    }

    public RelicSettingsButton(AbstractRelic relic, float x, float y, float width, float height, List<IUIElement> elements) {
        this(relic.img, relic.outlineImg, x, y, width, height, elements);
        this.relic = relic;
        this.w *= 1.5F;
        this.h *= 1.5F;
        if (!UnlockTracker.isRelicSeen(relic.relicId))
            this.rendColor = Color.BLACK;
    }

    public RelicSettingsButton(Texture image, Texture outline, float x, float y, float width, float height, List<IUIElement> elements, List<RelicSetting> settings) {
        this(image, outline, x, y, width, height, elements);
        this.settings = settings;
    }

    public RelicSettingsButton(Texture image, Texture outline, float x, float y, float width, float height, List<IUIElement> elements) {
        this.relic = null;
        this.image = image;
        this.outline = outline;
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.elements = elements;
        this.settings = new ArrayList<>();
        this.isSelected = false;
        this.hitbox = new Hitbox(this.x * Settings.scale, this.y * Settings.scale, this.w * Settings.scale, this.h * Settings.scale);
        this.resetButton = new ModButton(800.0F, 550.0F, Strops.settingsPanel, me -> resetAll());
        this.rendColor = Color.WHITE;
    }

    public void render(SpriteBatch sb) {
        if (this.outline != null) {
            sb.setColor(new Color(0.0F, 0.0F, 0.0F, 1.0F));
            sb.draw(this.outline, this.x * Settings.scale, this.y * Settings.scale, this.w * Settings.scale, this.h * Settings.scale);
        }
        sb.setColor(this.rendColor);
        sb.draw(this.image, this.x * Settings.scale, this.y * Settings.scale, this.w * Settings.scale, this.h * Settings.scale);
        this.hitbox.render(sb);
        if (this.isSelected) {
            for (IUIElement element : this.elements)
                element.render(sb);
            this.resetButton.render(sb);
        }
    }

    public void update() {
        this.hitbox.update();
        if (this.hitbox.hovered && InputHelper.justClickedLeft) {
            CardCrawlGame.sound.play("UI_CLICK_1");
            if (this.isSelected) {
                this.isSelected = false;
            } else {
                if (activeButton != null)
                    activeButton.isSelected = false;
                activeButton = this;
                this.isSelected = true;
            }
        }
        if (this.isSelected) {
            for (IUIElement element : this.elements)
                element.update();
            this.resetButton.update();
        }
    }

    public void resetAll() {
        for (RelicSetting element : this.settings)
            element.ResetToDefault();
    }

    public int renderLayer() {
        return 1;
    }

    public int updateOrder() {
        return 1;
    }

    public void addSetting(RelicSetting setting) {
        float ly = 550.0F;
        float lx = 350.0F;
        for (RelicSetting ls : this.settings) {
            ly -= ls.elementHeight;
            if (ly <= 0.0F) {
                ly = 550.0F + ((lx >= 600.0F) ? 80.0F : 0.0F);
                lx += 300.0F;
            }
        }
        this.settings.add(setting);
        this.elements.addAll(setting.GenerateElements(lx, ly));
    }
}

