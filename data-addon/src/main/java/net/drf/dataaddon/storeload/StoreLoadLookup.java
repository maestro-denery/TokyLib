/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.drf.dataaddon.storeload;

import java.util.stream.Stream;

import net.drf.dataaddon.mark.Mark;

/**
 * Class looking for native data and returns instantiated DataAddon classes added to a holder.
 * @param <T> DataAddon you want to lookup.
 * @param <N> Native type of DataAddon.
 */
public interface StoreLoadLookup<T, N> {

	Mark<T, N> mark();

	/**
	 * @return Natives lazy stream matching DataAddon requirements.
	 */
	Stream<N> getNatives();

	/**
	 * @return lazy lookup obtaining instances of DataAddon instances
	 */
	default Stream<T> lookup() {
		return getNatives().map(n -> mark().convert(n)).filter(t -> mark().matches().test(t));
	}
}
