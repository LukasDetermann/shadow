package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.query.WildcardLangModel;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.api.shadow.Wildcard;
import io.determann.shadow.meta_meta.Provider;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import java.util.Objects;
import java.util.Optional;

import static io.determann.shadow.meta_meta.Operations.WILDCARD_EXTENDS;
import static io.determann.shadow.meta_meta.Operations.WILDCARD_SUPER;

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
      return Objects.equals(getExtends(), Provider.request(otherWildcard, WILDCARD_EXTENDS)) &&
             Objects.equals(getSuper(), Provider.request(otherWildcard, WILDCARD_SUPER));
   }
}
