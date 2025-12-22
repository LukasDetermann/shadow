package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class InterfaceTest
{
   @Test
   void withGenerics()
   {
      ProcessorTest.process(context ->
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
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                                                                         public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                                                                            interface IndependentGeneric<C> {}
                                                                            interface DependentGeneric<D extends E, E> {}
                                                                         }
                                                                         """)
                   .compile();
   }

   @Test
   void interpolateGenerics()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Interface declared = context.getInterfaceOrThrow("InterpolateGenericsExample")
                                                              .withGenerics(context.getClassOrThrow("java.lang.String"),
                                                                            context.getConstants().getUnboundWildcard());

                               Ap.Interface capture = declared.interpolateGenerics();
                               Ap.Type interpolated = Optional.of(((Ap.Generic) capture.getGenericUsages().get(1)))
                                                              .map(Ap.Generic::getBound)
                                                              .map(Ap.Interface.class::cast)
                                                              .map(Ap.Interface::getGenericUsages)
                                                              .map(types -> types.get(0))
                                                              .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolated);

                               Ap.Interface independentExample = context.getInterfaceOrThrow("InterpolateGenericsExample.IndependentGeneric")
                                                                        .withGenerics(context.getConstants().getUnboundWildcard());

                               Ap.Interface independentCapture = independentExample.interpolateGenerics();
                               Ap.Type interpolatedIndependent = Optional.of(((Ap.Generic) independentCapture.getGenericUsages().get(0)))
                                                                         .map(Ap.Generic::getBound)
                                                                         .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.Object"), interpolatedIndependent);

                               Ap.Interface dependentExample = context.getInterfaceOrThrow("InterpolateGenericsExample.DependentGeneric")
                                                                      .withGenerics(context.getConstants().getUnboundWildcard(),
                                                                                    context.getClassOrThrow("java.lang.String"));

                               Ap.Interface dependentCapture = dependentExample.interpolateGenerics();
                               Ap.Type interpolatedDependent = Optional.of((Ap.Generic) dependentCapture.getGenericUsages().get(0))
                                                                       .map(Ap.Generic::getBound)
                                                                       .orElseThrow();
                               assertEquals(context.getClassOrThrow("java.lang.String"), interpolatedDependent);
                            })
                   .withCodeToCompile("InterpolateGenericsExample.java", """
                                                                         public interface InterpolateGenericsExample<A extends Comparable<B>, B extends Comparable<A>> {
                                                                            interface IndependentGeneric<C> {}
                                                                            interface DependentGeneric<D extends E, E> {}
                                                                         }
                                                                         """)
                   .compile();
   }

   @Test
   void getDirectInterfaces()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Interface function = context.getInterfaceOrThrow("java.util.function.Function");
                               Ap.Interface unaryOperator = context.getInterfaceOrThrow("java.util.function.UnaryOperator");

                               assertEquals(0, function.getDirectInterfaces().size());

                               assertEquals(List.of(function), unaryOperator.getDirectInterfaces());
                            })
                   .compile();
   }

   @Test
   void isFunctional()
   {
      ProcessorTest.process(context ->
                            {
                               Predicate<String> isFunctional = name ->
                               {
                                  Ap.Interface cInterface = context.getInterfaceOrThrow(name);
                                  return cInterface.isFunctional();
                               };

                               assertTrue(isFunctional.test("java.util.function.Function"));
                               assertTrue(isFunctional.test("java.lang.Comparable"));
                               assertFalse(isFunctional.test("java.util.List"));
                            })
                   .compile();
   }

   @Test
   void isSubtypeOf()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Interface function = context.getInterfaceOrThrow("java.util.function.Function");
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               Ap.Class number = context.getClassOrThrow("java.lang.Number");

                               assertTrue(function.isSubtypeOf(object));
                               assertTrue(function.isSubtypeOf(function));
                               assertFalse(function.isSubtypeOf(number));
                            })
                   .compile();
   }

   @Test
   void getDirectSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               Ap.Interface comparable = context.getInterfaceOrThrow("java.lang.Comparable");
                               Ap.Interface consumer = context.getInterfaceOrThrow("java.util.function.Consumer");

                               Ap.Interface noParent = context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceNoParent");
                               assertEquals(List.of(object), noParent.getDirectSuperTypes());

                               Ap.Interface parent = context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceParent");
                               assertEquals(List.of(object, comparable, consumer), parent.getDirectSuperTypes());
                            })
                   .withCodeToCompile("DirektSuperTypeExample.java", """
                                                                     import java.util.function.Consumer;
                                                                     import java.util.function.Supplier;
                                                                     
                                                                     public class DirektSuperTypeExample {
                                                                        interface InterfaceNoParent {}
                                                                     
                                                                        interface InterfaceParent extends Comparable<InterfaceParent>,
                                                                                                          Consumer<InterfaceParent> {}
                                                                     }
                                                                     """)
                   .compile();
   }

   @Test
   void getSuperTypes()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class object = context.getClassOrThrow("java.lang.Object");
                               Ap.Interface comparable = context.getInterfaceOrThrow("java.lang.Comparable");
                               Ap.Interface consumer = context.getInterfaceOrThrow("java.util.function.Consumer");

                               Ap.Interface noParent = context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceNoParent");
                               assertEquals(Set.of(object), noParent.getSuperTypes());

                               Ap.Interface parent = context.getInterfaceOrThrow("DirektSuperTypeExample.InterfaceParent");
                               assertEquals(Set.of(object, comparable, consumer), parent.getSuperTypes());
                            })
                   .withCodeToCompile("DirektSuperTypeExample.java", """
                                                                     import java.util.function.Consumer;
                                                                     import java.util.function.Supplier;
                                                                     
                                                                     public class DirektSuperTypeExample {
                                                                        interface InterfaceNoParent {}
                                                                     
                                                                        interface InterfaceParent extends Comparable<InterfaceParent>,
                                                                                                          Consumer<InterfaceParent> {}
                                                                     }
                                                                     """)
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
                                                    public interface Outer {
                                                          interface Inner {}
                                                      }
                                                    """)
                   .compile();
   }
}
