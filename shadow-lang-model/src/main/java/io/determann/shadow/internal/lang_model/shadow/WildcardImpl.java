package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.type.WildcardLangModel;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.api.shadow.type.Wildcard;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.api.shadow.Operations.WILDCARD_GET_EXTENDS;
import static io.determann.shadow.api.shadow.Operations.WILDCARD_GET_SUPER;

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
      return Objects.hash(getExtends(),
                          getSuper());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Wildcard otherWildcard))
      {
         return false;
      }
      return Objects.equals(getExtends(), Provider.requestOrEmpty(otherWildcard, WILDCARD_GET_EXTENDS)) &&
             Objects.equals(getSuper(), Provider.requestOrEmpty(otherWildcard, WILDCARD_GET_SUPER));
   }
}
