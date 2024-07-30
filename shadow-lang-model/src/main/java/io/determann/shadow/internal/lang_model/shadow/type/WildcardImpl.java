package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.WildcardLangModel;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.api.shadow.type.Wildcard;
import io.determann.shadow.implementation.support.api.shadow.type.WildcardSupport;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.Optional;

public class WildcardImpl extends ShadowImpl<WildcardType> implements Wildcard,
                                                                      WildcardLangModel
{
   public WildcardImpl(LangModelContext context, WildcardType wildcardTypeMirror)
   {
      super(context, wildcardTypeMirror);
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.WILDCARD;
   }

   @Override
   public Optional<Shadow> getExtends()
   {
      TypeMirror extendsBound = getMirror().getExtendsBound();
      if (extendsBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(LangModelAdapter.generalize(getApi(), extendsBound));
   }

   @Override
   public Optional<Shadow> getSuper()
   {
      TypeMirror superBound = getMirror().getSuperBound();
      if (superBound == null)
      {
         return Optional.empty();
      }
      return Optional.of(LangModelAdapter.generalize(getApi(), superBound));
   }

   @Override
   public boolean contains(Shadow shadow)
   {
      return LangModelAdapter.getTypes(getApi()).contains(getMirror(), LangModelAdapter.particularType(shadow));
   }

   @Override
   public int hashCode()
   {
      return WildcardSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return WildcardSupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return WildcardSupport.toString(this);
   }
}
