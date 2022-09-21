package org.determann.shadow.impl;

import org.determann.shadow.api.*;
import org.determann.shadow.api.converter.*;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Void;
import org.determann.shadow.api.shadow.*;
import org.determann.shadow.impl.converter.ConverterImpl;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
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
   private final int processingRoundNumber;


   public ShadowApiImpl(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRoundNumber)
   {
      this.processingRoundNumber = processingRoundNumber;
      this.jdkApiContext = new JdkApiContext(
            roundEnv,
            processingEnv.getElementUtils(),
            processingEnv.getTypeUtils(),
            processingEnv.getMessager(),
            processingEnv.getOptions(),
            processingEnv.getFiler(),
            processingEnv.getSourceVersion(),
            processingEnv.getLocale());

      if (!processingEnv.toString().startsWith("javac"))
      {
         proxySystemOut(jdkApiContext.messager());
      }
      proxySystemErr(jdkApiContext.messager());
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
   public AnnotationTypeChooser annotatedWith(String qualifiedAnnotation)
   {
      TypeElement annotation = jdkApiContext.elements().getTypeElement(qualifiedAnnotation);
      if (annotation == null || !annotation.getKind().equals(ElementKind.ANNOTATION_TYPE))
      {
         throw new IllegalArgumentException("No annotation found with qualified name \"" + qualifiedAnnotation + "\"");
      }
      return new AnnotationTypeChooserImpl(this, jdkApiContext.roundEnv().getElementsAnnotatedWith(annotation));
   }

   @Override
   public Package getPackage(String qualifiedName)
   {
      return getShadowFactory().shadowFromElement(jdkApiContext.elements().getPackageElement(qualifiedName));

   }

   @Override
   public Declared getDeclared(String qualifiedName)
   {
      TypeElement typeElement = getJdkApiContext().elements().getTypeElement(qualifiedName);
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
   public String to_UpperCamelCase(String toModify)
   {
      return StringUtils.to_UpperCamelCase(toModify);
   }

   @Override
   public String to_lowerCamelCase(String toModify)
   {
      return StringUtils.to_lowerCamelCase(toModify);
   }

   @Override
   public String to_SCREAMING_SNAKE_CASE(String toModify)
   {
      return StringUtils.to_SCREAMING_SNAKE_CASE(toModify);
   }

   @Override
   public void logError(String msg)
   {
      getJdkApiContext().messager().printMessage(Diagnostic.Kind.ERROR, msg);
   }

   @Override
   public void logInfo(String msg)
   {
      getJdkApiContext().messager().printMessage(Diagnostic.Kind.NOTE, msg);
   }

   @Override
   public void logWarning(String msg)
   {
      getJdkApiContext().messager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg);
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
      try (Writer writer = getJdkApiContext().filer().createSourceFile(qualifiedName).openWriter())
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
      try (Writer writer = getJdkApiContext().filer().createClassFile(qualifiedName).openWriter())
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
      try (Writer writer = getJdkApiContext().filer().createResource(location, moduleAndPkg, relativPath).openWriter())
      {
         writer.write(content);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   @Override
   public FileObject readOResource(StandardLocation location, String moduleAndPkg, String relativPath) throws IOException
   {
      return getJdkApiContext().filer().getResource(location, moduleAndPkg, relativPath);
   }

   @Override
   public boolean isLastRound()
   {
      return getJdkApiContext().roundEnv().processingOver();
   }

   @Override
   public boolean isFirstRound()
   {
      return processingRoundNumber == 0;
   }

   @Override
   public int roundNumber()
   {
      return processingRoundNumber;
   }

   //convert Shadows
   @Override
   public AnnotationConverter convert(Annotation annotationShadow)
   {
      return new ConverterImpl(this, annotationShadow);
   }

   @Override
   public ArrayConverter convert(Array array)
   {
      return new ConverterImpl(this, array);
   }

   @Override
   public ClassConverter convert(Class aClass)
   {
      return new ConverterImpl(this, aClass);
   }

   @Override
   public ConstructorConverter convert(Constructor constructor)
   {
      return new ConverterImpl(this, constructor);
   }

   @Override
   public DeclaredConverter convert(Declared declared)
   {
      return new ConverterImpl(this, declared);
   }

   @Override
   public EnumConstantConverter convert(EnumConstant enumConstant)
   {
      return new ConverterImpl(this, enumConstant);
   }

   @Override
   public EnumConverter convert(Enum enumShadow)
   {
      return new ConverterImpl(this, enumShadow);
   }

   @Override
   public ExecutableConverter convert(Executable executable)
   {
      return new ConverterImpl(this, executable);
   }

   @Override
   public FieldConverter convert(Field field)
   {
      return new ConverterImpl(this, field);
   }

   @Override
   public InterfaceConverter convert(Interface interfaceShadow)
   {
      return new ConverterImpl(this, interfaceShadow);
   }

   @Override
   public IntersectionConverter convert(Intersection intersection)
   {
      return new ConverterImpl(this, intersection);
   }

   @Override
   public MethodConverter convert(Method methodShadow)
   {
      return new ConverterImpl(this, methodShadow);
   }

   @Override
   public VoidConverter convert(Void aVoid)
   {
      return new ConverterImpl(this, aVoid);
   }

   @Override
   public NullConverter convert(Null aNull)
   {
      return new ConverterImpl(this, aNull);
   }

   @Override
   public PackageConverter convert(Package packageShadow)
   {
      return new ConverterImpl(this, packageShadow);
   }

   @Override
   public ParameterConverter convert(Parameter parameter)
   {
      return new ConverterImpl(this, parameter);
   }

   @Override
   public PrimitiveConverter convert(Primitive primitive)
   {
      return new ConverterImpl(this, primitive);
   }

   @Override
   public ShadowConverter convert(Shadow<? extends TypeMirror> shadow)
   {
      return new ConverterImpl(this, shadow);
   }

   @Override
   public GenericConverter convert(Generic generic)
   {
      return new ConverterImpl(this, generic);
   }

   @Override
   public VariableConverter convert(Variable<?> variable)
   {
      return new ConverterImpl(this, variable);
   }

   @Override
   public WildcardConverter convert(Wildcard wildcard)
   {
      return new ConverterImpl(this, wildcard);
   }

   @Override
   public ShadowApi getApi()
   {
      return this;
   }
}
