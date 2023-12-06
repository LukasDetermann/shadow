package io.determann.shadow.api.shadow;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.ShadowConverter;
import io.determann.shadow.api.property.Property;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static io.determann.shadow.api.converter.Converter.convert;
import static org.junit.jupiter.api.Assertions.*;

class ClassTest extends DeclaredTest<Class>
{
   ClassTest()
   {
      super(shadowApi -> shadowApi.getClassOrThrow("java.lang.Object"));
   }

   @Test
   void getSuperClassTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class integer = shadowApi.getClassOrThrow("java.lang.Integer");
                               Declared number = shadowApi.getClassOrThrow("java.lang.Number");
                               assertEquals(integer.getSuperClass(), number);

                               Declared object = shadowApi.getClassOrThrow("java.lang.Object");
                               assertNull(convert(object).toClassOrThrow().getSuperClass());

                               assertEquals(object, convert(number).toClassOrThrow().getSuperClass());
                            })
                   .compile();
   }

   @Test
   void getPropertiesTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               List<Property> properties = shadowApi.getClassOrThrow("PropertiesExample").getProperties();

                               assertEquals(2, properties.size());

                               Property id = properties.get(1);
                               assertEquals("id", id.getField().get().getSimpleName());
                               assertEquals(shadowApi.getConstants().getPrimitiveInt(), id.getField().get().getType());

                               assertEquals("getId", id.getGetter().getSimpleName());
                               assertEquals(shadowApi.getConstants().getPrimitiveInt(), id.getGetter().getReturnType());
                               assertEquals(Collections.emptyList(), id.getGetter().getParameters());

                               assertEquals("setId", id.getSetterOrThrow().getSimpleName());
                               assertEquals(shadowApi.getConstants().getVoid(), id.getSetterOrThrow().getReturnType());
                               assertEquals(Collections.singletonList(shadowApi.getConstants().getPrimitiveInt()),
                                            id.getSetterOrThrow().getParameterTypes());
                            })
                   .withCodeToCompile("PropertiesExample.java", "                           public class PropertiesExample {\n" +
                                                                "                              private int id;\n" +
                                                                "\n" +
                                                                "                              public int getId() {return id;}\n" +
                                                                "                              public void setId(int id) {this.id = id;}\n" +
                                                                "                           }")
                   .compile();
   }

   @Test
   void testGetOuterType()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(shadowApi.getClassOrThrow("OuterTypeExample"),
                                            shadowApi.getClassOrThrow("OuterTypeExample.InnerClass")
                                                     .getOuterType()
                                                     .orElseThrow(IllegalStateException::new));

                               assertEquals(Optional.empty(),
                                            shadowApi.getClassOrThrow("OuterTypeExample").getOuterType());

                               assertEquals(Optional.empty(),
                                            shadowApi.getClassOrThrow("OuterTypeExample.StaticInnerClass").getOuterType());
                            })
                   .withCodeToCompile("OuterTypeExample.java", "                           public class OuterTypeExample {\n" +
                                                               "                              private class InnerClass {}\n" +
                                                               "                              private static class StaticInnerClass {}\n" +
                                                               "                           }")
                   .compile();
   }

   @Test
   void getDirectInterfacesTest()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               List<Interface> directInterfaces = shadowApi.getClassOrThrow("DirectInterfacesExample.Child")
                                                                           .getDirectInterfaces();

                               assertEquals(1, directInterfaces.size());
                               assertEquals(shadowApi.getInterfaceOrThrow("Direct"), directInterfaces.get(0));
                            })
                   .withCodeToCompile("Direct.java", "public interface Direct{}")
                   .withCodeToCompile("InDirect.java", "public interface InDirect{}")
                   .withCodeToCompile("DirectInterfacesExample.java",
                                      "                           public class DirectInterfacesExample implements InDirect {\n" +
                                      "                              public static class Child extends DirectInterfacesExample implements Direct {}\n" +
                                      "                           }")
                   .compile();
   }

   @Test
   void testWithGenerics()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> shadowApi.withGenerics(shadowApi.getClassOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                         "java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> shadowApi.withGenerics(shadowApi.getClassOrThrow("java.lang.String"), "java.lang.String"));

                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"),
                                            shadowApi.withGenerics(shadowApi.getClassOrThrow("WithGenericsExample.Inner"), "java.lang.String")
                                                     .getGenericTypes()
                                                     .get(0));

                               assertEquals(Collections.singletonList(shadowApi.getClassOrThrow("java.lang.String")),
                                            shadowApi.withGenerics(shadowApi.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric"),
                                                                   "java.lang.String")
                                                     .getGenericTypes());

                               assertEquals(Arrays.asList(shadowApi.getClassOrThrow("java.lang.String"),
                                                          shadowApi.getClassOrThrow("java.lang.Number")),
                                            shadowApi.withGenerics(shadowApi.getClassOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                   "java.lang.String",
                                                                   "java.lang.Number")
                                                     .getGenericTypes());
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java",
                                      "                           public class InterpolateGenericsExample <A extends java.lang.Comparable<B>, B extends java.lang.Comparable<A>> {\n" +
                                      "                                static class IndependentGeneric<C> {}\n" +
                                      "                                static class DependentGeneric<D extends E, E> {}\n" +
                                      "                             }")
                   .withCodeToCompile("WithGenericsExample.java", "                           public class WithGenericsExample {\n" +
                                                                  "                              class Inner<T> {}\n" +
                                                                  "                           }")
                   .compile();
   }

   @Test
   void testInterpolateGenerics()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class declared = shadowApi.withGenerics(shadowApi.getClassOrThrow("InterpolateGenericsExample"),
                                                                       shadowApi.getClassOrThrow("java.lang.String"),
                                                                       shadowApi.getConstants().getUnboundWildcard());
                               Class capture = shadowApi.interpolateGenerics(declared);
                               Shadow interpolated = convert(capture.getGenericTypes().get(1))
                                     .toGeneric()
                                     .map(Generic::getExtends)
                                     .map(Converter::convert)
                                     .flatMap(ShadowConverter::toInterface)
                                     .map(Interface::getGenericTypes)
                                     .map(shadows -> shadows.get(0))
                                     .orElseThrow(IllegalStateException::new);
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolated);

                               Class independentExample = shadowApi.withGenerics(shadowApi.getClassOrThrow(
                                     "InterpolateGenericsExample.IndependentGeneric"), shadowApi.getConstants().getUnboundWildcard());
                               Class independentCapture = shadowApi.interpolateGenerics(independentExample);
                               Shadow interpolatedIndependent = convert(independentCapture.getGenericTypes().get(0))
                                     .toGeneric()
                                     .map(Generic::getExtends)
                                     .orElseThrow(IllegalStateException::new);
                               assertEquals(shadowApi.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               Class dependentExample = shadowApi.withGenerics(shadowApi.getClassOrThrow(
                                                                                     "InterpolateGenericsExample.DependentGeneric"), shadowApi.getConstants().getUnboundWildcard(),
                                                                               shadowApi.getClassOrThrow("java.lang.String"));
                               Class dependentCapture = shadowApi.interpolateGenerics(dependentExample);
                               Shadow interpolatedDependent = convert(dependentCapture.getGenericTypes().get(0))
                                     .toGeneric()
                                     .map(Generic::getExtends)
                                     .orElseThrow(IllegalStateException::new);
                               assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolatedDependent);
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java",
                                      "                           public class InterpolateGenericsExample <A extends Comparable<B>, B extends Comparable<A>> {\n" +
                                      "                                static class IndependentGeneric<C> {}\n" +
                                      "                                static class DependentGeneric<D extends E, E> {}\n" +
                                      "                             }")
                   .compile();
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(shadowApi.getClassOrThrow("java.lang.Long")
                                                   .isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number")));
                               assertTrue(shadowApi.getClassOrThrow("java.lang.Long").isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Long")));
                               assertFalse(shadowApi.getClassOrThrow("java.lang.Number")
                                                    .isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Long")));
                            })
                   .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               //same as isSubtypeOF
                               assertTrue(shadowApi.getClassOrThrow("java.lang.Long")
                                                   .isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Number")));
                               assertTrue(shadowApi.getClassOrThrow("java.lang.Long")
                                                   .isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Long")));
                               assertFalse(shadowApi.getClassOrThrow("java.lang.Number")
                                                    .isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Long")));

                               assertTrue(shadowApi.getClassOrThrow("java.lang.Integer")
                                                   .isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                            })
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(Collections.singletonList(shadowApi.getClassOrThrow("java.lang.Number")),
                                            shadowApi.getClassOrThrow(
                                                           "ClassParent")
                                                     .getDirectSuperTypes());

                               assertEquals(Arrays.asList(shadowApi.getClassOrThrow("java.lang.Number"),
                                                          shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                          shadowApi.getInterfaceOrThrow("java.util.function.Consumer")),
                                            shadowApi.getClassOrThrow(
                                                           "ClassMixedParent")
                                                     .getDirectSuperTypes());
                            })
                   .withCodeToCompile("ClassParent.java", "abstract class ClassParent extends Number {}")
                   .withCodeToCompile("ClassMixedParent.java",
                                      "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                   .compile();
   }

   @Test
   @Override
   void testGetSuperTypes()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertEquals(Collections.emptySet(),
                                            shadowApi.getClassOrThrow("java.lang.Object").getSuperTypes());

                               assertEquals(new HashSet<>(Arrays.asList(shadowApi.getClassOrThrow("java.lang.Object"),
                                                                        shadowApi.getClassOrThrow("java.lang.Number"),
                                                                        shadowApi.getInterfaceOrThrow("java.io.Serializable"))),
                                            shadowApi.getClassOrThrow("ClassParent").getSuperTypes());

                               assertEquals(new HashSet<>(Arrays.asList(shadowApi.getClassOrThrow("java.lang.Object"),
                                                                        shadowApi.getClassOrThrow("java.lang.Number"),
                                                                        shadowApi.getInterfaceOrThrow("java.io.Serializable"),
                                                                        shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                                        shadowApi.getInterfaceOrThrow("java.util.function.Consumer"))),
                                            shadowApi.getClassOrThrow("ClassMixedParent").getSuperTypes());
                            })
                   .withCodeToCompile("ClassNoParent.java", "class ClassNoParent {}")
                   .withCodeToCompile("ClassParent.java", "abstract class ClassParent extends Number {}")
                   .withCodeToCompile("ClassMixedParent.java",
                                      "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                   .compile();
   }
}
