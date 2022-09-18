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
public final class JdkApiContext
{
   private final RoundEnvironment roundEnv;
   private final Elements elements;
   private final Types types;
   private final Messager messager;
   private final Map<String, String> options;
   private final Filer filer;
   private final SourceVersion sourceVersion;
   private final Locale locale;

   public JdkApiContext(RoundEnvironment roundEnv,
                        Elements elements,
                        Types types,
                        Messager messager,
                        Map<String, String> options,
                        Filer filer,
                        SourceVersion sourceVersion,
                        Locale locale)
   {
      this.roundEnv = roundEnv;
      this.elements = elements;
      this.types = types;
      this.messager = messager;
      this.options = options;
      this.filer = filer;
      this.sourceVersion = sourceVersion;
      this.locale = locale;
   }

   @JdkApi
   public RoundEnvironment roundEnv()
   {
      return roundEnv;
   }

   @JdkApi
   public Elements elements()
   {
      return elements;
   }

   @JdkApi
   public Types types()
   {
      return types;
   }

   @JdkApi
   public Messager messager()
   {
      return messager;
   }

   @JdkApi
   public @UnmodifiableView Map<String, String> options()
   {
      return options;
   }

   @JdkApi
   public Filer filer()
   {
      return filer;
   }

   @JdkApi
   public SourceVersion sourceVersion()
   {
      return sourceVersion;
   }

   @JdkApi
   public Locale locale()
   {
      return locale;
   }
}
