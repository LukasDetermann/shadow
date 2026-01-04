package io.determann.shadow.api.annotation_processing.dsl.field;

import io.determann.shadow.api.annotation_processing.Modifier;
import org.jetbrains.annotations.Contract;

import java.util.Set;

public interface FieldModifierStep
      extends FieldTypeStep
{
   @Contract(value = "_ -> new", pure = true)
   FieldModifierStep modifier(String... modifiers);

   @Contract(value = "_ -> new", pure = true)
   default FieldModifierStep modifier(Modifier... modifiers)
   {
      return modifier(Set.of(modifiers));
   }

   @Contract(value = "_ -> new", pure = true)
   FieldModifierStep modifier(Set<Modifier> modifiers);

   @Contract(value = " -> new", pure = true)
   FieldModifierStep public_();

   @Contract(value = " -> new", pure = true)
   FieldModifierStep protected_();

   @Contract(value = " -> new", pure = true)
   FieldModifierStep private_();

   @Contract(value = " -> new", pure = true)
   FieldModifierStep final_();

   @Contract(value = " -> new", pure = true)
   FieldModifierStep static_();

   @Contract(value = " -> new", pure = true)
   FieldModifierStep strictfp_();

   @Contract(value = " -> new", pure = true)
   FieldModifierStep transient_();

   @Contract(value = " -> new", pure = true)
   FieldModifierStep volatile_();
}
