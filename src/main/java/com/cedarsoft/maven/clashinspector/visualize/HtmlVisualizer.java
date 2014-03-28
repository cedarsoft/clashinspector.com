package com.cedarsoft.maven.clashinspector.visualize;

import com.cedarsoft.maven.clashinspector.model.ClashCollectResultWrapper;
import com.cedarsoft.maven.clashinspector.model.DependencyNodeWrapper;
import com.cedarsoft.maven.clashinspector.mojos.ClashSeverity;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HtmlVisualizer {
  public void visualize( ClashCollectResultWrapper clashCollectResultWrapper, ClashSeverity clashSeverity ) {

    System.out.println( "Opening Html Analysis..." );

    Configuration cfg = this.initFreeMarkerConfiguration();

    //build data model
    /* TestData (UNCOMMENT FOR TEST)  (test.html)
         TestDataModelGenerator testDataModelGenerator = new TestDataModelGenerator();
         HashMap root = testDataModelGenerator.generateDataModel();
    */

    // for htmlMain.html

    //     HtmlInformationWrapper infWrap = new HtmlInformationWrapper(clashCollectResultWrapper, clashSeverity, clashHtmlMojo);
    //     HashMap root = infWrap.getRoot();


    generateFullTreeDataModel( clashCollectResultWrapper.getRoot(), 0 );

    try {
      //Get the template
      /* TestTemplate (UNCOMMENT FOR TEST)
           Template temp = cfg.getTemplate("test.ftl");
      */

      Template temp = cfg.getTemplate("htmlMain.flt");

      //where the html file will be generated
      FileWriter out = new FileWriter("C:/Users/Martin/maven-director/src/main/java/com/cedarsoft/maven/clashinspector/visualize/htmlOutput/htmlMain.html");
      // TODO für den relativen Pfad System.getProperty( "user.home" )
      // Merging the template with the data-model and generating output
      //temp.process(root, out);

      //open html file
      File htmlFile = new File("C:/Users/Martin/maven-director/src/main/java/com/cedarsoft/maven/clashinspector/visualize/htmlOutput/htmlMain.html");
      Desktop.getDesktop().browse(htmlFile.toURI());

    } catch ( IOException e ) {
      e.printStackTrace();
   // } catch ( TemplateException e ) {
      e.printStackTrace();
    }
  }

  public Configuration initFreeMarkerConfiguration(){
    Configuration cfg = new Configuration();

    // Specify the data source where the template files come from. Here I set a
    // plain directory for it, but non-file-system are possible too:
    try {
      //TODO Relativen Pfad anlegen!!!
      cfg.setDirectoryForTemplateLoading(new File("C:/Users/Martin/maven-director/src/main/java/com/cedarsoft/maven/clashinspector/visualize/htmlTemplates"));
    } catch ( IOException e ) {
      e.printStackTrace();
    }

    // Specify how templates will see the data-model. This is an advanced topic...
    // for now just use this:
    cfg.setObjectWrapper(new DefaultObjectWrapper());

    // Set your preferred charset template files are stored in. UTF-8 is
    // a good choice in most applications:
    cfg.setDefaultEncoding("UTF-8");

    // Sets how errors will appear. Here we assume we are developing HTML pages.
    // For production systems TemplateExceptionHandler.RETHROW_HANDLER is better.
    cfg.setTemplateExceptionHandler( TemplateExceptionHandler.HTML_DEBUG_HANDLER);

    // At least in new projects, specify that you want the fixes that aren't
    // 100% backward compatible too (these are very low-risk changes as far as the
    // 1st and 2nd version number remains):
    cfg.setIncompatibleImprovements(new Version(2, 3, 20));  // FreeMarker 2.3.20

    return cfg;
  }

  public List generateFullTreeDataModel(DependencyNodeWrapper dependencyNodeWrapper, int depth){
    List root = new ArrayList();
    for ( DependencyNodeWrapper dNW : dependencyNodeWrapper.getChildren() ) {
      System.out.println(dNW);
      root.add( dNW );
    }
    return root;
  }



  @Deprecated
  public HashMap buildDataModel(){
    HashMap root = new HashMap();

    //root.put("clashSeverity", clashSeverity);
    //root.put( "clashCollectResultWrapper", clashCollectResultWrapper );

    Map latest = new HashMap();
    latest.put("url", "products/greenmouse.html");
    latest.put("name", "green mouse");

    root.put("latestProduct", latest);


    return root;
  }
}



