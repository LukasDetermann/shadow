package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Interface;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class InterfaceImpl extends DeclaredImpl implements Interface
{
   public InterfaceImpl(ShadowApi shadowApi, DeclaredType declaredTypeMirror)
   {
      super(shadowApi, declaredTypeMirror);
   }

   public InterfaceImpl(ShadowApi shadowApi, TypeElement typeElement)
   {
      super(shadowApi, typeElement);
   }

   @Override
   public boolean isFunctional()
   {
      return getApi().getJdkApiContext().processingEnv().getElementUtils().isFunctionalInterface(getElement());
   }

   @Override
   @SafeVarargs
   public final Interface withGenerics(Shadow<? extends TypeMirror>... generics)
   {
      if (generics.length == 0 || getFormalGenerics().size() != generics.length)
      {
         throw new IllegalArgumentException(getQualifiedName() +
                                            " has " +
                                            getFormalGenerics().size() +
                                            " generics. " +
                                            generics.length +
                                            " are provided");
      }
      TypeMirror[] typeMirrors = Arrays.stream(generics)
                                       .map(Shadow::getMirror)
                                       .toArray(TypeMirror[]::new);

      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().processingEnv().getTypeUtils().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public Interface withGenerics(String... qualifiedGenerics)
   {
      //noinspection unchecked
      return withGenerics((Shadow[])Arrays.stream(qualifiedGenerics)
                                .map(qualifiedName -> getApi().getDeclaredOrThrow(qualifiedName))
                                .toArray(Shadow[]::new));
   }

   @Override
   public List<Shadow<TypeMirror>> getGenerics()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> getApi().getShadowFactory().<Shadow<TypeMirror>>shadowFromType(typeMirror))
                        .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public List<Generic> getFormalGenerics()// formal
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> getApi().getShadowFactory().<Generic>shadowFromElement(element))
                         .collect(collectingAndThen(toList(), Collections::unmodifiableList));
   }

   @Override
   public Interface interpolateGenerics()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().processingEnv().getTypeUtils().capture(getMirror()));
   }

   @Override
   public Interface erasure()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().processingEnv().getTypeUtils().erasure(getMirror()));
   }
}
