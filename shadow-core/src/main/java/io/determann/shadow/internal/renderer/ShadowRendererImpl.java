package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Enum;
import io.determann.shadow.api.shadow.type.Record;
import io.determann.shadow.api.shadow.type.*;

import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class ShadowRendererImpl
{
   public static String type(RenderingContextWrapper context, Shadow shadow)
   {
      return switch (requestOrThrow(shadow, SHADOW_GET_KIND))
      {
         case BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT, DOUBLE -> PrimitiveRendererImpl.type(context, ((Primitive) shadow));
         case CLASS -> ClassRendererImpl.type(context, ((Class) shadow));
         case INTERFACE -> InterfaceRendererImpl.type(context, ((Interface) shadow));
         case ENUM -> EnumRendererImpl.type(context, ((Enum) shadow));
         case ANNOTATION -> AnnotationRendererImpl.type(context, ((Annotation) shadow));
         case RECORD -> RecordRendererImpl.type(context, ((Record) shadow));
         case ARRAY -> ArrayRendererImpl.type(context, ((Array) shadow));
         case GENERIC -> GenericRendererImpl.type(context, ((Generic) shadow));
         case WILDCARD -> WildcardRendererImpl.type(context, ((Wildcard) shadow));
         case INTERSECTION -> IntersectionRendererImpl.type(context, ((Intersection) shadow));
         case VOID -> VoidRendererImpl.type();
         case NULL -> NullRendererImpl.type();

         case MODULE, ENUM_CONSTANT, FIELD, PARAMETER -> throw new IllegalArgumentException();
      };
   }

   public static String classDeclaration(RenderingContext context, Declared declared)
   {
      return context.renderName(declared) + ".class";
   }
}
