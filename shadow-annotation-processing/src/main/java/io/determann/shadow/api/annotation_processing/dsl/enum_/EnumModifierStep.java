package io.determann.shadow.api.annotation_processing.dsl.enum_;

import io.determann.shadow.api.annotation_processing.Modifier;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public interface EnumModifierStep
      extends EnumNameStep
{
   @Contract(value = "_ -> new", pure = true)
   EnumModifierStep modifier(String... modifiers);

   @Contract(value = "_ -> new", pure = true)
   default EnumModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   @Contract(value = "_ -> new", pure = true)
   EnumModifierStep modifier(Set<Modifier> modifiers);

   @Contract(value = " -> new", pure = true)
   EnumModifierStep public_();

   @Contract(value = " -> new", pure = true)
   EnumModifierStep protected_();

   @Contract(value = " -> new", pure = true)
   EnumModifierStep private_();

   @Contract(value = " -> new", pure = true)
   EnumModifierStep static_();

   @Contract(value = " -> new", pure = true)
   EnumModifierStep strictfp_();
}
