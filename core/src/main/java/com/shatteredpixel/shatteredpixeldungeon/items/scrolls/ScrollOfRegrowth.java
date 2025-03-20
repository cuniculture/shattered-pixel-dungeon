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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRegrowth extends Scroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_LULLABY;
	}
	
	@Override
	public void doRead() {
		
		Sample.INSTANCE.play(Assets.Sounds.READ);
		
		// Root all enemies in the hero's field of view
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (Dungeon.level.heroFOV[mob.pos]) {
				Buff.affect(mob, Roots.class, 10f);
				
				// If used by the Druid, roots last longer
				if (curUser.heroClass == HeroClass.DRUID) {
					Buff.affect(mob, Roots.class, 15f);
				}
				
				// Visual effect
				CellEmitter.get(mob.pos).burst(Speck.factory(Speck.EARTH), 5);
			}
		}
		
		// Also provide barkskin effect to the hero
		Buff.affect(curUser, com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin.class).set(curUser.HT / 4, 5);
		
		// If used by a Druid, also recharge their staff weapon
		if (curUser.heroClass == HeroClass.DRUID) {
			if (curUser.belongings.weapon instanceof com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Staff) {
				((com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Staff)curUser.belongings.weapon).gainCharge(1f, true);
			}
		}
		
		identify();
		
		readAnimation();
	}
	
	@Override
	public int value() {
		return isKnown() ? 40 * quantity : super.value();
	}
} 