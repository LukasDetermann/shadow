package io.determann.shadow.api;

import io.determann.shadow.api.metadata.JdkApi;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

/**
 * Holds all the parts of the java annotation processor api
 */
public record JdkApiContext(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv)
{
   @JdkApi
   @Override
   public ProcessingEnvironment processingEnv()
   {
      return processingEnv;
   }

   @JdkApi
   @Override
   public RoundEnvironment roundEnv()
   {
      return roundEnv;
   }
}
