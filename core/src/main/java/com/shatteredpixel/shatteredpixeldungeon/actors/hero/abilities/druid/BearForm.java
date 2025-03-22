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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.druid;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class BearForm extends ArmorAbility {

	{
		baseChargeUse = 35f;
	}

	@Override
	public String targetingPrompt() {
		return null;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();
		
		Buff.affect(hero, BearFormBuff.class, BearFormBuff.DURATION);
		
		hero.sprite.operate(hero.pos);
		Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
		SpellSprite.show(hero, SpellSprite.VISION);
		Invisibility.dispel();
	}
	
	@Override
	public int icon() {
		//return HeroIcon.BEARFORM;
		return HeroIcon.HEROIC_LEAP; // Temporary replacement
	}

	@Override
	public Talent[] talents() {
		//return new Talent[]{Talent.STRONGHOLD, Talent.THICK_HIDE, Talent.FEROCITY, Talent.HEROIC_ENERGY};
		return new Talent[]{Talent.HEROIC_ENERGY, Talent.HEROIC_ENERGY, Talent.HEROIC_ENERGY, Talent.HEROIC_ENERGY};
	}
	
	public static class BearFormBuff extends FlavourBuff {
		
		public static final float DURATION = 30f;
		
		{
			type = buffType.POSITIVE;
		}
		
		@Override
		public int icon() {
			return BuffIndicator.MIND_VISION;
		}
		
		@Override
		public String toString() {
			return Messages.get(this, "name");
		}
		
		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns());
		}
		
		@Override
		public void detach() {
			super.detach();
		}
		
		@Override
		public void fx(boolean on) {
			//if (on) target.sprite.add(CharSprite.State.BEAR_FORM);
			//else target.sprite.remove(CharSprite.State.BEAR_FORM);
			// Using existing state temporarily
			if (on) target.sprite.add(CharSprite.State.ILLUMINATED);
			else target.sprite.remove(CharSprite.State.ILLUMINATED);
		}
		
		public static final String HITS = "hits";
		private int hits = 0;
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(HITS, hits);
		}
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			hits = bundle.getInt(HITS);
		}
	}
} 