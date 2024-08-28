package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.LM_QualifiedNameable;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Property;
import io.determann.shadow.api.lang_model.shadow.type.LM_Class;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Interface;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.api.shadow.type.C_Class;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClassTest extends DeclaredTest<C_Class>
{
   ClassTest()
   {
      super(context -> context.getClassOrThrow("java.lang.Object"));
   }

   @Test
   void getSuperClassTest()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Class integer = context.getClassOrThrow("java.lang.Integer");
                               LM_Class number = context.getClassOrThrow("java.lang.Number");
                               assertEquals(integer.getSuperClass(), number);

                               LM_Class object = context.getClassOrThrow("java.lang.Object");
                               assertNull(object.getSuperClass());

                               assertEquals(object, number.getSuperClass());
                            })
                   .compile();
   }

   @Test
   void getPermittedSubClassesTest()
   {
      ProcessorTest.process(context -> assertEquals(List.of("PermittedSubClassesExample.Child"),
                                                      context.getClassOrThrow("PermittedSubClassesExample")
                                                               .getPermittedSubClasses()
                                                               .stream()
                                                               .map(LM_QualifiedNameable::getQualifiedName)
                                                               .toList()))
                   .withCodeToCompile("PermittedSubClassesExample.java", """
                         public sealed class PermittedSubClassesExample permits PermittedSubClassesExample.Child
                         {
                            public static final class Child extends PermittedSubClassesExample {}
                         }
                         """)
                   .compile();
   }

   @Test
   void getPropertiesTest()
   {
      ProcessorTest.process(context ->
                            {
                               List<LM_Property> properties = context.getClassOrThrow("PropertiesExample").getProperties();

                               assertEquals(2, properties.size());

                               LM_Property id = properties.get(1);
                               assertEquals("id", id.getField().get().getName());
                               assertEquals(context.getConstants().getPrimitiveInt(), id.getField().get().getType());

                               assertEquals("getId", id.getGetter().getName());
                               assertEquals(context.getConstants().getPrimitiveInt(), id.getGetter().getReturnType());
                               assertEquals(Collections.emptyList(), id.getGetter().getParameters());

                               assertEquals("setId", id.getSetterOrThrow().getName());
                               assertEquals(context.getConstants().getVoid(), id.getSetterOrThrow().getReturnType());
                               assertEquals(List.of(context.getConstants().getPrimitiveInt()), id.getSetterOrThrow().getParameterTypes());
                            })
                   .withCodeToCompile("PropertiesExample.java", """
                         public class PropertiesExample {
                            private int id;

                            public int getId() {return id;}
                            public void setId(int id) {this.id = id;}
                         }
                         """)
                   .compile();
   }

   @Test
   void testGetOuterType()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(context.getClassOrThrow("OuterTypeExample"),
                                            context.getClassOrThrow("OuterTypeExample.InnerClass").getOuterType().orElseThrow());

                               assertEquals(Optional.empty(),
                                            context.getClassOrThrow("OuterTypeExample").getOuterType());

                               assertEquals(Optional.empty(),
                                            context.getClassOrThrow("OuterTypeExample.StaticInnerClass").getOuterType());
                            })
                   .withCodeToCompile("OuterTypeExample.java", """
                         public class OuterTypeExample {
                            private class InnerClass {}
                            private static class StaticInnerClass {}
                         }
                         """)
                   .compile();
   }

   @Test
   void getDirectInterfacesTest()
   {
      ProcessorTest.process(context ->
                            {
                               List<LM_Interface> directInterfaces = context.getClassOrThrow("DirectInterfacesExample.Child")
                                                                            .getDirectInterfaces();

                               assertEquals(1, directInterfaces.size());
                               assertEquals(context.getInterfaceOrThrow("Direct"), directInterfaces.get(0));
                            })
                   .withCodeToCompile("Direct.java", "public interface Direct{}")
                   .withCodeToCompile("InDirect.java", "public interface InDirect{}")
                   .withCodeToCompile("DirectInterfacesExample.java", """
                         public class DirectInterfacesExample implements InDirect {
                            public static class Child extends DirectInterfacesExample implements Direct {}
                         }
                         """)
                   .compile();
   }

   @Test
   void testWithGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> context.withGenerics(context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                         "java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> context.withGenerics(context.getClassOrThrow("java.lang.String"), "java.lang.String"));

                               assertEquals(context.getClassOrThrow("java.lang.String"),
                                            context.withGenerics(context.getClassOrThrow("WithGenericsExample.Inner"), "java.lang.String")
                                                     .getGenericTypes()
                                                     .get(0));

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String")),
                                            context.withGenerics(context.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric"),
                                                                   "java.lang.String")
                                                     .getGenericTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                    context.getClassOrThrow("java.lang.Number")),
                                            context.withGenerics(context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric"),
                                                                   "java.lang.String",
                                                                   "java.lang.Number")
                                                     .getGenericTypes());
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public class InterpolateGenericsExample <A extends java.lang.Comparable<B>, B extends java.lang.Comparable<A>> {
                              static class IndependentGeneric<C> {}
                              static class DependentGeneric<D extends E, E> {}
                           }
                          """)
                   .withCodeToCompile("WithGenericsExample.java", """
                         public class WithGenericsExample {
                            class Inner<T> {}
                         }
                         """)
                   .compile();
   }

   @Test
   void testInterpolateGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               LM_Class declared = context.withGenerics(context.getClassOrThrow("InterpolateGenericsExample"),
                                                                        context.getClassOrThrow("java.lang.String"),
                                                                        context.getConstants().getUnboundWildcard());
                               LM_Class capture = context.interpolateGenerics(declared);
                               LM_Type interpolated = Optional.of((LM_Generic) capture.getGenericTypes().get(1))
                                                              .map(LM_Generic::getExtends)
                                                              .map(LM_Interface.class::cast)
                                                              .map(LM_Interface::getGenericTypes)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               LM_Class independentExample = context.withGenerics(context.getClassOrThrow(
                                                                                       "InterpolateGenericsExample.IndependentGeneric"),
                                                                                  context.getConstants().getUnboundWildcard());
                               LM_Class independentCapture = context.interpolateGenerics(independentExample);
                               LM_Type interpolatedIndependent = Optional.of(((LM_Generic) independentCapture.getGenericTypes().get(0)))
                                                                         .map(LM_Generic::getExtends)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               LM_Class dependentExample = context.withGenerics(context.getClassOrThrow(
                                                                                     "InterpolateGenericsExample.DependentGeneric"),
                                                                                context.getConstants().getUnboundWildcard(),
                                                                                context.getClassOrThrow("java.lang.String"));
                               LM_Class dependentCapture = context.interpolateGenerics(dependentExample);
                               LM_Type interpolatedDependent = Optional.of(((LM_Generic) dependentCapture.getGenericTypes().get(0)))
                                                                       .map(LM_Generic::getExtends)
                                                                       .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolatedDependent);
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                         public class InterpolateGenericsExample <A extends Comparable<B>, B extends Comparable<A>> {
                              static class IndependentGeneric<C> {}
                              static class DependentGeneric<D extends E, E> {}
                           }
                          """)
                   .compile();
   }

   @Test
   @Override
   void testisSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               assertTrue(context.getClassOrThrow("java.lang.Long")
                                                .isSubtypeOf(context.getClassOrThrow("java.lang.Number")));

                               assertTrue(context.getClassOrThrow("java.lang.Long").isSubtypeOf(context.getClassOrThrow("java.lang.Long")));

                               assertFalse(context.getClassOrThrow("java.lang.Number")
                                                 .isSubtypeOf(context.getClassOrThrow("java.lang.Long")));
                            })
                   .compile();
   }

   @Test
   void testIsAssignableFrom()
   {
      ProcessorTest.process(context ->
                            {
                               //same as isSubtypeOF
                               assertTrue(context.getClassOrThrow("java.lang.Long")
                                                   .isAssignableFrom(context.getClassOrThrow("java.lang.Number")));
                               assertTrue(context.getClassOrThrow("java.lang.Long")
                                                   .isAssignableFrom(context.getClassOrThrow("java.lang.Long")));
                               assertFalse(context.getClassOrThrow("java.lang.Number")
                                                    .isAssignableFrom(context.getClassOrThrow("java.lang.Long")));

                               assertTrue(context.getClassOrThrow("java.lang.Integer")
                                                   .isAssignableFrom(context.getConstants().getPrimitiveInt()));
                            })
                   .compile();
   }

   @Test
   @Override
   void testGetDirectSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               assertEquals(List.of(context.getClassOrThrow("java.lang.Number")),
                                            context.getClassOrThrow("ClassParent")
                                                  .getDirectSuperTypes());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.Number"),
                                                    context.getInterfaceOrThrow("java.lang.Comparable"),
                                                    context.getInterfaceOrThrow("java.util.function.Consumer")),
                                            context.getClassOrThrow("ClassMixedParent")
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
      ProcessorTest.process(context ->
                            {
                               assertEquals(Set.of(),
                                            context.getClassOrThrow("java.lang.Object").getSuperTypes());

                               assertEquals(Set.of(context.getClassOrThrow("java.lang.Object"),
                                                   context.getClassOrThrow("java.lang.Number"),
                                                   context.getInterfaceOrThrow("java.io.Serializable")),
                                            context.getClassOrThrow("ClassParent").getSuperTypes());

                               assertEquals(Set.of(context.getClassOrThrow("java.lang.Object"),
                                                   context.getClassOrThrow("java.lang.Number"),
                                                   context.getInterfaceOrThrow("java.io.Serializable"),
                                                   context.getInterfaceOrThrow("java.lang.Comparable"),
                                                   context.getInterfaceOrThrow("java.util.function.Consumer")),
                                            context.getClassOrThrow("ClassMixedParent").getSuperTypes());
                            })
                   .withCodeToCompile("ClassNoParent.java", "class ClassNoParent {}")
                   .withCodeToCompile("ClassParent.java", "abstract class ClassParent extends Number {}")
                   .withCodeToCompile("ClassMixedParent.java",
                                      "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                   .compile();
   }
}
