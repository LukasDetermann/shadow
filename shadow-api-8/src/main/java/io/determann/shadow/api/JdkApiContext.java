package io.determann.shadow.api;

import io.determann.shadow.api.metadata.JdkApi;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

/**
 * Holds all the parts of the java annotation processor api
 */
public final class JdkApiContext
{
   private final ProcessingEnvironment processingEnv;
   private final RoundEnvironment roundEnv;

   public JdkApiContext(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv)
   {
      this.processingEnv = processingEnv;
      this.roundEnv = roundEnv;
   }

   @JdkApi
   public ProcessingEnvironment processingEnv()
   {
      return processingEnv;
   }

   @JdkApi
   public RoundEnvironment roundEnv()
   {
      return roundEnv;
   }
}
