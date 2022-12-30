package org.determann.shadow.example.processor.builder;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.ShadowProcessor;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.wrapper.Property;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Builds a companion Builder class for each annotated class
 */
public class ShadowBuilderProcessor extends ShadowProcessor
{
   @Override
   public void process(final ShadowApi shadowApi)
   {
      //iterate over every class annotated with the BuilderPattern annotation
      for (Class aClass : shadowApi.annotatedWith("org.determann.shadow.example.processor.builder.BuilderPattern").classes())
      {
         String toBuildQualifiedName = aClass.getQualifiedName();
         String builderQualifiedName = toBuildQualifiedName + "ShadowBuilder";//qualifiedName of the companion builder class
         String builderSimpleName = aClass.getSimpleName() + "ShadowBuilder";//simpleName of the companion builder class

         //create a record holding the code needed to render a property in the builder
         List<BuilderElement> builderElements = aClass.getProperties()
                                                      .stream()
                                                      .map(property -> renderProperty(builderSimpleName, toBuildQualifiedName, property))
                                                      .toList();

         //writes the builder
         shadowApi.writeSourceFile(builderQualifiedName, renderBuilder(aClass, toBuildQualifiedName, builderSimpleName, builderElements));
      }
   }

   /**
    * renders a companion builder class
    */
   private String renderBuilder(final Class aClass,
                                final String toBuildQualifiedName,
                                final String builderSimpleName,
                                final List<BuilderElement> builderElements)
   {

      String fields = builderElements.stream()
                                     .map(BuilderElement::field)
                                     .collect(Collectors.joining("\n\n"));

      String mutators = builderElements.stream()
                                       .map(BuilderElement::mutator)
                                       .collect(Collectors.joining("\n\n"));

      String setterInvocations = builderElements.stream()
                                                .map(BuilderElement::toBuildSetter)
                                                .collect(Collectors.joining("\n\n"));
      return """
            package %1$s;
                  
            public class %2$s{
               %3$s
                  
            %4$s
                  
               public %5$s build() {
                  %5$s %6$s = new %5$s();
                  %7$s
                  return %6$s;
               }
            }
            """.formatted(aClass.getPackage().getQualifiedName(),
                          builderSimpleName,
                          fields,
                          mutators,
                          toBuildQualifiedName,
                          aClass.getApi().to_lowerCamelCase(toBuildQualifiedName),
                          setterInvocations);
   }

   /**
    * Creates a {@link BuilderElement} for each property of the annotated pojo
    */
   private BuilderElement renderProperty(final String builderSimpleName, final String toBuildQualifiedName, final Property property)
   {
      String propertyName = property.getField().getSimpleName();
      String type = property.getField().getType().toString();
      String field = "private " + type + " " + propertyName + ";";

      String mutator = """
               public %1$s with%2$s(%3$s %4$s) {
                  this.%4$s = %4$s;
                  return this;
               }
            """.formatted(builderSimpleName,
                          property.getApi().to_UpperCamelCase(propertyName),
                          type,
                          propertyName);

      String toBuildSetter = property.getApi().to_lowerCamelCase(toBuildQualifiedName) + "." + property.getSetterOrThrow().getSimpleName() +
                             "(" +
                             propertyName +
                             ");";

      return new BuilderElement(field, mutator, toBuildSetter);
   }

   /**
    * Used to render the code needed to render a property in the builder
    *
    * @param field ones rendered will hold the values being used to build the pojo
    * @param mutator ones rendered will set the value of the {@link #field}
    * @param toBuildSetter ones rendered will modify the build pojo
    */
   private record BuilderElement(String field,
                                 String mutator,
                                 String toBuildSetter) {}
}