package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.ShadowApi;

import javax.tools.Diagnostic;

import static io.determann.shadow.api.MirrorAdapter.getElement;
import static io.determann.shadow.api.MirrorAdapter.getProcessingEnv;

public interface AnnotationProcessing
{
   static void logError(ShadowApi api, String msg)
   {
      getProcessingEnv(api).getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
   }

   static void logInfo(ShadowApi api, String msg)
   {
      getProcessingEnv(api).getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
   }

   static void logWarning(ShadowApi api, String msg)
   {
      getProcessingEnv(api).getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg);
   }

   static void logErrorAt(ShadowApi api, Annotationable annotationable, String msg)
   {
      getProcessingEnv(api).getMessager().printMessage(Diagnostic.Kind.ERROR, msg, getElement(annotationable));
   }

   static void logInfoAt(ShadowApi api, Annotationable annotationable, String msg)
   {
      getProcessingEnv(api).getMessager().printMessage(Diagnostic.Kind.NOTE, msg, getElement(annotationable));
   }

   static void logWarningAt(ShadowApi api, Annotationable annotationable, String msg)
   {
      getProcessingEnv(api).getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, getElement(annotationable));
   }
}
