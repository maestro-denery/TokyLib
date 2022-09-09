/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.toky.tokylib.es;

/**
 * Result returned by an event. Usually, users of the event system interpret it by a context,
 * if you need to understand why some specific event returns some of these results, read a specification in docs or somewhere else about that event.
 */
public enum Result {
    /**
     * Usually, this result interprets as a successfully done action.
     */
    SUCCESS,
    /**
     * Usually, this result interprets as an action which was skipped / passed by some specific reason.
     */
    PASS,
    /**
     * Usually, this result interprets as a failed action. It may fail with or without any exception.
     */
    FAIL
}
