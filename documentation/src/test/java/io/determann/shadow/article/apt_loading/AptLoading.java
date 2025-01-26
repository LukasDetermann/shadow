package io.determann.shadow.article.apt_loading;

import javax.tools.StandardLocation;
import java.util.Collections;
import java.util.List;

import static javax.tools.StandardLocation.*;

public class AptLoading
{
//@formatter:off
//tag::javac[]
public static List<AnnotationProcessor> accumulateAllProcessors(
      String[] args,
      //source: javax.tools.JavaCompiler.CompilationTask#setProcessors(java.lang.Iterable)
      List<AnnotationProcessor> programmaticallySetProcessors) {

   //source: com.sun.tools.javac.main.JavaCompiler.initProcessAnnotations()
   if (isOptionSet(args, "-proc:", "none"))
   {
      return Collections.emptyList();
   }

   List<AnnotationProcessor> annotationProcessors =
         getAnnotationProcessors(args, programmaticallySetProcessors);

   if (annotationProcessors.isEmpty() || !annotationProcessingRequested(args))
   {
      return Collections.emptyList();
   }
   return annotationProcessors;
}

//source: com.sun.tools.javac.main.JavaCompiler.explicitAnnotationProcessingRequested()
private static boolean annotationProcessingRequested(String[] args) {

   return isOptionSet(args, "-processor") ||
          isOptionSet(args, "-processorpath") ||//alias --processor-path
          isOptionSet(args, "--processor-module-path") ||
          isOptionSet(args, "-proc:", "only") ||
          isOptionSet(args, "-proc:", "full") ||
          isOptionSet(args, "-A") ||
          isOptionSet(args, "-Xprint") ||
          hasLocation(ANNOTATION_PROCESSOR_PATH);
}

//source: com.sun.tools.javac.processing.JavacProcessingEnvironment.initProcessorIterator
private static List<AnnotationProcessor> getAnnotationProcessors(
      String[] args,
      List<AnnotationProcessor> programmaticallySetProcessors) {

   if (isOptionSet(args, "-Xprint"))//<1>
   {
      return List.of(new PrintingProcessor());
   }
   if (programmaticallySetProcessors != null)//<2>
   {
      return programmaticallySetProcessors;
   }
   List<String> processorNames = getOption(args, "-processor");//<3>
   if (processorNames != null)
   {
      return loadAnnotationProcessorsByNames(processorNames);
   }
   return automaticlyFindAnnotationProcessors();
}

//source: com.sun.tools.javac.processing.JavacProcessingEnvironment#initProcessorLoader())
private static List<AnnotationProcessor> automaticlyFindAnnotationProcessors()
{
   if (hasLocation(ANNOTATION_PROCESSOR_MODULE_PATH))
   {
      return loadAnnotationProcessors(ANNOTATION_PROCESSOR_MODULE_PATH);//<4>
   }
   if (hasLocation(ANNOTATION_PROCESSOR_PATH))
   {
      return loadAnnotationProcessors(ANNOTATION_PROCESSOR_PATH);//<5>
   }
   return loadAnnotationProcessors(CLASS_PATH);//<6>
}
//end::javac[]
//@formatter:on

   private static List<AnnotationProcessor> loadAnnotationProcessorsByNames(List<String> processorNames)
   {
      return null;
   }

   private static boolean hasLocation(StandardLocation location)
   {
      return false;
   }

   public static List<AnnotationProcessor> loadAnnotationProcessors(StandardLocation standardLocation)
   {
      return null;
   }

   private interface AnnotationProcessor {}

   private static class PrintingProcessor
         implements AnnotationProcessor {}

   private static boolean isOptionSet(String[] args, String option, String value)
   {
      return false;
   }

   private static boolean isOptionSet(String[] args, String option)
   {
      return false;
   }

   private static List<String> getOption(String[] args, String s)
   {
      return null;
   }
}
