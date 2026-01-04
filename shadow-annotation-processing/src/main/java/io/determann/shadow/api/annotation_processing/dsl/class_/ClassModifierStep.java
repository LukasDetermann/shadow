package io.determann.shadow.api.annotation_processing.dsl.class_;

import io.determann.shadow.api.annotation_processing.Modifier;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public interface ClassModifierStep
      extends ClassNameStep
{
   @Contract(value = "_ -> new", pure = true)
   ClassModifierStep modifier(String... modifiers);

   @Contract(value = "_ -> new", pure = true)
   default ClassModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   @Contract(value = "_ -> new", pure = true)
   ClassModifierStep modifier(Set<Modifier> modifiers);

   @Contract(value = " -> new", pure = true)
   ClassModifierStep abstract_();

   @Contract(value = " -> new", pure = true)
   ClassModifierStep public_();

   @Contract(value = " -> new", pure = true)
   ClassModifierStep protected_();

   @Contract(value = " -> new", pure = true)
   ClassModifierStep private_();

   @Contract(value = " -> new", pure = true)
   ClassModifierStep final_();

   @Contract(value = " -> new", pure = true)
   ClassModifierStep sealed();

   @Contract(value = " -> new", pure = true)
   ClassModifierStep nonSealed();

   @Contract(value = " -> new", pure = true)
   ClassModifierStep static_();

   @Contract(value = " -> new", pure = true)
   ClassModifierStep strictfp_();
}
