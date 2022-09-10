/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.ca.lookup;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Class representing mark on some native data.
 * @param <T> type
 * @param <S> Native data.
 */
public interface TypeMark<T, S> {
	/**
	 * Adds some identifier/"mark" for a native type, so {@link #matches(Object)} ()} will produce true for a native object on the next server load.
	 * @param n Native data.
	 */
	void mark(S n);

	/**
	 * By specification this method should return true if method {@link #mark(Object)} was executed before on the native type you want to "match".
	 * @return true if some native type matches properties to be a custom type.
	 */
	boolean matches(S n);

	/**
	 * Converts a native type to a custom one simply by transferring data, on this stage custom type shouldn't have their own custom data specified.
	 * @param n Native data.
	 * @return An instance of custom type having native data transferred into it.
	 */
	T convert(S n);

	static <T, S> TypeMark<T, S> create(Function<? super S, ? extends T> convert, Predicate<S> matches, Consumer<? super S> mark) {
		return new TypeMark<>() {
			@Override
			public void mark(S n) {
				mark.accept(n);
			}

			@Override
			public boolean matches(S n) {
				return matches.test(n);
			}

			@Override
			public T convert(S n) {
				return convert.apply(n);
			}
		};
	}
}
