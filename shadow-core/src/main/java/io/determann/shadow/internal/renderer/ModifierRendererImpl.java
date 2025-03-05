package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.ModifierRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public class ModifierRendererImpl implements ModifierRenderer
{
   private static final Map<C_Modifier, String> MODIFIERS_IN_ORDER = new LinkedHashMap<>();

   static
   {
      //https://cr.openjdk.org/~alundblad/styleguide/index-v6.html#toc-modifiers
      MODIFIERS_IN_ORDER.put(C_Modifier.PUBLIC, "public");
      MODIFIERS_IN_ORDER.put(C_Modifier.PROTECTED, "protected");
      MODIFIERS_IN_ORDER.put(C_Modifier.PRIVATE, "private");
      MODIFIERS_IN_ORDER.put(C_Modifier.ABSTRACT, "abstract");
      MODIFIERS_IN_ORDER.put(C_Modifier.SEALED, "sealed");
      MODIFIERS_IN_ORDER.put(C_Modifier.NON_SEALED, "non-sealed");
      MODIFIERS_IN_ORDER.put(C_Modifier.STATIC, "static");
      MODIFIERS_IN_ORDER.put(C_Modifier.FINAL, "final");
      MODIFIERS_IN_ORDER.put(C_Modifier.TRANSIENT, "transient");
      MODIFIERS_IN_ORDER.put(C_Modifier.VOLATILE, "volatile");
      MODIFIERS_IN_ORDER.put(C_Modifier.DEFAULT, "default");
      MODIFIERS_IN_ORDER.put(C_Modifier.SYNCHRONIZED, "synchronized");
      MODIFIERS_IN_ORDER.put(C_Modifier.NATIVE, "native");
      MODIFIERS_IN_ORDER.put(C_Modifier.STRICTFP, "strictfp");
   }

   public static String render(C_Modifier modifier)
   {
      return MODIFIERS_IN_ORDER.getOrDefault(modifier, "");
   }

   public static String render(Set<C_Modifier> modifiers)
   {
      return MODIFIERS_IN_ORDER.entrySet()
                               .stream()
                               .map(entry -> modifiers.contains(entry.getKey()) ? Optional.of(entry.getValue()) : Optional.<String>empty())
                               .filter(Optional::isPresent)
                               .map(Optional::get)
                               .collect(joining(" "));
   }

   private final C_Modifier[] modifiers;

   public ModifierRendererImpl(C_Modifier... modifiers)
   {
      this.modifiers = modifiers;
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return render(Set.of(modifiers));
   }
}
