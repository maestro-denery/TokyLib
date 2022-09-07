/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.storeload;

import com.mojang.datafixers.util.Pair;

import java.util.stream.Stream;

/**
 * Class looking for native data and returns instantiated DataAddon classes added to a holder.
 * @param <T> DataAddon you want to lookup.
 * @param <N> Native type of DataAddon.
 */
public interface StoreLoadLookup<T, N> {

	/**
	 * @return A mark, see {@link Mark}
	 */
	Mark<T, N> mark();

	/**
	 * @return Natives lazy stream matching DataAddon requirements.
	 */
	Stream<N> getNatives();

	/**
	 * @return lazy lookup obtaining instances of DataAddon instances
	 */
	default Stream<Pair<T, N>> lookup() {
		return getNatives().filter(n -> mark().matches(n)).map(n -> Pair.of(mark().convert(n), n));
	}
}
