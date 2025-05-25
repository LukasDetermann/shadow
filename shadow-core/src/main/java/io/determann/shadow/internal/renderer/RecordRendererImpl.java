package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.RecordRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.api.shadow.type.C_Record;
import io.determann.shadow.api.shadow.type.C_Type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.renderer.RenderingContextWrapper.wrap;
import static java.util.stream.Collectors.joining;

public record RecordRendererImpl(C_Record aRecord) implements RecordRenderer
{
   public static String declaration(RenderingContextWrapper context, C_Record aRecord, String content)
   {
      StringBuilder sb = new StringBuilder();

      sb.append(RenderingSupport.annotations(context, aRecord, '\n'));

      Set<C_Modifier> modifiers = new HashSet<>(requestOrThrow(aRecord, MODIFIABLE_GET_MODIFIERS));
      modifiers.remove(C_Modifier.FINAL);
      modifiers.remove(C_Modifier.STATIC);
      modifiers.remove(C_Modifier.PACKAGE_PRIVATE);

      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("record");
      sb.append(' ');
      sb.append(requestOrThrow(aRecord, NAMEABLE_GET_NAME));

      List<? extends C_Generic> generics = requestOrThrow(aRecord, RECORD_GET_GENERICS);
      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream().map(type -> TypeRendererImpl.type(context, type)).collect(joining(", ")));
         sb.append('>');
      }

      sb.append('(');
      List<? extends C_RecordComponent> recordComponents = requestOrThrow(aRecord, RECORD_GET_RECORD_COMPONENTS);
      if (!recordComponents.isEmpty())
      {
         sb.append(recordComponents.stream()
                                   .map(component -> RecordComponentRendererImpl.declaration(context, component))
                                   .collect(joining(", ")));
      }
      sb.append(')');

      List<? extends C_Interface> directInterfaces = requestOrThrow(aRecord, DECLARED_GET_DIRECT_INTERFACES);
      if (!directInterfaces.isEmpty())
      {
         sb.append(' ');
         sb.append("implements");
         sb.append(' ');
         sb.append(directInterfaces.stream()
                                   .map(anInterface -> InterfaceRendererImpl.type(context, anInterface))
                                   .collect(joining(", ")));
      }
      sb.append(' ');
      sb.append('{');
      if (!content.isEmpty())
      {
         sb.append('\n');
         sb.append(content);
         if (!content.endsWith("\n"))
         {
            sb.append('\n');
         }
      }
      sb.append('}');
      sb.append('\n');

      return sb.toString();
   }

   public static String type(RenderingContextWrapper context, C_Record aRecord)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(context.renderName(aRecord));

      List<? extends C_Type> genericTypes = requestOrThrow(aRecord, RECORD_GET_GENERIC_TYPES);
      if (!genericTypes.isEmpty())
      {
         sb.append('<');
         sb.append(genericTypes.stream().map(type -> TypeRendererImpl.type(context, type)).collect(joining(", ")));
         sb.append('>');
      }
      return sb.toString();
   }

   @Override
   public String declaration(RenderingContext renderingContext)
   {
      return declaration(wrap(renderingContext), aRecord, "");
   }

   @Override
   public String declaration(RenderingContext renderingContext, String content)
   {
      return declaration(wrap(renderingContext), aRecord, content);
   }

   @Override
   public String type(RenderingContext renderingContext)
   {
      return type(wrap(renderingContext), aRecord);
   }
}
