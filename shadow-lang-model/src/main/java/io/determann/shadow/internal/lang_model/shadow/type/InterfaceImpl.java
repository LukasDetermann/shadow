package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.type.LM_Generic;
import io.determann.shadow.api.lang_model.shadow.type.LM_Interface;
import io.determann.shadow.api.lang_model.shadow.type.LM_Type;
import io.determann.shadow.implementation.support.api.shadow.type.InterfaceSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static io.determann.shadow.api.lang_model.LM_Adapter.generalize;
import static io.determann.shadow.api.lang_model.LM_Adapter.getTypes;
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
      return LM_Adapter.getElements(getApi()).isFunctionalInterface(getElement());
   }

   @Override
   public List<LM_Type> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> LM_Adapter.<LM_Type>generalize(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<LM_Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> LM_Adapter.<LM_Generic>generalize(getApi(), element))
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
            .map(LM_Adapter::particularType)
            .toArray(TypeMirror[]::new);

      return LM_Adapter.generalize(getApi(), LM_Adapter.getTypes(getApi()).getDeclaredType(getElement(), typeMirrors));
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
      return LM_Adapter.generalize(getApi(), LM_Adapter.getTypes(getApi()).capture(getMirror()));
   }

   @Override
   public LM_Interface erasure()
   {
      return generalize(getApi(), getTypes(getApi()).erasure(getMirror()));
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
