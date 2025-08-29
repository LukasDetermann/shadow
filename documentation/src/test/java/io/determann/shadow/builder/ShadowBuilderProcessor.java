package io.determann.shadow.builder;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.dsl.Dsl;
import io.determann.shadow.api.dsl.RenderingContext;
import io.determann.shadow.api.dsl.field.FieldRenderable;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.dsl.parameter.ParameterRenderable;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

/// Builds a companion Builder class for each annotated class
public class ShadowBuilderProcessor
      extends Ap.Processor
{
   @Override
   public void process(final Ap.Context context)
   {
      //iterate over every class annotated with the BuilderPattern annotation
      for (Ap.Class aClass : context
            .getClassesAnnotatedWith("io.determann.shadow.builder.BuilderPattern"))
      {
         String toBuildQualifiedName = aClass.getQualifiedName();
         //qualifiedName of the companion builder class
         String builderQualifiedName = toBuildQualifiedName + "ShadowBuilder";
         //simpleName of the companion builder class
         String builderSimpleName = aClass.getName() + "ShadowBuilder";
         String builderVariableName = uncapitalize(builderSimpleName);

         //create a record holding the code needed to render a property in the builder
         List<BuilderElement> builderElements =
               aClass.getProperties()
                     .stream()
                     .filter(Ap.Property::isMutable)
                     .map(property -> renderProperty(builderSimpleName,
                                                     builderVariableName,
                                                     property))
                     .toList();

         //writes the builder
         context.writeAndCompileSourceFile(builderQualifiedName,
                                           renderBuilder(aClass.getPackage().getQualifiedName(),
                                                         toBuildQualifiedName,
                                                         builderSimpleName,
                                                         builderVariableName,
                                                         builderElements));
      }
   }

   /// renders a companion builder class
   private String renderBuilder(final String packageName,
                                final String toBuildQualifiedName,
                                final String builderSimpleName,
                                final String builderVariableName,
                                final List<BuilderElement> builderElements)
   {
      String setterInvocations = builderElements.stream()
                                                .map(BuilderElement::toBuildSetterInvocation)
                                                .collect(Collectors.joining("\n\n"));

      return Dsl.class_()
                .package_(packageName)
                .name(builderSimpleName)
                .field(builderElements.stream().map(BuilderElement::field).toArray(FieldRenderable[]::new))
                .method(builderElements.stream().map(BuilderElement::mutator).toArray(MethodRenderable[]::new))
                .method("""
                        public %1$s build() {
                           %1$s %2$s = new %1$s();
                           %3$s
                           return %2$s;
                        }""".formatted(toBuildQualifiedName,
                                       builderVariableName,
                                       setterInvocations))
                .renderDeclaration(RenderingContext.DEFAULT);
   }

   /// Creates a {@link BuilderElement} for each property of the annotated pojo
   private BuilderElement renderProperty(final String builderSimpleName,
                                         final String builderVariableName,
                                         final Ap.Property property)
   {
      String propertyName = property.getName();

      FieldRenderable field = property.getField().map(FieldRenderable.class::cast).orElseGet(() -> Dsl.field(property.getType(), propertyName));

      ParameterRenderable propertyParam = Dsl.parameter(property.getType(), propertyName);

      MethodRenderable mutator = Dsl.method()
                                    .public_()
                                    .result(builderSimpleName)
                                    .name("with" + capitalize(propertyName))
                                    .parameter(propertyParam)
                                    .body("""
                                          this.%1$s = %1$s;
                                          return this;""".formatted(propertyName));

      String toBuildSetterInvocation = builderVariableName + "." +
                                       property.getSetterOrThrow().getName() +
                                       "(" + propertyName + ");";

      return new BuilderElement(field, mutator, toBuildSetterInvocation);
   }

   /// Used to render the code needed to render a property in the builder
   ///
   /// @param field ones rendered will hold the values being used to build the pojo
   /// @param mutator ones rendered will set the value of the {@link #field}
   /// @param toBuildSetterInvocation ones rendered will modify the build pojo
   private record BuilderElement(FieldRenderable field,
                                 MethodRenderable mutator,
                                 String toBuildSetterInvocation) {}
}