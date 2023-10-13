<p align="center">
  <a href="#why-shadow-api">Why?</a> |
  <a href="#api-goals">Goals</a> |
  <a href="#project-status">Status</a> |
  <a href="#getting-started">Getting started</a> |
  <a href="#examples">Examples</a>
</p>

# Shadow API <br><b style="font-size: 65%">An easy way to write your annotation processor</b>

[maven central](https://central.sonatype.com/namespace/io.determann)

#### What do annotation processors do?

Annotation-processors run at compile time. They process representations of the source code that is being compiled. 
They also can create new Files, including source files, and raise errors or warnings.


## Why Shadow API?

Annotation processing is powerful, but the JDK APIs 
(<a href = "https://docs.oracle.com/javase/7/docs/jdk/api/apt/mirror/overview-summary.html">Mirror API</a>,
<a href = "https://docs.oracle.com/en/java/javase/17/docs/api/java.compiler/javax/lang/model/package-summary.html">javax.lang.model</a>)
did not age well.

Shadow API offers:

<details><summary>Straightforward data structure</summary>
<table>
<tr>
<th>Shadow API</th>
<th>JDK</th>
</tr>
<tr>
<td width="300">

- Shadow
  - Declared
    - Class 
    - Enum 
    - Record 
    - Annotation
        - AnnotationUsage
  - Array
  - Executable
    - Constructor
    - Method
  - Intersection
  - Void
  - Module
  - Package
  - RecordComponent
  - Null
  - Primitive
  - Generic
  - Wildcard
  - Variable
    - EnumConstant
    - Field
    - Parameter
</td>
<td width="300">

- TypeMirror
  - ReferenceType
    - ArrayType  
    - DeclaredType  
    - ErrorType  
    - NullType  
    - TypeVariable
  - ExecutableType
  - IntersectionType
  - NoType
  - PrimitiveType
  - UnionType
  - WildcardType
  

- AnnotationMirror


- Element
  - ExecutableElement
  - ModuleElement
  - PackageElement
  - RecordComponentElement
  - TypeElement
  - TypeParameterElement
  - VariableElement
</td>
</tr>
</table>
</details>
<details><summary>Safe conversion between objects </summary>
<br/>

**Let's say you process the following class and want to get the type of the list**

````java
import java.util.List;

class MyClass
{
   private final List<String> myField;
}
````
<br/>
<table>
<tr>
<th>Shadow API</th>
<th>JDK</th>
</tr>
<tr>
<td width="50%">

````java
class ConversionTest
{
  @Test
  void testConversion()
  {
    ProcessorTest
      .process(shadowApi ->
         {
           Shadow<TypeMirror> myField = shadowApi.getClassOrThrow("MyClass")
                                                 .getFieldOrThrow("myField")
                                                 .getType();
           //Converters limit the conversion to possible types
           Shadow<TypeMirror> genericType = convert(myField)
                   .toInterfaceOrThrow()
                   .getGenerics()
                   .get(0);
                   
           assertEquals(shadowApi.getClassOrThrow("java.lang.String"), 
                        genericType);
         })
      .withCodeToCompile("MyClass.java", """
         import java.util.List;
         class MyClass {
            private List<String> myField;
         }""")
      .compile();
  }
}
````
</td>
<td width="50%">

````java
class ConversionTest
{
  @Test
  void testConversion()
  {
    ProcessorTest
      .process(shadowApi ->
         {
           Elements elements = shadowApi.getJdkApiContext()
                                        .getProcessingEnv()
                                        .getElementUtils();
           //get a type -> Element data structure 
           List<? extends Element> myClass = elements.getTypeElement("MyClass")
                                                     .getEnclosedElements();

           //get fields of that type -> Element data structure 
           VariableElement myField = ElementFilter
                   .fieldsIn(myClass)
                   .stream()
                   .filter(field -> field.getSimpleName()
                                         .toString()
                                         .equals("myField"))
                   .findAny()
                   .orElseThrow();
           
           //get Generic -> switch to Type data structure  
           TypeMirror genericType = ((DeclaredType) myField.asType())
                   .getTypeArguments().get(0);
                   
           //switch back to Element data structure for comparison
           Element genericElement = ((DeclaredType) genericType).asElement();
           
           assertEquals(elements.getTypeElement("java.lang.String"),
                        genericElement);
         })
      .withCodeToCompile("MyClass.java", """
         import java.util.List;
         class MyClass {
            private List<String> myField;
         }""")
      .compile();
  }
}
````
</td>
</tr>
</table>
</details>
<details><summary>Better documentation, especially for hard to understand topics</summary>

<table>
<tr>
<th>Shadow API</th>
<th>JDK</th>
</tr>
<tr>
<td width="50%">

````java
public interface Shadow
{
   //...
  /**
   * Information regarding generics is lost after the compilation. For Example 
   * {@code List<String>} becomes {@code List}. This method Does the same.
   * This can be useful if you want to check if a shadow implements for example 
   * {@link java.util.Collection} 
   * {@code shadowToTest.erasure().isSubtypeOf(shadowApi.getDeclaredOrThrow("java.util.Collection").erasure())}
   */
  Shadow<TypeMirror> erasure();
  //...
}
````
</td>
<td width="50%">

````java
public interface Types {
   //...
  /**
   * {@return the erasure of a type}
   *
   * @param t  the type to be erased
   * @throws IllegalArgumentException if given a type for a package or module
   * @jls 4.6 Type Erasure
   */
  TypeMirror erasure(TypeMirror t);
  //...
}
````
</td>
</tr>
</table>
</details>
<details><summary>Good interoperability with `javax.lang.model`</summary>

<table>
<tr>
<th>Shadow API -> JDK</th>
<th>JDK -> Shadow API</th>
</tr>
<tr>
<td width="50%">

````java
class ConversionTest
{
  @Test
  void testConversion1()
  {
    ProcessorTest
      .process(shadowApi ->
         {
           ProcessingEnvironment processingEnv = shadowApi.getJdkApiContext().getProcessingEnv();
           RoundEnvironment roundEnv = shadowApi.getJdkApiContext().getRoundEnv();

           Element typeElement = shadowApi.getClassOrThrow("java.lang.String").getElement();
           TypeMirror mNyClass1 = shadowApi.getClassOrThrow("java.lang.String").getMirror();
         })
      .compile();
  }
}
````
</td>
<td width="50%">

````java
class ConversionTest extends AbstractProcessor
{
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
  {
    ShadowApi shadowApi = ShadowApi.create(processingEnv, roundEnv, 0);

    Shadow<? extends TypeMirror> shadow = shadowApi.getShadowFactory().shadowFromElement(null);
    Shadow<? extends TypeMirror> shadow1 = shadowApi.getShadowFactory().shadowFromType(null);
    List<AnnotationUsage> annotationUsages = shadowApi.getShadowFactory().annotationUsages(null);
  }
}
````
</td>
</tr>
</table>
</details>
<details><summary>Better rendering of existing sources</summary>


**A simple method like this**

````java
public abstract class MyClass {
   
  @MyAnnotation
  public abstract <T> T get(int index);
}
````
can be rendered in the following ways

<table>
<tr>
<th>Rendering</th>
<th>Shadow API</th>
<th>JDK</th>
</tr>
<tr>
<td>

````java
@MyAnnotation
public abstract <T> T get(int index);
````
</td>
<td>

````java
render(method).declaration()
````
</td>
</tr>
<tr>
<td>

````java
@MyAnnotation
public <T> T get(int index) {
//do stuff
}
````
</td>
<td>

````java
render(method).declaration("//do stuff")
````
</td>
</tr>
<tr>
<td>

````java
get()
````
</td>
<td>

````java
render(method).invocation()
````
</td>
</tr>
<tr>
<td>

````java
get(5413)
````
</td>
<td>

````java
render(method).invocation("5413")
````
</td>
</tr>
<tr>
<td>

````
<T>get(int)
````
</td>
<td>

````java
method.toString()
method.getElement().toString()
````
</td>
<td>

````java
methodElement.toString()
````
</td>
</tr>
<tr>
<td>

````
<T>(int)T
````
</td>
<td>

````java
method.getMirror().toString()
````
</td>
<td>

````java
methodMirror.toString()
````
</td>
</tr>
</table>

Names can be rendered as
- QualifiedNames
- SimpleNames
- WithoutNeedingImports (default)

and a Callback can be registered for NameRenderedEvents to create for example imports.
</details>

## API Goals

#### Short-term

Make annotation processing more accessible and easier to comprehend.

#### Long-term

Provide an API for annotation processing, reflection, reading/writing class files and reading/writing java files.
With the option to support other java code metamodels.

## Project status

The annotation processing API is close to final.
The annotation processing specific parts will be separated.
And then the implementation of this API using reflection will start.
Java 21 does not bring anything new for annotation processing. 
While only annotation processing is supported there is no need for shadow-api-21.

## Getting started

It's normal annotation processing with a better API. The setup is the same. The differences only start when extending
`io.determann.shadow.api.ShadowProcessor` instead of `javax.annotation.processing.AbstractProcessor`.<br>
A good starting point for your own processor is `ShadowApi.getAnnotatedWith(String qualifiedAnnotation)`.


## Examples

There are many examples for how to use specific parts of the API within the [tests](/shadow-api-17/src/test/java/io/determann/shadow/api).

<details><summary>Show setup</summary>
<p>
We will create everything you need to get started with your first annotation processor in maven in this setup. 

### 1) Two Modules

The annotation processor must be compiled first.
Create two maven modules. 
One having the code to process and one containing the annotation processor.

#### processor module
````xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>io.determann</groupId>
    <artifactId>processor-example</artifactId>
    <version>0.2.0</version>
</project>
````

#### processed module
````xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>io.determann</groupId>
    <artifactId>processed-example</artifactId>
    <version>0.2.0</version>
</project>
````

### 2) Dependencies

The processor needs to depend on the `shadow-api`
````xml
        <dependency>
            <groupId>io.determann</groupId>
            <artifactId>shadow</artifactId>
            <version>0.2.0</version>
        </dependency>
````

And the processed module needs to depend on the processor module
````xml
        <dependency>
            <groupId>io.determann</groupId>
            <artifactId>processor-example</artifactId>
            <version>0.2.0</version>
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
                            <groupId>io.determann</groupId>
                            <artifactId>processor-example</artifactId>
                            <version>0.2.0</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
````

### 4) Disable annotation processing

Disable annotation processing in the processor module, otherwise the annotation processor would be used to process itself
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
                    <!--don't compile the annotation processor using the annotation processor-->
                    <compilerArgument>-proc:none</compilerArgument>
                </configuration>
            </plugin>
        </plugins>
    </build>
````

### 5) The processor itself

Extend `ShadowProcessor` for your own processor and override `process()`
````java
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.ShadowProcessor;

public class MyProcessor extends ShadowProcessor
{
   @Override
   public void process(final ShadowApi shadowApi) {
   }
}
````

### 6) Register this processor

Create a file in `src/main/resources/META-INF/services/` called `javax.annotation.processing.Processor` and add your qualified path

````text
io.determann.shadow.example.processor.MyProcessor
````

### 7) Annotation

Now create an Annotation to process in the processor module
````java
public @interface MyAnnotation {}
````

### 8) Process

And finally process anything annotated with that annotation 
````java
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.ShadowProcessor;

public class MyProcessor extends ShadowProcessor
{
   @Override
   public void process(final ShadowApi shadowApi) {
      for (Shadow<TypeMirror> shadow : shadowApi.getAnnotatedWith("io.determann.shadow.example.processor.MyAnnotation").all())
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
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.ShadowProcessor;
import io.determann.shadow.api.property.MutableProperty;
import io.determann.shadow.api.shadow.Class;

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
  public void process(final ShadowApi shadowApi)
  {
    //iterate over every class annotated with the BuilderPattern annotation
    for (Class aClass : shadowApi.getAnnotatedWith("io.determann.shadow.example.processor.builder.BuilderPattern").classes())
    {
      String toBuildQualifiedName = aClass.getQualifiedName();
      String builderQualifiedName = toBuildQualifiedName + "ShadowBuilder";//qualifiedName of the companion builder class
      String builderSimpleName = aClass.getSimpleName() + "ShadowBuilder";//simpleName of the companion builder class
      String builderVariableName = uncapitalize(builderSimpleName);

      //create a record holding the code needed to render a property in the builder
      List<BuilderElement> builderElements = aClass.getMutableProperties()
                                                   .stream()
                                                   .map(property -> renderProperty(builderSimpleName,
                                                                                   builderVariableName,
                                                                                   property))
                                                   .toList();

      //writes the builder
      shadowApi.writeSourceFile(builderQualifiedName,
                                renderBuilder(aClass, toBuildQualifiedName, builderSimpleName, builderVariableName, builderElements));
    }
  }

  /**
   * renders a companion builder class
   */
  private String renderBuilder(final Class aClass,
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
                                        final MutableProperty property)
  {
    String propertyName = property.getSimpleName();
    String type = property.getType().toString();
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

    String toBuildSetter = builderVariableName + "." + property.getSetter().getSimpleName() + "(" + propertyName + ");";

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

   public io.determann.shadow.example.processed.Customer build() {
     io.determann.shadow.example.processed.Customer customer = new io.determann.shadow.example.processed.Customer();
      customer.setName(name);
      return customer;
   }
}
````
</p>
</details>

## Build

To build this project you need a maven [toolchains.xml](https://maven.apache.org/guides/mini/guide-using-toolchains.html) like 
<details><summary>this</summary>

````xml
<?xml version="1.0" encoding="UTF-8"?>
<toolchains>
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>1.8</version>
        </provides>
        <configuration>
            <jdkHome>my/path/to/jdk/8</jdkHome>
        </configuration>
    </toolchain>
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>11</version>
        </provides>
        <configuration>
            <jdkHome>my/path/to/jdk/11</jdkHome>
        </configuration>
    </toolchain>
    <toolchain>
        <type>jdk</type>
        <provides>
            <version>17</version>
        </provides>
        <configuration>
            <jdkHome>my/path/to/jdk/17</jdkHome>
        </configuration>
    </toolchain>
</toolchains>
````
</details>