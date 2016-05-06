package org.freedom.infra.x.UIMaker.effect;

import java.util.ArrayList;
import java.util.List;

public class SetEffect {

	private final List<Effect> effects = new ArrayList<Effect>();

	public SetEffect() {

	}

	public void doStart() {
		for (Effect e : effects) {
			e.doStart();
		}
	}

	public void doStop() {
		for (Effect e : effects) {
			e.doStop();
		}
	}

	public void addEffect(Effect effect) {
		effects.add(effect);
	}

	public void removeEffect(int indexEffect) {
		effects.remove(indexEffect);
	}

	public void removeEffect(Effect effect) {
		effects.remove(effect);
	}

}
