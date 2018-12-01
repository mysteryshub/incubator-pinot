/**
 * Copyright (C) 2014-2018 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.controller.helix.core.assignment.instance;

import java.util.Map;
import org.apache.helix.ZNRecord;
import org.apache.helix.store.zk.ZkHelixPropertyStore;


public class InstanceSelectionResultUtils {
  private InstanceSelectionResultUtils() {
  }

  public static Map<InstanceRole, InstanceSelectionResult> readFromZK(ZkHelixPropertyStore<ZNRecord> propertyStore,
      String resource) {
    //TODO: implement it
    throw new UnsupportedOperationException();
  }

  public static boolean writeToZK(ZkHelixPropertyStore<ZNRecord> propertyStore, String resource,
      Map<InstanceRole, InstanceSelectionResult> instanceSelectionResults) {
    //TODO: implement it
    throw new UnsupportedOperationException();
  }
}
