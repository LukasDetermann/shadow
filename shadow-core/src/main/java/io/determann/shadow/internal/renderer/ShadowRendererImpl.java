package io.determann.shadow.internal.renderer;

import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Shadow;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class ShadowRendererImpl
{
   public static String type(RenderingContextWrapper context, Shadow shadow)
   {
      return switch (requestOrThrow(shadow, SHADOW_GET_KIND))
      {
         case BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT, DOUBLE -> PrimitiveRendererImpl.type(context, convert(shadow).toPrimitiveOrThrow());
         case CLASS -> ClassRendererImpl.type(context, convert(shadow).toClassOrThrow());
         case INTERFACE -> InterfaceRendererImpl.type(context, convert(shadow).toInterfaceOrThrow());
         case ENUM -> EnumRendererImpl.type(context, convert(shadow).toEnumOrThrow());
         case ANNOTATION -> AnnotationRendererImpl.type(context, convert(shadow).toAnnotationOrThrow());
         case RECORD -> RecordRendererImpl.type(context, convert(shadow).toRecordOrThrow());
         case ARRAY -> ArrayRendererImpl.type(context, convert(shadow).toArrayOrThrow());
         case GENERIC -> GenericRendererImpl.type(context, convert(shadow).toGenericOrThrow());
         case WILDCARD -> WildcardRendererImpl.type(context, convert(shadow).toWildcardOrThrow());
         case INTERSECTION -> IntersectionRendererImpl.type(context, convert(shadow).toIntersectionOrThrow());
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
