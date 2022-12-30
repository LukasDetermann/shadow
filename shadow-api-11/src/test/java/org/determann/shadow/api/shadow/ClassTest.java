package org.determann.shadow.api.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.converter.ShadowConverter;
import org.determann.shadow.api.test.CompilationTest;
import org.determann.shadow.api.wrapper.Property;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.determann.shadow.api.ShadowApi.convert;
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
      CompilationTest.process(shadowApi ->
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
      CompilationTest.process(shadowApi ->
                              {
                                 List<Property> properties = shadowApi.getClassOrThrow("PropertiesExample").getProperties();

                                 assertEquals(1, properties.size());

                                 Property id = properties.get(0);
                                 assertEquals("id", id.getField().getSimpleName());
                                 assertEquals(shadowApi.getConstants().getPrimitiveInt(), id.getField().getType());

                                 assertEquals("getId", id.getGetter().getSimpleName());
                                 assertEquals(shadowApi.getConstants().getPrimitiveInt(), id.getGetter().getReturnType());
                                 assertEquals(Collections.emptyList(), id.getGetter().getParameters());

                                 assertEquals("setId", id.getSetterOrThrow().getSimpleName());
                                 assertEquals(shadowApi.getConstants().getVoid(), id.getSetterOrThrow().getReturnType());
                                 assertEquals(List.of(shadowApi.getConstants().getPrimitiveInt()), id.getSetterOrThrow().getParameterTypes());
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
                                 assertEquals(shadowApi.getClassOrThrow("OuterTypeExample"),
                                              shadowApi.getClassOrThrow("OuterTypeExample.InnerClass").getOuterType().orElseThrow());

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
      CompilationTest.process(shadowApi ->
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertThrows(IllegalArgumentException.class,
                                              () -> shadowApi.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                             .withGenerics("java.lang.String"));

                                 assertThrows(IllegalArgumentException.class,
                                              () -> shadowApi.getClassOrThrow("java.lang.String").withGenerics("java.lang.String"));

                                 assertEquals(shadowApi.getClassOrThrow("java.lang.String"),
                                              shadowApi.getClassOrThrow("WithGenericsExample.Inner")
                                                       .withGenerics("java.lang.String")
                                                       .getGenerics()
                                                       .get(0));

                                 assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.String")),
                                              shadowApi.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                       .withGenerics("java.lang.String")
                                                       .getGenerics());

                                 assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.String"), shadowApi.getClassOrThrow("java.lang.Number")),
                                              shadowApi.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
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
                                 Class declared = shadowApi.getClassOrThrow("InterpolateGenericsExample")
                                                           .withGenerics(shadowApi.getClassOrThrow("java.lang.String"),
                                                                         shadowApi.getConstants().getUnboundWildcard());
                                 Class capture = declared.interpolateGenerics();
                                 Shadow<TypeMirror> interpolated = convert(capture.getGenerics().get(1))
                                                                            .toGeneric()
                                                                            .map(Generic::getExtends)
                                                                            .map(ShadowApi::convert)
                                                                            .flatMap(ShadowConverter::toInterface)
                                                                            .map(Interface::getGenerics)
                                                                            .map(shadows -> shadows.get(0))
                                                                            .orElseThrow();
                                 assertEquals(shadowApi.getClassOrThrow("java.lang.String"), interpolated);

                                 Class independentExample = shadowApi.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                     .withGenerics(shadowApi.getConstants().getUnboundWildcard());
                                 Class independentCapture = independentExample.interpolateGenerics();
                                 Shadow<TypeMirror> interpolatedIndependent = convert(independentCapture.getGenerics().get(0))
                                                                                       .toGeneric()
                                                                                       .map(Generic::getExtends)
                                                                                       .orElseThrow();
                                 assertEquals(shadowApi.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                                 Class dependentExample = shadowApi.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                   .withGenerics(shadowApi.getConstants().getUnboundWildcard(),
                                                                                 shadowApi.getClassOrThrow("java.lang.String"));
                                 Class dependentCapture = dependentExample.interpolateGenerics();
                                 Shadow<TypeMirror> interpolatedDependent = convert(dependentCapture.getGenerics().get(0))
                                                                                     .toGeneric()
                                                                                     .map(Generic::getExtends)
                                                                                     .orElseThrow();
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.getClassOrThrow("java.lang.Long").isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Number")));
                                 assertTrue(shadowApi.getClassOrThrow("java.lang.Long").isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Long")));
                                 assertFalse(shadowApi.getClassOrThrow("java.lang.Number").isSubtypeOf(shadowApi.getClassOrThrow("java.lang.Long")));
                              })
                     .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 //same as isSubtypeOF
                                 assertTrue(shadowApi.getClassOrThrow("java.lang.Long").isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Number")));
                                 assertTrue(shadowApi.getClassOrThrow("java.lang.Long").isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Long")));
                                 assertFalse(shadowApi.getClassOrThrow("java.lang.Number").isAssignableFrom(shadowApi.getClassOrThrow("java.lang.Long")));

                                 assertTrue(shadowApi.getClassOrThrow("java.lang.Integer")
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
                                 assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Number")),
                                              shadowApi.getClassOrThrow(
                                                             "ClassParent")
                                                       .getDirectSuperTypes());

                                 assertEquals(List.of(shadowApi.getClassOrThrow("java.lang.Number"),
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
      CompilationTest.process(shadowApi ->
                              {
                                 assertEquals(Set.of(),
                                              shadowApi.getClassOrThrow("java.lang.Object").getSuperTypes());

                                 assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                     shadowApi.getClassOrThrow("java.lang.Number"),
                                                     shadowApi.getInterfaceOrThrow("java.io.Serializable")),
                                              shadowApi.getClassOrThrow("ClassParent").getSuperTypes());

                                 assertEquals(Set.of(shadowApi.getClassOrThrow("java.lang.Object"),
                                                     shadowApi.getClassOrThrow("java.lang.Number"),
                                                     shadowApi.getInterfaceOrThrow("java.io.Serializable"),
                                                     shadowApi.getInterfaceOrThrow("java.lang.Comparable"),
                                                     shadowApi.getInterfaceOrThrow("java.util.function.Consumer")),
                                              shadowApi.getClassOrThrow("ClassMixedParent").getSuperTypes());
                              })
                     .withCodeToCompile("ClassNoParent.java", "class ClassNoParent {}")
                     .withCodeToCompile("ClassParent.java", "abstract class ClassParent extends Number {}")
                     .withCodeToCompile("ClassMixedParent.java",
                                        "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                     .compile();
   }
}
