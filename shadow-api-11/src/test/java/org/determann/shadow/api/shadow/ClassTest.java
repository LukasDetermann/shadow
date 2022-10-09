package org.determann.shadow.api.shadow;

import org.determann.shadow.api.converter.ShadowConverter;
import org.determann.shadow.api.test.CompilationTest;
import org.determann.shadow.api.wrapper.Property;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClassTest extends DeclaredTest<Class>
{
   ClassTest()
   {
      super(shadowApi -> shadowApi.getClass("java.lang.Object"));
   }

   @Test
   void getSuperClassTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Class integer = shadowApi.getClass("java.lang.Integer");
                                 Declared number = shadowApi.getClass("java.lang.Number");
                                 assertEquals(integer.getSuperClass(), number);

                                 Declared object = shadowApi.getClass("java.lang.Object");
                                 assertNull(shadowApi.convert(object).toClass().getSuperClass());

                                 assertEquals(object, shadowApi.convert(number).toClass().getSuperClass());
                              })
                     .compile();
   }

   @Test
   void getPropertiesTest()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 List<Property> properties = shadowApi.getClass("PropertiesExample").getProperties();

                                 assertEquals(1, properties.size());

                                 Property id = properties.get(0);
                                 assertEquals("id", id.getField().getSimpleName());
                                 assertEquals(shadowApi.getConstants().getPrimitiveInt(), id.getField().getType());

                                 assertEquals("getId", id.getGetter().getSimpleName());
                                 assertEquals(shadowApi.getConstants().getPrimitiveInt(), id.getGetter().getReturnType());
                                 assertEquals(Collections.emptyList(), id.getGetter().getParameters());

                                 assertEquals("setId", id.getSetter().getSimpleName());
                                 assertEquals(shadowApi.getConstants().getVoid(), id.getSetter().getReturnType());
                                 assertEquals(List.of(shadowApi.getConstants().getPrimitiveInt()), id.getSetter().getParameterTypes());
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(shadowApi.getClass("OuterTypeExample"),
                                              shadowApi.getClass("OuterTypeExample.InnerClass").getOuterType().orElseThrow());

                                 assertEquals(Optional.empty(),
                                              shadowApi.getClass("OuterTypeExample").getOuterType());

                                 assertEquals(Optional.empty(),
                                              shadowApi.getClass("OuterTypeExample.StaticInnerClass").getOuterType());
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
      CompilationTest.process(shadowApi ->
                              {
                                 List<Interface> directInterfaces = shadowApi.getClass("DirectInterfacesExample.Child")
                                                                             .getDirectInterfaces();

                                 assertEquals(1, directInterfaces.size());
                                 assertEquals(shadowApi.getInterface("Direct"), directInterfaces.get(0));
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertThrows(IllegalArgumentException.class,
                                              () -> shadowApi.getClass("InterpolateGenericsExample.DependentGeneric")
                                                             .withGenerics("java.lang.String"));

                                 assertThrows(IllegalArgumentException.class,
                                              () -> shadowApi.getClass("java.lang.String").withGenerics("java.lang.String"));

                                 assertEquals(shadowApi.getClass("java.lang.String"),
                                              shadowApi.getClass("WithGenericsExample.Inner")
                                                       .withGenerics("java.lang.String")
                                                       .getGenerics()
                                                       .get(0));

                                 assertEquals(List.of(shadowApi.getClass("java.lang.String")),
                                              shadowApi.getClass("InterpolateGenericsExample.IndependentGeneric")
                                                       .withGenerics("java.lang.String")
                                                       .getGenerics());

                                 assertEquals(List.of(shadowApi.getClass("java.lang.String"), shadowApi.getClass("java.lang.Number")),
                                              shadowApi.getClass("InterpolateGenericsExample.DependentGeneric")
                                                       .withGenerics("java.lang.String", "java.lang.Number")
                                                       .getGenerics());
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
      CompilationTest.process(shadowApi ->
                              {
                                 Class declared = shadowApi.getClass("InterpolateGenericsExample")
                                                           .withGenerics(shadowApi.getClass("java.lang.String"),
                                                                         shadowApi.getConstants().getUnboundWildcard());
                                 Class capture = declared.interpolateGenerics();
                                 Shadow<TypeMirror> interpolated = shadowApi.convert(capture.getGenerics().get(1))
                                                                            .toOptionalGeneric()
                                                                            .map(Generic::getExtends)
                                                                            .map(shadowApi::convert)
                                                                            .flatMap(ShadowConverter::toOptionalInterface)
                                                                            .map(Interface::getGenerics)
                                                                            .map(shadows -> shadows.get(0))
                                                                            .orElseThrow();
                                 assertEquals(shadowApi.getClass("java.lang.String"), interpolated);

                                 Class independentExample = shadowApi.getClass("InterpolateGenericsExample.IndependentGeneric")
                                                                     .withGenerics(shadowApi.getConstants().getUnboundWildcard());
                                 Class independentCapture = independentExample.interpolateGenerics();
                                 Shadow<TypeMirror> interpolatedIndependent = shadowApi.convert(independentCapture.getGenerics().get(0))
                                                                                       .toOptionalGeneric()
                                                                                       .map(Generic::getExtends)
                                                                                       .orElseThrow();
                                 assertEquals(shadowApi.getClass("java.lang.Object"), interpolatedIndependent);

                                 Class dependentExample = shadowApi.getClass("InterpolateGenericsExample.DependentGeneric")
                                                                   .withGenerics(shadowApi.getConstants().getUnboundWildcard(),
                                                                                 shadowApi.getClass("java.lang.String"));
                                 Class dependentCapture = dependentExample.interpolateGenerics();
                                 Shadow<TypeMirror> interpolatedDependent = shadowApi.convert(dependentCapture.getGenerics().get(0))
                                                                                     .toOptionalGeneric()
                                                                                     .map(Generic::getExtends)
                                                                                     .orElseThrow();
                                 assertEquals(shadowApi.getClass("java.lang.String"), interpolatedDependent);
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getClass("java.lang.Long").isSubtypeOf(shadowApi.getClass("java.lang.Number")));
                                 assertTrue(shadowApi.getClass("java.lang.Long").isSubtypeOf(shadowApi.getClass("java.lang.Long")));
                                 assertFalse(shadowApi.getClass("java.lang.Number").isSubtypeOf(shadowApi.getClass("java.lang.Long")));
                              })
                     .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 //same as isSubtypeOF
                                 assertTrue(shadowApi.getClass("java.lang.Long").isAssignableFrom(shadowApi.getClass("java.lang.Number")));
                                 assertTrue(shadowApi.getClass("java.lang.Long").isAssignableFrom(shadowApi.getClass("java.lang.Long")));
                                 assertFalse(shadowApi.getClass("java.lang.Number").isAssignableFrom(shadowApi.getClass("java.lang.Long")));

                                 assertTrue(shadowApi.getClass("java.lang.Integer")
                                                     .isAssignableFrom(shadowApi.getConstants().getPrimitiveInt()));
                              })
                     .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(List.of(shadowApi.getClass("java.lang.Number")),
                                              shadowApi.getClass(
                                                             "ClassParent")
                                                       .getDirectSuperTypes());

                                 assertEquals(List.of(shadowApi.getClass("java.lang.Number"),
                                                      shadowApi.getInterface("java.lang.Comparable"),
                                                      shadowApi.getInterface("java.util.function.Consumer")),
                                              shadowApi.getClass(
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(Set.of(),
                                              shadowApi.getClass("java.lang.Object").getSuperTypes());

                                 assertEquals(Set.of(shadowApi.getClass("java.lang.Object"),
                                                     shadowApi.getClass("java.lang.Number"),
                                                     shadowApi.getInterface("java.io.Serializable")),
                                              shadowApi.getClass("ClassParent").getSuperTypes());

                                 assertEquals(Set.of(shadowApi.getClass("java.lang.Object"),
                                                     shadowApi.getClass("java.lang.Number"),
                                                     shadowApi.getInterface("java.io.Serializable"),
                                                     shadowApi.getInterface("java.lang.Comparable"),
                                                     shadowApi.getInterface("java.util.function.Consumer")),
                                              shadowApi.getClass("ClassMixedParent").getSuperTypes());
                              })
                     .withCodeToCompile("ClassNoParent.java", "class ClassNoParent {}")
                     .withCodeToCompile("ClassParent.java", "abstract class ClassParent extends Number {}")
                     .withCodeToCompile("ClassMixedParent.java",
                                        "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                     .compile();
   }
}
