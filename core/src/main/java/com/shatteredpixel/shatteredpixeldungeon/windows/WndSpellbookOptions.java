/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.watabou.noosa.Image;

public class WndSpellbookOptions extends Window {

    private static final int WIDTH = 120;
    private static final int MARGIN = 2;
    private static final int BUTTON_HEIGHT = 16;
    private static final int DESC_HEIGHT = 120;
    private static final int GAP = 5;

    private SelectCallback callback;
    private RenderedTextBlock descText;
    private RedButton confirmBtn;
    
    // We need to keep track of which spellbook created us
    private Image spellbookIcon;

    public interface SelectCallback {
        void onSelect(int index);
    }

    public WndSpellbookOptions(Image icon, String title, String message,
                            Scroll regularScroll,
                            ExoticScroll exoticScroll,
                            SelectCallback callback) {
        super();
        
        this.callback = callback;
        this.spellbookIcon = icon;

        // Title with icon
        IconTitle titleBar = new IconTitle(icon, title);
        titleBar.setRect(0, 0, WIDTH, 0);
        add(titleBar);

        // Message
        RenderedTextBlock msgText = PixelScene.renderTextBlock(message, 6);
        msgText.maxWidth(WIDTH);
        msgText.setPos(0, titleBar.bottom() + MARGIN);
        add(msgText);

        float pos = msgText.bottom() + MARGIN * 2;

        // First option
        String option1Text = regularScroll != null ? regularScroll.trueName() : "Regular Scroll";
        RedButton btn1 = new RedButton(option1Text) {
            @Override
            protected void onClick() {
                updateDescription(regularScroll != null ? regularScroll.desc() : "No description available");
            }
        };
        btn1.setRect(0, pos, WIDTH, BUTTON_HEIGHT);
        add(btn1);
        pos = btn1.bottom() + MARGIN;

        // Second option
        String option2Text = exoticScroll != null ? exoticScroll.trueName() : "Exotic Scroll";
        RedButton btn2 = new RedButton(option2Text) {
            @Override
            protected void onClick() {
                updateDescription(exoticScroll != null ? exoticScroll.desc() : "No description available");
            }
        };
        btn2.setRect(0, pos, WIDTH, BUTTON_HEIGHT);
        add(btn2);
        pos = btn2.bottom() + MARGIN;

        // Description text area
        descText = PixelScene.renderTextBlock(6);
        descText.maxWidth(WIDTH);
        descText.setPos(0, pos);
        add(descText);
        
        // Initially show the first scroll description
        String initialDesc = regularScroll != null ? regularScroll.desc() : "No description available";
        descText.text(initialDesc);
        
        // Force a minimum height for description area while allowing expansion
        float descHeight = Math.max(Math.min(descText.height(), DESC_HEIGHT), 50);
        
        // Position buttons below description area
        pos = descText.top() + descHeight + GAP;
        
        // Confirm button
        confirmBtn = new RedButton("Read") {
            @Override
            protected void onClick() {
                hide();
                if (callback != null) {
                    // Call the callback with the index of the button that's currently showing in the description
                    boolean isRegularSelected = descText.text().equals(regularScroll != null ? regularScroll.desc() : "No description available");
                    callback.onSelect(isRegularSelected ? 0 : 1);
                }
            }
        };
        confirmBtn.setRect(0, pos, WIDTH, BUTTON_HEIGHT);
        add(confirmBtn);
        
        pos = confirmBtn.bottom() + MARGIN;
        
        resize(WIDTH, (int)pos);
    }
    
    private void updateDescription(String desc) {
        descText.text(desc);
        
        // Calculate appropriate height for description and window
        float descHeight = Math.min(descText.height(), DESC_HEIGHT);
        
        // Always position button below description
        float newButtonY = descText.top() + descHeight + GAP;
        confirmBtn.setPos(confirmBtn.left(), newButtonY);
        
        // Resize the window
        resize(WIDTH, (int)(confirmBtn.bottom() + MARGIN));
    }

    @Override
    public void onBackPressed() {
        // Show confirmation dialog
        GameScene.show(new WndOptions(Icons.get(Icons.WARNING),
                "Confirm Exit",
                "Exiting will still consume a charge from your spellbook. Are you sure?",
                "Yes, exit", 
                "No, stay") {
            @Override
            protected void onSelect(int index) {
                if (index == 0) {
                    // User confirmed exit - hide window WITHOUT triggering a scroll effect
                    WndSpellbookOptions.super.hide();
                    
                    // We need to detach the ExploitHandler from the hero to prevent
                    // the automatic scroll reading in the next game tick
                    if (callback != null) {
                        // We need to call onSelect with a special index (-1) to signal
                        // that we want to cancel but still detach the handler
                        callback.onSelect(-1);
                    }
                }
                // If index is 1, do nothing (stay in the window)
            }
        });
    }
} 