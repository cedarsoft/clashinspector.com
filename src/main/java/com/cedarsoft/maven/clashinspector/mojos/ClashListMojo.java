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
package com.cedarsoft.maven.clashinspector.mojos;

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


import com.cedarsoft.maven.clashinspector.DependencyService;
import com.cedarsoft.maven.clashinspector.model.ClashCollectResultWrapper;
import com.cedarsoft.maven.clashinspector.visualize.ConsoleVisualizer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

/**
 * Displays the dependencies with version clashes and a statistical summary of the clashes.
 *
 * @since 0.3
 */
@Mojo(name = "list", requiresProject = true, defaultPhase = LifecyclePhase.NONE)
public class ClashListMojo extends AbstractClashMojo {


  //big tree .. small tree und level mitgeben
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    super.execute();
       super.printStartParameter( "list" );

    Artifact artifact;
    try {

      artifact = new DefaultArtifact( this.getProject().getArtifact().toString() );


      DependencyService dependencyService = new DependencyService();

      ConsoleVisualizer consoleVisualizer = new ConsoleVisualizer();

      ClashCollectResultWrapper clashCollectResultWrapper = new ClashCollectResultWrapper( dependencyService.getDependencyTree( artifact, this.getRepoSession(), this.getRepoSystem(), this.getIncludedScopesList(), this.getExcludedScopesList(), this.isIncludeOptional() ) );

      consoleVisualizer.visualize( clashCollectResultWrapper, this.getSeverity(), this );


    } catch ( IllegalArgumentException e ) {
      throw new MojoFailureException( e.getMessage(), e );
    }
  }


}


