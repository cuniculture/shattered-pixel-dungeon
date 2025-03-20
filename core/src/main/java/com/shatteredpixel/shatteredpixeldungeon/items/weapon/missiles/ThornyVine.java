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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ThornyVine extends MissileWeapon {
	
	{
		image = ItemSpriteSheet.THROWING_KNIFE;    // Temporary, would need a custom sprite
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1.1f;
		
		tier = 1;
		baseUses = 5;  // Has more uses than typical tier 1 missiles
	}
	
	@Override
	public int max(int lvl) {
		return 4 * tier +                  // 4 base, down from 5
				(tier == 1 ? lvl*1 : lvl*tier); // +1 per level, +2 at tier 2 and so on
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {
		// Thorny Vines have a chance to root enemies
		if (Math.random() < 0.3f) {
			Buff.affect(defender, Roots.class, 3f);
		}
		return super.proc(attacker, defender, damage);
	}
	
	@Override
	public float durabilityPerUse() {
		return 0.8f;   // More durable than normal
	}
	
	@Override
	public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
		if (owner instanceof Hero && ((Hero)owner).heroClass == com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.DRUID){
			return type == Blooming.class;
		}
		return super.hasEnchant(type, owner);
	}
} 