[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building Our Maven Plugin 0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- ourplugin-maven-plugin:0.1-SNAPSHOT:dependencies (default-cli) @ ourplugin-maven-plugin ---
[central (http://repo.maven.apache.org/maven2, releases)]
Downloading: http://repository.jboss.org/maven2/org/jboss/ejb3/jboss-ejb3-api/3.1.0/jboss-ejb3-api-3.1.0.pom
[WARNING] The POM for xerces:xerces-impl:jar:2.6.2 is missing, no dependency information available
[WARNING] The POM for xml-apis:xml-apis:jar:2.6.2 is missing, no dependency information available
Failed to collect dependencies at com.cedarsoft.hsrt.zkb:ourplugin-maven-plugin:maven-plugin:0.1-SNAPSHOT -> org.apache.maven:maven-plugin-api:jar:3.1.1 -> org.eclipse.sisu:org.eclipse.sisu.plexus:jar:0.0.0.M5 -> javax.enterprise:cdi-api:jar:1.0 -> org.jboss.ejb3:jboss-ejb3-api:jar:3.1.0
[INFO] org.apache.maven:maven-plugin-api:jar:3.1.1 (compile)
[INFO]   org.apache.maven:maven-model:jar:3.1.1 (compile)
[INFO]     org.codehaus.plexus:plexus-utils:jar:3.0.15 (compile)
[INFO]   org.apache.maven:maven-artifact:jar:3.1.1 (compile)
[INFO]     org.codehaus.plexus:plexus-utils:jar:3.0.15 (compile)
[INFO]   org.eclipse.sisu:org.eclipse.sisu.plexus:jar:0.0.0.M5 (compile)
[INFO]     org.slf4j:slf4j-api:jar:1.6.4 (compile?)
[INFO]     javax.enterprise:cdi-api:jar:1.0 (compile)
[INFO]       javax.el:el-api:jar:2.1.2-b04 (compile?)
[INFO]       org.jboss.interceptor:jboss-interceptor-api:jar:1.1 (compile)
[INFO]       javax.annotation:jsr250-api:jar:1.0 (compile)
[INFO]       javax.inject:javax.inject:jar:1 (compile)
[INFO]     com.google.guava:guava:jar:10.0.1 (compile)
[INFO]     org.sonatype.sisu:sisu-guice:jar:no_aop:3.1.0 (compile)
[INFO]       javax.inject:javax.inject:jar:1 (compile)
[INFO]       aopalliance:aopalliance:jar:1.0 (compile)
[INFO]       org.slf4j:slf4j-api:jar:1.6.2 (compile?)
[ERROR]       Version Clash 1.6.4 and 1.6.2 !!!!!!!!!!!!!!!!!!!!!!
[INFO]       cglib:cglib:jar:2.2.2 (compile?)
[INFO]         asm:asm:jar:3.3.1 (compile?)
[INFO]         asm:asm-util:jar:3.3.1 (compile?)
[INFO]           asm:asm-tree:jar:3.3.1 (compile?)
[INFO]             asm:asm:jar:3.3.1 (compile)
[INFO]         ant:ant:jar:1.6.2 (compile?)
[INFO]           xerces:xerces-impl:jar:2.6.2 (compile?)
[INFO]           xml-apis:xml-apis:jar:2.6.2 (compile?)
[INFO]       org.sonatype.sisu:sisu-guava:jar:0.9.9 (compile)
[INFO]         com.google.code.findbugs:jsr305:jar:1.3.9 (compile?)
[INFO]     org.eclipse.sisu:org.eclipse.sisu.inject:jar:0.0.0.M5 (compile)
[INFO]       com.google.inject:guice:jar:3.0 (compile)
[INFO]         javax.inject:javax.inject:jar:1 (compile)
[INFO]         aopalliance:aopalliance:jar:1.0 (compile)
[INFO]         org.sonatype.sisu.inject:cglib:jar:2.2.1-v20090111 (compile?)
[INFO]           asm:asm:jar:3.1 (compile)
[ERROR]           Version Clash 3.3.1 and 3.1 !!!!!!!!!!!!!!!!!!!!!!
[INFO]           asm:asm-util:jar:3.1 (compile?)
[ERROR]           Version Clash 3.3.1 and 3.1 !!!!!!!!!!!!!!!!!!!!!!
[INFO]           org.apache.ant:ant:jar:1.8.1 (compile?)
[INFO]             org.apache.ant:ant-launcher:jar:1.8.1 (compile?)
[INFO]     org.codehaus.plexus:plexus-component-annotations:jar:1.5.5 (compile)
[INFO]     org.codehaus.plexus:plexus-classworlds:jar:2.4 (compile)
[INFO]     org.codehaus.plexus:plexus-utils:jar:2.1 (compile)
[ERROR]     Version Clash 3.0.15 and 2.1 !!!!!!!!!!!!!!!!!!!!!!
[INFO]     junit:junit:jar:4.10 (compile?)
[INFO]       org.hamcrest:hamcrest-core:jar:1.1 (compile?)
[INFO] org.apache.maven:maven-core:jar:3.1.1 (compile)
[INFO]   org.apache.maven:maven-model:jar:3.1.1 (compile)
[INFO]   org.apache.maven:maven-settings:jar:3.1.1 (compile)
[INFO]     org.codehaus.plexus:plexus-utils:jar:3.0.15 (compile)
[INFO]   org.apache.maven:maven-settings-builder:jar:3.1.1 (compile)
[INFO]     org.codehaus.plexus:plexus-utils:jar:3.0.15 (compile)
[INFO]     org.codehaus.plexus:plexus-interpolation:jar:1.19 (compile)
[INFO]     org.codehaus.plexus:plexus-component-annotations:jar:1.5.5 (compile)
[INFO]     org.apache.maven:maven-settings:jar:3.1.1 (compile)
[INFO]     org.sonatype.plexus:plexus-sec-dispatcher:jar:1.3 (compile)
[INFO]   org.apache.maven:maven-repository-metadata:jar:3.1.1 (compile)
[INFO]     org.codehaus.plexus:plexus-utils:jar:3.0.15 (compile)
[INFO]   org.apache.maven:maven-artifact:jar:3.1.1 (compile)
[INFO]   org.apache.maven:maven-plugin-api:jar:3.1.1 (compile)
[INFO]   org.apache.maven:maven-model-builder:jar:3.1.1 (compile)
[INFO]     org.codehaus.plexus:plexus-utils:jar:3.0.15 (compile)
[INFO]     org.codehaus.plexus:plexus-interpolation:jar:1.19 (compile)
[INFO]     org.codehaus.plexus:plexus-component-annotations:jar:1.5.5 (compile)
[INFO]     org.apache.maven:maven-model:jar:3.1.1 (compile)
[INFO]   org.apache.maven:maven-aether-provider:jar:3.1.1 (compile)
[INFO]     org.apache.maven:maven-model:jar:3.1.1 (compile)
[INFO]     org.apache.maven:maven-model-builder:jar:3.1.1 (compile)
[INFO]     org.apache.maven:maven-repository-metadata:jar:3.1.1 (compile)
[INFO]     org.eclipse.aether:aether-api:jar:0.9.0.M2 (compile)
[INFO]     org.eclipse.aether:aether-spi:jar:0.9.0.M2 (compile)
[INFO]       org.eclipse.aether:aether-api:jar:0.9.0.M2 (compile)
[INFO]     org.eclipse.aether:aether-util:jar:0.9.0.M2 (compile)
[INFO]     org.eclipse.aether:aether-impl:jar:0.9.0.M2 (compile)
[INFO]     org.codehaus.plexus:plexus-component-annotations:jar:1.5.5 (compile)
[INFO]     org.codehaus.plexus:plexus-utils:jar:3.0.15 (compile)
[INFO]     org.sonatype.sisu:sisu-guice:jar:no_aop:3.1.3 (compile?)
[ERROR]     Version Clash 3.1.0 and 3.1.3 !!!!!!!!!!!!!!!!!!!!!!
[INFO]   org.eclipse.aether:aether-impl:jar:0.9.0.M2 (compile)
[INFO]     org.eclipse.aether:aether-api:jar:0.9.0.M2 (compile)
[INFO]     org.eclipse.aether:aether-spi:jar:0.9.0.M2 (compile)
[INFO]     org.eclipse.aether:aether-util:jar:0.9.0.M2 (compile)
[INFO]   org.eclipse.aether:aether-api:jar:0.9.0.M2 (compile)
[INFO]   org.eclipse.aether:aether-util:jar:0.9.0.M2 (compile)
[INFO]   org.eclipse.sisu:org.eclipse.sisu.plexus:jar:0.0.0.M5 (compile)
[INFO]   org.codehaus.plexus:plexus-interpolation:jar:1.19 (compile)
[INFO]   org.codehaus.plexus:plexus-utils:jar:3.0.15 (compile)
[INFO]   org.codehaus.plexus:plexus-classworlds:jar:2.5.1 (compile)
[ERROR]   Version Clash 2.4 and 2.5.1 !!!!!!!!!!!!!!!!!!!!!!
[INFO]   org.codehaus.plexus:plexus-component-annotations:jar:1.5.5 (compile)
[INFO]   org.sonatype.plexus:plexus-sec-dispatcher:jar:1.3 (compile)
[INFO]     org.codehaus.plexus:plexus-utils:jar:1.5.5 (compile)
[ERROR]     Version Clash 3.0.15 and 1.5.5 !!!!!!!!!!!!!!!!!!!!!!
[INFO]     org.sonatype.plexus:plexus-cipher:jar:1.4 (compile)
[INFO] org.apache.maven.plugin-tools:maven-plugin-annotations:jar:3.2 (compile)
[INFO]   org.apache.maven:maven-artifact:jar:3.0 (compile)
[ERROR]   Version Clash 3.1.1 and 3.0 !!!!!!!!!!!!!!!!!!!!!!
[INFO] com.google.guava:guava:jar:14.0.1 (compile)
[ERROR] Version Clash 10.0.1 and 14.0.1 !!!!!!!!!!!!!!!!!!!!!!
[INFO] org.eclipse.aether:aether-api:jar:0.9.0.M2 (compile)
[INFO] org.eclipse.aether:aether-util:jar:0.9.0.M2 (compile)
[INFO]   org.eclipse.aether:aether-api:jar:0.9.0.M2 (compile)
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3.182s
[INFO] Finished at: Fri Nov 15 14:08:42 CET 2013
[INFO] Final Memory: 8M/98M
[INFO] ------------------------------------------------------------------------
