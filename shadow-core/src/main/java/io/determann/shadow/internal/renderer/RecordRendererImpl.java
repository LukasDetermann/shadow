package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.RecordRenderer;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Operations;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Interface;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.Shadow;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static java.util.stream.Collectors.joining;

public class RecordRendererImpl implements RecordRenderer
{
   private final RenderingContextWrapper context;
   private final Record aRecord;

   public RecordRendererImpl(RenderingContext renderingContext, Record aRecord)
   {
      this.context = new RenderingContextWrapper(renderingContext);
      this.aRecord = aRecord;
   }

   public static String declaration(RenderingContextWrapper context, Record aRecord, String content)
   {
      StringBuilder sb = new StringBuilder();

      //noinspection OptionalContainsCollection
      Optional<List<? extends AnnotationUsage>> annotationUsages = requestOrEmpty(aRecord, Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES);
      if (!annotationUsages.map(List::isEmpty).orElse(true))
      {
         sb.append(annotationUsages.get()
                          .stream()
                          .map(usage -> AnnotationUsageRendererImpl.usage(context, usage) + "\n")
                          .collect(Collectors.joining()));
      }

      Set<Modifier> modifiers = new HashSet<>(requestOrThrow(aRecord, MODIFIABLE_GET_MODIFIERS));
      modifiers.remove(Modifier.FINAL);
      modifiers.remove(Modifier.STATIC);
      modifiers.remove(Modifier.PACKAGE_PRIVATE);

      if (!modifiers.isEmpty())
      {
         sb.append(ModifierRendererImpl.render(modifiers));
         sb.append(' ');
      }
      sb.append("record");
      sb.append(' ');
      sb.append(requestOrThrow(aRecord, NAMEABLE_GET_NAME));

      List<? extends Generic> generics = requestOrThrow(aRecord, RECORD_GET_GENERICS);
      if (!generics.isEmpty())
      {
         sb.append('<');
         sb.append(generics.stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }

      sb.append('(');
      List<? extends RecordComponent> recordComponents = requestOrThrow(aRecord, RECORD_GET_RECORD_COMPONENTS);
      if (!recordComponents.isEmpty())
      {
         sb.append(recordComponents.stream()
                                   .map(component -> RecordComponentRendererImpl.declaration(context, component))
                                   .collect(joining(", ")));
      }
      sb.append(')');

      List<? extends Interface> directInterfaces = requestOrThrow(aRecord, DECLARED_GET_DIRECT_INTERFACES);
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

   public static String type(RenderingContextWrapper context, Record aRecord)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(context.renderName(aRecord));

      List<? extends Shadow> genericTypes = requestOrThrow(aRecord, RECORD_GET_GENERIC_TYPES);
      if (!genericTypes.isEmpty())
      {
         sb.append('<');
         sb.append(genericTypes.stream().map(shadow -> ShadowRendererImpl.type(context, shadow)).collect(joining(", ")));
         sb.append('>');
      }
      return sb.toString();
   }

   @Override
   public String declaration()
   {
      return declaration(context, aRecord, "");
   }

   @Override
   public String declaration(String content)
   {
      return declaration(context, aRecord, content);
   }

   @Override
   public String type()
   {
      return type(context, aRecord);
   }
}
