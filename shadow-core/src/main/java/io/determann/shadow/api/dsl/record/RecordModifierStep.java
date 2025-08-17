package io.determann.shadow.api.dsl.record;

import io.determann.shadow.api.Modifier;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public interface RecordModifierStep
      extends RecordNameStep
{
   @Contract(value = "_ -> new", pure = true)
   RecordModifierStep modifier(String... modifiers);

   @Contract(value = "_ -> new", pure = true)
   default RecordModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   @Contract(value = "_ -> new", pure = true)
   RecordModifierStep modifier(Set<Modifier> modifiers);

   @Contract(value = " -> new", pure = true)
   RecordModifierStep public_();

   @Contract(value = " -> new", pure = true)
   RecordModifierStep protected_();

   @Contract(value = " -> new", pure = true)
   RecordModifierStep private_();

   @Contract(value = " -> new", pure = true)
   RecordModifierStep final_();

   @Contract(value = " -> new", pure = true)
   RecordModifierStep static_();

   @Contract(value = " -> new", pure = true)
   RecordModifierStep strictfp_();
}
