package io.determann.shadow.impl.renderer;

import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Shadow;

import static io.determann.shadow.api.ShadowApi.convert;

public class ShadowRendererImpl
{
   public static String type(Context context, Shadow<?> shadow)
   {
      switch (shadow.getTypeKind())
      {
         case BOOLEAN:
         case BYTE:
         case SHORT:
         case INT:
         case LONG:
         case CHAR:
         case FLOAT:
         case DOUBLE:
            return PrimitiveRendererImpl.type(context, convert(shadow).toPrimitiveOrThrow());
         case CLASS:
            return ClassRendererImpl.type(context, convert(shadow).toClassOrThrow());
         case INTERFACE:
            return InterfaceRendererImpl.type(context, convert(shadow).toInterfaceOrThrow());
         case ENUM:
            return EnumRendererImpl.type(context, convert(shadow).toEnumOrThrow());
         case ANNOTATION:
            return AnnotationRendererImpl.type(context, convert(shadow).toAnnotationOrThrow());
         case VOID:
            return VoidRendererImpl.type();
         case NULL:
            return NullRendererImpl.type();
         case ARRAY:
            return ArrayRendererImpl.type(context, convert(shadow).toArrayOrThrow());
         case GENERIC:
            return GenericRendererImpl.type(context, convert(shadow).toGenericOrThrow());
         case WILDCARD:
            return WildcardRendererImpl.type(context, convert(shadow).toWildcardOrThrow());
         case INTERSECTION:
            return IntersectionRendererImpl.type(context, convert(shadow).toIntersectionOrThrow());
         default:
            throw new IllegalArgumentException();
      }
   }

   public static String classDeclaration(Context context, Declared declared)
   {
      return context.renderName(declared) + ".class";
   }
}
