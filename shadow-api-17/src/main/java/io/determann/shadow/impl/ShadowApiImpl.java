package io.determann.shadow.impl;

import io.determann.shadow.api.*;
import io.determann.shadow.api.shadow.Annotation;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.List;

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
         proxySystemOut(getJdkApiContext().processingEnv().getMessager());
      }
      proxySystemErr(getJdkApiContext().processingEnv().getMessager());
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
      TypeElement annotation = getJdkApiContext().processingEnv().getElementUtils().getTypeElement(qualifiedAnnotation);
      if (annotation == null || !annotation.getKind().equals(ElementKind.ANNOTATION_TYPE))
      {
         throw new IllegalArgumentException("No annotation found with qualified name \"" + qualifiedAnnotation + "\"");
      }
      return new AnnotationTypeChooserImpl(this, jdkApiContext.roundEnv().getElementsAnnotatedWith(annotation));
   }

   @Override
   public AnnotationTypeChooser getAnnotatedWith(Annotation annotation)
   {
      return getAnnotatedWith(annotation.getQualifiedName());
   }

   @Override
   public List<Module> getModules()
   {
      return getJdkApiContext().processingEnv().getElementUtils()
                          .getAllModuleElements()
                          .stream()
                          .map(moduleElement -> getShadowFactory().<Module>shadowFromElement(moduleElement))
                          .toList();
   }

   @Override
   public Module getModuleOrThrow(String name)
   {
      ModuleElement moduleElement = getJdkApiContext().processingEnv().getElementUtils().getModuleElement(name);
      if (moduleElement == null)
      {
         throw new IllegalArgumentException("no module fond with name \"" + name + "\"");
      }
      return getShadowFactory().shadowFromElement(moduleElement);
   }

   @Override
   public List<Package> getPackagesOrThrow(String qualifiedName)
   {
      return getJdkApiContext().processingEnv().getElementUtils()
                          .getAllPackageElements(qualifiedName)
                          .stream()
                          .map(packageElement -> getShadowFactory().<Package>shadowFromElement(packageElement))
                          .toList();
   }

   @Override
   public List<Package> getPackages()
   {
      return getJdkApiContext().processingEnv().getElementUtils()
                          .getAllModuleElements()
                          .stream()
                          .flatMap(moduleElement -> moduleElement.getEnclosedElements().stream())
                          .map(packageElement -> getShadowFactory().<Package>shadowFromElement(packageElement))
                          .toList();
   }

   @Override
   public Package getPackageOrThrow(String qualifiedModuleName, String qualifiedPackageName)
   {
      return getShadowFactory().shadowFromElement(getJdkApiContext().processingEnv().getElementUtils()
                                                               .getPackageElement(getModuleOrThrow(qualifiedModuleName).getElement(),
                                                                                  qualifiedPackageName));
   }

   @Override
   public Package getPackageOrThrow(Module module, String qualifiedPackageName)
   {
      return getShadowFactory().shadowFromElement(getJdkApiContext().processingEnv().getElementUtils()
                                                               .getPackageElement(module.getElement(),
                                                                                  qualifiedPackageName));
   }

   @Override
   public Declared getDeclaredOrThrow(String qualifiedName)
   {
      TypeElement typeElement = getJdkApiContext().processingEnv().getElementUtils().getTypeElement(qualifiedName);
      if (typeElement == null)
      {
         throw new IllegalArgumentException("no Declared found for \"" + qualifiedName + "\"");
      }
      return getShadowFactory().shadowFromElement(typeElement);
   }

   @Override
   public List<Declared> getDeclared()
   {
      return getPackages()
            .stream()
            .flatMap(packageShadow -> packageShadow.getContent().stream())
            .toList();
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
      getJdkApiContext().processingEnv().getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
   }

   @Override
   public void logInfo(String msg)
   {
      getJdkApiContext().processingEnv().getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
   }

   @Override
   public void logWarning(String msg)
   {
      getJdkApiContext().processingEnv().getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg);
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
      try (Writer writer = getJdkApiContext().processingEnv().getFiler().createSourceFile(qualifiedName).openWriter())
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
      try (Writer writer = getJdkApiContext().processingEnv().getFiler().createClassFile(qualifiedName).openWriter())
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
      try (Writer writer = getJdkApiContext().processingEnv().getFiler().createResource(location, moduleAndPkg, relativPath).openWriter())
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
      return getJdkApiContext().processingEnv().getFiler().getResource(location, moduleAndPkg, relativPath);
   }

   @Override
   public boolean isLastRound()
   {
      return getJdkApiContext().roundEnv().processingOver();
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
