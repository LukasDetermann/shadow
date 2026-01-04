package io.determann.shadow.api.annotation_processing.dsl.interface_;

import io.determann.shadow.api.annotation_processing.Modifier;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public interface InterfaceModifierStep
      extends InterfaceNameStep
{
   @Contract(value = "_ -> new", pure = true)
   InterfaceModifierStep modifier(String... modifiers);

   @Contract(value = "_ -> new", pure = true)
   default InterfaceModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   @Contract(value = "_ -> new", pure = true)
   InterfaceModifierStep modifier(Set<Modifier> modifiers);

   @Contract(value = " -> new", pure = true)
   InterfaceModifierStep abstract_();

   @Contract(value = " -> new", pure = true)
   InterfaceModifierStep public_();

   @Contract(value = " -> new", pure = true)
   InterfaceModifierStep protected_();

   @Contract(value = " -> new", pure = true)
   InterfaceModifierStep private_();

   @Contract(value = " -> new", pure = true)
   InterfaceModifierStep sealed();

   @Contract(value = " -> new", pure = true)
   InterfaceModifierStep nonSealed();

   @Contract(value = " -> new", pure = true)
   InterfaceModifierStep static_();

   @Contract(value = " -> new", pure = true)
   InterfaceModifierStep strictfp_();
}
