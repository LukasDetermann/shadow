package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.modifier.Modifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public class ModifierRendererImpl
{
   private static final Map<Modifier, String> MODIFIERS_IN_ORDER = new LinkedHashMap<>();

   static
   {
      //https://cr.openjdk.org/~alundblad/styleguide/index-v6.html#toc-modifiers
      MODIFIERS_IN_ORDER.put(Modifier.PUBLIC, "public");
      MODIFIERS_IN_ORDER.put(Modifier.PROTECTED, "protected");
      MODIFIERS_IN_ORDER.put(Modifier.PRIVATE, "private");
      MODIFIERS_IN_ORDER.put(Modifier.ABSTRACT, "abstract");
      MODIFIERS_IN_ORDER.put(Modifier.SEALED, "sealed");
      MODIFIERS_IN_ORDER.put(Modifier.NON_SEALED, "non-sealed");
      MODIFIERS_IN_ORDER.put(Modifier.STATIC, "static");
      MODIFIERS_IN_ORDER.put(Modifier.FINAL, "final");
      MODIFIERS_IN_ORDER.put(Modifier.TRANSIENT, "transient");
      MODIFIERS_IN_ORDER.put(Modifier.VOLATILE, "volatile");
      MODIFIERS_IN_ORDER.put(Modifier.DEFAULT, "default");
      MODIFIERS_IN_ORDER.put(Modifier.SYNCHRONIZED, "synchronized");
      MODIFIERS_IN_ORDER.put(Modifier.NATIVE, "native");
      MODIFIERS_IN_ORDER.put(Modifier.STRICTFP, "strictfp");
   }

   public static String render(Set<Modifier> modifiers)
   {
      return MODIFIERS_IN_ORDER.entrySet()
                               .stream()
                               .map(entry -> modifiers.contains(entry.getKey()) ? Optional.of(entry.getValue()) : Optional.<String>empty())
                               .filter(Optional::isPresent)
                               .map(Optional::get)
                               .collect(joining(" "));
   }
}
