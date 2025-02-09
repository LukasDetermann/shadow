package io.determann.shadow.article.apt_loading;

import javax.tools.StandardLocation;
import java.util.Collections;
import java.util.List;

import static javax.tools.StandardLocation.*;

public class AptLoading
{
//@formatter:off
//tag::javac[]
public static List<AnnotationProcessor> getAnnotationProcessors(
      String[] args,
      List<AnnotationProcessor> programmaticallySetProcessors)
{     //source: javax.tools.JavaCompiler.CompilationTask#setProcessors(java.lang.Iterable)

   //source: com.sun.tools.javac.main.JavaCompiler.initProcessAnnotations()
   if (isOptionSet(args, "-proc:", "none") || !isRequested(args))
   {
      return Collections.emptyList();
   }

   return chooseProcessors(args, programmaticallySetProcessors);
}

//source: com.sun.tools.javac.main.JavaCompiler.explicitAnnotationProcessingRequested()
private static boolean isRequested(String[] args)
{
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
private static List<AnnotationProcessor> chooseProcessors(
      String[] args,
      List<AnnotationProcessor> programmaticallySetProcessors)
{
   if (isOptionSet(args, "-Xprint"))//<1>
   {
      return List.of(new PrintingProcessor());
   }
   if (programmaticallySetProcessors != null)//<2>
   {
      return programmaticallySetProcessors;
   }

   List<AnnotationProcessor> processors = loadProcessors();

   List<String> processorNames = getOption(args, "-processor");//<3>
   if (processorNames != null)
   {
      return processors.stream()
                       .filter(p -> processorNames.contains(p.getName()))
                       .toList();
   }
   return processors;
}

//source: com.sun.tools.javac.processing.JavacProcessingEnvironment#initProcessorLoader())
private static List<AnnotationProcessor> loadProcessors()
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

   public interface AnnotationProcessor {

      String getName();
   }

   private static class PrintingProcessor
         implements AnnotationProcessor {
      @Override
      public String getName()
      {
         return "";
      }
   }

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
