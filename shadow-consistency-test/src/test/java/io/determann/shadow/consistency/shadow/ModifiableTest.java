package io.determann.shadow.consistency.shadow;

import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.reflection.ReflectionAdapter.generalize;
import static io.determann.shadow.api.shadow.Operations.MODIFIABLE_HAS_MODIFIER;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.api.shadow.modifier.Modifier.NON_SEALED;
import static io.determann.shadow.api.shadow.modifier.Modifier.SEALED;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModifiableTest
{
   @Test
   void sealed()
   {
      ConsistencyTest.compileTime(context -> context.getInterfaceOrThrow("Parent"))
                     .runtime(stringClassFunction -> generalize(stringClassFunction.apply("Parent")))
                     .withCode("Parent.java", "sealed interface Parent permits Child {}")
                     .withCode("Child.java", "non-sealed interface Child extends Parent {}")
                     .test(anInterface -> assertTrue(requestOrThrow(anInterface, MODIFIABLE_HAS_MODIFIER, SEALED)));
   }

   @Test
   void nonSealed()
   {
      ConsistencyTest.compileTime(context -> context.getInterfaceOrThrow("Child"))
                     .runtime(stringClassFunction -> generalize(stringClassFunction.apply("Child")))
                     .withCode("Parent.java", "sealed interface Parent permits Child {}")
                     .withCode("Child.java", "non-sealed interface Child extends Parent {}")
                     .test(anInterface -> assertTrue(requestOrThrow(anInterface, MODIFIABLE_HAS_MODIFIER, NON_SEALED)));
   }
}
