package io.determann.shadow.internal.lang_model.shadow.type;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;
import io.determann.shadow.api.lang_model.adapter.TypeAdapter;
import io.determann.shadow.implementation.support.api.shadow.type.InterfaceSupport;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

import static io.determann.shadow.api.lang_model.adapter.Adapters.adapt;
import static java.util.Arrays.stream;

public class InterfaceImpl extends DeclaredImpl implements LM.Interface
{
   public InterfaceImpl(LM.Context context, DeclaredType declaredTypeMirror)
   {
      super(context, declaredTypeMirror);
   }

   public InterfaceImpl(LM.Context context, TypeElement typeElement)
   {
      super(context, typeElement);
   }



   @Override
   public boolean isFunctional()
   {
      return adapt(getApi()).toElements().isFunctionalInterface(getElement());
   }

   @Override
   public List<LM.Declared> getPermittedSubTypes()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> adapt(getApi(), ((DeclaredType) typeMirror)))
                         .toList();
   }

   @Override
   public List<LM.Type> getGenericTypes()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> Adapters.adapt(getApi(), typeMirror))
                        .toList();
   }

   @Override
   public List<LM.Generic> getGenerics()
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> Adapters.adapt(getApi(), element))
                         .toList();
   }

   @Override
   public LM.Interface withGenerics(LM.Type... generics)
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
            .map(Adapters::adapt)
            .map(TypeAdapter::toTypeMirror)
            .toArray(TypeMirror[]::new);

      return (LM.Interface) adapt(getApi(), adapt(getApi()).toTypes().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public LM.Interface withGenerics(String... qualifiedGenerics)
   {
      return withGenerics(stream(qualifiedGenerics)
            .map(name -> getApi().getDeclaredOrThrow(name))
            .toArray(LM.Type[]::new));
   }

   @Override
   public LM.Interface interpolateGenerics()
   {
      return (LM.Interface) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().capture(getMirror())));
   }

   @Override
   public LM.Interface erasure()
   {
      return (LM.Interface) adapt(getApi(), ((DeclaredType) adapt(getApi()).toTypes().erasure(getMirror())));
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
