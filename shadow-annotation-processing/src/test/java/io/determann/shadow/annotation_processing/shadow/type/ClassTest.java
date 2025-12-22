package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClassTest
{
   @Test
   void withGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               assertThrows(IllegalArgumentException.class,
                                            () -> context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                         .withGenerics("java.lang.String"));

                               assertThrows(IllegalArgumentException.class,
                                            () -> context.getClassOrThrow("java.lang.String").withGenerics("java.lang.String"));

                               assertEquals(context.getClassOrThrow("java.lang.String"),
                                            context.getClassOrThrow("WithGenericsExample.Inner").withGenerics("java.lang.String")
                                                   .getGenericUsages()
                                                   .get(0));

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String")),
                                            context.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                   .withGenerics("java.lang.String")
                                                   .getGenericUsages());

                               assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                    context.getClassOrThrow("java.lang.Number")),
                                            context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                   .withGenerics("java.lang.String",
                                                                 "java.lang.Number")
                                                   .getGenericUsages());
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
   void interpolateGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class declared = context.getClassOrThrow("InterpolateGenericsExample")
                                                          .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                        context.getConstants().getUnboundWildcard());

                               Ap.Class capture = declared.interpolateGenerics();
                               Ap.Type interpolated = Optional.of((Ap.Generic) capture.getGenericUsages().get(1))
                                                              .map(Ap.Generic::getBound)
                                                              .map(Ap.Interface.class::cast)
                                                              .map(Ap.Interface::getGenericUsages)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               Ap.Class independentExample = context.getClassOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                    .withGenerics(context.getConstants().getUnboundWildcard());

                               Ap.Class independentCapture = independentExample.interpolateGenerics();
                               Ap.Type interpolatedIndependent = Optional.of(((Ap.Generic) independentCapture.getGenericUsages().get(0)))
                                                                         .map(Ap.Generic::getBound)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               Ap.Class dependentExample = context.getClassOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                  .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                context.getClassOrThrow("java.lang.String"));

                               Ap.Class dependentCapture = dependentExample.interpolateGenerics();
                               Ap.Type interpolatedDependent = Optional.of(((Ap.Generic) dependentCapture.getGenericUsages().get(0)))
                                                                       .map(Ap.Generic::getBound)
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
   void getSuperClass()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class integer = context.getClassOrThrow("java.lang.Integer");
                               Ap.Class number = context.getClassOrThrow("java.lang.Number");
                               assertEquals(integer.getSuperClass(), number);
                            })
                   .compile();
   }

   @Test
   void getSuperClassOfObject()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               assertNull(object.getSuperClass());
                            })
                   .compile();
   }

   @Test
   void getPermittedSubClasses()
   {
      ProcessorTest.process(context ->
                            {
                               List<Ap.Class> expected = List.of(context.getClassOrThrow("PermittedSubClassesExample.Child"));

                               Ap.Class permittedSubClassesExample = context.getClassOrThrow("PermittedSubClassesExample");
                               assertEquals(expected, permittedSubClassesExample.getPermittedSubClasses());
                            })
                   .withCodeToCompile("PermittedSubClassesExample.java", """
                                                                         public sealed class PermittedSubClassesExample permits PermittedSubClassesExample.Child
                                                                         {
                                                                            public static final class Child extends PermittedSubClassesExample {}
                                                                         }
                                                                         """)
                   .compile();
   }

   @Test
   void getDirectInterfaces()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class child = context.getClassOrThrow("DirectInterfacesExample.Child");
                               List<Ap.Interface> directInterfaces = child.getDirectInterfaces();

                               assertEquals(1, directInterfaces.size());
                               assertEquals(context.getDeclaredOrThrow("Direct"), directInterfaces.get(0));
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
   void isSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class declaredLong = context.getClassOrThrow("java.lang.Long");
                               Ap.Class number = context.getClassOrThrow("java.lang.Number");

                               assertTrue(declaredLong.isSubtypeOf(number));
                               assertTrue(declaredLong.isSubtypeOf(declaredLong));
                               assertFalse(number.isSubtypeOf(declaredLong));
                            })
                   .compile();
   }

   @Test
   void isAssignableFrom()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class declaredLong = context.getClassOrThrow("java.lang.Long");
                               Ap.Class integer = context.getClassOrThrow("java.lang.Integer");
                               Ap.Class number = context.getClassOrThrow("java.lang.Number");

                               assertTrue(declaredLong.isAssignableFrom(number));
                               assertTrue(declaredLong.isAssignableFrom(declaredLong));
                               assertFalse(number.isAssignableFrom(declaredLong));

                               assertTrue(integer.isAssignableFrom(context.getConstants().getPrimitiveInt()));
                            })
                   .compile();
   }

   @Test
   void iGetDirectSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class number = context.getClassOrThrow("java.lang.Number");
                               Ap.Class classParent = context.getClassOrThrow("ClassParent");
                               Ap.Interface comparable = context.getInterfaceOrThrow("java.lang.Comparable");
                               Ap.Interface consumer = context.getInterfaceOrThrow("java.util.function.Consumer");
                               Ap.Class classMixedParent = context.getClassOrThrow("ClassMixedParent");

                               assertEquals(List.of(number), classParent.getDirectSuperTypes());

                               assertEquals(List.of(number, comparable, consumer), classMixedParent.getDirectSuperTypes());
                            })
                   .withCodeToCompile("ClassParent.java", "abstract class ClassParent extends Number {}")
                   .withCodeToCompile("ClassMixedParent.java",
                                      "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                   .compile();
   }

   @Test
   void getSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               Ap.Class number = context.getClassOrThrow("java.lang.Number");
                               Ap.Interface serializable = context.getInterfaceOrThrow("java.io.Serializable");
                               Ap.Interface comparable = context.getInterfaceOrThrow("java.lang.Comparable");
                               Ap.Interface consumer = context.getInterfaceOrThrow("java.util.function.Consumer");

                               assertEquals(Collections.emptySet(), object.getSuperTypes());

                               Ap.Class mixedParent = context.getClassOrThrow("ClassMixedParent");
                               assertEquals(Set.of(object, number, serializable, comparable, consumer), mixedParent.getSuperTypes());
                            })
                   .withCodeToCompile("ClassNoParent.java", "class ClassNoParent {}")
                   .withCodeToCompile("ClassParent.java", "abstract class ClassParent extends Number {}")
                   .withCodeToCompile("ClassMixedParent.java",
                                      "abstract class ClassMixedParent extends Number implements java.lang.Comparable<ClassMixedParent>, java.util.function.Consumer<ClassMixedParent> {}")
                   .compile();
   }

   @Test
   void getSurounding()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Declared inner = context.getDeclaredOrThrow("Outer.Inner");
                               Ap.Declared outer = inner.getSurrounding().orElseThrow();
                               assertEquals(context.getDeclaredOrThrow("Outer"), outer);
                            })
                   .withCodeToCompile("Outer.java", """
                                                    public class Outer {
                                                         static class Inner {}
                                                      }
                                                    """)
                   .compile();
   }
}
