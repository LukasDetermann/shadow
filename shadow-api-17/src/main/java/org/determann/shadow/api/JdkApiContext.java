package org.determann.shadow.api;

import org.determann.shadow.api.metadata.JdkApi;
import org.jetbrains.annotations.UnmodifiableView;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Locale;
import java.util.Map;

/**
 * Holds all the parts of the java annotation processor api
 */
public record JdkApiContext(RoundEnvironment roundEnv,
                            Elements elements,
                            Types types,
                            Messager messager,
                            Map<String, String> options,
                            Filer filer,
                            SourceVersion sourceVersion,
                            Locale locale,
                            boolean isPreviewEnabled)
{
   @JdkApi
   @Override
   public RoundEnvironment roundEnv()
   {
      return roundEnv;
   }

   @JdkApi
   @Override
   public Elements elements()
   {
      return elements;
   }

   @JdkApi
   @Override
   public Types types()
   {
      return types;
   }

   @JdkApi
   @Override
   public Messager messager()
   {
      return messager;
   }

   @JdkApi
   @Override
   public @UnmodifiableView Map<String, String> options()
   {
      return options;
   }

   @JdkApi
   @Override
   public Filer filer()
   {
      return filer;
   }

   @JdkApi
   @Override
   public SourceVersion sourceVersion()
   {
      return sourceVersion;
   }

   @JdkApi
   @Override
   public Locale locale()
   {
      return locale;
   }

   @JdkApi
   @Override
   public boolean isPreviewEnabled()
   {
      return isPreviewEnabled;
   }
}
