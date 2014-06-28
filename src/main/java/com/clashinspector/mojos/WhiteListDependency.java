package com.clashinspector.mojos;

import com.clashinspector.model.DependencyNodeWrapper;

public class WhiteListDependency {

  private String groupId;
  private String version;
  private String artifactId;

  public String getGroupId() {
    return groupId;
  }

  public String getVersion() {
    return version;
  }

  public String getArtifactId() {
    return artifactId;
  }


  @Override
  public String toString() {
    return groupId + ":" + artifactId + ":" + version;
  }


  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null ) return false;

    if ( getClass() == o.getClass() ) {
      WhiteListDependency that = ( WhiteListDependency ) o;
      if ( artifactId != null ? !artifactId.equals( that.artifactId ) : that.artifactId != null ) return false;
      if ( groupId != null ? !groupId.equals( that.groupId ) : that.groupId != null ) return false;
      if ( version != null ? !version.equals( that.version ) : that.version != null ) return false;

      return true;

    }


    if ( o instanceof DependencyNodeWrapper ) {


      DependencyNodeWrapper that = ( DependencyNodeWrapper ) o;

      //System.out.println("DependencyNodeWrapper: groupId: " + that.getGroupId() +" ,  artifactId: " + that.getArtifactId() +" , version: " + that.getVersion())   ;
      //System.out.println("WhiteListDependency: groupId: " + this.getGroupId() +" ,  artifactId: " + this.getArtifactId() +" , version: " + this.getVersion())   ;

      if ( artifactId != null ? !artifactId.equals( that.getArtifactId() ) : that.getArtifactId() != null ) return false;

      if ( groupId != null ? !groupId.equals( that.getGroupId() ) : that.getGroupId() != null ) return false;

      if ( version != null ? !version.equals( that.getVersion().toString() ) : that.getVersion().toString() != null ) return false;


      return true;

    }
    return false;

  }

  @Override
  public int hashCode() {
    int result = groupId != null ? groupId.hashCode() : 0;
    result = 31 * result + ( version != null ? version.hashCode() : 0 );
    result = 31 * result + ( artifactId != null ? artifactId.hashCode() : 0 );
    return result;
  }
}
