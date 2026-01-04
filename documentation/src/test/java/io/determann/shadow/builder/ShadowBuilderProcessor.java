package io.determann.shadow.builder;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.Dsl;
import io.determann.shadow.api.annotation_processing.dsl.class_.ClassBodyStep;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

/// Builds a companion Builder class for each annotated class
public class ShadowBuilderProcessor
      extends Ap.Processor
{
   @Override
   public void process(final Ap.Context context)
   {
      // iterate over every class annotated with the BuilderPattern annotation
      for (Ap.Class toBuild : context
            .getClassesAnnotatedWith("io.determann.shadow.builder.BuilderPattern"))
      {
         // create the Builder Class
         ClassBodyStep step = Dsl.class_()
                                 .package_(toBuild.getPackage().getQualifiedName())
                                 .name(toBuild.getName() + "ShadowBuilder");

         String toBuildVariableName = uncapitalize(toBuild.getName());
         List<String> setterInvocations = new ArrayList<>();

         // create a record holding the code needed to render a property in the builder
         for (Ap.Property property : toBuild.getProperties()
                                            .stream()
                                            .filter(Ap.Property::isMutable)
                                            .toList())
         {
            String propertyName = property.getName();

            // render the existing field if possible, otherwise create a new one
            step = property.getField().map(step::field)
                           .orElse(step.field(Dsl.field().private_().type(property.getType()).name(propertyName)));

            // render the wither
            step = step.method(Dsl.method()
                                  .public_()
                                  .resultType(step)//use the builder type
                                  .name("with" + capitalize(propertyName))
                                  .parameter(Dsl.parameter(property.getType(), propertyName))
                                  .body("""
                                        this.%1$s = %1$s;
                                        return this;""".formatted(propertyName)));

            // collect all setter invocations for the object being build
            setterInvocations.add(toBuildVariableName + "." +
                                  property.getSetterOrThrow().getName() +
                                  "(" + propertyName + ");");
         }

         // render the build method
         step = step.method(Dsl.method().public_().resultType(toBuild).name("build")
                               .body("""
                                     %1$s %2$s = new %1$s();
                                     %3$s
                                     return %2$s;
                                     """.formatted(toBuild.getName(),
                                                   toBuildVariableName,
                                                   join("\n\n", setterInvocations))));

         //writes the builder
         context.writeAndCompileSourceFile(step);
      }
   }
}