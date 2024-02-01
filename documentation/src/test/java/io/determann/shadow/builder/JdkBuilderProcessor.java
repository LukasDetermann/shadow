package io.determann.shadow.builder;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("io.determann.shadow.builder.BuilderPattern")
public class JdkBuilderProcessor extends AbstractProcessor
{
   @Override
   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
   {
      for (TypeElement annotation : annotations)
      {
         for (Element element : roundEnv.getElementsAnnotatedWith(annotation))
         {
            if (element.getKind().equals(ElementKind.CLASS))
            {
               TypeElement typeElement = (TypeElement) element;
               String toBuildQualifiedName = typeElement.getQualifiedName().toString();
               String toBuildSimpleName = typeElement.getSimpleName().toString();
               String builderQualifiedName = toBuildQualifiedName + "JdkBuilder";
               String builderSimpleName = typeElement.getSimpleName() + "JdkBuilder";

               Map<VariableElement, ExecutableElement> fieldSetter = findFieldSetter(typeElement);
               List<BuilderElement> builderElements = fieldSetter.entrySet()
                                                                 .stream()
                                                                 .map(fieldSetter1 -> renderFieldSetter(builderSimpleName,
                                                                                                        toBuildSimpleName,
                                                                                                        fieldSetter1))
                                                                 .toList();

               String builderSrc = renderBuilder(typeElement, toBuildQualifiedName, builderSimpleName, builderElements);

               try (Writer writer = processingEnv.getFiler().createSourceFile(builderQualifiedName)
                                                .openWriter())
               {
                  writer.write(builderSrc);
               }
               catch (IOException e)
               {
                  throw new RuntimeException(e);
               }
            }
         }
      }
      return false;
   }

   private String renderBuilder(TypeElement typeElement,
                                String toBuildQualifiedName,
                                String builderSimpleName,
                                List<BuilderElement> builderElements)
   {
      return "package " + processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName() + ";\n" +
             "\n" +
             "public class " + builderSimpleName + "{\n" +
             builderElements.stream().map(BuilderElement::field).collect(Collectors.joining("\n\n")) +
             "\n\n" +
             builderElements.stream().map(BuilderElement::method).collect(Collectors.joining("\n\n")) +
             "\n\n" +
             "public " + toBuildQualifiedName + " build()\n" +
             "{\n" +
             toBuildQualifiedName + " " + typeElement.getSimpleName().toString().toLowerCase() + " = new " + toBuildQualifiedName + "();\n" +
             builderElements.stream().map(BuilderElement::setter).collect(Collectors.joining("\n\n")) + "\n" +
             "return " + typeElement.getSimpleName().toString().toLowerCase() + ";\n" +
             "}\n" +
             "}";
   }

   private BuilderElement renderFieldSetter(String builderSimpleName,
                                            String toBuildSimpleName,
                                            Map.Entry<VariableElement, ExecutableElement> fieldSetter)
   {
      String propertyName = fieldSetter.getKey().getSimpleName().toString();
      String type = fieldSetter.getKey().asType().toString();

      return new BuilderElement("private " + type + " " + propertyName + ";",
                                "public " + builderSimpleName + " with" + capitaliseFirstChar(propertyName) + "(" + type + " " +
                                propertyName +
                                ") {\n" +
                                "this." + propertyName + " = " + propertyName + ";\n" +
                                "return this;\n" +
                                "}",
                                toBuildSimpleName.toLowerCase() +
                                "." + fieldSetter.getValue().getSimpleName() + "(" + propertyName + ");"
      );
   }

   private Map<VariableElement, ExecutableElement> findFieldSetter(TypeElement typeElement)
   {
      List<VariableElement> fields = ElementFilter.fieldsIn(typeElement.getEnclosedElements());
      List<ExecutableElement> methods = ElementFilter.methodsIn(typeElement.getEnclosedElements());

      Map<VariableElement, ExecutableElement> fieldSetter = new HashMap<>();

      for (VariableElement field : fields)
      {
         if (field.getModifiers().contains(Modifier.STATIC))
         {
            continue;
         }
         Name fieldName = field.getSimpleName();
         String capitalisedFieldName = capitaliseFirstChar(fieldName.toString());
         String setterName = "set" + capitalisedFieldName;
         String booleanSetterName = "is" + capitalisedFieldName;

         methods.stream()
                .map(executableElement ->
                     {
                        String methodName = executableElement.getSimpleName().toString();
                        if (methodName.equals(setterName) || methodName.equals(booleanSetterName))
                        {
                           return Optional.of(executableElement);
                        }
                        return Optional.<ExecutableElement>empty();
                     })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .ifPresent(executableElement -> fieldSetter.put(field, executableElement));
      }
      return fieldSetter;
   }

   private String capitaliseFirstChar(String toCapitalise)
   {
      return Character.toUpperCase(toCapitalise.charAt(0)) + toCapitalise.substring(1);
   }

   private record BuilderElement(String field,
                                 String method,
                                 String setter)
   {}
}
