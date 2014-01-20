/**
 * Copyright (C) cedarsoft GmbH.
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
package com.cedarsoft.maven.clashinspector.model;

import com.cedarsoft.maven.clashinspector.mojos.ClashSeverity;

/**
 * Copyright 2014 Behr Michael, Kampa Martin, Schneider Johannes, Zhu Huina
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class InnerVersionClash {

  private final DependencyNodeWrapper usedDependencyNodeWrapper;
  private final DependencyNodeWrapper referredDependencyNodeWrapper;
  private final ClashSeverity clashSeverity;

  public InnerVersionClash( DependencyNodeWrapper usedDependencyNodeWrapper, DependencyNodeWrapper referredDependencyNodeWrapper ) {
    this.usedDependencyNodeWrapper = usedDependencyNodeWrapper;
    this.referredDependencyNodeWrapper = referredDependencyNodeWrapper;

    this.clashSeverity = this.referredDependencyNodeWrapper.getRelationShipToUsedVersion().getClashSeverity();

  }

  public boolean isClashForSeverityLevel( ClashSeverity clashSeverity ) {
    if ( this.clashSeverity.ordinal() >= clashSeverity.ordinal() ) {
      return true;
    } else {
      return false;
    }
  }


  public DependencyNodeWrapper getUsedDependencyNodeWrapper() {
    return usedDependencyNodeWrapper;
  }

  public DependencyNodeWrapper getReferredDependencyNodeWrapper() {
    return referredDependencyNodeWrapper;
  }

  public ClashSeverity getClashSeverity() {
    return clashSeverity;
  }
}
