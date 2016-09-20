/*
 * Copyright 2016 FuckBoilerplate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fuckboilerplate.rx_social_connect.query_string;

import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Use it when some provider does not honor the standard query string formatter.
 */
public interface QueryStringStrategy {
  /**
   * Given an uri, parse the query string to extract the associated oauth code.
   * @param uri the uri
   * @return the param code
   */
  String extractCode(Uri uri);

  /**
   * Given an uri, parse the query string to extract the associated error.
   * @param uri the uri
   * @return the error message
   */
  @Nullable
  String extractError(Uri uri);
}