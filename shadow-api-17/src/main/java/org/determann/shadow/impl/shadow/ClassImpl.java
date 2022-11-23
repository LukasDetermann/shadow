package org.determann.shadow.impl.shadow;

import org.determann.shadow.api.ShadowApi;
import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Declared;
import org.determann.shadow.api.shadow.Generic;
import org.determann.shadow.api.shadow.Shadow;
import org.determann.shadow.api.wrapper.Property;
import org.determann.shadow.impl.shadow.wraper.PropertyImpl;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.determann.shadow.api.ShadowApi.convert;

public class ClassImpl extends DeclaredImpl implements Class
{
   public ClassImpl(ShadowApi shadowApi, DeclaredType declaredTypeMirror)
   {
      super(shadowApi, declaredTypeMirror);
   }

   public ClassImpl(ShadowApi shadowApi, TypeElement typeElement)
   {
      super(shadowApi, typeElement);
   }

   @Override
   public boolean isAssignableFrom(Shadow<? extends TypeMirror> shadow)
   {
      return getApi().getJdkApiContext().types().isAssignable(getMirror(), shadow.getMirror());
   }

   @Override
   public Class getSuperClass()
   {
      TypeMirror superclass = getElement().getSuperclass();
      if (javax.lang.model.type.TypeKind.NONE.equals(superclass.getKind()))
      {
         return null;
      }
      return getApi().getShadowFactory().shadowFromType(superclass);
   }

   @Override
   public List<Class> getPermittedSubClasses()
   {
      return getElement().getPermittedSubclasses()
                         .stream()
                         .map(typeMirror -> getApi().getShadowFactory().<Class>shadowFromType(typeMirror))
                         .toList();
   }

   @Override
   public List<Property> getProperties()
   {
      return PropertyImpl.of(getApi(), this);
   }

   @Override
   public Optional<Declared> getOuterType()
   {
      TypeMirror enclosingType = getMirror().getEnclosingType();
      if (enclosingType.getKind().equals(javax.lang.model.type.TypeKind.NONE))
      {
         return Optional.empty();
      }
      return Optional.of(getApi().getShadowFactory().shadowFromType(enclosingType));
   }

   @Override
   @SafeVarargs
   public final Class withGenerics(Shadow<? extends TypeMirror>... generics)
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
      if (getOuterType().flatMap(typeMirrorShadow -> convert(typeMirrorShadow)
                              .toInterface()
                              .map(anInterface -> !anInterface.getFormalGenerics().isEmpty())
                              .or(() -> convert(typeMirrorShadow).toClass().map(aClass -> !aClass.getGenerics().isEmpty())))
                        .orElse(false))
      {
         throw new IllegalArgumentException("cant add generics to " +
                                            getQualifiedName() +
                                            " when the class is not static and the outer class has generics");
      }
      TypeMirror[] typeMirrors = Arrays.stream(generics)
                                       .map(Shadow::getMirror)
                                       .toArray(TypeMirror[]::new);

      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().getDeclaredType(getElement(), typeMirrors));
   }

   @Override
   public Class withGenerics(String... qualifiedGenerics)
   {
      //noinspection unchecked
      return withGenerics(Arrays.stream(qualifiedGenerics)
                                .map(qualifiedName -> getApi().getDeclaredOrThrow(qualifiedName))
                                .toArray(Shadow[]::new));
   }

   @Override
   public List<Shadow<TypeMirror>> getGenerics()
   {
      return getMirror().getTypeArguments()
                        .stream()
                        .map(typeMirror -> getApi().getShadowFactory().<Shadow<TypeMirror>>shadowFromType(typeMirror))
                        .toList();
   }

   @Override
   public List<Generic> getFormalGenerics()// formal
   {
      return getElement().getTypeParameters()
                         .stream()
                         .map(element -> getApi().getShadowFactory().<Generic>shadowFromElement(element))
                         .toList();
   }

   @Override
   public Class interpolateGenerics()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().capture(getMirror()));
   }
}
