package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.FieldRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Field;

import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.renderer.RenderingContextWrapper.wrap;

public record FieldRendererImpl(C_Field field) implements FieldRenderer
{
   public static String declaration(RenderingContextWrapper context, C_Field field)
   {
      StringBuilder sb = new StringBuilder();

      sb.append(RenderingSupport.annotations(context, field, '\n'));

      Set<C_Modifier> modifiers = requestOrThrow(field, MODIFIABLE_GET_MODIFIERS);
      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append(TypeRendererImpl.type(context, requestOrThrow(field, VARIABLE_GET_TYPE)));
      sb.append(' ');
      sb.append(requestOrThrow(field, NAMEABLE_GET_NAME));
      sb.append(';');
      sb.append('\n');

      return sb.toString();
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return declaration(wrap(renderingContext), field);
   }
}
