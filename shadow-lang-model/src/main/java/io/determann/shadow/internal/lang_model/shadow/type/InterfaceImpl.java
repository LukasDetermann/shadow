package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.adapter.LM_TypeAdapter;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Interface;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.implementation.support.api.shadow.type.InterfaceSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static io.determann.shadow.api.lang_model.adapter.LM_Adapters.adapt;
import static java.util.Arrays.stream;

public class InterfaceImpl extends DeclaredImpl implements LM_Interface
{
   public InterfaceImpl(LM_Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public InterfaceImpl(LM_Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }

   @Override
   public boolean isFunctional()
   {
      return adapt(getApi()).toElements().isFunctionalInterface(getElement());
   }

   @Override
   public List<LM_Type> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> LM_Adapters.adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<LM_Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LM_Adapters.adapt(getApi(), element))
                         .toList();
   }

   @Override
   public LM_Interface withGenerics(LM_Type... generics)
   {
      if (generics.length == 0 || getGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(getQualifiedName() +
                                            " has " +
                                            getGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = stream(generics)
            .map(LM_Adapters::adapt)
            .map(LM_TypeAdapter::toTypeMirror)
            .toArray(TypeMirror[]::new);

      return (LM_Interface) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public LM_Interface withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
            .map(name -> getApi().getDeclaredOrThrow(name))
            .toArray(LM_Type[]::new));
   }

   @Override
   public LM_Interface interpolateGenerics()
   {
      return (LM_Interface) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public LM_Interface erasure()
   {
      return (LM_Interface) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
   }

   @Override
   public boolean equals(Object other)
   {
      return InterfaceSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return InterfaceSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return InterfaceSupport.toString(this);
   }
}
