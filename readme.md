# Shadow Api


Java SE and the Java ecosystem offer a multitude of Metaprogramming
Apis. They serve the same purpose just in different contexts. Annotation
processing to analyse classes in the during compilation process, or
Reflection to do the same at runtime. Each having unique concepts and
quirks. This makes Metaprogramming harder to understand and creates a
maintenance overhead. It’s very hard to switch from one Api to another.
Once something is written with java.lang.reflect it’s hard to change it
to an Annotation Processor. Some JDK Apis are not up to the normal
standard.


This project is an Abstraction for Metaprogramming


<img src="_images/Overview.svg" style="width:80.0%" alt="Overview" />


It has two Types of Apis



## General Purpose Api


This Api works with any kind of Metaprogramming, but the featureset is
limited to common operations

<details>
<summary>Example</summary>


The Api is request based. As a caller you can request for example a
field of a class. Accessing fields may or may not be supported.


``` highlightjs
@Test
void request()
{
   //adapter for the reflection api
   Class systemClass = ReflectionAdapter.generalize(System.class);
   //request the field "out" for the class java.lang.System
   Response<Field> out = Provider.request(systemClass,
                                          Operations.DECLARED_GET_FIELD,
                                          "out");

   switch (out)
   {
      //the implementation may not support this operation
      //e.g. it's impossible to access fields with reflection
      case Response.Unsupported<Field> unsupported -> Assertions.fail();
      //the implementation may support this operation, but there is no
      //result for this instance
      //e.g. the class java.lang.System does not have a field called "out"
      case Response.Empty<Field> empty -> Assertions.fail();
      //accessing fields via reflection is possible and java.lang.System
      //does have a field called "out" therefore a result is expected
      case Response.Result<Field> result -> assertNotNull(result.value());
   }
}
```

Or use a convenience method if `Optional.empty()` or throwing an
Exception is a fitting default behavior.

``` highlightjs
@Test
void requestOrEmpty()
{
   //adapter for the reflection api
   Class systemClass = ReflectionAdapter.generalize(System.class);
   //request the field "out" for the class java.lang.System.
   //If its unsupported an Empty Optional is returned
   Optional<Field> out = Provider.requestOrEmpty(systemClass,
                                                 Operations.DECLARED_GET_FIELD,
                                                 "out");

   assertTrue(out.isPresent());
}
```
``` highlightjs
@Test
void requestOrThrow()
{
   //adapter for the reflection api
   Class systemClass = ReflectionAdapter.generalize(System.class);
   //request the field "out" for the class java.lang.System.
   //If its unsupported an Exception is thrown
   Field out = Provider.requestOrThrow(systemClass,
                                       Operations.DECLARED_GET_FIELD,
                                       "out");

   assertNotNull(out);
}
```
</details>



## Specific Apis

There is one Api for each kind of Metaprogramming. One for Annotation
processing and another very similar one for reflection. Each being able
to support the complete featureset.

<details>
<summary>Example</summary>


This Annotation Processor generates Builder


``` highlightjs
package io.determann.shadow.builder;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.annotation_processing.ShadowProcessor;
import io.determann.shadow.api.lang_model.shadow.NameableLangModel;
import io.determann.shadow.api.lang_model.shadow.QualifiedNameableLamgModel;
import io.determann.shadow.api.lang_model.shadow.structure.PropertyLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ClassLangModel;
import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

/**
 * Builds a companion Builder class for each annotated class
 */
public class ShadowBuilderProcessor extends ShadowProcessor
{
   @Override
   public void process(final AnnotationProcessingContext context)
   {
      //iterate over every class annotated with the BuilderPattern annotation
      for (ClassLangModel aClass : context
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
                     .filter(PropertyLangModel::isMutable)
                     .map(property -> renderProperty(builderSimpleName,
                                                     builderVariableName,
                                                     property))
                     .toList();

         //writes the builder
         context.writeAndCompileSourceFile(builderQualifiedName,
                                           renderBuilder(aClass,
                                                         toBuildQualifiedName,
                                                         builderSimpleName,
                                                         builderVariableName,
                                                         builderElements));
      }
   }

   /**
    * renders a companion builder class
    */
   private String renderBuilder(final ClassLangModel aClass,
                                final String toBuildQualifiedName,
                                final String builderSimpleName,
                                final String builderVariableName,
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
                          builderVariableName,
                          setterInvocations);
   }

   /**
    * Creates a {@link BuilderElement} for each property of the annotated pojo
    */
   private BuilderElement renderProperty(final String builderSimpleName,
                                         final String builderVariableName,
                                         final PropertyLangModel property)
   {
      String propertyName = property.getName();
      String type = renderType(property.getType());
      String field = "private " + type + " " + propertyName + ";";

      String mutator = """
               public %1$s with%2$s(%3$s %4$s) {
                  this.%4$s = %4$s;
                  return this;
               }
            """.formatted(builderSimpleName,
                          capitalize(propertyName),
                          type,
                          propertyName);

      String toBuildSetter = builderVariableName + "." +
                             property.getSetterOrThrow().getName() +
                             "(" + propertyName + ");";

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

   private static String renderType(ShadowLangModel shadow)
   {
      if (shadow instanceof QualifiedNameableLamgModel qualifiedNameable)
      {
         return qualifiedNameable.getQualifiedName();
      }
      if (shadow instanceof NameableLangModel nameable)
      {
         return nameable.getName();
      }
      return shadow.toString();
   }
}
```
</details>



## Supported

- <a href="Annotation%20Processing.html" class="xref page">Annotation
  Processing</a>

- <a href="java.lang.model.html" class="xref page">java.lang.model</a>

- <a href="Reflection.html" class="xref page">reflection</a>
  (experimental)



## Next Goals

- A buch of Api improvements

- Move more of the implementation to the `shadow-implementation-support`
  module for better consistency and easier support of multiple
  implementations

- Support the complete Lexical Structure

- Create a maven archetype to reduce the effort of supporting new
  Metaprogramming Implementations