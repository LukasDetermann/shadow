package io.determann.shadow.impl;

import io.determann.shadow.api.*;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Package;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;

import static java.lang.System.err;
import static java.lang.System.out;

public class ShadowApiImpl implements ShadowApi
{
   private final JdkApiContext jdkApiContext;
   private final ShadowFactory shadowFactory = new ShadowFactoryImpl(this);
   private final int processingRound;


   public ShadowApiImpl(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      this.processingRound = processingRound;
      this.jdkApiContext = new JdkApiContext(processingEnv, roundEnv);

      if (!processingEnv.toString().startsWith("javac"))
      {
         proxySystemOut(getJdkApiContext().getProcessingEnv().getMessager());
      }
      proxySystemErr(getJdkApiContext().getProcessingEnv().getMessager());
   }

   @Override
   public JdkApiContext getJdkApiContext()
   {
      return jdkApiContext;
   }

   private void proxySystemOut(Messager messager)
   {
      //in >= java 18 out.getCharset()
      PrintStream printStream = new PrintStream(out)
      {
         @Override
         public void println(String x)
         {
            super.println(x);
            if (x != null)
            {
               messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, x);
            }
         }
      };
      System.setOut(printStream);
   }

   private void proxySystemErr(Messager messager)
   {
      //in >= java 18 out.getCharset()
      PrintStream printStream = new PrintStream(err)
      {
         @Override
         public void println(String x)
         {
            if (x != null)
            {
               messager.printMessage(Diagnostic.Kind.ERROR, x);
            }
         }
      };
      System.setErr(printStream);
   }

   @Override
   public AnnotationTypeChooser getAnnotatedWith(String qualifiedAnnotation)
   {
      TypeElement annotation = getJdkApiContext().getProcessingEnv().getElementUtils().getTypeElement(qualifiedAnnotation);
      if (annotation == null || !annotation.getKind().equals(ElementKind.ANNOTATION_TYPE))
      {
         throw new IllegalArgumentException("No annotation found with qualified name \"" + qualifiedAnnotation + "\"");
      }
      return new AnnotationTypeChooserImpl(this, jdkApiContext.getRoundEnv().getElementsAnnotatedWith(annotation));
   }

   @Override
   public AnnotationTypeChooser getAnnotatedWith(Annotation annotation)
   {
      return getAnnotatedWith(annotation.getQualifiedName());
   }

   @Override
   public Package getPackageOrThrow(String qualifiedName)
   {
      return getShadowFactory().shadowFromElement(getJdkApiContext().getProcessingEnv().getElementUtils().getPackageElement(qualifiedName));

   }

   @Override
   public Declared getDeclaredOrThrow(String qualifiedName)
   {
      TypeElement typeElement = getJdkApiContext().getProcessingEnv().getElementUtils().getTypeElement(qualifiedName);
      if (typeElement == null)
      {
         throw new IllegalArgumentException("no Declared found for \"" + qualifiedName + "\"");
      }
      return getShadowFactory().shadowFromElement(typeElement);
   }

   @Override
   public ShadowFactory getShadowFactory()
   {
      return shadowFactory;
   }

   @Override
   public ShadowConstants getConstants()
   {
      return new ShadowConstantsImpl(this);
   }

   @Override
   public void logError(String msg)
   {
      getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
   }

   @Override
   public void logInfo(String msg)
   {
      getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
   }

   @Override
   public void logWarning(String msg)
   {
      getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg);
   }

   @Override
   public void logErrorAt(ElementBacked<?> elementBacked, String msg)
   {
      elementBacked.logError(msg);
   }

   @Override
   public void logInfoAt(ElementBacked<?> elementBacked, String msg)
   {
      elementBacked.logInfo(msg);
   }

   @Override
   public void logWarningAt(ElementBacked<?> elementBacked, String msg)
   {
      elementBacked.logWarning(msg);
   }

   @Override
   public void writeSourceFile(String qualifiedName, String content)
   {
      try (Writer writer = getJdkApiContext().getProcessingEnv().getFiler().createSourceFile(qualifiedName).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void writeClassFile(String qualifiedName, String content)
   {
      try (Writer writer = getJdkApiContext().getProcessingEnv().getFiler().createClassFile(qualifiedName).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void writeResource(StandardLocation location, String moduleAndPkg, String relativPath, String content)
   {
      try (Writer writer = getJdkApiContext().getProcessingEnv().getFiler().createResource(location, moduleAndPkg, relativPath).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public FileObject readResource(StandardLocation location, String moduleAndPkg, String relativPath) throws IOException
   {
      return getJdkApiContext().getProcessingEnv().getFiler().getResource(location, moduleAndPkg, relativPath);
   }

   @Override
   public boolean isLastRound()
   {
      return getJdkApiContext().getRoundEnv().processingOver();
   }

   @Override
   public boolean isFirstRound()
   {
      return processingRound == 0;
   }

   @Override
   public int getProcessingRound()
   {
      return processingRound;
   }

   @Override
   public ShadowApi getApi()
   {
      return this;
   }
}
