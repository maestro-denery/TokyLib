/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.drf.dataaddon.mark;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class representing mark on some native data.
 * @param <T> type
 * @param <N> Native data.
 */
public interface Mark<T, N> {

	void mark(N n);

	Predicate<T> matches();

	T convert(N n);

	static <T, N> Mark<T, N> create(Function<? super N, ? extends T> convert, Predicate<T> matches, Consumer<? super N> mark) {
		return new Mark<>() {
			@Override
			public void mark(N n) {
				mark.accept(n);
			}

			@Override
			public Predicate<T> matches() {
				return matches;
			}

			@Override
			public T convert(N n) {
				return convert.apply(n);
			}
		};
	}
}
