<p align="center">
  <a href="#why-shadow-api">Why?</a> |
  <a href="#api-goals">Goals</a> |
  <a href="#project-status">Status</a> |
  <a href="#getting-started">Getting started</a> |
  <a href="#examples">Examples</a>
</p>

# Shadow API <br><b style="font-size: 65%">An easy way to write your annotation processor</b>


#### What do annotation processors do?

Annotation-processors run at compile time. They process representations of the source code that is being compiled. And can create 
new Files, including source files, and raise errors or warnings.


## Why Shadow API?

Annotation processing is powerful, but the JDK APIs 
(<a href = "https://docs.oracle.com/javase/7/docs/jdk/api/apt/mirror/overview-summary.html">Mirror API</a>,
<a href = "https://docs.oracle.com/en/java/javase/17/docs/api/java.compiler/javax/lang/model/package-summary.html">javax.lang.model</a>)
did not age well.

Shadow API offers:
- A simplified data structure
- Safe conversion between objects 
- Better documentation, especially at hard to understand points
- Good interoperability with `javax.lang.model`
- No additional support for creating source files over `javax.lang.model`


## API Goals

Make annotation processing more accessible and easier to comprehend.


## Project status

Not production ready! At the moment it's all about finalising the API. There will most likely be breaking changes during that period.

## Getting started

It's normal annotation processing with a better API. The setup is the same. The differences only start when extending
`org.determann.shadow.api.ShadowProcessor` instead of `javax.annotation.processing.AbstractProcessor`.<br>
A good starting point for your own processor is `ShadowApi.annotatedWith(String qualifiedAnnotation)`.


## Examples

There are a lot of examples for how to use specific parts of the API in the [tests](/processor-example/src/main/java/org/determann/shadow/example/processor/test).
The code processed in the tests can be found [here](/processed-example/src/main/java/org/determann/shadow/example/processed/test)

<details><summary>Show setup</summary>
<p>
We will create everything you need to get started with your first annotation processor in maven in this setup. 

### 1) Two Modules

In order to use the annotation processor to process the code that will be built later, the annotation processor must first be compiled.
Create two Maven modules for that. One having the code to process and one containing the annotation processor

#### processor module
````xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.determann</groupId>
    <artifactId>processor-example</artifactId>
    <version>1.0-SNAPSHOT</version>
</project>
````

#### processed module
````xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>org.determann</groupId>
    <artifactId>processed-example</artifactId>
    <version>1.0-SNAPSHOT</version>
</project>
````

### 2) Dependencies

The processor needs to depend on the `shadow-api`
````xml
        <dependency>
            <groupId>org.determann</groupId>
            <artifactId>shadow</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
````

And the processed module need to depend on the processor module
````xml
        <dependency>
            <groupId>org.determann</groupId>
            <artifactId>processor-example</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
````

### 3) Processor paths

The module being processed needs to know the module it's processed by
````xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.determann</groupId>
                            <artifactId>processor-example</artifactId>
                            <version>1.0-SNAPSHOT</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
````

### 4) Disable annotation processing

Disable annotation processing in the processor module. Otherwise, the annotation processor would be used to process itself
````xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                    <!--                    don't compile the annotation processor using the annotation processor-->
                    <compilerArgument>-proc:none</compilerArgument>
                </configuration>
            </plugin>
        </plugins>
    </build>
````

### 5) The processor itself

Extend `ShadowProcessor` for your own processor and override `process()`
````java
import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.ShadowProcessor;

public class MyProcessor extends ShadowProcessor
{
   @Override
   public void process(final ShadowApi shadowApi) {
   }
}
````

### 6) Register this processor

create a file in `src/main/resources/META-INF/services/` called `javax.annotation.processing.Processor` and add your qualified path

````text
org.determann.shadow.example.processor.MyProcessor
````

### 7) Annotation

Now create an Annotation to process in the processor module
````java
public @interface MyAnnotation {}
````

### 8) Process

And finally process anything annotated with that annotation 
````java
import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.ShadowProcessor;

public class MyProcessor extends ShadowProcessor
{
   @Override
   public void process(final ShadowApi shadowApi) {
      for (Shadow<TypeMirror> shadow : shadowApi.annotatedWith("org.determann.shadow.example.processor.MyAnnotation").all())
      {
      }
   }
}
````

</p>
</details>

<details><summary>Show simple builder example</summary>
<p>

An annotation to mark classes
````java
@Target(ElementType.TYPE)
public @interface BuilderPattern {}
````
A Processor creating a simple Builder companion object
````java
import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.ShadowProcessor;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.wrapper.Property;

/**
 * Builds a companion Builder class for each annotated class
 */
public class ShadowBuilderProcessor extends ShadowProcessor
{
   @Override
   public void process(final ShadowApi shadowApi) {
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
                                final List<BuilderElement> builderElements) {

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
   private BuilderElement renderProperty(final String builderSimpleName, final String toBuildQualifiedName, final Property property) {
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

      String toBuildSetter = property.getApi().to_lowerCamelCase(toBuildQualifiedName) + "." + property.getSetter().getSimpleName() +
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
   private record BuilderElement(String field, String mutator, String toBuildSetter) {}
}
````
For a simple pojo like
````java
@BuilderPattern
public class Customer
{
   public static CustomerShadowBuilder builder()
   {
      return new CustomerShadowBuilder();
   }
   
   private String name;

   public String getName() { return name; }

   public void setName(String name) { this.name = name; }
}
````
This builder would be generated
````java
public class CustomerShadowBuilder{
   private java.lang.String name;

   public CustomerShadowBuilder withName(java.lang.String name) {
      this.name = name;
      return this;
   }

   public org.determann.shadow.example.processed.Customer build() {
      org.determann.shadow.example.processed.Customer customer = new org.determann.shadow.example.processed.Customer();
      customer.setName(name);
      return customer;
   }
}
````
</p>
</details>