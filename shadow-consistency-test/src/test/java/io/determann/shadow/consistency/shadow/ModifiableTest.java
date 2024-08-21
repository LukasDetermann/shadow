package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.MODIFIABLE_HAS_MODIFIER;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.R_Adapter.generalize;
import static io.determann.shadow.api.shadow.modifier.C_Modifier.NON_SEALED;
import static io.determann.shadow.api.shadow.modifier.C_Modifier.SEALED;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModifiableTest
{
   @Test
   void sealed()
   {
      ConsistencyTest.<C_Interface>compileTime(context -> context.getInterfaceOrThrow("Parent"))
                     .runtime(stringClassFunction -> generalize(stringClassFunction.apply("Parent")))
                     .withCode("Parent.java", "sealed interface Parent permits Child {}")
                     .withCode("Child.java", "non-sealed interface Child extends Parent {}")
                     .test(anInterface -> assertTrue(requestOrThrow(anInterface, MODIFIABLE_HAS_MODIFIER, SEALED)));
   }

   @Test
   void nonSealed()
   {
      ConsistencyTest.<C_Interface>compileTime(context -> context.getInterfaceOrThrow("Child"))
                     .runtime(stringClassFunction -> generalize(stringClassFunction.apply("Child")))
                     .withCode("Parent.java", "sealed interface Parent permits Child {}")
                     .withCode("Child.java", "non-sealed interface Child extends Parent {}")
                     .test(anInterface -> assertTrue(requestOrThrow(anInterface, MODIFIABLE_HAS_MODIFIER, NON_SEALED)));
   }
}
