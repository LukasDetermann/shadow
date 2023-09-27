package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Shadow;

import static io.determann.shadow.api.ShadowApi.convert;

public class ShadowRendererImpl
{
   public static String type(Context context, Shadow<?> shadow)
   {
      return switch (shadow.getTypeKind())
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

         case PACKAGE, MODULE, ENUM_CONSTANT, METHOD, RECORD_COMPONENT, CONSTRUCTOR, FIELD, PARAMETER -> throw new IllegalArgumentException();
      };
   }

   public static String classDeclaration(Context context, Declared declared)
   {
      return context.renderName(declared) + ".class";
   }
}
