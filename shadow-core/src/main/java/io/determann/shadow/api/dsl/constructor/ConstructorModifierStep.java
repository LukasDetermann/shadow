package io.determann.shadow.api.dsl.constructor;

import io.determann.shadow.api.Modifier;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public interface ConstructorModifierStep
      extends ConstructorGenericStep
{
   @Contract(value = "_ -> new", pure = true)
   ConstructorModifierStep modifier(String... modifiers);

   @Contract(value = "_ -> new", pure = true)
   default ConstructorModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   @Contract(value = "_ -> new", pure = true)
   ConstructorModifierStep modifier(Set<Modifier> modifiers);

   @Contract(value = " -> new", pure = true)
   ConstructorModifierStep public_();

   @Contract(value = " -> new", pure = true)
   ConstructorModifierStep protected_();

   @Contract(value = " -> new", pure = true)
   ConstructorModifierStep private_();
}
