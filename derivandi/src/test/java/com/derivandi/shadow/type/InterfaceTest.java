package com.derivandi.shadow.type;

import com.derivandi.api.Ap;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static com.derivandi.api.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest
{
   @Test
   void withGenerics()
   {
      processorTest().withCodeToCompile("InterpolateGenericsExample.java", """
                                                                           public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                                                                              interface IndependentGeneric<C> {}
                                                                              interface DependentGeneric<D extends E, E> {}
                                                                           }
                                                                           """)
                     .process(context ->
                              {
                                 assertThrows(IllegalArgumentException.class,
                                              () -> context.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                           .withGenerics("java.lang.String"));

                                 assertThrows(IllegalArgumentException.class,
                                              () -> context.getInterfaceOrThrow("java.io.Serializable").withGenerics("java.io.Serializable"));

                                 assertEquals(List.of(context.getClassOrThrow("java.lang.String")),
                                              context.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                     .withGenerics("java.lang.String")
                                                     .getGenericUsages());

                                 assertEquals(List.of(context.getClassOrThrow("java.lang.String"),
                                                      context.getClassOrThrow("java.lang.Number")),
                                              context.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                     .withGenerics("java.lang.String",
                                                                   "java.lang.Number")
                                                     .getGenericUsages());
                              });
   }

   @Test
   void capture()
   {
      processorTest().withCodeToCompile("InterpolateGenericsExample.java", """
                                                                           public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                                                                              interface IndependentGeneric<C> {}
                                                                              interface DependentGeneric<D extends E, E> {}
                                                                           }
                                                                           """)
                     .process(context ->
                              {
                                 Ap.Interface declared = context.getInterfaceOrThrow("InterpolateGenericsExample")
                                                                .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                              context.getConstants().getUnboundWildcard());

                                 Ap.Interface capture = declared.capture();
                                 Ap.Type interpolated = Optional.of(((Ap.Generic) capture.getGenericUsages().get(1)))
                                                                .map(Ap.Generic::getBound)
                                                                .map(Ap.Interface.class::cast)
                                                                .map(Ap.Interface::getGenericUsages)
                                                                .map(types -> types.get(0))
                                                                .orElseThrow();
                                 assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                                 Ap.Interface independentExample = context.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                          .withGenerics(context.getConstants().getUnboundWildcard());

                                 Ap.Interface independentCapture = independentExample.capture();
                                 Ap.Type interpolatedIndependent = Optional.of(((Ap.Generic) independentCapture.getGenericUsages().get(0)))
                                                                           .map(Ap.Generic::getBound)
                                                                           .orElseThrow();
                                 assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                                 Ap.Interface dependentExample = context.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                        .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                      context.getClassOrThrow("java.lang.String"));

                                 Ap.Interface dependentCapture = dependentExample.capture();
                                 Ap.Type interpolatedDependent = Optional.of((Ap.Generic) dependentCapture.getGenericUsages().get(0))
                                                                         .map(Ap.Generic::getBound)
                                                                         .orElseThrow();
                                 assertEquals(context.getClassOrThrow("java.lang.String"), interpolatedDependent);
                              });
   }

   @Test
   void getDirectInterfaces()
   {
      processorTest().process(context ->
                              {
                                 Ap.Interface function = context.getInterfaceOrThrow("java.util.function.Function");
                                 Ap.Interface unaryOperator = context.getInterfaceOrThrow("java.util.function.UnaryOperator");

                                 assertEquals(0, function.getDirectInterfaces().size());

                                 assertEquals(List.of(function), unaryOperator.getDirectInterfaces());
                              });
   }

   @Test
   void isFunctional()
   {
      processorTest().process(context ->
                              {
                                 Predicate<String> isFunctional = name ->
                                 {
                                    Ap.Interface cInterface = context.getInterfaceOrThrow(name);
                                    return cInterface.isFunctional();
                                 };

                                 assertTrue(isFunctional.test("java.util.function.Function"));
                                 assertTrue(isFunctional.test("java.lang.Comparable"));
                                 assertFalse(isFunctional.test("java.util.List"));
                              });
   }

   @Test
   void isSubtypeOf()
   {
      processorTest().process(context ->
                              {
                                 Ap.Interface function = context.getInterfaceOrThrow("java.util.function.Function");
                                 Ap.Class object = context.getClassOrThrow("java.lang.Object");
                                 Ap.Class number = context.getClassOrThrow("java.lang.Number");

                                 assertTrue(function.isSubtypeOf(object));
                                 assertTrue(function.isSubtypeOf(function));
                                 assertFalse(function.isSubtypeOf(number));
                              });
   }

   @Test
   void getDirectSuperTypes()
   {
      processorTest().withCodeToCompile("DirektSuperTypeExample.java", """
                                                                       import java.util.function.Consumer;
                                                                       import java.util.function.Supplier;
                                                                       
                                                                       public class DirektSuperTypeExample {
                                                                          interface InterfaceNoParent {}
                                                                       
                                                                          interface InterfaceParent extends Comparable<InterfaceParent>,
                                                                                                            Consumer<InterfaceParent> {}
                                                                       }
                                                                       """)
                     .process(context ->
                              {
                                 Ap.Class object = context.getClassOrThrow("java.lang.Object");
                                 Ap.Interface comparable = context.getInterfaceOrThrow("java.lang.Comparable");
                                 Ap.Interface consumer = context.getInterfaceOrThrow("java.util.function.Consumer");

                                 Ap.Interface noParent = context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceNoParent");
                                 assertEquals(List.of(object), noParent.getDirectSuperTypes());

                                 Ap.Interface parent = context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceParent");
                                 assertEquals(List.of(object, comparable, consumer), parent.getDirectSuperTypes());
                              });
   }

   @Test
   void getSuperTypes()
   {
      processorTest().withCodeToCompile("DirektSuperTypeExample.java", """
                                                                       import java.util.function.Consumer;
                                                                       import java.util.function.Supplier;
                                                                       
                                                                       public class DirektSuperTypeExample {
                                                                          interface InterfaceNoParent {}
                                                                       
                                                                          interface InterfaceParent extends Comparable<InterfaceParent>,
                                                                                                            Consumer<InterfaceParent> {}
                                                                       }
                                                                       """)
                     .process(context ->
                              {
                                 Ap.Class object = context.getClassOrThrow("java.lang.Object");
                                 Ap.Interface comparable = context.getInterfaceOrThrow("java.lang.Comparable");
                                 Ap.Interface consumer = context.getInterfaceOrThrow("java.util.function.Consumer");

                                 Ap.Interface noParent = context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceNoParent");
                                 assertEquals(Set.of(object), noParent.getSuperTypes());

                                 Ap.Interface parent = context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceParent");
                                 assertEquals(Set.of(object, comparable, consumer), parent.getSuperTypes());
                              });
   }

   @Test
   void getSurounding()
   {
      processorTest().withCodeToCompile("Outer.java", """
                                                      public interface Outer {
                                                            interface Inner {}
                                                        }
                                                      """)
                     .process(context ->
                              {
                                 Ap.Declared inner = context.getDeclaredOrThrow("Outer.Inner");
                                 Ap.Declared outer = inner.getSurrounding().orElseThrow();
                                 assertEquals(context.getDeclaredOrThrow("Outer"), outer);
                              });
   }
}
