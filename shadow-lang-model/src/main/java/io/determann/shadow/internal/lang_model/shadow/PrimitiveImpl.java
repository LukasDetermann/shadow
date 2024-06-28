package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.PrimitiveLangModel;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Primitive;
import io.determann.shadow.api.shadow.type.Shadow;

import javax.lang.model.type.PrimitiveType;
import java.util.Objects;

import static io.determann.shadow.api.shadow.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class PrimitiveImpl extends ShadowImpl<PrimitiveType> implements Primitive,
                                                                        PrimitiveLangModel
{
   public PrimitiveImpl(LangModelContext context, PrimitiveType primitiveTypeMirror)
   {
      super(context, primitiveTypeMirror);
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isSubtype(LangModelAdapter.particularType(shadow), getMirror());
   }

   @Override
   public boolean isAssignableFrom(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).isAssignable(getMirror(), LangModelAdapter.particularType(shadow));
   }

   @Override
   public Class asBoxed()
   {
      return LangModelAdapter
                     .generalize(getApi(), LangModelAdapter.getTypes(getApi()).boxedClass(getMirror()).asType());
   }

   @Override
   public TypeKind getKind()
   {
      return switch (getMirror().getKind())
      {
         case BOOLEAN -> TypeKind.BOOLEAN;
         case BYTE -> TypeKind.BYTE;
         case SHORT -> TypeKind.SHORT;
         case INT -> TypeKind.INT;
         case LONG -> TypeKind.LONG;
         case CHAR -> TypeKind.CHAR;
         case FLOAT -> TypeKind.FLOAT;
         case DOUBLE -> TypeKind.DOUBLE;
         default -> throw new IllegalStateException();
      };
   }

   @Override
   public int hashCode()
   {
      return Objects.hashCode(getKind());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Primitive otherPrimitive))
      {
         return false;
      }
      return Objects.equals(getKind(), requestOrThrow(otherPrimitive, SHADOW_GET_KIND));
   }
}
